package com.eurobuddha.nftwallet;

import android.graphics.Typeface;
import android.net.Uri;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * The Mint tab — three creation paths, matching the old Wallet's token/NFT creation suite:
 *
 *  TOKEN — fungible: name, amount, decimals, ticker, description, icon URL, web validation,
 *          arbitrary key/value metadata, burn → one {@code tokencreate} with a JSON name.
 *  NFT   — decimals:0 editions: image upload (compressed ≤{@link #ARTIMAGE_BUDGET} base64,
 *          embedded on-chain in &lt;artimage&gt;) or URL/IPFS mode, creator name, external URL,
 *          web validation, optional creator signature ({@code signtoken}).
 *  STATE NFT COLLECTION — the statenft-suite locked-edition pipeline (enabled in the next stage).
 *
 * Every path shows a confirmation card before posting and a result dialog after.
 */
public class MintView extends BaseView {

    /** Base64 budget for embedded NFT art in token metadata (the family's proven wallet-icon size). */
    private static final int ARTIMAGE_BUDGET = 6000;

    /** Keys the wallet writes itself — a custom metadata pair may not overwrite them. */
    private static final java.util.Set<String> RESERVED_META = new java.util.HashSet<>(java.util.Arrays.asList(
            "name", "url", "description", "ticker", "webvalidate", "external_url", "owner", "nft", "icon"));

    private enum Screen { HUB, TOKEN, NFT }

    private final LinearLayout container;
    private Screen screen = Screen.HUB;

    // ---- token form (built once, values persist across re-renders) ----
    private LinearLayout tokenForm;
    private EditText tName, tAmount, tDecimals, tTicker, tDesc, tUrl, tWebv, tBurn;
    private LinearLayout tMetaRows;
    private TextView tStatus;

    // ---- NFT form ----
    private LinearLayout nftForm;
    private EditText nName, nEditions, nDesc, nExtUrl, nUrl, nCreator, nWebv, nBurn;
    private TextView nStatus, nImageNote, nModeEmbed, nModeUrl;
    private ImageView nPreview;
    private CheckBox nSign;
    private boolean nftEmbedMode = true;
    private String nftImageB64 = "";

    public MintView(MainActivity a) {
        super(a, R.layout.view_mint);
        container = find(R.id.mintRoot);
        root.setBackgroundColor(Design.bg());
    }

    @Override
    public void refresh() {
        container.removeAllViews();
        switch (screen) {
            case TOKEN: container.addView(tokenForm()); break;
            case NFT:   container.addView(nftForm());   break;
            default:    renderHub();
        }
    }

    // ===================== HUB =====================

    private void renderHub() {
        container.addView(hubCard("Token",
                "Fungible. Name, ticker, decimals, description, icon URL, web validation, custom metadata.",
                false, v -> { screen = Screen.TOKEN; refresh(); }));
        container.addView(hubCard("NFT",
                "1-of-1 or edition, decimals 0. Embed an image on-chain or point at a URL / IPFS.",
                false, v -> { screen = Screen.NFT; refresh(); }));
        container.addView(hubCard("State NFT collection",
                "One tokenid, 2–20 unique items. Per-item identity sealed in coin state — immutable even to you.",
                true, v -> Toast.makeText(act, "Collection minting lands in the next build step.", Toast.LENGTH_SHORT).show()));
    }

    private View hubCard(String title, String blurb, boolean accent, View.OnClickListener click) {
        LinearLayout card = new LinearLayout(act);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(dp(14), dp(13), dp(14), dp(13));
        android.graphics.drawable.GradientDrawable bgd = new android.graphics.drawable.GradientDrawable();
        bgd.setColor(Design.surface());
        bgd.setStroke(Math.max(1, dp(1)), accent ? Design.accent() : Design.border());
        bgd.setCornerRadius(dp((int) Design.radiusDp()));
        card.setBackground(bgd);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.bottomMargin = dp(10);
        card.setLayoutParams(lp);

        LinearLayout top = new LinearLayout(act);
        top.setOrientation(LinearLayout.HORIZONTAL);
        TextView t = new TextView(act);
        t.setText(title);
        t.setTextColor(Design.heading());
        t.setTextSize(15f);
        t.setTypeface(Design.typefaceBold());
        top.addView(t, new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
        TextView arrow = new TextView(act);
        arrow.setText("→");
        arrow.setTextColor(Design.accent());
        arrow.setTextSize(15f);
        top.addView(arrow);
        card.addView(top);

        TextView b = new TextView(act);
        b.setText(blurb);
        b.setTextColor(Design.dim());
        b.setTextSize(12f);
        b.setPadding(0, dp(4), 0, 0);
        card.addView(b);

        card.setOnClickListener(click);
        return card;
    }

    // ===================== TOKEN FORM =====================

    private View tokenForm() {
        if (tokenForm != null) return detachedForReuse(tokenForm);
        tokenForm = new LinearLayout(act);
        tokenForm.setOrientation(LinearLayout.VERTICAL);

        tokenForm.addView(backRow("Mint token"));
        tName = addField(tokenForm, "Name *", "My token", InputType.TYPE_CLASS_TEXT);
        LinearLayout triple = new LinearLayout(act);
        triple.setOrientation(LinearLayout.HORIZONTAL);
        tokenForm.addView(triple);
        tAmount = addWeightedField(triple, "Amount *", "1000000", InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL, 1.4f);
        tDecimals = addWeightedField(triple, "Decimals", "8", InputType.TYPE_CLASS_NUMBER, 1f);
        tTicker = addWeightedField(triple, "Ticker", "TOK", InputType.TYPE_CLASS_TEXT, 1f);
        tDesc = addField(tokenForm, "Description", "Max 255 characters", InputType.TYPE_CLASS_TEXT);
        tUrl = addField(tokenForm, "Icon / image URL", "https://…", InputType.TYPE_TEXT_VARIATION_URI);
        tWebv = addField(tokenForm, "Web validation URL", "https://…/token.txt  (host the tokenid there after minting)", InputType.TYPE_TEXT_VARIATION_URI);
        tBurn = addField(tokenForm, "Burn (optional)", "0.0", InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        tMetaRows = new LinearLayout(act);
        tMetaRows.setOrientation(LinearLayout.VERTICAL);
        tokenForm.addView(tMetaRows);
        TextView addMeta = new TextView(act);
        addMeta.setText("+ Add metadata (key / value)");
        addMeta.setTextColor(Design.accent());
        addMeta.setTextSize(13f);
        addMeta.setPadding(dp(2), dp(8), 0, dp(8));
        addMeta.setOnClickListener(v -> addMetaRow());
        tokenForm.addView(addMeta);

        tokenForm.addView(primaryButton("Review & mint", v -> onReviewToken()));
        tStatus = statusLine();
        tokenForm.addView(tStatus);
        return tokenForm;
    }

    private void addMetaRow() {
        LinearLayout row = new LinearLayout(act);
        row.setOrientation(LinearLayout.HORIZONTAL);
        EditText k = plainField("key", InputType.TYPE_CLASS_TEXT);
        EditText v = plainField("value", InputType.TYPE_CLASS_TEXT);
        LinearLayout.LayoutParams klp = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        klp.rightMargin = dp(6);
        row.addView(k, klp);
        row.addView(v, new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.6f));
        LinearLayout.LayoutParams rlp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        rlp.topMargin = dp(6);
        row.setLayoutParams(rlp);
        tMetaRows.addView(row);
    }

    private void onReviewToken() {
        final String name = tName.getText().toString().trim();
        String err = badValue(name, true);
        if (err != null) { status(tStatus, "Name: " + err, false); return; }

        final String amountStr = tAmount.getText().toString().trim();
        final BigDecimal amount;
        try { amount = new BigDecimal(amountStr); } catch (Exception e) { status(tStatus, "Enter a valid amount.", false); return; }
        if (amount.signum() <= 0) { status(tStatus, "Amount must be greater than zero.", false); return; }
        if (amount.compareTo(new BigDecimal("1000000000000")) > 0) { status(tStatus, "Amount is above the 1 trillion cap.", false); return; }

        int decimals = 8;
        String decStr = tDecimals.getText().toString().trim();
        if (!decStr.isEmpty()) {
            try { decimals = Integer.parseInt(decStr); } catch (Exception e) { decimals = -1; }
            if (decimals < 0 || decimals > 16) { status(tStatus, "Decimals must be 0–16.", false); return; }
        }
        if (Util.decimalPlaces(amount) > 0) { status(tStatus, "Total supply must be a whole number.", false); return; }

        final String ticker = tTicker.getText().toString().trim();
        if (ticker.length() > 5) { status(tStatus, "Ticker is at most 5 characters.", false); return; }
        if ((err = badValue(ticker, false)) != null) { status(tStatus, "Ticker: " + err, false); return; }

        final String desc = tDesc.getText().toString().trim();
        if (desc.length() > 255) { status(tStatus, "Description is at most 255 characters.", false); return; }
        if ((err = badValue(desc, false)) != null) { status(tStatus, "Description: " + err, false); return; }

        final String url = tUrl.getText().toString().trim();
        if (!url.isEmpty() && badUrl(url)) { status(tStatus, "Icon URL must be a plain http(s) or ipfs link.", false); return; }
        final String webv = tWebv.getText().toString().trim();
        if (!webv.isEmpty() && badUrl(webv)) { status(tStatus, "Web validation must be a plain https link.", false); return; }

        final String burn = tBurn.getText().toString().trim();
        if (!burn.isEmpty() && badAmount(burn)) { status(tStatus, "Invalid burn amount.", false); return; }

        // metadata pairs
        final List<String[]> meta = new ArrayList<>();
        for (int i = 0; i < tMetaRows.getChildCount(); i++) {
            LinearLayout row = (LinearLayout) tMetaRows.getChildAt(i);
            String k = ((EditText) row.getChildAt(0)).getText().toString().trim();
            String v = ((EditText) row.getChildAt(1)).getText().toString().trim();
            if (k.isEmpty() && v.isEmpty()) continue;
            if (k.isEmpty()) { status(tStatus, "Metadata row " + (i + 1) + " has no key.", false); return; }
            if (badValue(k, false) != null || badValue(v, false) != null) {
                status(tStatus, "Metadata may not contain quotes, backslashes or semicolons.", false); return;
            }
            if (RESERVED_META.contains(k.toLowerCase())) {
                status(tStatus, "“" + k + "” is a reserved field — use the form inputs for it.", false); return;
            }
            meta.add(new String[]{k, v});
        }
        tStatus.setVisibility(View.GONE);

        JSONObject json = new JSONObject();
        try {
            json.put("name", name);
            if (!desc.isEmpty()) json.put("description", desc);
            if (!ticker.isEmpty()) json.put("ticker", ticker);
            if (!url.isEmpty()) json.put("url", url);
            if (!webv.isEmpty()) json.put("webvalidate", webv);
            for (String[] kv : meta) json.put(kv[0], kv[1]);
        } catch (Exception ignored) {}

        StringBuilder cmd = new StringBuilder("tokencreate name:").append(json)
                .append(" amount:").append(amount.toBigInteger())
                .append(" decimals:").append(decimals);
        if (!burn.isEmpty() && positive(burn)) cmd.append(" burn:").append(burn);
        if (!webv.isEmpty()) cmd.append(" webvalidate:").append(webv);

        // Confirmation card, old-wallet style
        LinearLayout body = confirmBody();
        addConfirmRow(body, "Name", name);
        addConfirmRow(body, "Total supply", amount.toBigInteger().toString());
        addConfirmRow(body, "Decimals", String.valueOf(decimals));
        if (!ticker.isEmpty()) addConfirmRow(body, "Ticker", ticker.toUpperCase());
        addConfirmRow(body, "Description", desc.isEmpty() ? "Not set" : desc);
        if (!url.isEmpty()) addConfirmRow(body, "Icon URL", url);
        addConfirmRow(body, "Web validation", webv.isEmpty() ? "Not set" : webv);
        for (String[] kv : meta) addConfirmRow(body, kv[0], kv[1]);
        if (!burn.isEmpty() && positive(burn)) addConfirmRow(body, "Burn", burn + " MINIMA");
        showConfirm("Mint this token?", body, "Mint →", () -> runMint(cmd.toString(), tStatus, "Token"));
    }

    // ===================== NFT FORM =====================

    private View nftForm() {
        if (nftForm != null) return detachedForReuse(nftForm);
        nftForm = new LinearLayout(act);
        nftForm.setOrientation(LinearLayout.VERTICAL);

        nftForm.addView(backRow("Mint NFT"));

        // image row: preview + mode toggle
        LinearLayout imgRow = new LinearLayout(act);
        imgRow.setOrientation(LinearLayout.HORIZONTAL);
        nftForm.addView(imgRow);

        nPreview = new ImageView(act);
        LinearLayout.LayoutParams plp = new LinearLayout.LayoutParams(dp(92), dp(92));
        plp.rightMargin = dp(10);
        nPreview.setLayoutParams(plp);
        nPreview.setScaleType(ImageView.ScaleType.CENTER_CROP);
        nPreview.setBackgroundColor(Design.surface2());
        nPreview.setOnClickListener(v -> pickNftImage());
        imgRow.addView(nPreview);

        LinearLayout modeCol = new LinearLayout(act);
        modeCol.setOrientation(LinearLayout.VERTICAL);
        modeCol.setGravity(Gravity.CENTER_VERTICAL);
        imgRow.addView(modeCol, new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));

        LinearLayout seg = new LinearLayout(act);
        seg.setOrientation(LinearLayout.HORIZONTAL);
        nModeEmbed = segBtn("EMBED IMAGE", v -> setNftMode(true));
        nModeUrl = segBtn("URL / IPFS", v -> setNftMode(false));
        seg.addView(nModeEmbed, new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
        seg.addView(nModeUrl, new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
        modeCol.addView(seg);

        nImageNote = new TextView(act);
        nImageNote.setTextColor(Design.dim());
        nImageNote.setTextSize(10f);
        nImageNote.setPadding(0, dp(6), 0, 0);
        modeCol.addView(nImageNote);

        nUrl = addField(nftForm, "Image URL (URL mode)", "https://…  or  ipfs://…", InputType.TYPE_TEXT_VARIATION_URI);
        nName = addField(nftForm, "Name *", "My NFT", InputType.TYPE_CLASS_TEXT);
        LinearLayout duo = new LinearLayout(act);
        duo.setOrientation(LinearLayout.HORIZONTAL);
        nftForm.addView(duo);
        nEditions = addWeightedField(duo, "Editions *", "1", InputType.TYPE_CLASS_NUMBER, 1f);
        nCreator = addWeightedField(duo, "Creator", "your name", InputType.TYPE_CLASS_TEXT, 2f);
        nDesc = addField(nftForm, "Description", "Tell collectors what this is", InputType.TYPE_CLASS_TEXT);
        nExtUrl = addField(nftForm, "External URL", "https://…", InputType.TYPE_TEXT_VARIATION_URI);
        nWebv = addField(nftForm, "Web validation URL", "https://…/nft.txt", InputType.TYPE_TEXT_VARIATION_URI);
        nBurn = addField(nftForm, "Burn (optional)", "0.0", InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        nSign = new CheckBox(act);
        nSign.setText("Sign as creator (signtoken — proves this wallet minted it)");
        nSign.setTextColor(Design.dim());
        nSign.setTextSize(12f);
        nSign.setChecked(true);
        nftForm.addView(nSign);

        nftForm.addView(primaryButton("Review & mint NFT", v -> onReviewNft()));
        nStatus = statusLine();
        nftForm.addView(nStatus);

        setNftMode(true);
        prefillCreator();
        return nftForm;
    }

    private TextView segBtn(String label, View.OnClickListener click) {
        TextView t = new TextView(act);
        t.setText(label);
        t.setTextSize(10f);
        t.setLetterSpacing(0.08f);
        t.setTypeface(Design.typefaceBold());
        t.setGravity(Gravity.CENTER);
        t.setPadding(dp(6), dp(7), dp(6), dp(7));
        t.setOnClickListener(click);
        return t;
    }

    private void setNftMode(boolean embed) {
        nftEmbedMode = embed;
        nModeEmbed.setTextColor(embed ? Design.onAccent() : Design.dim());
        nModeEmbed.setBackgroundColor(embed ? Design.accent() : Design.surface2());
        nModeUrl.setTextColor(!embed ? Design.onAccent() : Design.dim());
        nModeUrl.setBackgroundColor(!embed ? Design.accent() : Design.surface2());
        nUrl.setVisibility(embed ? View.GONE : View.VISIBLE);
        nImageNote.setText(embed
                ? (nftImageB64.isEmpty()
                    ? "Tap the square to pick an image — compressed and stored on-chain (≤" + ARTIMAGE_BUDGET + " b64 chars)."
                    : "Image ready — " + nftImageB64.length() + " base64 chars, stored on-chain.")
                : "The NFT points at an external image URL (or ipfs://…).");
    }

    private void pickNftImage() {
        act.pickImage(uri -> {
            if (uri == null) return;
            nPreview.setImageURI(uri);
            nImageNote.setText("Compressing…");
            new Thread(() -> {
                String b64;
                try { b64 = ImageTools.compressUri(act, uri, ARTIMAGE_BUDGET); }
                catch (Exception e) { b64 = ""; }
                final String result = b64;
                act.runOnUiThread(() -> {
                    if (result.isEmpty()) {
                        nftImageB64 = "";
                        nImageNote.setText("Could not read that image — try another.");
                    } else {
                        nftImageB64 = result;
                        setNftMode(true);   // picking an image implies embed mode
                    }
                });
            }).start();
        });
    }

    /** Prefill the creator field from the node's Maxima name (best-effort). */
    private void prefillCreator() {
        act.node().cmd("maxima action:info", new NodeApi.Cb() {
            @Override public void onResult(JSONObject json) {
                JSONObject r = json.optJSONObject("response");
                String name = r == null ? "" : r.optString("name", "");
                if (!name.isEmpty() && !"noname".equals(name)
                        && nCreator.getText().toString().trim().isEmpty()) {
                    nCreator.setText(name);
                }
            }
            @Override public void onError(String message) {}
        });
    }

    private void onReviewNft() {
        final String name = nName.getText().toString().trim();
        String err = badValue(name, true);
        if (err != null) { status(nStatus, "Name: " + err, false); return; }

        final String edStr = nEditions.getText().toString().trim();
        final int editions;
        try { editions = edStr.isEmpty() ? 1 : Integer.parseInt(edStr); }
        catch (Exception e) { status(nStatus, "Editions must be a whole number.", false); return; }
        if (editions < 1 || editions > 1000000) { status(nStatus, "Editions must be 1–1,000,000.", false); return; }

        final String desc = nDesc.getText().toString().trim();
        if (desc.length() > 255) { status(nStatus, "Description is at most 255 characters.", false); return; }
        if ((err = badValue(desc, false)) != null) { status(nStatus, "Description: " + err, false); return; }
        final String creator = nCreator.getText().toString().trim();
        if ((err = badValue(creator, false)) != null) { status(nStatus, "Creator: " + err, false); return; }

        final String extUrl = nExtUrl.getText().toString().trim();
        if (!extUrl.isEmpty() && badUrl(extUrl)) { status(nStatus, "External URL must be a plain http(s) link.", false); return; }
        final String webv = nWebv.getText().toString().trim();
        if (!webv.isEmpty() && badUrl(webv)) { status(nStatus, "Web validation must be a plain https link.", false); return; }
        final String burn = nBurn.getText().toString().trim();
        if (!burn.isEmpty() && badAmount(burn)) { status(nStatus, "Invalid burn amount.", false); return; }

        final String imageValue;
        if (nftEmbedMode) {
            if (nftImageB64.isEmpty()) { status(nStatus, "Pick an image first (or switch to URL mode).", false); return; }
            imageValue = "<artimage>" + nftImageB64 + "</artimage>";
        } else {
            String u = nUrl.getText().toString().trim();
            boolean okScheme = u.startsWith("https://") || u.startsWith("http://") || u.startsWith("ipfs://");
            if (u.isEmpty() || !okScheme || hasBreakers(u)) {
                status(nStatus, "Enter a plain image URL (https or ipfs, no spaces or quotes).", false); return;
            }
            imageValue = u;
        }
        nStatus.setVisibility(View.GONE);

        JSONObject json = new JSONObject();
        try {
            json.put("name", name);
            json.put("url", imageValue);
            if (!desc.isEmpty()) json.put("description", desc);
            if (!creator.isEmpty()) json.put("owner", creator);
            if (!extUrl.isEmpty()) json.put("external_url", extUrl);
            if (!webv.isEmpty()) json.put("webvalidate", webv);
            json.put("nft", "true");
        } catch (Exception ignored) {}

        final StringBuilder cmd = new StringBuilder("tokencreate name:").append(json)
                .append(" amount:").append(editions)
                .append(" decimals:0");
        if (!burn.isEmpty() && positive(burn)) cmd.append(" burn:").append(burn);
        if (!webv.isEmpty()) cmd.append(" webvalidate:").append(webv);

        LinearLayout body = confirmBody();
        if (nftEmbedMode) {
            ImageView iv = new ImageView(act);
            LinearLayout.LayoutParams ivlp = new LinearLayout.LayoutParams(dp(110), dp(110));
            ivlp.gravity = Gravity.CENTER_HORIZONTAL; ivlp.bottomMargin = dp(8);
            iv.setLayoutParams(ivlp);
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            ImageLoader.loadOver(act, "data:image/jpeg;base64," + nftImageB64, iv, null);
            body.addView(iv, 0);
        }
        addConfirmRow(body, "Name", name);
        addConfirmRow(body, "Total to mint", String.valueOf(editions));
        addConfirmRow(body, "Image", nftEmbedMode ? "embedded on-chain (" + nftImageB64.length() + " b64)" : imageValue);
        addConfirmRow(body, "Description", desc.isEmpty() ? "Not set" : desc);
        addConfirmRow(body, "Creator", creator.isEmpty() ? "Not set" : creator);
        addConfirmRow(body, "External URL", extUrl.isEmpty() ? "None set" : extUrl);
        addConfirmRow(body, "Web validation", webv.isEmpty() ? "None set" : webv);
        addConfirmRow(body, "Creator signature", nSign.isChecked() ? "yes (signtoken)" : "no");
        if (!burn.isEmpty() && positive(burn)) addConfirmRow(body, "Burn", burn + " MINIMA");

        showConfirm("Mint this NFT?", body, "Mint NFT →", () -> {
            if (nSign.isChecked()) {
                // fetch a wallet public key to sign the token as its creator
                act.node().cmd("getaddress", new NodeApi.Cb() {
                    @Override public void onResult(JSONObject json) {
                        JSONObject r = json.optJSONObject("response");
                        String pk = r == null ? "" : r.optString("publickey", "");
                        if (!pk.isEmpty()) cmd.append(" signtoken:").append(pk);
                        runMint(cmd.toString(), nStatus, "NFT");
                    }
                    @Override public void onError(String message) { runMint(cmd.toString(), nStatus, "NFT"); }
                });
            } else {
                runMint(cmd.toString(), nStatus, "NFT");
            }
        });
    }

    // ===================== shared =====================

    /** Posts the tokencreate and shows the result dialog. */
    private void runMint(String cmd, final TextView statusView, final String what) {
        status(statusView, "Minting — building and mining the transaction…", true);
        act.node().cmd(cmd, new NodeApi.Cb() {
            @Override public void onResult(JSONObject json) {
                if (json.optBoolean("status", false)) {
                    status(statusView, "✓ " + what + " minted — it appears in Balances once confirmed.", true);
                    resultDialog(what + " minted",
                            "The mint transaction is posted. Your new " + what.toLowerCase()
                                    + " appears in Balances (unconfirmed first) within a couple of blocks.");
                    act.reload();
                } else {
                    String err = json.optString("error", json.optString("message", "Mint failed."));
                    status(statusView, "Failed: " + err, false);
                }
            }
            @Override public void onError(String message) {
                if (NodeApi.ERR_NOT_ENABLED.equals(message)) message = "Enable this wallet in Minima Core → Apps first.";
                status(statusView, "Failed: " + message, false);
            }
        });
    }

    private void resultDialog(String title, String message) {
        new androidx.appcompat.app.AlertDialog.Builder(act)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("View balances", (d, w) -> act.goToTab(MainActivity.TAB_BALANCES))
                .setNegativeButton("Close", null)
                .show();
    }

    private LinearLayout confirmBody() {
        LinearLayout body = new LinearLayout(act);
        body.setOrientation(LinearLayout.VERTICAL);
        body.setBackgroundColor(Design.bg());
        body.setPadding(dp(18), dp(10), dp(18), dp(4));
        return body;
    }

    private void addConfirmRow(LinearLayout body, String label, String value) {
        LinearLayout r = new LinearLayout(act);
        r.setOrientation(LinearLayout.HORIZONTAL);
        r.setPadding(0, dp(3), 0, dp(3));
        TextView l = new TextView(act);
        l.setText(label);
        l.setTextColor(Design.dim());
        l.setTextSize(12f);
        r.addView(l, new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
        TextView v = new TextView(act);
        v.setText(value);
        v.setTextColor("Not set".equals(value) || "None set".equals(value) ? Design.dim() : Design.text());
        if ("Not set".equals(value) || "None set".equals(value)) v.setTypeface(null, Typeface.ITALIC);
        v.setTextSize(12f);
        v.setGravity(Gravity.END);
        v.setMaxLines(2);
        v.setEllipsize(android.text.TextUtils.TruncateAt.MIDDLE);
        r.addView(v, new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.6f));
        body.addView(r);
    }

    private void showConfirm(String title, LinearLayout body, String action, Runnable go) {
        TextView warn = new TextView(act);
        warn.setText("Minting consumes a small fraction of Minima and cannot be undone.");
        warn.setTextColor(Design.dim());
        warn.setTextSize(11f);
        warn.setPadding(0, dp(10), 0, 0);
        body.addView(warn);
        ScrollView sv = new ScrollView(act);
        sv.addView(body);
        new androidx.appcompat.app.AlertDialog.Builder(act)
                .setTitle(title)
                .setView(sv)
                .setNegativeButton("Back", null)
                .setPositiveButton(action, (d, w) -> go.run())
                .show();
    }

    // ---- field + validation helpers ----

    private TextView backRow(String title) {
        TextView t = new TextView(act);
        t.setText("‹  " + title);
        t.setTextColor(Design.heading());
        t.setTextSize(16f);
        t.setTypeface(Design.typefaceBold());
        t.setPadding(0, 0, 0, dp(10));
        t.setOnClickListener(v -> { screen = Screen.HUB; refresh(); });
        return t;
    }

    private EditText addField(LinearLayout into, String label, String hint, int inputType) {
        TextView l = new TextView(act);
        l.setText(label);
        l.setTextColor(Design.dim());
        l.setTextSize(12f);
        l.setPadding(0, dp(10), 0, dp(4));
        into.addView(l);
        EditText e = plainField(hint, inputType);
        into.addView(e);
        return e;
    }

    private EditText addWeightedField(LinearLayout row, String label, String hint, int inputType, float weight) {
        LinearLayout col = new LinearLayout(act);
        col.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams clp = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, weight);
        if (row.getChildCount() > 0) clp.leftMargin = dp(8);
        col.setLayoutParams(clp);
        TextView l = new TextView(act);
        l.setText(label);
        l.setTextColor(Design.dim());
        l.setTextSize(12f);
        l.setPadding(0, dp(10), 0, dp(4));
        col.addView(l);
        EditText e = plainField(hint, inputType);
        col.addView(e);
        row.addView(col);
        return e;
    }

    private EditText plainField(String hint, int inputType) {
        EditText e = new EditText(act);
        e.setHint(hint);
        e.setHintTextColor(Design.dim2());
        e.setTextColor(Design.text());
        e.setTextSize(13f);
        e.setBackgroundColor(Design.surface2());
        e.setPadding(dp(10), dp(10), dp(10), dp(10));
        e.setInputType(inputType == InputType.TYPE_TEXT_VARIATION_URI
                ? InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_URI : inputType);
        e.setMaxLines(2);
        return e;
    }

    private android.widget.Button primaryButton(String label, View.OnClickListener click) {
        android.widget.Button b = new android.widget.Button(act);
        b.setText(label);
        b.setAllCaps(false);
        b.setBackgroundTintList(android.content.res.ColorStateList.valueOf(Design.accent()));
        b.setTextColor(Design.onAccent());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.topMargin = dp(16);
        b.setLayoutParams(lp);
        b.setOnClickListener(click);
        return b;
    }

    private TextView statusLine() {
        TextView t = new TextView(act);
        t.setTextSize(13f);
        t.setGravity(Gravity.CENTER);
        t.setPadding(0, dp(12), 0, dp(12));
        t.setVisibility(View.GONE);
        return t;
    }

    private void status(TextView view, String msg, boolean ok) {
        view.setVisibility(View.VISIBLE);
        view.setText(msg);
        view.setTextColor(ok ? Design.success() : Design.red());
    }

    /** A cached form may still be attached to the old container after a refresh — detach first. */
    private View detachedForReuse(View v) {
        if (v.getParent() instanceof android.view.ViewGroup) {
            ((android.view.ViewGroup) v.getParent()).removeView(v);
        }
        return v;
    }

    /**
     * Field-value guard: quotes and backslashes break the metadata JSON when the node re-parses
     * the command line, and a semicolon would let a value smuggle a second node command.
     */
    private String badValue(String s, boolean required) {
        if (s == null || s.isEmpty()) return required ? "required." : null;
        if (s.contains("\"") || s.contains("\\") || s.contains(";") || s.contains("\n")) {
            return "may not contain quotes, backslashes or semicolons.";
        }
        return null;
    }

    /** Characters that break the command line or the metadata JSON. */
    private boolean hasBreakers(String u) {
        return u.contains(" ") || u.contains("\"") || u.contains("\\") || u.contains(";") || u.contains("\n");
    }

    /** URLs ride the command line outside JSON too (webvalidate:) — no spaces or breakers allowed. */
    private boolean badUrl(String u) {
        if (hasBreakers(u)) return true;
        return !(u.startsWith("https://") || u.startsWith("http://"));
    }

    private boolean badAmount(String s) {
        try { return new BigDecimal(s).signum() < 0; } catch (Exception e) { return true; }
    }

    private boolean positive(String s) {
        try { return new BigDecimal(s).signum() > 0; } catch (Exception e) { return false; }
    }

    private int dp(int v) { return (int) (v * act.getResources().getDisplayMetrics().density); }
}
