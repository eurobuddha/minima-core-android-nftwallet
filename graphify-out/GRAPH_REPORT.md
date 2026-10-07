# Graph Report - apks/NFTwallet  (2026-09-04)

## Corpus Check
- 65 files · ~62,015 words
- Verdict: corpus is large enough that graph structure adds value.

## Summary
- 1029 nodes · 3153 edges · 35 communities (23 shown, 12 thin omitted)
- Extraction: 95% EXTRACTED · 5% INFERRED · 0% AMBIGUOUS · INFERRED: 154 edges (avg confidence: 0.8)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `8f8b8219`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- MintView
- MintService
- .show
- StateNft
- android.content.Context
- android.graphics.Bitmap
- utxoWallet — Native Android clone: figma-style mapping & build blueprint
- ImageFormatTest
- org.junit.Test
- ReceiveView
- Versioning guardrail — every code change ships with a version bump
- .from
- FillOrderTest
- IconBudgetTest
- SendView
- Screen
- MainActivity
- TxnBuilder
- .isValidHexId
- java.util.regex.Pattern
- CmdChain
- install.sh
- pre-commit
- .jointGate
- .parse
- Progress
- Design
- NFT Wallet (native Android)
- gradlew
- Coin
- HistoryDb
- .create

## God Nodes (most connected - your core abstractions)
1. `MainActivity` - 112 edges
2. `MintView` - 62 edges
3. `NodeApi` - 44 edges
4. `MintEngine` - 37 edges
5. `Design` - 36 edges
6. `SendView` - 36 edges
7. `Coin` - 35 edges
8. `GalleryView` - 31 edges
9. `StateNft` - 29 edges
10. `BalancesView` - 28 edges

## Surprising Connections (you probably didn't know these)
- `BaseView` --references--> `MainActivity`  [EXTRACTED]
  apks/NFTwallet/app/src/main/java/com/eurobuddha/nftwallet/BaseView.java → apks/NFTwallet/app/src/main/java/com/eurobuddha/nftwallet/MainActivity.java
- `GalleryView` --inherits--> `BaseView`  [EXTRACTED]
  apks/NFTwallet/app/src/main/java/com/eurobuddha/nftwallet/GalleryView.java → apks/NFTwallet/app/src/main/java/com/eurobuddha/nftwallet/BaseView.java
- `HistoryView` --inherits--> `BaseView`  [EXTRACTED]
  apks/NFTwallet/app/src/main/java/com/eurobuddha/nftwallet/HistoryView.java → apks/NFTwallet/app/src/main/java/com/eurobuddha/nftwallet/BaseView.java
- `MainPager` --references--> `BaseView`  [EXTRACTED]
  apks/NFTwallet/app/src/main/java/com/eurobuddha/nftwallet/MainPager.java → apks/NFTwallet/app/src/main/java/com/eurobuddha/nftwallet/BaseView.java
- `MintView` --inherits--> `BaseView`  [EXTRACTED]
  apks/NFTwallet/app/src/main/java/com/eurobuddha/nftwallet/MintView.java → apks/NFTwallet/app/src/main/java/com/eurobuddha/nftwallet/BaseView.java

## Import Cycles
- None detected.

## Hyperedges (group relationships)
- **The four sub-principles that operationalize RULE 0** — apks_nftwallet_claude_rule_0_follow_explicit_instructions, apks_nftwallet_claude_reuse_before_you_reinvent, apks_nftwallet_claude_blocking_instructions, apks_nftwallet_claude_never_silently_substitute, apks_nftwallet_claude_disagree_openly_never_disobey_quietly [EXTRACTED 1.00]
- **Version-bump enforcement chain (policy → gradle → hook → installer → no bypass)** — claude_versioning_guardrail_every_code_change_ships_with_a_version_bump, apks_nftwallet_claude_version_bump, apks_nftwallet_claude_app_build_gradle, apks_nftwallet_claude_pre_commit_hook, apks_nftwallet_claude_githooks_install_sh, apks_nftwallet_claude_no_verify_prohibition [EXTRACTED 1.00]
- **Commit discipline for a real-funds app** — apks_nftwallet_claude_real_funds_real_chain, apks_nftwallet_claude_one_change_one_version_one_commit_one_push, apks_nftwallet_claude_docs_config_commit_exemption, claude_versioning_guardrail_every_code_change_ships_with_a_version_bump [INFERRED 0.85]

## Communities (35 total, 12 thin omitted)

### Community 0 - "MintView"
Cohesion: 0.06
Nodes (30): Adapter, android.graphics.Typeface, android.widget.CheckBox, android.widget.EditText, android.widget.FrameLayout, android.widget.ImageView, android.widget.LinearLayout, android.widget.TextView (+22 more)

### Community 1 - "MintService"
Cohesion: 0.06
Nodes (26): android.app.Notification, android.app.PendingIntent, android.app.Service, android.content.BroadcastReceiver, android.content.Intent, android.os.IBinder, android.view.ViewGroup, androidx.annotation.NonNull (+18 more)

### Community 2 - ".show"
Cohesion: 0.06
Nodes (14): android.app.Dialog, CoinDetailDialog, LinearLayout, OnClickListener, TextView, Format, TextView, SettingsDialog (+6 more)

### Community 3 - "StateNft"
Cohesion: 0.13
Nodes (4): Item, JSONObject, Meta, StateNft

### Community 4 - "android.content.Context"
Cohesion: 0.06
Nodes (30): android.content.Context, android.content.SharedPreferences, android.os.Handler, HiddenTokens, JSONArray, JSONObject, LocalStore, Done (+22 more)

### Community 5 - "android.graphics.Bitmap"
Cohesion: 0.06
Nodes (17): android.graphics.Bitmap, android.graphics.Canvas, android.graphics.Paint, android.net.Uri, android.util.LruCache, Identicon, ImageLoader, ImageTools (+9 more)

### Community 6 - "utxoWallet — Native Android clone: figma-style mapping & build blueprint"
Cohesion: 0.08
Nodes (24): 0. Design languages (runtime toggle), 1.1 ORIGINAL — light (`:root`, default), 1.2 ORIGINAL — dark (`:root[data-theme="dark"]`), 1.3 CURRENT (existing native dark), 1.4 Type & metrics (ORIGINAL), 1.5 Component note colors (`.field-note`, `.toast`, pills), 1. Design tokens, 2. Component catalog (ORIGINAL; CURRENT = Material equivalents) (+16 more)

### Community 8 - "org.junit.Test"
Cohesion: 0.21
Nodes (3): FormatTest, StateNftTest, org.junit.Test

### Community 9 - "ReceiveView"
Cohesion: 0.23
Nodes (4): LinearLayout, Override, TextView, ReceiveView

### Community 10 - "Versioning guardrail — every code change ships with a version bump"
Cohesion: 0.18
Nodes (16): app/build.gradle (version source of truth), Blocking instruction forms (Look at X / use Y / do Z first / don't do W), Disagree openly; never disobey quietly, Docs/config-only commits need no bump, .githooks/install.sh (one-time hook installer), Never silently substitute your own approach, Do NOT bypass with --no-verify, One logical change = one version = one commit = one push (+8 more)

### Community 14 - "SendView"
Cohesion: 0.06
Nodes (13): HistoryView, LinearLayout, Override, TextView, JSONArray, JSONObject, NodeTx, LinearLayout (+5 more)

### Community 15 - "Screen"
Cohesion: 0.33
Nodes (6): Screen, COLLECTION, HUB, NFT, PROGRESS, TOKEN

### Community 16 - "MainActivity"
Cohesion: 0.08
Nodes (10): ActivityResultLauncher, android.os.Bundle, androidx.appcompat.app.AppCompatActivity, androidx.viewpager.widget.ViewPager, BroadcastReceiver, Override, Uri, MainActivity (+2 more)

### Community 17 - "TxnBuilder"
Cohesion: 0.16
Nodes (4): Done, OutCoin, Progress, TxnBuilder

### Community 19 - "java.util.regex.Pattern"
Cohesion: 0.21
Nodes (3): IconResolver, SvgSanitizer, java.util.regex.Pattern

### Community 28 - "Design"
Cohesion: 0.05
Nodes (28): android.graphics.drawable.Drawable, android.graphics.drawable.GradientDrawable, android.view.View, android.widget.Button, BalancesView, Dialog, EditText, LinearLayout (+20 more)

### Community 29 - "NFT Wallet (native Android)"
Cohesion: 0.33
Nodes (5): Build, Lineage, NFT Wallet (native Android), StateNFT protocol (from mds/statenft-suite — proven on-chain), Tabs

### Community 30 - "gradlew"
Cohesion: 0.60
Nodes (3): gradlew script, die(), warn()

### Community 46 - "Coin"
Cohesion: 0.06
Nodes (11): Coin, DistributeJob, JSONObject, DistributeManager, Out, AddrList, TxnUtil, EditText (+3 more)

### Community 58 - "HistoryDb"
Cohesion: 0.16
Nodes (5): android.database.sqlite.SQLiteDatabase, android.database.sqlite.SQLiteOpenHelper, HistoryDb, Override, HistoryRow

## Knowledge Gaps
- **45 isolated node(s):** `install.sh script`, `ORIGINAL_LIGHT`, `ORIGINAL_DARK`, `CURRENT`, `CLEAN_LIGHT` (+40 more)
  These have ≤1 connection - possible missing edges or undocumented components.
- **12 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `MainActivity` connect `MainActivity` to `MintView`, `MintService`, `.show`, `android.content.Context`, `android.graphics.Bitmap`, `.create`, `ReceiveView`, `.from`, `Coin`, `SendView`, `TxnBuilder`, `.cmd`, `.parse`, `HistoryDb`, `Progress`, `Design`?**
  _High betweenness centrality (0.225) - this node is a cross-community bridge._
- **Why does `NodeApi` connect `android.content.Context` to `MintService`, `.show`, `.create`, `MainActivity`, `CmdChain`, `.cmd`?**
  _High betweenness centrality (0.066) - this node is a cross-community bridge._
- **Why does `MintView` connect `MintView` to `StateNft`, `android.content.Context`, `android.graphics.Bitmap`, `.create`, `Screen`, `Design`?**
  _High betweenness centrality (0.046) - this node is a cross-community bridge._
- **What connects `install.sh script`, `ORIGINAL_LIGHT`, `ORIGINAL_DARK` to the rest of the system?**
  _45 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `MintView` be split into smaller, more focused modules?**
  _Cohesion score 0.06064240970278555 - nodes in this community are weakly interconnected._
- **Should `MintService` be split into smaller, more focused modules?**
  _Cohesion score 0.06153846153846154 - nodes in this community are weakly interconnected._
- **Should `.show` be split into smaller, more focused modules?**
  _Cohesion score 0.06274509803921569 - nodes in this community are weakly interconnected._