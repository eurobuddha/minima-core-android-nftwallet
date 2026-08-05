package com.eurobuddha.nftwallet;

import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The Gallery tab — the NFT-first view of the wallet: a two-column grid of every NFT you hold.
 *
 *  - Regular NFTs (decimals-0 tokens) are one card per token.
 *  - StateNFT collections expand into one card PER OWNED ITEM (per coin), each with its sealed
 *    index and its own image (embedded state art beats the URL base).
 *  - Collected / Favourites tabs (long-press or ♥ in detail to favourite), search by name,
 *    creator or tokenid, and a deep detail dialog with Send / Transfer / Bury.
 */
public class GalleryView extends BaseView {

    private static final String PREFS = "nftwallet_gallery";
    private static final String KEY_FAVS = "favs";

    private final LinearLayout header;
    private final RecyclerView grid;
    private final GalleryAdapter adapter = new GalleryAdapter();

    private EditText search;
    private TextView tabCollected, tabFavs, emptyNote;
    private boolean showFavs = false;

    /** tokenid → token record (script + meta) from `tokens tokenid:`; null value = fetch in flight. */
    private final Map<String, JSONObject> tokenRecords = new HashMap<>();
    private final Set<String> fetching = new HashSet<>();

    private final List<GItem> items = new ArrayList<>();

    public GalleryView(MainActivity a) {
        super(a, R.layout.view_gallery);
        header = find(R.id.galleryHeader);
        grid = find(R.id.galleryGrid);
        grid.setLayoutManager(new GridLayoutManager(a, 2));
        grid.setAdapter(adapter);
        buildHeader();
    }

    private void buildHeader() {
        root.setBackgroundColor(Design.bg());

        LinearLayout tabs = new LinearLayout(act);
        tabs.setOrientation(LinearLayout.HORIZONTAL);
        tabCollected = tabBtn("COLLECTED", v -> { showFavs = false; refresh(); });
        tabFavs = tabBtn("FAVOURITES", v -> { showFavs = true; refresh(); });
        tabs.addView(tabCollected, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
        tabs.addView(tabFavs, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
        header.addView(tabs);

        search = new EditText(act);
        search.setHint("Search by name, creator or tokenid");
        search.setHintTextColor(Design.dim2());
        search.setTextColor(Design.text());
        search.setTextSize(13f);
        search.setBackgroundColor(Design.surface2());
        search.setPadding(dp(10), dp(9), dp(10), dp(9));
        search.setMaxLines(1);
        LinearLayout.LayoutParams slp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        slp.topMargin = dp(8);
        slp.bottomMargin = dp(8);
        search.setLayoutParams(slp);
        search.addTextChangedListener(new android.text.TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int a, int b, int c) {}
            @Override public void onTextChanged(CharSequence s, int a, int b, int c) {}
            @Override public void afterTextChanged(android.text.Editable s) { rebuild(); }
        });
        header.addView(search);

        emptyNote = new TextView(act);
        emptyNote.setTextColor(Design.dim());
        emptyNote.setTextSize(13f);
        emptyNote.setGravity(Gravity.CENTER);
        emptyNote.setPadding(0, dp(30), 0, 0);
        emptyNote.setVisibility(View.GONE);
        header.addView(emptyNote);
    }

    private TextView tabBtn(String label, View.OnClickListener click) {
        TextView t = new TextView(act);
        t.setText(label);
        t.setTextSize(11f);
        t.setLetterSpacing(0.1f);
        t.setTypeface(Design.typefaceBold());
        t.setGravity(Gravity.CENTER);
        t.setPadding(0, dp(8), 0, dp(8));
        t.setOnClickListener(click);
        return t;
    }

    @Override
    public void refresh() {
        boolean fav = showFavs;
        tabCollected.setTextColor(!fav ? Design.onAccent() : Design.dim());
        tabCollected.setBackgroundColor(!fav ? Design.accent() : Design.surface2());
        tabFavs.setTextColor(fav ? Design.onAccent() : Design.dim());
        tabFavs.setBackgroundColor(fav ? Design.accent() : Design.surface2());
        rebuild();
    }

    // ===================== item assembly =====================

    /** True NFT candidate: any non-Minima token with no fractional decimals (the nftstudio rule). */
    private boolean isNftCandidate(TokenBalance b) {
        if (b.isMinima()) return false;
        String d = b.meta == null ? "" : b.meta.decimals;
        return d.isEmpty() || "0".equals(d.trim());
    }

    private void rebuild() {
        items.clear();
        String q = search == null ? "" : search.getText().toString().trim().toLowerCase();
        Set<String> favs = favs();

        for (TokenBalance b : act.balances()) {
            if (!isNftCandidate(b)) continue;
            JSONObject record = tokenRecords.get(b.tokenid);
            if (record == null) { ensureRecord(b.tokenid); }
            String script = record == null ? "" : record.optString("script", "");
            boolean stateNft = !script.isEmpty() && StateNft.isStateNftScript(script);

            if (stateNft) {
                StateNft.Meta meta = StateNft.parseMeta(b.tokenid, record);
                for (Coin c : act.coins()) {
                    if (!c.tokenid.equals(b.tokenid)) continue;
                    String idx = StateNft.stamped(c.raw);
                    GItem it = new GItem();
                    it.tokenid = b.tokenid;
                    it.stateNft = true;
                    it.meta = meta;
                    it.coin = c;
                    it.idx = idx == null ? -1 : parseInt(idx);
                    it.name = meta.name + (idx != null ? " #" + idx : " (unstamped)");
                    it.creator = b.meta == null ? "" : b.meta.owner;
                    it.sub = "state · " + (idx != null ? "#" + idx + " of " + meta.size : "unstamped");
                    it.imageUrl = StateNft.imageUrl(meta, it.idx <= 0 ? 1 : it.idx, c.raw);
                    it.favKey = b.tokenid + "#" + it.idx;
                    it.webvalidate = meta.webvalidate;
                    it.externalUrl = meta.externalUrl;
                    it.description = meta.description;
                    items.add(it);
                }
            } else {
                GItem it = new GItem();
                it.tokenid = b.tokenid;
                it.stateNft = false;
                it.balance = b;
                it.idx = -1;
                it.name = b.name;
                it.creator = b.meta == null ? "" : b.meta.owner;
                it.sub = "nft · " + Util.tidyAmount(b.sendable) + "/" + Util.tidyAmount(b.total);
                it.imageUrl = b.meta == null ? "" : b.meta.iconUrl;
                it.favKey = b.tokenid;
                it.webvalidate = b.meta == null ? "" : b.meta.webvalidate;
                it.externalUrl = b.meta == null ? "" : b.meta.externalUrl;
                it.description = b.meta == null ? "" : b.meta.description;
                items.add(it);
            }
        }

        // filter: favourites tab + search
        List<GItem> filtered = new ArrayList<>();
        for (GItem it : items) {
            if (showFavs && !favs.contains(it.favKey)) continue;
            if (!q.isEmpty()
                    && !(it.name != null && it.name.toLowerCase().contains(q))
                    && !(it.creator != null && it.creator.toLowerCase().contains(q))
                    && !it.tokenid.toLowerCase().contains(q)) continue;
            filtered.add(it);
        }
        items.clear();
        items.addAll(filtered);

        emptyNote.setVisibility(items.isEmpty() ? View.VISIBLE : View.GONE);
        emptyNote.setText(showFavs ? "No favourites yet — open an NFT and tap ♥."
                : "No NFTs yet — mint one from the Mint tab.");
        adapter.notifyDataSetChanged();
    }

    /** Fetch the token record once per tokenid (script decides state-vs-regular; meta feeds images). */
    private void ensureRecord(final String tokenid) {
        if (tokenRecords.containsKey(tokenid) || fetching.contains(tokenid)) return;
        fetching.add(tokenid);
        act.node().cmd("tokens tokenid:" + tokenid, new NodeApi.Cb() {
            @Override public void onResult(JSONObject json) {
                fetching.remove(tokenid);
                Object resp = json.opt("response");
                JSONObject tok = resp instanceof JSONObject ? (JSONObject) resp
                        : (resp instanceof org.json.JSONArray && ((org.json.JSONArray) resp).length() > 0
                            ? ((org.json.JSONArray) resp).optJSONObject(0) : null);
                if (tok != null) {
                    tokenRecords.put(tokenid, tok);
                    rebuild();
                }
            }
            @Override public void onError(String message) { fetching.remove(tokenid); }
        });
    }

    // ===================== favourites =====================

    private Set<String> favs() {
        return new HashSet<>(act.getSharedPreferences(PREFS, android.content.Context.MODE_PRIVATE)
                .getStringSet(KEY_FAVS, new HashSet<>()));
    }

    private void toggleFav(String key) {
        Set<String> f = favs();
        if (!f.remove(key)) f.add(key);
        act.getSharedPreferences(PREFS, android.content.Context.MODE_PRIVATE)
                .edit().putStringSet(KEY_FAVS, f).apply();
        rebuild();
    }

    // ===================== grid =====================

    private static class GItem {
        String tokenid, name, creator, sub, imageUrl, favKey, webvalidate, externalUrl, description;
        boolean stateNft;
        int idx;
        Coin coin;                 // state item's UTXO
        TokenBalance balance;      // regular NFT's balance row
        StateNft.Meta meta;        // state collection meta
    }

    private class Holder extends RecyclerView.ViewHolder {
        final ImageView art;
        final TextView name, sub, idxChip, heart;

        Holder(FrameLayout shell, ImageView art, TextView name, TextView sub, TextView idxChip, TextView heart) {
            super(shell);
            this.art = art; this.name = name; this.sub = sub; this.idxChip = idxChip; this.heart = heart;
        }
    }

    private class GalleryAdapter extends RecyclerView.Adapter<Holder> {

        @Override public int getItemCount() { return items.size(); }

        @Override public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            FrameLayout shell = new FrameLayout(act);
            RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            int m = dp(4);
            lp.setMargins(m, m, m, m);
            shell.setLayoutParams(lp);
            shell.setBackgroundColor(Design.surface());

            LinearLayout col = new LinearLayout(act);
            col.setOrientation(LinearLayout.VERTICAL);
            shell.addView(col, new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            ImageView art = new ImageView(act);
            art.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp(150)));
            art.setScaleType(ImageView.ScaleType.CENTER_CROP);
            col.addView(art);

            LinearLayout meta = new LinearLayout(act);
            meta.setOrientation(LinearLayout.VERTICAL);
            meta.setPadding(dp(8), dp(6), dp(8), dp(7));
            TextView name = new TextView(act);
            name.setTextColor(Design.text());
            name.setTextSize(12f);
            name.setTypeface(Design.typefaceBold());
            name.setMaxLines(1);
            name.setEllipsize(TextUtils.TruncateAt.END);
            TextView sub = new TextView(act);
            sub.setTextColor(Design.dim());
            sub.setTextSize(9.5f);
            sub.setLetterSpacing(0.04f);
            meta.addView(name);
            meta.addView(sub);
            col.addView(meta);

            TextView idxChip = new TextView(act);
            idxChip.setTextColor(0xFFFFFFFF);
            idxChip.setBackgroundColor(0x8C000000);
            idxChip.setTextSize(9f);
            idxChip.setTypeface(Typeface.MONOSPACE, Typeface.BOLD);
            idxChip.setPadding(dp(6), dp(3), dp(6), dp(3));
            FrameLayout.LayoutParams clp = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,
                    Gravity.START | Gravity.TOP);
            clp.setMargins(dp(6), dp(6), 0, 0);
            shell.addView(idxChip, clp);

            TextView heart = new TextView(act);
            heart.setTextSize(14f);
            heart.setShadowLayer(4f, 0, 1, 0xAA000000);
            FrameLayout.LayoutParams hlp = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,
                    Gravity.END | Gravity.TOP);
            hlp.setMargins(0, dp(4), dp(8), 0);
            shell.addView(heart, hlp);

            return new Holder(shell, art, name, sub, idxChip, heart);
        }

        @Override public void onBindViewHolder(Holder h, int position) {
            final GItem it = items.get(position);
            h.name.setText(it.name == null ? "NFT" : it.name);
            h.sub.setText(it.sub == null ? "" : it.sub.toUpperCase());
            h.idxChip.setVisibility(it.stateNft && it.idx > 0 ? View.VISIBLE : View.GONE);
            if (it.stateNft && it.meta != null) h.idxChip.setText("#" + it.idx + " / " + it.meta.size);
            boolean fav = favs().contains(it.favKey);
            h.heart.setText(fav ? "♥" : "♡");
            h.heart.setTextColor(fav ? 0xFFFF5A6E : 0xFFFFFFFF);
            h.heart.setOnClickListener(v -> toggleFav(it.favKey));

            h.art.setImageBitmap(Identicon.forToken(it.tokenid + "#" + it.idx, dp(150)));
            if (it.imageUrl != null && !it.imageUrl.isEmpty()) {
                ImageLoader.loadOver(act, it.imageUrl, h.art, null);
            }
            ((FrameLayout) h.itemView).setForeground(null);
            h.itemView.setOnClickListener(v -> showDetail(it));
            h.itemView.setOnLongClickListener(v -> { toggleFav(it.favKey); return true; });
        }
    }

    // ===================== detail =====================

    private void showDetail(final GItem it) {
        ScrollView sv = new ScrollView(act);
        LinearLayout box = new LinearLayout(act);
        box.setOrientation(LinearLayout.VERTICAL);
        box.setBackgroundColor(Design.bg());
        box.setPadding(dp(18), dp(12), dp(18), dp(10));
        sv.addView(box);

        // hero
        final ImageView hero = new ImageView(act);
        hero.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp(200)));
        hero.setScaleType(ImageView.ScaleType.CENTER_CROP);
        hero.setImageBitmap(Identicon.forToken(it.tokenid + "#" + it.idx, dp(200)));
        if (it.imageUrl != null && !it.imageUrl.isEmpty()) {
            ImageLoader.loadOver(act, it.imageUrl, hero, null);
            hero.setOnClickListener(v -> showImageFull(it.imageUrl));
        }
        box.addView(hero);
        TextView hint = new TextView(act);
        hint.setText("tap image for full resolution");
        hint.setTextColor(Design.dim());
        hint.setTextSize(10f);
        hint.setGravity(Gravity.CENTER);
        hint.setPadding(0, dp(3), 0, dp(6));
        box.addView(hint);

        TextView title = new TextView(act);
        title.setText(it.name);
        title.setTextColor(Design.heading());
        title.setTextSize(18f);
        title.setTypeface(Design.typefaceBold());
        box.addView(title);

        // chips line
        StringBuilder chips = new StringBuilder();
        if (it.stateNft) {
            chips.append(it.idx > 0 ? "#" + it.idx + " of " + (it.meta == null ? "?" : it.meta.size) : "unstamped");
            chips.append("  ·  LOCKED EDITION");
        } else if (it.balance != null) {
            chips.append("1".equals(it.balance.total) ? "1 OF 1" : it.balance.total + " EDITIONS");
        }
        if (it.webvalidate != null && !it.webvalidate.isEmpty()
                && Boolean.TRUE.equals(WebValidate.status(it.tokenid))) {
            chips.append("  ·  ✓ WEB VALIDATED");
        }
        TextView chipLine = new TextView(act);
        chipLine.setText(chips.toString());
        chipLine.setTextColor(Design.accent());
        chipLine.setTextSize(10f);
        chipLine.setLetterSpacing(0.06f);
        chipLine.setTypeface(Design.typefaceBold());
        chipLine.setPadding(0, dp(3), 0, dp(8));
        box.addView(chipLine);

        if (it.description != null && !it.description.isEmpty()) {
            TextView d = new TextView(act);
            d.setText(it.description);
            d.setTextColor(Design.dim());
            d.setTextSize(13f);
            d.setPadding(0, 0, 0, dp(6));
            box.addView(d);
        }

        kvCopy(box, "Token ID", it.tokenid);
        if (it.coin != null) kvCopy(box, "Coin ID", it.coin.coinid);
        if (it.creator != null && !it.creator.isEmpty()) kvCopy(box, "Creator", it.creator);
        if (it.stateNft && it.idx > 0) kvCopy(box, "Identity (state 0)", it.idx + " · sealed");
        if (it.externalUrl != null && !it.externalUrl.isEmpty()) linkRow(box, "External URL", it.externalUrl);
        if (it.webvalidate != null && !it.webvalidate.isEmpty()) linkRow(box, "Web validation", it.webvalidate);

        androidx.appcompat.app.AlertDialog.Builder b = new androidx.appcompat.app.AlertDialog.Builder(act)
                .setView(sv)
                .setNegativeButton("Close", null);

        final boolean fav = favs().contains(it.favKey);
        b.setNeutralButton(fav ? "♥ Unfavourite" : "♡ Favourite", (d, w) -> toggleFav(it.favKey));

        if (it.stateNft && it.coin != null) {
            final String display = it.name;
            final String collection = it.meta == null ? it.name : it.meta.name;
            b.setPositiveButton("Transfer", (d, w) ->
                    StateNftActions.transferDialog(act, it.tokenid, display, it.coin.raw));
            final androidx.appcompat.app.AlertDialog dlg = b.show();
            // Bury lives behind long-press on Transfer? No — add a dedicated row instead.
            TextView bury = new TextView(act);
            bury.setText("✝  Bury this item (irreversible)");
            bury.setTextColor(Design.red());
            bury.setTextSize(12f);
            bury.setPadding(0, dp(12), 0, dp(4));
            bury.setOnClickListener(v -> {
                dlg.dismiss();
                StateNftActions.buryDialog(act, it.tokenid, collection, display, it.coin.raw);
            });
            box.addView(bury);
        } else {
            b.setPositiveButton("Send", (d, w) -> act.sendToken(it.tokenid));
            b.show();
        }

        // fire the web-validation check so the badge is fresh next open
        if (it.webvalidate != null && !it.webvalidate.isEmpty()) {
            WebValidate.ensure(act, it.tokenid, it.webvalidate, () -> {});
        }
    }

    /** Full-screen, full-resolution image. Tap to dismiss. */
    private void showImageFull(String url) {
        ImageView iv = new ImageView(act);
        iv.setAdjustViewBounds(true);
        iv.setBackgroundColor(0xFF000000);
        iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageLoader.loadFull(act, url, iv, R.drawable.ic_coin_placeholder);
        androidx.appcompat.app.AlertDialog dlg =
                new androidx.appcompat.app.AlertDialog.Builder(act).setView(iv).create();
        iv.setOnClickListener(v -> dlg.dismiss());
        dlg.show();
    }

    private void kvCopy(LinearLayout box, final String label, final String value) {
        LinearLayout r = new LinearLayout(act);
        r.setOrientation(LinearLayout.HORIZONTAL);
        r.setPadding(0, dp(4), 0, dp(4));
        TextView l = new TextView(act);
        l.setText(label);
        l.setTextColor(Design.dim());
        l.setTextSize(12f);
        r.addView(l, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
        TextView v = new TextView(act);
        v.setText(value.length() > 26 ? Util.shorten(value) : value);
        v.setTextColor(Design.text());
        v.setTextSize(12f);
        v.setTypeface(Typeface.MONOSPACE);
        v.setGravity(Gravity.END);
        r.addView(v, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.4f));
        r.setOnClickListener(x -> CoinDetailDialog.copy(act, label, value));
        box.addView(r);
    }

    private void linkRow(LinearLayout box, String label, final String url) {
        LinearLayout r = new LinearLayout(act);
        r.setOrientation(LinearLayout.HORIZONTAL);
        r.setPadding(0, dp(6), 0, dp(6));
        TextView l = new TextView(act);
        l.setText(label);
        l.setTextColor(Design.dim());
        l.setTextSize(12f);
        r.addView(l, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
        TextView v = new TextView(act);
        v.setText("↗ open");
        v.setTextColor(Design.accent());
        v.setTextSize(12f);
        v.setGravity(Gravity.END);
        r.addView(v);
        r.setOnClickListener(x -> {
            try { act.startActivity(new android.content.Intent(
                    android.content.Intent.ACTION_VIEW, android.net.Uri.parse(url))); }
            catch (Exception ignore) {}
        });
        box.addView(r);
    }

    private static int parseInt(String s) {
        try { return Integer.parseInt(s); } catch (Exception e) { return -1; }
    }

    private int dp(int v) { return (int) (v * act.getResources().getDisplayMetrics().density); }
}
