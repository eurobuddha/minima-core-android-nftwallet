package com.eurobuddha.nftwallet;

import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * The Mint tab: three creation paths (Token / NFT / State NFT collection) plus the resumable
 * recent-mints list. Stage-1 shell — the forms and the MintEngine wiring land in stages 3–4.
 */
public class MintView extends BaseView {

    private final LinearLayout root;

    public MintView(MainActivity a) {
        super(a, R.layout.view_mint);
        root = find(R.id.mintRoot);
    }

    @Override
    public void refresh() {
        root.removeAllViews();
        TextView t = new TextView(act);
        t.setText("Mint suite — coming online in this build.");
        t.setTextColor(Design.dim());
        t.setTextSize(14);
        t.setTypeface(Design.typeface());
        t.setPadding(8, 24, 8, 24);
        root.addView(t);
    }
}
