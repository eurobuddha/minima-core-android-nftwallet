package com.eurobuddha.nftwallet;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Turns a picked image into base64 small enough to live on-chain.
 *
 * Two things this must get right, because what it produces is sealed into a locked-edition coin
 * and can never be corrected:
 *
 *  1. ORIENTATION. Phone cameras store the sensor image and record how to rotate it in an EXIF
 *     tag; BitmapFactory ignores that tag entirely. Without the fix below, every portrait photo
 *     is stamped 90° out and the whole collection is ruined.
 *  2. QUALITY. The ladder searches for the best-looking image that fits the byte budget, starting
 *     from a resolution worth looking at rather than bottoming out at a blurry thumbnail.
 */
public final class ImageTools {

    /** Starting long edge, in pixels. Dropped only when quality alone can't reach the budget. */
    private static final int START_DIM = 720;
    /** Never go below this — past here the picture isn't worth sealing on-chain. */
    private static final int MIN_DIM = 240;
    private static final int START_QUALITY = 92;
    /** Below this JPEG gets visibly blocky; shrink the image instead. */
    private static final int MIN_QUALITY = 62;
    private static final int QUALITY_STEP = 6;

    private ImageTools() {}

    public static String compressUri(Context c, Uri uri, int budget) throws Exception {
        byte[] raw;
        try (InputStream in = c.getContentResolver().openInputStream(uri);
             ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            if (in == null) return "";
            byte[] buf = new byte[8192];
            int n;
            while ((n = in.read(buf)) > 0) bos.write(buf, 0, n);
            raw = bos.toByteArray();
        }
        Bitmap src = BitmapFactory.decodeByteArray(raw, 0, raw.length);
        if (src == null) return "";
        src = applyExifOrientation(src, raw);

        // Best quality that fits: hold the resolution and walk quality down first, only then
        // shrink. (The old ladder started at 400px and fell to quality 45 — visibly mushy.)
        int dim = START_DIM;
        while (dim >= MIN_DIM) {
            Bitmap scaled = scaleToLongEdge(src, dim);
            for (int quality = START_QUALITY; quality >= MIN_QUALITY; quality -= QUALITY_STEP) {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                scaled.compress(Bitmap.CompressFormat.JPEG, quality, out);
                String b64 = Base64.encodeToString(out.toByteArray(), Base64.NO_WRAP);
                if (b64.length() <= budget) return b64;
            }
            dim = Math.round(dim * 0.8f);
        }
        return "";
    }

    /** Scale so the LONG edge is at most {@code dim}, preserving aspect ratio. */
    private static Bitmap scaleToLongEdge(Bitmap src, int dim) {
        float scale = Math.min(1f, dim / (float) Math.max(src.getWidth(), src.getHeight()));
        if (scale >= 1f) return src;
        return Bitmap.createScaledBitmap(src,
                Math.max(1, Math.round(src.getWidth() * scale)),
                Math.max(1, Math.round(src.getHeight() * scale)), true);
    }

    /**
     * Rotate/flip the decoded bitmap to match its EXIF orientation tag.
     *
     * Camera photos are almost always stored in the sensor's native landscape with a tag saying
     * how to present them. Every gallery honours the tag; BitmapFactory does not — so a portrait
     * photo decodes sideways. We bake the correction into the pixels here, because the JPEG we
     * re-encode carries no EXIF at all and whatever we produce is what gets sealed on-chain.
     */
    private static Bitmap applyExifOrientation(Bitmap src, byte[] raw) {
        int orientation;
        try (InputStream in = new ByteArrayInputStream(raw)) {
            orientation = new ExifInterface(in)
                    .getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        } catch (Throwable t) {
            return src;   // no EXIF (PNG, screenshot, stripped) — the pixels are already upright
        }

        Matrix m = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:      m.setRotate(90); break;
            case ExifInterface.ORIENTATION_ROTATE_180:     m.setRotate(180); break;
            case ExifInterface.ORIENTATION_ROTATE_270:     m.setRotate(270); break;
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL: m.setScale(-1f, 1f); break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:   m.setScale(1f, -1f); break;
            case ExifInterface.ORIENTATION_TRANSPOSE:      m.setRotate(90);  m.postScale(-1f, 1f); break;
            case ExifInterface.ORIENTATION_TRANSVERSE:     m.setRotate(270); m.postScale(-1f, 1f); break;
            default: return src;                           // NORMAL or UNDEFINED
        }
        try {
            Bitmap fixed = Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), m, true);
            if (fixed != src) src.recycle();
            return fixed;
        } catch (Throwable t) {
            return src;   // OOM on a huge image — an upright-but-unrotated picture beats no picture
        }
    }
}
