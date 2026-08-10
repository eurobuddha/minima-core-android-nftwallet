# Graph Report - NFTwallet  (2026-08-10)

## Corpus Check
- 62 files · ~60,965 words
- Verdict: corpus is large enough that graph structure adds value.

## Summary
- 405 nodes · 520 edges · 146 communities (18 shown, 128 thin omitted)
- Extraction: 99% EXTRACTED · 1% INFERRED · 0% AMBIGUOUS · INFERRED: 6 edges (avg confidence: 0.8)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `e41bdeb4`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- .phaseNeedImages
- Bitmap
- .markOwnership
- MintService
- BuriedCoinTest
- .sealFrom
- DefBudgetTest
- Sheet
- ReceiveView
- Format
- .updateTabArrows
- MintDriver
- ImageFormatTest
- NodeApi
- Context
- Context
- Pattern
- .onReceive
- MintWorker
- IconBudgetTest
- FillOrderTest
- Notifier
- Test
- Context
- Uri
- JSONObject
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
- JSONArray
- Button
- EditText
- ImageView
- LinearLayout
- OnClickListener
- Override
- TextView
- View
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
- JSONArray
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
- CheckBox
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
- Typeface
- ViewHolder
- ViewPager

## God Nodes (most connected - your core abstractions)
1. `Sheet` - 21 edges
2. `MintService` - 18 edges
3. `ReceiveView` - 16 edges
4. `MintDriver` - 15 edges
5. `ImageFormatTest` - 11 edges
6. `Format` - 9 edges
7. `Result` - 9 edges
8. `Progress` - 9 edges
9. `FillOrderTest` - 9 edges
10. `IconBudgetTest` - 8 edges

## Surprising Connections (you probably didn't know these)
- None detected - all connections are within the same source files.

## Import Cycles
- None detected.

## Communities (146 total, 128 thin omitted)

### Community 0 - ".phaseNeedImages"
Cohesion: 0.40
Nodes (3): Context, JSONObject, NodeApi

### Community 3 - "MintService"
Cohesion: 0.15
Nodes (11): Override, BroadcastReceiver, Context, Intent, NodeApi, Override, MintService, IBinder (+3 more)

### Community 5 - ".sealFrom"
Cohesion: 0.11
Nodes (6): Bitmap, Context, Uri, JSONObject, Uri, SuppressWarnings

### Community 8 - "Sheet"
Cohesion: 0.10
Nodes (16): Context, Dialog, LinearLayout, TextView, View, OnTap, Progress, Sheet (+8 more)

### Community 9 - "ReceiveView"
Cohesion: 0.20
Nodes (7): ReceiveView, BaseView, ImageView, LinearLayout, MainActivity, Override, TextView

### Community 10 - "Format"
Cohesion: 0.18
Nodes (4): Format, Context, FormatTest, Test

### Community 12 - "MintDriver"
Cohesion: 0.14
Nodes (11): Done, Context, JSONObject, NodeApi, MintDriver, Result, BUSY, NEEDS_IMAGES (+3 more)

### Community 13 - "ImageFormatTest"
Cohesion: 0.21
Nodes (4): Pattern, SvgSanitizer, ImageFormatTest, Test

### Community 18 - ".onReceive"
Cohesion: 0.26
Nodes (9): BootReceiver, Context, Intent, Override, HeartbeatReceiver, Context, Intent, Override (+1 more)

### Community 19 - "MintWorker"
Cohesion: 0.33
Nodes (6): Context, Override, MintWorker, NonNull, Worker, WorkerParameters

### Community 22 - "Notifier"
Cohesion: 0.43
Nodes (3): Context, Notifier, PendingIntent

## Knowledge Gaps
- **8 isolated node(s):** `STARTED`, `BUSY`, `NEEDS_IMAGES`, `NOTHING_TO_DO`, `NOT_PAIRED` (+3 more)
  These have ≤1 connection - possible missing edges or undocumented components.
- **128 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `MintDriver` connect `MintDriver` to `MintService`?**
  _High betweenness centrality (0.009) - this node is a cross-community bridge._
- **Why does `Result` connect `MintDriver` to `MintWorker`?**
  _High betweenness centrality (0.006) - this node is a cross-community bridge._
- **What connects `STARTED`, `BUSY`, `NEEDS_IMAGES` to the rest of the system?**
  _8 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `MintService` be split into smaller, more focused modules?**
  _Cohesion score 0.14532019704433496 - nodes in this community are weakly interconnected._
- **Should `.sealFrom` be split into smaller, more focused modules?**
  _Cohesion score 0.1067193675889328 - nodes in this community are weakly interconnected._
- **Should `Sheet` be split into smaller, more focused modules?**
  _Cohesion score 0.10121951219512196 - nodes in this community are weakly interconnected._
- **Should `MintDriver` be split into smaller, more focused modules?**
  _Cohesion score 0.14492753623188406 - nodes in this community are weakly interconnected._