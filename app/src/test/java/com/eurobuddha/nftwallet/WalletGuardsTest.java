package com.eurobuddha.nftwallet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

import java.util.List;

/**
 * Guards that stand between chain-supplied data and a node command.
 *
 * The node serialises a JSON-shaped token name into its replies unescaped, so a hostile token can
 * inject synthetic fields — including ids — into a balance/coins reply. Node commands are parsed by
 * whitespace and multi-commands split on ';', so an unvetted id in a command is code execution.
 */
public class WalletGuardsTest {

    @Test public void hexIdAcceptsRealIds() {
        assertTrue(Util.isValidHexId("0x00"));
        assertTrue(Util.isValidHexId(
                "0x3C451D33AA1B2C3D4E5F60718293A4B5C6D7E8F90A1B2C3D4E5F60718293A4B5"));
        assertTrue(Util.isValidHexId("0xFED5"));
    }

    @Test public void hexIdRejectsInjectionShapes() {
        assertFalse(Util.isValidHexId(null));
        assertFalse(Util.isValidHexId(""));
        assertFalse(Util.isValidHexId("0xAB;send address:Mx1 amount:99"));   // command chaining
        assertFalse(Util.isValidHexId("0xAB burn:100"));                     // extra parameter
        assertFalse(Util.isValidHexId("0xAB\nsend"));                        // newline
        assertFalse(Util.isValidHexId("nothex"));
        assertFalse(Util.isValidHexId("0xZZ"));
        assertFalse(Util.isValidHexId("0x"));
    }

    @Test public void webUrlAllowsOnlyPlainHttp() {
        assertTrue(Util.isWebUrl("https://minima.global/nft.txt"));
        assertTrue(Util.isWebUrl("http://example.com"));
        assertFalse(Util.isWebUrl("javascript:alert(1)"));
        assertFalse(Util.isWebUrl("file:///data/data/com.eurobuddha.nftwallet/"));
        assertFalse(Util.isWebUrl("intent://scan/#Intent;scheme=x;end"));
        assertFalse(Util.isWebUrl("https://example.com/a b"));
        assertFalse(Util.isWebUrl(null));
    }

    /** A stamped unit holding more than one token must move its real amount, not a hardcoded 1. */
    @Test public void transferUsesTheCoinsRealAmount() throws Exception {
        JSONObject coin = new JSONObject();
        coin.put("coinid", "0xC01D");
        coin.put("tokenamount", "3");
        JSONArray state = new JSONArray();
        state.put(new JSONObject().put("port", "0").put("data", "7"));
        coin.put("state", state);

        List<String> cmds = StateNft.transferCommands("tr1", "0xT0K", coin, "0xDEST");
        assertTrue(joined(cmds).contains("txnoutput id:tr1 amount:3 address:0xDEST"));
    }

    /** Transfers must pre-delete, balance-check before signing, and replay every state port. */
    @Test public void transferIsPreDeletedCheckedAndReplaysState() throws Exception {
        JSONObject coin = new JSONObject();
        coin.put("coinid", "0xC01D");
        coin.put("tokenamount", "1");
        JSONArray state = new JSONArray();
        state.put(new JSONObject().put("port", "0").put("data", "2"));
        state.put(new JSONObject().put("port", "1").put("data", "[QUJD]"));
        coin.put("state", state);

        List<String> cmds = StateNft.transferCommands("tr2", "0xT0K", coin, "0xDEST");
        assertEquals("txndelete id:tr2", cmds.get(0));
        assertEquals("txncreate id:tr2", cmds.get(1));

        String all = joined(cmds);
        assertTrue(all.contains("storestate:true"));
        assertTrue(all.contains("txnstate id:tr2 port:0 value:2"));
        assertTrue(all.contains("txnstate id:tr2 port:1 value:[QUJD]"));
        // the balance check must come before any signature
        assertTrue(all.indexOf("txncheck") < all.indexOf("txnsign"));
        assertTrue(all.indexOf("txnsign") < all.indexOf("txnpost"));
    }

    /** Hostile state (a value that could smuggle extra command parameters) is never replayable. */
    @Test public void hostileStateIsRefused() throws Exception {
        JSONObject coin = new JSONObject();
        coin.put("coinid", "0xC01D");
        JSONArray state = new JSONArray();
        state.put(new JSONObject().put("port", "0").put("data", "1 burn:100"));
        coin.put("state", state);
        assertFalse(StateNft.replayableState(coin));

        JSONObject clean = new JSONObject();
        clean.put("coinid", "0xC01D");
        JSONArray ok = new JSONArray();
        ok.put(new JSONObject().put("port", "0").put("data", "1"));
        clean.put("state", ok);
        assertTrue(StateNft.replayableState(clean));
    }

    private static String joined(List<String> cmds) {
        StringBuilder sb = new StringBuilder();
        for (String c : cmds) sb.append(c).append('\n');
        return sb.toString();
    }
}
