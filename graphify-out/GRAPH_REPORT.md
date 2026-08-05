# Graph Report - apks/NFTwallet  (2026-08-05)

## Corpus Check
- 47 files · ~49,646 words
- Verdict: corpus is large enough that graph structure adds value.

## Summary
- 802 nodes · 2330 edges · 33 communities (28 shown, 5 thin omitted)
- Extraction: 95% EXTRACTED · 5% INFERRED · 0% AMBIGUOUS · INFERRED: 106 edges (avg confidence: 0.8)
- Token cost: 0 input · 0 output

## Community Hubs (Navigation)
- NodeApi
- TxnBuilder
- MintView
- MainActivity
- Design
- .showTokenDetail
- HistoryView
- SendView
- GalleryView
- BaseView
- StateNft
- .show
- HistoryDb
- ImageLoader
- WalletTools
- .isValidHexId
- Screen
- QrUtil
- gradlew
- LocalStore
- .onCreate
- CmdChain
- .show
- utxoWallet — Native Android clone: figma-style mapping & build blueprint
- DistributeManager
- .recordPosting
- Coin
- NFT Wallet (native Android)
- User instructions — AUTHORITATIVE. These override default behavior and must be followed exactly.

## God Nodes (most connected - your core abstractions)
1. `MainActivity` - 103 edges
2. `MintView` - 50 edges
3. `NodeApi` - 40 edges
4. `Design` - 36 edges
5. `SendView` - 36 edges
6. `Coin` - 33 edges
7. `MintEngine` - 33 edges
8. `BalancesView` - 28 edges
9. `GalleryView` - 25 edges
10. `HistoryView` - 24 edges

## Surprising Connections (you probably didn't know these)
- `BalancesView` --inherits--> `BaseView`  [EXTRACTED]
  apks/NFTwallet/app/src/main/java/com/eurobuddha/nftwallet/BalancesView.java → apks/NFTwallet/app/src/main/java/com/eurobuddha/nftwallet/BaseView.java
- `BaseView` --references--> `MainActivity`  [EXTRACTED]
  apks/NFTwallet/app/src/main/java/com/eurobuddha/nftwallet/BaseView.java → apks/NFTwallet/app/src/main/java/com/eurobuddha/nftwallet/MainActivity.java
- `GalleryView` --inherits--> `BaseView`  [EXTRACTED]
  apks/NFTwallet/app/src/main/java/com/eurobuddha/nftwallet/GalleryView.java → apks/NFTwallet/app/src/main/java/com/eurobuddha/nftwallet/BaseView.java
- `HistoryView` --inherits--> `BaseView`  [EXTRACTED]
  apks/NFTwallet/app/src/main/java/com/eurobuddha/nftwallet/HistoryView.java → apks/NFTwallet/app/src/main/java/com/eurobuddha/nftwallet/BaseView.java
- `MintView` --inherits--> `BaseView`  [EXTRACTED]
  apks/NFTwallet/app/src/main/java/com/eurobuddha/nftwallet/MintView.java → apks/NFTwallet/app/src/main/java/com/eurobuddha/nftwallet/BaseView.java

## Import Cycles
- None detected.

## Communities (33 total, 5 thin omitted)

### Community 0 - "NodeApi"
Cohesion: 0.11
Nodes (15): Cb, CoinsCb, Done, Context, JSONArray, JSONObject, MintEngine, Cb (+7 more)

### Community 1 - "TxnBuilder"
Cohesion: 0.17
Nodes (5): Done, JSONObject, OutCoin, Progress, TxnBuilder

### Community 2 - "MintView"
Cohesion: 0.11
Nodes (14): ImageTools, Context, Uri, Button, EditText, ImageView, JSONObject, LinearLayout (+6 more)

### Community 3 - "MainActivity"
Cohesion: 0.10
Nodes (10): ActivityResultLauncher, Handler, JSONObject, TextView, Uri, View, MainActivity, AppCompatActivity (+2 more)

### Community 4 - "Design"
Cohesion: 0.10
Nodes (16): Design, Context, Mode, CLEAN_LIGHT, CURRENT, ORIGINAL_DARK, ORIGINAL_LIGHT, Button (+8 more)

### Community 5 - ".showTokenDetail"
Cohesion: 0.11
Nodes (11): BalancesView, Bitmap, Drawable, EditText, LinearLayout, Override, TextView, View (+3 more)

### Community 6 - "HistoryView"
Cohesion: 0.13
Nodes (7): HistoryView, LinearLayout, Override, View, JSONArray, JSONObject, NodeTx

### Community 7 - "SendView"
Cohesion: 0.12
Nodes (9): Button, EditText, LinearLayout, Override, TextView, View, SendView, JSONObject (+1 more)

### Community 9 - "GalleryView"
Cohesion: 0.11
Nodes (18): Adapter, GalleryAdapter, GalleryView, GItem, Holder, EditText, ImageView, JSONObject (+10 more)

### Community 10 - "BaseView"
Cohesion: 0.08
Nodes (14): BaseView, View, Override, View, ViewGroup, MainPager, ImageView, LinearLayout (+6 more)

### Community 11 - "StateNft"
Cohesion: 0.09
Nodes (12): AlertDialog, Item, JSONArray, JSONObject, Pattern, Meta, StateNft, Cb (+4 more)

### Community 12 - ".show"
Cohesion: 0.26
Nodes (6): CoinDetailDialog, Context, LinearLayout, OnClickListener, Pattern, TextView

### Community 13 - "HistoryDb"
Cohesion: 0.16
Nodes (6): HistoryDb, Context, Override, HistoryRow, SQLiteDatabase, SQLiteOpenHelper

### Community 14 - "ImageLoader"
Cohesion: 0.16
Nodes (8): Identicon, Bitmap, ImageLoader, Bitmap, ImageView, Canvas, LruCache, Paint

### Community 15 - "WalletTools"
Cohesion: 0.19
Nodes (4): EditText, LinearLayout, Status, WalletTools

### Community 16 - ".isValidHexId"
Cohesion: 0.09
Nodes (7): IconResolver, Pattern, JSONArray, JSONObject, TokenMeta, Test, WalletGuardsTest

### Community 17 - "Screen"
Cohesion: 0.33
Nodes (6): Screen, COLLECTION, HUB, NFT, PROGRESS, TOKEN

### Community 19 - "gradlew"
Cohesion: 0.60
Nodes (3): gradlew script, die(), warn()

### Community 23 - "LocalStore"
Cohesion: 0.18
Nodes (8): DistributeJob, Context, JSONObject, Context, JSONArray, JSONObject, LocalStore, SharedPreferences

### Community 24 - ".onCreate"
Cohesion: 0.15
Nodes (3): Override, BroadcastReceiver, Bundle

### Community 25 - "CmdChain"
Cohesion: 0.24
Nodes (3): CmdChain, Done, JSONObject

### Community 27 - "utxoWallet — Native Android clone: figma-style mapping & build blueprint"
Cohesion: 0.08
Nodes (24): 0. Design languages (runtime toggle), 1.1 ORIGINAL — light (`:root`, default), 1.2 ORIGINAL — dark (`:root[data-theme="dark"]`), 1.3 CURRENT (existing native dark), 1.4 Type & metrics (ORIGINAL), 1.5 Component note colors (`.field-note`, `.toast`, pills), 1. Design tokens, 2. Component catalog (ORIGINAL; CURRENT = Material equivalents) (+16 more)

### Community 29 - ".recordPosting"
Cohesion: 0.24
Nodes (3): Out, AddrList, TxnUtil

### Community 31 - "NFT Wallet (native Android)"
Cohesion: 0.33
Nodes (5): Build, Lineage, NFT Wallet (native Android), StateNFT protocol (from mds/statenft-suite — proven on-chain), Tabs

## Knowledge Gaps
- **34 isolated node(s):** `ORIGINAL_LIGHT`, `ORIGINAL_DARK`, `CURRENT`, `CLEAN_LIGHT`, `HUB` (+29 more)
  These have ≤1 connection - possible missing edges or undocumented components.
- **5 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `MainActivity` connect `MainActivity` to `NodeApi`, `TxnBuilder`, `Design`, `.showTokenDetail`, `HistoryView`, `SendView`, `.cmd`, `GalleryView`, `BaseView`, `StateNft`, `.show`, `HistoryDb`, `ImageLoader`, `WalletTools`, `.isValidHexId`, `LocalStore`, `.onCreate`, `.show`, `DistributeManager`, `.recordPosting`, `Coin`?**
  _High betweenness centrality (0.325) - this node is a cross-community bridge._
- **Why does `NodeApi` connect `NodeApi` to `TxnBuilder`, `MainActivity`, `.cmd`, `.onCreate`, `CmdChain`, `.show`?**
  _High betweenness centrality (0.109) - this node is a cross-community bridge._
- **Why does `BaseView` connect `BaseView` to `MintView`, `MainActivity`, `Design`, `.showTokenDetail`, `HistoryView`, `SendView`, `GalleryView`?**
  _High betweenness centrality (0.076) - this node is a cross-community bridge._
- **What connects `ORIGINAL_LIGHT`, `ORIGINAL_DARK`, `CURRENT` to the rest of the system?**
  _34 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `NodeApi` be split into smaller, more focused modules?**
  _Cohesion score 0.11494252873563218 - nodes in this community are weakly interconnected._
- **Should `MintView` be split into smaller, more focused modules?**
  _Cohesion score 0.11187122736418512 - nodes in this community are weakly interconnected._
- **Should `MainActivity` be split into smaller, more focused modules?**
  _Cohesion score 0.10256410256410256 - nodes in this community are weakly interconnected._