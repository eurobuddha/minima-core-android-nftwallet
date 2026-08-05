# Graph Report - NFTwallet  (2026-08-05)

## Corpus Check
- 47 files · ~48,779 words
- Verdict: corpus is large enough that graph structure adds value.

## Summary
- 777 nodes · 2015 edges · 37 communities (29 shown, 8 thin omitted)
- Extraction: 95% EXTRACTED · 5% INFERRED · 0% AMBIGUOUS · INFERRED: 93 edges (avg confidence: 0.8)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `cead0d24`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- Cb
- Coin
- MintView
- MainActivity
- Design
- BalancesView
- DistributeManager
- SendView
- NodeApi
- GalleryView
- BaseView
- StateNft
- .show
- NodeTx
- ImageLoader
- Identicon
- TokenBalance
- Screen
- QrUtil
- gradlew
- LocalStore
- .onCreate
- CmdChain
- .show
- HiddenTokens
- ImageTools.java
- Bitmap
- Drawable
- ViewGroup
- Handler
- Uri
- Cb

## God Nodes (most connected - your core abstractions)
1. `MainActivity` - 102 edges
2. `MintView` - 50 edges
3. `Cb` - 36 edges
4. `SendView` - 36 edges
5. `Design` - 36 edges
6. `MintEngine` - 32 edges
7. `BalancesView` - 28 edges
8. `GalleryView` - 25 edges
9. `HistoryView` - 24 edges
10. `StateNft` - 22 edges

## Surprising Connections (you probably didn't know these)
- `GItem` --references--> `Meta`  [EXTRACTED]
  app/src/main/java/com/eurobuddha/nftwallet/GalleryView.java → app/src/main/java/com/eurobuddha/nftwallet/StateNft.java
- `BaseView` --references--> `MainActivity`  [EXTRACTED]
  app/src/main/java/com/eurobuddha/nftwallet/BaseView.java → app/src/main/java/com/eurobuddha/nftwallet/MainActivity.java
- `DistributeManager` --references--> `MainActivity`  [EXTRACTED]
  app/src/main/java/com/eurobuddha/nftwallet/DistributeManager.java → app/src/main/java/com/eurobuddha/nftwallet/MainActivity.java
- `TxnBuilder` --references--> `MainActivity`  [EXTRACTED]
  app/src/main/java/com/eurobuddha/nftwallet/TxnBuilder.java → app/src/main/java/com/eurobuddha/nftwallet/MainActivity.java
- `WalletTools` --references--> `MainActivity`  [EXTRACTED]
  app/src/main/java/com/eurobuddha/nftwallet/WalletTools.java → app/src/main/java/com/eurobuddha/nftwallet/MainActivity.java

## Import Cycles
- None detected.

## Communities (37 total, 8 thin omitted)

### Community 0 - "Cb"
Cohesion: 0.18
Nodes (8): Cb, CoinsCb, Done, Context, JSONArray, JSONObject, NodeApi, MintEngine

### Community 1 - "Coin"
Cohesion: 0.07
Nodes (14): Coin, JSONObject, Done, JSONObject, Out, OutCoin, Progress, TxnBuilder (+6 more)

### Community 2 - "MintView"
Cohesion: 0.15
Nodes (11): Button, EditText, ImageView, JSONObject, LinearLayout, OnClickListener, Override, TextView (+3 more)

### Community 3 - "MainActivity"
Cohesion: 0.09
Nodes (12): ActivityResultLauncher, BaseView, Coin, JSONObject, TextView, View, MainActivity, AppCompatActivity (+4 more)

### Community 4 - "Design"
Cohesion: 0.06
Nodes (22): Design, Context, Mode, CLEAN_LIGHT, CURRENT, ORIGINAL_DARK, ORIGINAL_LIGHT, ImageView (+14 more)

### Community 5 - "BalancesView"
Cohesion: 0.14
Nodes (11): BalancesView, Coin, EditText, LinearLayout, Override, TextView, TokenBalance, View (+3 more)

### Community 6 - "DistributeManager"
Cohesion: 0.15
Nodes (4): DistributeJob, Context, JSONObject, DistributeManager

### Community 7 - "SendView"
Cohesion: 0.07
Nodes (16): HistoryView, LinearLayout, Override, View, Button, Coin, EditText, LinearLayout (+8 more)

### Community 8 - "NodeApi"
Cohesion: 0.16
Nodes (8): Cb, Context, Handler, JSONObject, NodeApi, PairingListener, MinimaAPI, MinimaAPIListener

### Community 9 - "GalleryView"
Cohesion: 0.13
Nodes (19): Adapter, GalleryAdapter, GalleryView, GItem, Holder, Coin, EditText, ImageView (+11 more)

### Community 10 - "BaseView"
Cohesion: 0.15
Nodes (8): BaseView, View, Override, View, ViewGroup, MainPager, NonNull, PagerAdapter

### Community 11 - "StateNft"
Cohesion: 0.07
Nodes (13): AlertDialog, Item, JSONArray, JSONObject, Pattern, Meta, StateNft, JSONObject (+5 more)

### Community 12 - ".show"
Cohesion: 0.22
Nodes (7): CoinDetailDialog, Coin, Context, LinearLayout, OnClickListener, Pattern, TextView

### Community 13 - "NodeTx"
Cohesion: 0.11
Nodes (9): HistoryDb, Context, Override, HistoryRow, JSONArray, JSONObject, NodeTx, SQLiteDatabase (+1 more)

### Community 14 - "ImageLoader"
Cohesion: 0.16
Nodes (5): ImageLoader, Bitmap, ImageView, WebValidate, LruCache

### Community 15 - "Identicon"
Cohesion: 0.41
Nodes (4): Identicon, Bitmap, Canvas, Paint

### Community 16 - "TokenBalance"
Cohesion: 0.16
Nodes (5): IconResolver, Pattern, JSONObject, TokenBalance, TokenMeta

### Community 17 - "Screen"
Cohesion: 0.33
Nodes (6): Screen, COLLECTION, HUB, NFT, PROGRESS, TOKEN

### Community 19 - "gradlew"
Cohesion: 0.60
Nodes (3): gradlew script, die(), warn()

### Community 23 - "LocalStore"
Cohesion: 0.32
Nodes (5): Context, JSONArray, JSONObject, LocalStore, SharedPreferences

### Community 24 - ".onCreate"
Cohesion: 0.13
Nodes (6): NodeApi, Override, Bundle, DistributeManager, HistoryDb, MainPager

### Community 25 - "CmdChain"
Cohesion: 0.24
Nodes (4): CmdChain, Done, JSONObject, NodeApi

### Community 26 - ".show"
Cohesion: 0.31
Nodes (3): TokenBalance, TextView, SettingsDialog

### Community 28 - "ImageTools.java"
Cohesion: 0.47
Nodes (3): ImageTools, Context, Uri

## Knowledge Gaps
- **9 isolated node(s):** `HUB`, `TOKEN`, `NFT`, `COLLECTION`, `PROGRESS` (+4 more)
  These have ≤1 connection - possible missing edges or undocumented components.
- **8 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `MainActivity` connect `MainActivity` to `Coin`, `Design`, `BalancesView`, `DistributeManager`, `SendView`, `GalleryView`, `BaseView`, `StateNft`, `.show`, `ImageLoader`, `.onCreate`, `.show`, `.reload`, `.fetchCoinsFor`?**
  _High betweenness centrality (0.435) - this node is a cross-community bridge._
- **Why does `Cb` connect `Cb` to `MintView`, `MainActivity`, `BalancesView`, `SendView`, `GalleryView`, `StateNft`, `.show`, `CmdChain`, `.reload`, `.fetchCoinsFor`?**
  _High betweenness centrality (0.115) - this node is a cross-community bridge._
- **Why does `MintView` connect `MintView` to `Screen`, `MainActivity`, `GalleryView`?**
  _High betweenness centrality (0.088) - this node is a cross-community bridge._
- **Are the 21 inferred relationships involving `Cb` (e.g. with `.loadCoins()` and `.loadContract()`) actually correct?**
  _`Cb` has 21 INFERRED edges - model-reasoned connections that need verification._
- **What connects `HUB`, `TOKEN`, `NFT` to the rest of the system?**
  _9 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `Coin` be split into smaller, more focused modules?**
  _Cohesion score 0.06806526806526807 - nodes in this community are weakly interconnected._
- **Should `MintView` be split into smaller, more focused modules?**
  _Cohesion score 0.14876632801161102 - nodes in this community are weakly interconnected._