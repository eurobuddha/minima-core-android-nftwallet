package com.eurobuddha.nftwallet;

import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * The Gallery tab: two-column NFT grid with Collected / Favourites, search by
 * name / creator / tokenid, and the deep NFT detail view. Stage-1 shell — the grid
 * and detail land in stage 5.
 */
public class GalleryView extends BaseView {

    private final LinearLayout header;

    public GalleryView(MainActivity a) {
        super(a, R.layout.view_gallery);
        header = find(R.id.galleryHeader);
    }

    @Override
    public void refresh() {
        header.removeAllViews();
        TextView t = new TextView(act);
        t.setText("Gallery — coming online in this build.");
        t.setTextColor(Design.dim());
        t.setTextSize(14);
        t.setTypeface(Design.typeface());
        t.setPadding(8, 24, 8, 24);
        header.addView(t);
    }
}
