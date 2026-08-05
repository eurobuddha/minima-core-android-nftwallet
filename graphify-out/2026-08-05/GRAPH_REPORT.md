# Graph Report - NFTwallet  (2026-08-05)

## Corpus Check
- 47 files · ~49,646 words
- Verdict: corpus is large enough that graph structure adds value.

## Summary
- 799 nodes · 2014 edges · 47 communities (28 shown, 19 thin omitted)
- Extraction: 95% EXTRACTED · 5% INFERRED · 0% AMBIGUOUS · INFERRED: 101 edges (avg confidence: 0.8)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `59039cf4`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- Cb
- Coin
- MintView
- MainActivity
- Design
- BalancesView
- HistoryView
- SendView
- NodeApi
- GalleryView
- BaseView
- StateNft
- .show
- HistoryDb
- ImageLoader
- WalletTools
- .parse
- Screen
- QrUtil
- gradlew
- LocalStore
- .onCreate
- CmdChain
- .show
- HiddenTokens
- ImageTools.java
- StateNftActions
- Coin
- Bitmap
- Drawable
- ViewGroup
- Handler
- Uri
- Cb
- MainActivity.java
- TokenBalance
- Pattern
- Context
- Context
- Button
- EditText
- LinearLayout
- TokenBalance
- NodeApi

## God Nodes (most connected - your core abstractions)
1. `MainActivity` - 105 edges
2. `MintView` - 50 edges
3. `SendView` - 36 edges
4. `Design` - 36 edges
5. `Cb` - 35 edges
6. `MintEngine` - 33 edges
7. `BalancesView` - 28 edges
8. `GalleryView` - 25 edges
9. `HistoryView` - 24 edges
10. `Coin` - 23 edges

## Surprising Connections (you probably didn't know these)
- `BaseView` --references--> `MainActivity`  [EXTRACTED]
  app/src/main/java/com/eurobuddha/nftwallet/BaseView.java → app/src/main/java/com/eurobuddha/nftwallet/MainActivity.java
- `DistributeManager` --references--> `MainActivity`  [EXTRACTED]
  app/src/main/java/com/eurobuddha/nftwallet/DistributeManager.java → app/src/main/java/com/eurobuddha/nftwallet/MainActivity.java
- `MainActivity` --references--> `TokenBalance`  [EXTRACTED]
  app/src/main/java/com/eurobuddha/nftwallet/MainActivity.java → app/src/main/java/com/eurobuddha/nftwallet/TokenBalance.java
- `TxnBuilder` --references--> `MainActivity`  [EXTRACTED]
  app/src/main/java/com/eurobuddha/nftwallet/TxnBuilder.java → app/src/main/java/com/eurobuddha/nftwallet/MainActivity.java
- `WalletTools` --references--> `MainActivity`  [EXTRACTED]
  app/src/main/java/com/eurobuddha/nftwallet/WalletTools.java → app/src/main/java/com/eurobuddha/nftwallet/MainActivity.java

## Import Cycles
- None detected.

## Communities (47 total, 19 thin omitted)

### Community 0 - "Cb"
Cohesion: 0.18
Nodes (9): Cb, CoinsCb, Done, JSONArray, JSONObject, NodeApi, MintEngine, Context (+1 more)

### Community 1 - "Coin"
Cohesion: 0.06
Nodes (14): Coin, JSONObject, DistributeJob, Context, JSONObject, DistributeManager, Done, JSONObject (+6 more)

### Community 2 - "MintView"
Cohesion: 0.15
Nodes (11): Button, EditText, ImageView, JSONObject, LinearLayout, OnClickListener, Override, TextView (+3 more)

### Community 3 - "MainActivity"
Cohesion: 0.09
Nodes (6): ActivityResultLauncher, BaseView, Coin, MainActivity, ScanOptions, Uri

### Community 4 - "Design"
Cohesion: 0.06
Nodes (22): Design, Context, Mode, CLEAN_LIGHT, CURRENT, ORIGINAL_DARK, ORIGINAL_LIGHT, ImageView (+14 more)

### Community 5 - "BalancesView"
Cohesion: 0.11
Nodes (11): BalancesView, EditText, LinearLayout, Override, TextView, TokenBalance, View, JSONObject (+3 more)

### Community 6 - "HistoryView"
Cohesion: 0.11
Nodes (8): HistoryView, LinearLayout, Override, View, JSONArray, JSONObject, NodeTx, NodeTx

### Community 7 - "SendView"
Cohesion: 0.10
Nodes (12): Coin, Override, TextView, View, SendView, JSONObject, TokenBalance, BaseView (+4 more)

### Community 8 - "NodeApi"
Cohesion: 0.16
Nodes (8): Cb, Context, Handler, JSONObject, NodeApi, PairingListener, MinimaAPI, MinimaAPIListener

### Community 9 - "GalleryView"
Cohesion: 0.14
Nodes (18): Adapter, GalleryAdapter, GalleryView, GItem, Holder, Coin, EditText, ImageView (+10 more)

### Community 10 - "BaseView"
Cohesion: 0.15
Nodes (8): BaseView, View, Override, View, ViewGroup, MainPager, NonNull, PagerAdapter

### Community 11 - "StateNft"
Cohesion: 0.09
Nodes (9): Item, JSONArray, JSONObject, Meta, StateNft, StateNftTest, WalletGuardsTest, Pattern (+1 more)

### Community 12 - ".show"
Cohesion: 0.22
Nodes (7): CoinDetailDialog, Coin, Context, LinearLayout, OnClickListener, Pattern, TextView

### Community 13 - "HistoryDb"
Cohesion: 0.16
Nodes (6): HistoryDb, Context, Override, HistoryRow, SQLiteDatabase, SQLiteOpenHelper

### Community 14 - "ImageLoader"
Cohesion: 0.12
Nodes (9): Identicon, Bitmap, ImageLoader, Bitmap, ImageView, WebValidate, Canvas, LruCache (+1 more)

### Community 15 - "WalletTools"
Cohesion: 0.13
Nodes (6): EditText, LinearLayout, Status, WalletTools, DistributeManager, Out

### Community 16 - ".parse"
Cohesion: 0.27
Nodes (3): IconResolver, Pattern, TokenMeta

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
Cohesion: 0.16
Nodes (5): NodeApi, Override, Bundle, HistoryDb, MainPager

### Community 25 - "CmdChain"
Cohesion: 0.24
Nodes (4): CmdChain, Done, JSONObject, NodeApi

### Community 28 - "ImageTools.java"
Cohesion: 0.47
Nodes (3): ImageTools, Context, Uri

### Community 29 - "StateNftActions"
Cohesion: 0.24
Nodes (4): AlertDialog, JSONObject, StateNftActions, Handler

### Community 37 - "MainActivity.java"
Cohesion: 0.20
Nodes (7): JSONArray, JSONObject, TextView, View, AppCompatActivity, BroadcastReceiver, ViewPager

## Knowledge Gaps
- **9 isolated node(s):** `HUB`, `TOKEN`, `NFT`, `COLLECTION`, `PROGRESS` (+4 more)
  These have ≤1 connection - possible missing edges or undocumented components.
- **19 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `MainActivity` connect `MainActivity` to `Coin`, `Design`, `BalancesView`, `HistoryView`, `MainActivity.java`, `SendView`, `GalleryView`, `BaseView`, `.show`, `ImageLoader`, `WalletTools`, `.onCreate`, `.show`, `StateNftActions`?**
  _High betweenness centrality (0.457) - this node is a cross-community bridge._
- **Why does `Cb` connect `Cb` to `MintView`, `MainActivity`, `HistoryView`, `SendView`, `GalleryView`, `.show`, `CmdChain`, `StateNftActions`?**
  _High betweenness centrality (0.103) - this node is a cross-community bridge._
- **Why does `MintView` connect `MintView` to `Screen`, `MainActivity`, `SendView`?**
  _High betweenness centrality (0.089) - this node is a cross-community bridge._
- **Are the 20 inferred relationships involving `Cb` (e.g. with `.fail()` and `.step()`) actually correct?**
  _`Cb` has 20 INFERRED edges - model-reasoned connections that need verification._
- **What connects `HUB`, `TOKEN`, `NFT` to the rest of the system?**
  _9 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `Coin` be split into smaller, more focused modules?**
  _Cohesion score 0.05517503805175038 - nodes in this community are weakly interconnected._
- **Should `MintView` be split into smaller, more focused modules?**
  _Cohesion score 0.14876632801161102 - nodes in this community are weakly interconnected._