package com.eurobuddha.nftwallet;

import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.Set;

/** Settings: theme picker (all four Design modes), hidden-tokens manager, pairing status, about. */
public final class SettingsDialog {

    private SettingsDialog() {}

    public static void show(final MainActivity act) {
        ScrollView sv = new ScrollView(act);
        LinearLayout box = new LinearLayout(act);
        box.setOrientation(LinearLayout.VERTICAL);
        box.setBackgroundColor(Design.bg());
        int pad = dp(act, 20);
        box.setPadding(pad, dp(act, 12), pad, dp(act, 12));
        sv.addView(box);

        // ---- theme ----
        box.addView(section(act, "THEME"));
        RadioGroup group = new RadioGroup(act);
        Design.Mode[] modes = {Design.Mode.CURRENT, Design.Mode.CLEAN_LIGHT,
                Design.Mode.ORIGINAL_LIGHT, Design.Mode.ORIGINAL_DARK};
        String[] labels = {"Dark — family palette", "Clean Light — old-wallet white",
                "Original Light — brutalist", "Original Dark — brutalist"};
        for (int i = 0; i < modes.length; i++) {
            final Design.Mode m = modes[i];
            RadioButton rb = new RadioButton(act);
            rb.setText(labels[i]);
            rb.setTextColor(Design.text());
            rb.setTextSize(13f);
            rb.setChecked(Design.mode() == m);
            rb.setOnClickListener(v -> { Design.set(act, m); act.recreate(); });
            group.addView(rb);
        }
        box.addView(group);

        // ---- hidden tokens ----
        box.addView(section(act, "HIDDEN TOKENS"));
        Set<String> hidden = HiddenTokens.all(act);
        if (hidden.isEmpty()) {
            TextView none = new TextView(act);
            none.setText("None. Hide a token from its detail sheet on the Balances tab.");
            none.setTextColor(Design.dim());
            none.setTextSize(12f);
            box.addView(none);
        } else {
            for (final String tid : hidden) {
                LinearLayout r = new LinearLayout(act);
                r.setOrientation(LinearLayout.HORIZONTAL);
                r.setGravity(Gravity.CENTER_VERTICAL);
                r.setPadding(0, dp(act, 5), 0, dp(act, 5));
                TextView t = new TextView(act);
                String name = tid;
                for (TokenBalance b : act.balances()) {
                    if (b.tokenid.equals(tid)) { name = b.name + "  ·  " + Util.shorten(tid); break; }
                }
                if (name.equals(tid)) name = Util.shorten(tid);
                t.setText(name);
                t.setTextColor(Design.text());
                t.setTextSize(12f);
                t.setTypeface(android.graphics.Typeface.MONOSPACE);
                r.addView(t, new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
                TextView un = new TextView(act);
                un.setText("UNHIDE");
                un.setTextColor(Design.accent());
                un.setTextSize(10f);
                un.setLetterSpacing(0.08f);
                un.setPadding(dp(act, 8), dp(act, 4), 0, dp(act, 4));
                un.setOnClickListener(v -> {
                    HiddenTokens.unhide(act, tid);
                    r.setAlpha(0.3f);
                    un.setText("UNHIDDEN");
                    act.refreshAll();
                });
                r.addView(un);
                box.addView(r);
            }
        }

        // ---- node ----
        box.addView(section(act, "NODE"));
        NodeApi node = act.node();
        TextView n = new TextView(act);
        boolean paired = node != null && node.isEnabled();
        long ok = node == null ? 0 : node.lastOkMs();
        String last = ok <= 0 ? "never" : ((System.currentTimeMillis() - ok) / 1000) + "s ago";
        n.setText((paired ? "✓ Paired with Minima Core" : "◦ Not paired — enable this app in Minima Core → Apps")
                + "\nLast node reply: " + last
                + "\nIPC: broadcast Intent · bounded paging · large replies via file hand-off");
        n.setTextColor(paired ? Design.success() : Design.amber());
        n.setTextSize(12f);
        box.addView(n);

        // ---- about ----
        box.addView(section(act, "ABOUT"));
        TextView a = new TextView(act);
        String ver;
        try { ver = act.getPackageManager().getPackageInfo(act.getPackageName(), 0).versionName; }
        catch (Exception e) { ver = "?"; }
        a.setText("NFT Wallet v" + ver + " — the full-suite Minima companion.\n"
                + "Tokens · NFTs · State NFT locked editions.\n"
                + "Java + classic Views · node access only via minimaapi broadcast IPC.");
        a.setTextColor(Design.dim());
        a.setTextSize(12f);
        box.addView(a);

        new androidx.appcompat.app.AlertDialog.Builder(act)
                .setTitle("Settings")
                .setView(sv)
                .setPositiveButton("Close", null)
                .show();
    }

    private static TextView section(MainActivity act, String label) {
        TextView t = new TextView(act);
        t.setText(label);
        t.setTextColor(Design.dim());
        t.setTextSize(11f);
        t.setLetterSpacing(0.1f);
        t.setPadding(0, dp(act, 14), 0, dp(act, 6));
        return t;
    }

    private static int dp(MainActivity act, int v) {
        return (int) (v * act.getResources().getDisplayMetrics().density);
    }
}
