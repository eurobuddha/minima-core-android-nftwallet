# NFT Wallet (native Android)

The full-suite [Minima](https://minima.global) companion wallet — the old official Wallet's
2.47.2-era feature set in a native Java body, plus regular NFT minting and **State NFT
locked-edition collections**.

Talks to the Minima Core node APK (`org.minimarex.minimacore`) exclusively over the
`minimaapi.aar` broadcast-Intent IPC. No embedded node, no local keys — the node signs
(`txnsign publickey:auto`).

## Tabs

| Tab | What it does |
|---|---|
| **Balances** | Search, sendable/confirmed/unconfirmed on every card, verified + web-validation badges, hidden tokens. Token detail sheet: copy-everywhere, every URL openable, contract viewer with StateNFT detection, per-token **coin explorer** → full coin modal (state ports, embedded-art preview). |
| **Gallery** | Two-column NFT grid. Regular NFTs one card per token; StateNFT collections one card **per owned item** with its sealed #index. Favourites, search, deep detail, Send / Transfer / Bury. |
| **Mint** | Token (JSON metadata, ticker, icon URL, webvalidate, custom key/values) · NFT (image → on-chain `<artimage>` embed, or URL/IPFS; editions; signtoken) · **State NFT collection** (2–20 items, embed ≤8000-b64 per item or URL base+index; resumable CREATE→MOVE→SPLIT→STAMP engine with live progress). |
| **Send** | QUICK SEND (token picker, burn, split 1–20, QR scan) and COIN CONTROL (manual UTXO construction from the Coins tab, editable change, confirm breakdown). |
| **Receive** | QR (tap to copy), Mx + 0x, `checkaddress` safety report, full address pool. |
| **Coins** | The utxo coin picker: every UTXO, select inputs for expert sends, long-press → coin modal. Consolidate/distribute tools. |
| **History** | Adaptive `history max:8` paging (never overloads the node), persisted rows, filters, tap detail with input/output breakdown, CSV/JSON export. |

Dual theme from day one: family dark ↔ old-wallet clean light (all four Design modes in ⚙ Settings).

## StateNFT protocol (from mds/statenft-suite — proven on-chain)

- Locked-edition contract, byte-exact: sentinel state 0, creator bypass only while unstamped,
  `SAMESTATE(0 0|0 1)` + `VERIFYOUT(@INPUT GETOUTADDR(@INPUT) @AMOUNT @TOKENID TRUE)`.
- Coins are `sendable:0` — every move is a manual txn replaying **every** state port verbatim
  with `storestate:true`; state values are sanitized (`^[0-9]+$` / `^\[b64\]$`) before replay.
- `txnpost status:true` is never trusted — success = the input coin leaving the UTXO set.
- `txndelete` on every path; ≤3 token outputs per txn, halving on "size too large".
- Unstamped units refuse transfer (the creator bypass is still live on them).

## Build

```bash
JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home" ./gradlew assembleDebug
./gradlew test        # StateNft engine + metadata unit tests
```

Release via the family script: `support/release.sh NFTwallet minima-nft-wallet <X.Y.Z> "notes"`.

## Lineage

Scaffolded from `apks/utxo` (shell, rendering stack, txn builder, history), hardened `NodeApi`
from `apks/pandadex`, newest `minimaapi.aar` from `apks/base/dist` (content:// large-response
hand-off), StateNFT engine from `mds/statenft-suite/android`, QR writer from `apks/nftstudio`.
