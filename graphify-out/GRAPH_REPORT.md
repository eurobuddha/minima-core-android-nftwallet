# Graph Report - NFTwallet  (2026-08-10)

## Corpus Check
- 62 files · ~61,292 words
- Verdict: corpus is large enough that graph structure adds value.

## Summary
- 527 nodes · 1031 edges · 141 communities (15 shown, 126 thin omitted)
- Extraction: 98% EXTRACTED · 2% INFERRED · 0% AMBIGUOUS · INFERRED: 21 edges (avg confidence: 0.8)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `4cf56acc`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- MintEngine
- Bitmap
- .markOwnership
- MintService
- BuriedCoinTest
- MintView
- StateNft
- Sheet
- ReceiveView
- Format
- .updateTabArrows
- .compressBitmap
- ImageFormatTest
- NodeApi
- Context
- Context
- Pattern
- .onReceive
- Screen
- IconBudgetTest
- FillOrderTest
- Context
- Test
- Context
- Uri
- NodeApi
- Bitmap
- ActivityResultLauncher
- Adapter
- AlertDialog
- Dialog
- Drawable
- EditText
- LinearLayout
- Override
- TextView
- View
- View
- JSONObject
- NodeApi
- JSONObject
- LinearLayout
- OnClickListener
- Pattern
- TextView
- Context
- JSONObject
- EditText
- ImageView
- JSONObject
- LinearLayout
- OnClickListener
- Override
- TextView
- ViewGroup
- Context
- Override
- Override
- View
- Bitmap
- Bitmap
- ImageView
- Context
- JSONArray
- JSONObject
- BaseView
- BroadcastReceiver
- Handler
- JSONArray
- JSONObject
- TextView
- Override
- View
- ViewGroup
- Uri
- Button
- EditText
- ImageView
- LinearLayout
- OnClickListener
- Override
- TextView
- View
- Test
- Context
- Handler
- JSONObject
- JSONArray
- JSONObject
- ImageView
- LinearLayout
- Override
- TextView
- Button
- EditText
- LinearLayout
- Override
- TextView
- View
- TextView
- Pattern
- Cb
- JSONObject
- JSONObject
- Context
- JSONObject
- EditText
- LinearLayout
- Button
- Drawable
- LinearLayout
- Override
- TextView
- View
- Test
- AppCompatActivity
- Bundle
- Canvas
- DistributeManager
- Drawable
- Handler
- HistoryDb
- LruCache
- MainPager
- MinimaAPIListener
- NodeTx
- Out
- PagerAdapter
- Paint
- RecyclerView
- ScanOptions
- SharedPreferences
- SQLiteDatabase
- SQLiteOpenHelper
- TokenMeta
- ViewHolder
- ViewPager

## God Nodes (most connected - your core abstractions)
1. `MintView` - 62 edges
2. `MintEngine` - 37 edges
3. `StateNft` - 29 edges
4. `Sheet` - 21 edges
5. `Cb` - 19 edges
6. `MintService` - 18 edges
7. `ReceiveView` - 16 edges
8. `MintDriver` - 15 edges
9. `Done` - 14 edges
10. `Meta` - 11 edges

## Surprising Connections (you probably didn't know these)
- `MintView` --references--> `Screen`  [EXTRACTED]
  app/src/main/java/com/eurobuddha/nftwallet/MintView.java → app/src/main/java/com/eurobuddha/nftwallet/MintView.java  _Bridges community 5 → community 19_
- `MintView` --inherits--> `BaseView`  [EXTRACTED]
  app/src/main/java/com/eurobuddha/nftwallet/MintView.java →   _Bridges community 5 → community 9_

## Import Cycles
- None detected.

## Communities (141 total, 126 thin omitted)

### Community 0 - "MintEngine"
Cohesion: 0.19
Nodes (8): Cb, CoinsCb, Done, JSONArray, JSONObject, MintEngine, Context, NodeApi

### Community 3 - "MintService"
Cohesion: 0.07
Nodes (28): Override, Done, Context, JSONObject, NodeApi, MintDriver, Result, BUSY (+20 more)

### Community 4 - "BuriedCoinTest"
Cohesion: 0.38
Nodes (3): BuriedCoinTest, Test, Coin

### Community 5 - "MintView"
Cohesion: 0.11
Nodes (12): JSONObject, MintView, Button, CheckBox, EditText, ImageView, LinearLayout, OnClickListener (+4 more)

### Community 7 - "StateNft"
Cohesion: 0.11
Nodes (8): Item, JSONArray, JSONObject, Meta, StateNft, DefBudgetTest, Pattern, Test

### Community 8 - "Sheet"
Cohesion: 0.10
Nodes (16): Context, Dialog, LinearLayout, TextView, View, OnTap, Progress, Sheet (+8 more)

### Community 9 - "ReceiveView"
Cohesion: 0.23
Nodes (4): ReceiveView, BaseView, MainActivity, Override

### Community 10 - "Format"
Cohesion: 0.18
Nodes (4): Format, Context, FormatTest, Test

### Community 13 - "ImageFormatTest"
Cohesion: 0.17
Nodes (6): Context, Uri, Pattern, SvgSanitizer, ImageFormatTest, Test

### Community 18 - ".onReceive"
Cohesion: 0.16
Nodes (12): BootReceiver, Context, Intent, Override, HeartbeatReceiver, Context, Intent, Override (+4 more)

### Community 19 - "Screen"
Cohesion: 0.33
Nodes (6): Screen, COLLECTION, HUB, NFT, PROGRESS, TOKEN

## Knowledge Gaps
- **13 isolated node(s):** `HUB`, `TOKEN`, `NFT`, `COLLECTION`, `PROGRESS` (+8 more)
  These have ≤1 connection - possible missing edges or undocumented components.
- **126 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `MintView` connect `MintView` to `ReceiveView`, `Screen`, `StateNft`?**
  _High betweenness centrality (0.048) - this node is a cross-community bridge._
- **Why does `StateNft` connect `StateNft` to `BuriedCoinTest`?**
  _High betweenness centrality (0.032) - this node is a cross-community bridge._
- **Why does `Cb` connect `MintEngine` to `MintView`?**
  _High betweenness centrality (0.020) - this node is a cross-community bridge._
- **What connects `HUB`, `TOKEN`, `NFT` to the rest of the system?**
  _13 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `MintService` be split into smaller, more focused modules?**
  _Cohesion score 0.06605222734254992 - nodes in this community are weakly interconnected._
- **Should `MintView` be split into smaller, more focused modules?**
  _Cohesion score 0.11468531468531469 - nodes in this community are weakly interconnected._
- **Should `StateNft` be split into smaller, more focused modules?**
  _Cohesion score 0.10631229235880399 - nodes in this community are weakly interconnected._