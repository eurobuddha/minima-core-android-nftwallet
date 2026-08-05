package com.eurobuddha.nftwallet;

import android.os.Handler;
import android.os.Looper;
import android.text.InputType;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.List;

/**
 * Transfer and Bury for StateNFT unit coins — the statenft-suite flows, wallet-side.
 *
 * Hard rules honoured here (proven on-chain):
 *  - never {@code send} (script coins are sendable:0) — always the manual txn path;
 *  - replay EVERY state port verbatim with storestate:true (SAMESTATE);
 *  - refuse coins whose state fails {@link StateNft#replayableState} — hostile state could
 *    smuggle extra command parameters when replayed;
 *  - {@code txndelete} on every failure path (CmdChain cleanup);
 *  - never trust txnpost status — confirm by watching the input coin leave the UTXO set.
 */
public final class StateNftActions {

    private static final int WATCH_INTERVAL_MS = 20000;
    private static final int WATCH_TRIES = 20;

    private StateNftActions() {}

    // ===================== transfer =====================

    /** Ask for a recipient, confirm, then post the identity-preserving transfer. */
    public static void transferDialog(MainActivity act, String tokenid, String displayName, JSONObject coin) {
        if (!idsSafe(act, tokenid, coin)) return;
        if (StateNft.stamped(coin) == null) {
            // An unstamped unit still carries the sentinel — the creator bypass is LIVE on it, so a
            // recipient would hold a coin the creator can rewrite; and an active mint's MOVE phase
            // would fight to pull it back. Only sealed identities leave the wallet.
            new androidx.appcompat.app.AlertDialog.Builder(act)
                    .setTitle("Cannot transfer")
                    .setMessage("This item has not been stamped yet — its identity isn't sealed, and "
                            + "the creator key could still rewrite it. Wait for the mint to finish stamping.")
                    .setPositiveButton("Close", null)
                    .show();
            return;
        }
        if (!StateNft.replayableState(coin)) {
            new androidx.appcompat.app.AlertDialog.Builder(act)
                    .setTitle("Cannot transfer")
                    .setMessage("This coin carries state this wallet does not recognise as safe to replay. "
                            + "Transferring it could corrupt or misuse the transaction — refusing.")
                    .setPositiveButton("Close", null)
                    .show();
            return;
        }

        LinearLayout box = new LinearLayout(act);
        box.setOrientation(LinearLayout.VERTICAL);
        box.setBackgroundColor(Design.bg());
        int pad = dp(act, 18);
        box.setPadding(pad, dp(act, 8), pad, dp(act, 8));

        TextView t = new TextView(act);
        t.setText("The item's on-chain identity is replayed verbatim to the new owner. "
                + "One transaction, owner signature only.");
        t.setTextColor(Design.dim());
        t.setTextSize(12f);
        box.addView(t);

        LinearLayout addrRow = new LinearLayout(act);
        addrRow.setOrientation(LinearLayout.HORIZONTAL);
        addrRow.setGravity(Gravity.CENTER_VERTICAL);
        final EditText addr = new EditText(act);
        addr.setHint("Recipient  Mx…  or  0x…");
        addr.setHintTextColor(Design.dim2());
        addr.setTextColor(Design.text());
        addr.setTextSize(13f);
        addr.setTypeface(android.graphics.Typeface.MONOSPACE);
        addr.setBackgroundColor(Design.surface2());
        addr.setPadding(dp(act, 10), dp(act, 10), dp(act, 10), dp(act, 10));
        addr.setInputType(InputType.TYPE_CLASS_TEXT);
        addrRow.addView(addr, new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
        TextView scan = new TextView(act);
        scan.setText(" ⌗ ");
        scan.setTextColor(Design.accent());
        scan.setTextSize(16f);
        scan.setOnClickListener(v -> act.scanQr(text -> { if (text != null) addr.setText(text.trim()); }));
        addrRow.addView(scan);
        LinearLayout.LayoutParams arlp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        arlp.topMargin = dp(act, 10);
        addrRow.setLayoutParams(arlp);
        box.addView(addrRow);

        final androidx.appcompat.app.AlertDialog dlg = new androidx.appcompat.app.AlertDialog.Builder(act)
                .setTitle("Transfer " + displayName)
                .setView(box)
                .setNegativeButton("Back", null)
                .setPositiveButton("Transfer →", null)   // set below so a bad address doesn't dismiss
                .create();
        dlg.show();
        dlg.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            String to = addr.getText().toString().trim();
            if (!Util.isValidAddress(to)) {
                addr.setError("Invalid address");
                return;
            }
            dlg.dismiss();
            runTransfer(act, tokenid, displayName, coin, to);
        });
    }

    private static void runTransfer(MainActivity act, String tokenid, String displayName,
                                    JSONObject coin, String to) {
        final String coinid = coin.optString("coinid", "");
        String txn = "tr" + shortId(coinid);
        List<String> cmds = StateNft.transferCommands(txn, tokenid, coin, to);
        final androidx.appcompat.app.AlertDialog progress = progressDialog(act,
                "Transferring " + displayName, "Building and posting the transaction…");
        CmdChain.run(act.node(), cmds, "txndelete id:" + txn, new CmdChain.Done() {
            @Override public void ok(JSONObject last) {
                act.node().cmd("txndelete id:" + txn, NOOP);
                setMessage(progress, "Posted. Waiting for the chain to accept it — the coin must "
                        + "leave your UTXO set. This can take a few minutes. You can close this; "
                        + "the wallet keeps watching.");
                makeDismissable(progress);
                watchDeparture(act, tokenid, coinid, progress,
                        "✓ Transferred — " + displayName + " now belongs to " + Util.shorten(to) + ".");
            }
            @Override public void fail(String message) {
                setMessage(progress, "Failed: " + message);
                makeDismissable(progress);
            }
        });
    }

    // ===================== bury =====================

    /** Typed-confirmation bury to the graveyard address (irreversible). Stamped units only. */
    public static void buryDialog(MainActivity act, String tokenid, String collectionName,
                                  String displayName, JSONObject coin) {
        if (!idsSafe(act, tokenid, coin)) return;
        if (StateNft.stamped(coin) == null) {
            new androidx.appcompat.app.AlertDialog.Builder(act)
                    .setTitle("Cannot bury")
                    .setMessage("Only stamped items can be buried from the wallet. Unstamped mint "
                            + "coins are managed by the mint pipeline.")
                    .setPositiveButton("Close", null)
                    .show();
            return;
        }
        if (!StateNft.replayableState(coin)) {
            new androidx.appcompat.app.AlertDialog.Builder(act)
                    .setTitle("Cannot bury")
                    .setMessage("This coin carries state this wallet does not recognise as safe to replay — refusing.")
                    .setPositiveButton("Close", null)
                    .show();
            return;
        }

        LinearLayout box = new LinearLayout(act);
        box.setOrientation(LinearLayout.VERTICAL);
        box.setBackgroundColor(Design.bg());
        int pad = dp(act, 18);
        box.setPadding(pad, dp(act, 8), pad, dp(act, 8));

        TextView t = new TextView(act);
        t.setText("Burying sends " + displayName + " to the graveyard — an address whose contract "
                + "is RETURN FALSE. Nobody, ever, can spend it again.\n\nType the collection name to confirm:\n"
                + collectionName);
        t.setTextColor(Design.red());
        t.setTextSize(12f);
        box.addView(t);

        final EditText confirm = new EditText(act);
        confirm.setHint(collectionName);
        confirm.setHintTextColor(Design.dim2());
        confirm.setTextColor(Design.text());
        confirm.setTextSize(13f);
        confirm.setBackgroundColor(Design.surface2());
        confirm.setPadding(dp(act, 10), dp(act, 10), dp(act, 10), dp(act, 10));
        LinearLayout.LayoutParams clp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        clp.topMargin = dp(act, 10);
        confirm.setLayoutParams(clp);
        box.addView(confirm);

        final androidx.appcompat.app.AlertDialog dlg = new androidx.appcompat.app.AlertDialog.Builder(act)
                .setTitle("Bury " + displayName + "?")
                .setView(box)
                .setNegativeButton("Back", null)
                .setPositiveButton("Bury forever", null)
                .create();
        dlg.show();
        dlg.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE).setTextColor(Design.red());
        dlg.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            if (!confirm.getText().toString().trim().equals(collectionName)) {
                confirm.setError("Type the exact collection name");
                return;
            }
            dlg.dismiss();
            runBury(act, tokenid, displayName, coin);
        });
    }

    private static void runBury(MainActivity act, String tokenid, String displayName, JSONObject coin) {
        final String coinid = coin.optString("coinid", "");
        String txn = "by" + shortId(coinid);
        // Stamped unit: identity-preserving path, owner signature only (creatorPk empty).
        List<String> cmds = StateNft.buryCommands(txn, tokenid, "", coin, true);
        final androidx.appcompat.app.AlertDialog progress = progressDialog(act,
                "Burying " + displayName, "Building and posting the graveyard transaction…");
        CmdChain.run(act.node(), cmds, "txndelete id:" + txn, new CmdChain.Done() {
            @Override public void ok(JSONObject last) {
                act.node().cmd("txndelete id:" + txn, NOOP);
                setMessage(progress, "Posted. Waiting for the chain to accept it… You can close this; "
                        + "the wallet keeps watching.");
                makeDismissable(progress);
                watchDeparture(act, tokenid, coinid, progress,
                        "✝ Buried — " + displayName + " is at the graveyard, unspendable forever.");
            }
            @Override public void fail(String message) {
                setMessage(progress, "Failed: " + message);
                makeDismissable(progress);
            }
        });
    }

    // ===================== departure watch =====================

    /**
     * txnpost status:true is NOT proof — an invalid spend posts fine and is rejected on-chain.
     * The only proof is the input coin disappearing from {@code coins relevant:true tokenid:}.
     */
    private static void watchDeparture(MainActivity act, String tokenid, String coinid,
                                       androidx.appcompat.app.AlertDialog progress, String successMsg) {
        final Handler h = new Handler(Looper.getMainLooper());
        final int[] tries = {0};
        final Runnable[] poll = new Runnable[1];
        poll[0] = () -> {
            // Stop when the activity is gone — a recreated activity confirms via balances anyway,
            // and polling a released NodeApi for 7 minutes just leaks the old activity + dialog.
            if (act.isFinishing() || act.isDestroyed()) return;
            act.node().cmd("coins relevant:true tokenid:" + tokenid, new NodeApi.Cb() {
            @Override public void onResult(JSONObject json) {
                boolean present = false;
                org.json.JSONArray arr = json.optJSONArray("response");
                if (arr != null) {
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject c = arr.optJSONObject(i);
                        if (c != null && coinid.equals(c.optString("coinid"))) { present = true; break; }
                    }
                }
                if (!present) {
                    setMessage(progress, successMsg);
                    makeDismissable(progress);
                    act.reload();
                    return;
                }
                reschedule();
            }
            @Override public void onError(String message) {
                if (NodeApi.ERR_TOO_LONG.equals(message)) {
                    // The reply was capped, so absence proves nothing — say so instead of
                    // polling to a false "may have been rejected".
                    setMessage(progress, "Posted, but this wallet can't confirm it here — the node's "
                            + "coin list for this token is too large to fetch. Check the item in "
                            + "Gallery in a few minutes.");
                    makeDismissable(progress);
                    act.reload();
                    return;
                }
                reschedule();
            }
            private void reschedule() {
                if (++tries[0] >= WATCH_TRIES) {
                    setMessage(progress, "Still unconfirmed after " + WATCH_TRIES
                            + " checks — the spend may have been rejected. The coin is still yours "
                            + "if it stays in the coin list.");
                    makeDismissable(progress);
                    return;
                }
                h.postDelayed(poll[0], WATCH_INTERVAL_MS);
            }
        });
        };
        h.postDelayed(poll[0], WATCH_INTERVAL_MS);
    }

    // ===================== plumbing =====================

    /**
     * Both ids are replayed into node commands, and both arrive as chain data — the node emits a
     * JSON-shaped token name unescaped, so a hostile token can inject synthetic id strings into a
     * balance/coins reply. Anything that isn't plain hex never reaches a command.
     */
    private static boolean idsSafe(MainActivity act, String tokenid, JSONObject coin) {
        String coinid = coin == null ? "" : coin.optString("coinid", "");
        if (Util.isValidHexId(tokenid) && Util.isValidHexId(coinid)) return true;
        new androidx.appcompat.app.AlertDialog.Builder(act)
                .setTitle("Refusing this coin")
                .setMessage("Its token or coin id is not a plain hex value. That should be impossible "
                        + "for a genuine coin, so this wallet will not build a transaction from it.")
                .setPositiveButton("Close", null)
                .show();
        return false;
    }

    private static final NodeApi.Cb NOOP = new NodeApi.Cb() {
        @Override public void onResult(JSONObject json) {}
        @Override public void onError(String message) {}
    };

    private static androidx.appcompat.app.AlertDialog progressDialog(MainActivity act, String title, String msg) {
        androidx.appcompat.app.AlertDialog d = new androidx.appcompat.app.AlertDialog.Builder(act)
                .setTitle(title)
                .setMessage(msg)
                .setCancelable(false)
                .create();
        d.show();
        return d;
    }

    private static void setMessage(androidx.appcompat.app.AlertDialog d, String msg) {
        try { d.setMessage(msg); } catch (Exception ignored) {}
    }

    private static void makeDismissable(androidx.appcompat.app.AlertDialog d) {
        try {
            d.setCancelable(true);
            d.setCanceledOnTouchOutside(true);
        } catch (Exception ignored) {}
    }

    private static String shortId(String coinid) {
        String c = coinid == null ? "" : coinid.replace("0x", "");
        return c.length() > 10 ? c.substring(0, 10) : c;
    }

    private static int dp(MainActivity act, int v) {
        return (int) (v * act.getResources().getDisplayMetrics().density);
    }
}
