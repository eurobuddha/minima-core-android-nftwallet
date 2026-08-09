package com.eurobuddha.nftwallet;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * The token definition (icon + name + description + external URL + script
 * scaffolding) is IMMUTABLE after tokencreate, and past ~10.5K estimated chars
 * it can no longer be split under the 64KB TxPoW cap — every token-carrying
 * output AND the input embed the full definition, plus a multi-KB signature.
 * Calibrated against Atelier's measured failure: an 18.4KB definition
 * (estimate ~11.9K) never split; estimate-to-actual runs ~1.55x.
 */
public class DefBudgetTest {

    @Test public void heavyIconAndTextTripTheGuard() {
        String icon = "A".repeat(6000);
        String longDesc = "B".repeat(5000);
        assertTrue(StateNft.estimatedDefLen(icon, "Math", longDesc, "") > StateNft.DEF_BUDGET);
    }

    @Test public void slimmedIconPasses() {
        String slim = "A".repeat(4000);
        String longDesc = "B".repeat(5000);
        assertTrue(StateNft.estimatedDefLen(slim, "Math", longDesc, "") <= StateNft.DEF_BUDGET);
    }

    @Test public void normalCollectionIsNowhereNearTheBudget() {
        // 6K icon (the ICON_BUDGET cap) + realistic text — the everyday case
        assertTrue(StateNft.estimatedDefLen("A".repeat(6000), "My Collection",
                "Twenty sealed plates", "https://example.com") <= StateNft.DEF_BUDGET);
    }

    @Test public void nullsCountAsZero() {
        assertEquals(900, StateNft.estimatedDefLen(null, null, null, null));
    }

    /* (k units + change + input) full token definitions must fit ~40KB under
     * the 64KB TxPoW cap — a fixed 3-unit batch silently stalled heavy mints. */
    @Test public void splitBatchSizesToTokenDefinition() {
        assertEquals(3, MintEngine.splitBatch(7000));    // legacy: proven batch
        assertEquals(1, MintEngine.splitBatch(11079));   // heavy: unit + change
        assertEquals(3, MintEngine.splitBatch(500));     // tiny: capped
        assertEquals(2, MintEngine.splitBatch(10000));
        assertEquals(1, MintEngine.splitBatch(90000));   // degenerate: floor 1
    }
}
