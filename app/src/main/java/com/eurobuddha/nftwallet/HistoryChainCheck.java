package com.eurobuddha.nftwallet;

import android.widget.LinearLayout;
import android.widget.TextView;
import org.json.JSONObject;

/** Explorer's on-demand proof display, with the wallet's strict inclusion parser.
 * Read-only: never changes stored receipts, reservations or transaction outcomes. */
final class HistoryChainCheck {
    static final class Result {
        final JSONObject proof;
        final boolean missing;
        Result(JSONObject proof,boolean missing){this.proof=proof;this.missing=missing;}
    }
    static Result result(JSONObject reply) {
        if(reply==null||!Boolean.TRUE.equals(reply.opt("status"))||Boolean.TRUE.equals(reply.opt("pending")))throw invalid();
        JSONObject response=reply.optJSONObject("response");
        if(response!=null&&Boolean.FALSE.equals(response.opt("found")))return new Result(null,true);
        JSONObject proof=observedInclusion(reply);
        if(!proof.optString("blockid").matches("0x[0-9a-fA-F]{64}"))throw invalid();
        return new Result(proof,false);
    }
    static void show(MainActivity act,LinearLayout parent,String txpowid) {
        TextView status=new TextView(act);
        status.setTextSize(12f);status.setTextColor(Design.dim());
        status.setText("Checking on-chain…");parent.addView(status);
        if(txpowid==null||!txpowid.matches("(?i)0x[0-9a-f]{64}")
                ||new java.math.BigInteger(txpowid.substring(2),16).signum()==0) {
            status.setText("Outcome unverified");return;
        }
        final boolean[] answered={false};
        act.node().cmd("txpow onchain:"+txpowid,new NodeApi.Cb(){
            private boolean accept(){
                if(answered[0]||act.isFinishing()||act.isDestroyed())return false;
                answered[0]=true;return true;
            }
            public void onResult(JSONObject reply){
                if(!accept())return;
                try {
                    Result result=result(reply);
                    CharSequence evidence=result.missing
                            ?"Not found on this node · Outcome unverified"
                            :"✓ on chain · block " + result.proof.optString("block") + " · confirmations: " + result.proof.optString("confirmations");
                    status.setText(evidence + "\nLast node check: " + new java.text.SimpleDateFormat("dd MMM yyyy  HH:mm:ss", java.util.Locale.ENGLISH).format(new java.util.Date()));
                }catch(RuntimeException malformed){status.setText("Outcome unverified");}
            }
            public void onError(String message){
                if(accept())status.setText("Outcome unverified");
            }
        });
    }
    /** Complete current-chain observation; display may show depths below settlement's three blocks. */
    static JSONObject observedInclusion(JSONObject reply) {
        if (reply == null || !Boolean.TRUE.equals(reply.opt("status")) || Boolean.TRUE.equals(reply.opt("pending"))) throw invalid();
        JSONObject value = reply.optJSONObject("response");
        if (value == null || !Boolean.TRUE.equals(value.opt("found"))) throw invalid();
        String blockId = id(value,"blockid");
        if (new java.math.BigInteger(blockId.substring(2),16).signum() == 0) throw invalid();
        java.math.BigInteger block = whole(value,"block"), tip = whole(value,"tip"), depth = whole(value,"confirmations");
        if (block.signum() <= 0 || depth.signum() < 0
                || !tip.subtract(block).equals(depth)) throw invalid();
        try { return new JSONObject().put("found",true).put("blockid",blockId)
                .put("block",block.toString()).put("tip",tip.toString()).put("confirmations",depth.toString()); }
        catch (org.json.JSONException e) { throw invalid(); }
    }
    private static java.math.BigInteger whole(JSONObject object,String key) {
        Object raw=object.opt(key);
        if (!(raw instanceof String) || !((String)raw).matches("[0-9]{1,30}")) throw invalid();
        return new java.math.BigInteger((String)raw);
    }
    static String id(JSONObject value, String key) {
        Object raw = value == null ? null : value.opt(key);
        if (!(raw instanceof String) || !Util.isValidHexId((String) raw)) throw invalid();
        return (String) raw;
    }
    static IllegalArgumentException invalid() { return new IllegalArgumentException("Invalid chain observation"); }

}
