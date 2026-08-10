# Graph Report - /Users/eurobuddha/Projects/minima  (2026-08-10)

## Corpus Check
- 42 files · ~60,823 words
- Verdict: corpus is large enough that graph structure adds value.

## Summary
- 1118 nodes · 3245 edges · 36 communities (33 shown, 3 thin omitted)
- Extraction: 94% EXTRACTED · 6% INFERRED · 0% AMBIGUOUS · INFERRED: 206 edges (avg confidence: 0.81)
- Token cost: 138,203 input · 0 output

## Community Hubs (Navigation)
- Command Chain Runner
- Balances Screen
- History Screen
- Background Mint Lifecycle
- Coin Model & Wallet State
- Image Pipeline & Design Tokens
- Gallery Grid & Collections
- StateNFT Protocol & Metadata
- Modal Sheet System
- Tab View Framework
- Amount Formatting & Decimals
- Main Activity Shell
- Distribute Batch Manager
- SVG Sanitising & Image Format Tests
- Activity Lifecycle & Theming
- History Database
- Coin Detail Modal
- Token Metadata & Icon Resolution
- Send Flow & Validation Spec
- RULE 0 Working Agreement
- App Shell & Chain Events Spec
- Multi-Image Fill Tests
- NFT Wallet Feature Map
- Design Language System
- Transaction Building & 64KB Guard
- Distribute Job Persistence
- Minima App Icon SVG
- Mint Screen States
- QR Encoding
- Minima Coin Logomark
- Gradle Wrapper
- Node IPC Transport
- Coin Selection & Consolidate
- Minima Logo Asset

## God Nodes (most connected - your core abstractions)
1. `MainActivity` - 120 edges
2. `MintView` - 64 edges
3. `Cb` - 44 edges
4. `NodeApi` - 40 edges
5. `MintEngine` - 37 edges
6. `SendView` - 37 edges
7. `Design` - 36 edges
8. `Coin` - 35 edges
9. `GalleryView` - 32 edges
10. `BalancesView` - 29 edges

## Surprising Connections (you probably didn't know these)
- `utxoWallet index.html v1.0.52 (source of truth)` --conceptually_related_to--> `NFT Wallet (native Android)`  [AMBIGUOUS]
  apks/NFTwallet/DESIGN_MAP.md → apks/NFTwallet/README.md
- `Send tab (QUICK SEND + COIN CONTROL)` --semantically_similar_to--> `Send flow (full construction) — Send + Confirm modals`  [INFERRED] [semantically similar]
  apks/NFTwallet/README.md → apks/NFTwallet/DESIGN_MAP.md
- `txndelete on every path + ≤3 token outputs with halving` --semantically_similar_to--> `64KB post-size guard`  [INFERRED] [semantically similar]
  apks/NFTwallet/README.md → apks/NFTwallet/DESIGN_MAP.md
- `Resumable CREATE→MOVE→SPLIT→STAMP mint engine` --semantically_similar_to--> `Distribute flow (auto-chained batches)`  [INFERRED] [semantically similar]
  apks/NFTwallet/README.md → apks/NFTwallet/DESIGN_MAP.md
- `Dual theme — family dark ↔ old-wallet clean light` --semantically_similar_to--> `Runtime design-language toggle (ORIGINAL / CURRENT)`  [INFERRED] [semantically similar]
  apks/NFTwallet/README.md → apks/NFTwallet/DESIGN_MAP.md

## Import Cycles
- None detected.

## Hyperedges (group relationships)
- **Send transaction construction pipeline (validate → build → guard → sign/post → history)** — apks_nftwallet_design_map_send_flow, apks_nftwallet_design_map_validateaddress, apks_nftwallet_design_map_validateamount, apks_nftwallet_design_map_resolvedefaultchangeaddr, apks_nftwallet_design_map_buildtransaction, apks_nftwallet_design_map_post_size_guard, apks_nftwallet_design_map_signandpost, apks_nftwallet_design_map_state_persistence [EXTRACTED 1.00]
- **StateNFT on-chain safety invariants** — apks_nftwallet_readme_statenft_protocol, apks_nftwallet_readme_state_port_replay, apks_nftwallet_readme_txnpost_status_distrust, apks_nftwallet_readme_txndelete_discipline, apks_nftwallet_readme_unstamped_transfer_refusal, apks_nftwallet_readme_statenft_mint_engine [EXTRACTED 1.00]
- **Runtime design-language system (toggle, token object, both languages, theme, CFG)** — apks_nftwallet_design_map_design_language_toggle, apks_nftwallet_design_map_design_token_object, apks_nftwallet_design_map_original_design_language, apks_nftwallet_design_map_current_design_language, apks_nftwallet_design_map_theme_toggle, apks_nftwallet_design_map_cfg_screen, apks_nftwallet_readme_dual_theme [INFERRED 0.85]
- **Minima Coin Asset Visual Identity System** — app_src_main_res_drawable_nodpi_minima_coin, app_src_main_res_drawable_nodpi_minima_coin_brand_mark, app_src_main_res_drawable_nodpi_minima_coin_monochrome_palette, app_src_main_res_drawable_nodpi_minima_coin_nodpi_bucket [INFERRED 0.75]
- **Minima brand identity: orange tile + ink chevron mark** — app_src_main_res_raw_minima_icon_icon, app_src_main_res_raw_minima_icon_minima_logo_mark, app_src_main_res_raw_minima_icon_rounded_square_tile, app_src_main_res_raw_minima_icon_brand_orange_ffa010, app_src_main_res_raw_minima_icon_brand_ink_17191c [EXTRACTED 1.00]

## Communities (36 total, 3 thin omitted)

### Community 0 - "Command Chain Runner"
Cohesion: 0.06
Nodes (25): CmdChain, Done, JSONObject, NodeApi, Context, JSONArray, JSONObject, LocalStore (+17 more)

### Community 1 - "Balances Screen"
Cohesion: 0.06
Nodes (28): BalancesView, Bitmap, Dialog, Drawable, EditText, LinearLayout, Override, TextView (+20 more)

### Community 2 - "History Screen"
Cohesion: 0.06
Nodes (18): HistoryView, LinearLayout, Override, TextView, View, JSONArray, JSONObject, NodeTx (+10 more)

### Community 3 - "Background Mint Lifecycle"
Cohesion: 0.05
Nodes (37): BootReceiver, Context, Intent, Override, HeartbeatReceiver, Context, Intent, Override (+29 more)

### Community 4 - "Coin Model & Wallet State"
Cohesion: 0.05
Nodes (17): Coin, JSONObject, Done, JSONObject, Out, OutCoin, Progress, TxnBuilder (+9 more)

### Community 5 - "Image Pipeline & Design Tokens"
Cohesion: 0.09
Nodes (17): ImageTools, Bitmap, Context, Uri, Button, EditText, ImageView, JSONObject (+9 more)

### Community 6 - "Gallery Grid & Collections"
Cohesion: 0.06
Nodes (28): Adapter, GalleryAdapter, GalleryView, GItem, Holder, EditText, ImageView, JSONObject (+20 more)

### Community 7 - "StateNFT Protocol & Metadata"
Cohesion: 0.06
Nodes (14): Item, JSONArray, JSONObject, Pattern, Meta, StateNft, DefBudgetTest, Test (+6 more)

### Community 8 - "Modal Sheet System"
Cohesion: 0.09
Nodes (17): AlertDialog, Context, Dialog, LinearLayout, TextView, View, OnTap, Progress (+9 more)

### Community 9 - "Tab View Framework"
Cohesion: 0.09
Nodes (13): BaseView, View, Override, View, ViewGroup, MainPager, ImageView, LinearLayout (+5 more)

### Community 10 - "Amount Formatting & Decimals"
Cohesion: 0.09
Nodes (8): Format, Context, HiddenTokens, Context, TextView, SettingsDialog, FormatTest, Test

### Community 11 - "Main Activity Shell"
Cohesion: 0.09
Nodes (14): ActivityResultLauncher, BaseView, BroadcastReceiver, Handler, JSONArray, JSONObject, TextView, Uri (+6 more)

### Community 13 - "SVG Sanitising & Image Format Tests"
Cohesion: 0.23
Nodes (4): Pattern, SvgSanitizer, ImageFormatTest, Test

### Community 14 - "Activity Lifecycle & Theming"
Cohesion: 0.12
Nodes (6): NodeApi, Override, Bundle, DistributeManager, HistoryDb, MainPager

### Community 15 - "History Database"
Cohesion: 0.16
Nodes (6): HistoryDb, Context, Override, HistoryRow, SQLiteDatabase, SQLiteOpenHelper

### Community 16 - "Coin Detail Modal"
Cohesion: 0.26
Nodes (6): CoinDetailDialog, Context, LinearLayout, OnClickListener, Pattern, TextView

### Community 17 - "Token Metadata & Icon Resolution"
Cohesion: 0.19
Nodes (4): IconResolver, Pattern, JSONObject, TokenMeta

### Community 18 - "Send Flow & Validation Spec"
Cohesion: 0.23
Nodes (12): CFG (Settings) screen, defaultChangeMode (rotate | first input), Node lock state check, resolveDefaultChangeAddr, Selection bar (Clear / Tools ▾ / Send →), Send flow (full construction) — Send + Confirm modals, Untrack / Track / Receive flows, validateAddress (+4 more)

### Community 19 - "RULE 0 Working Agreement"
Cohesion: 0.22
Nodes (11): "Look at X / use Y / do Z first / don't do W" are hard blocking instructions, Disagree openly; never disobey quietly, Never silently substitute your own approach, Reuse before you reinvent, RULE 0 — Explicit user instructions are blocking, utxoWallet index.html v1.0.52 (source of truth), utxoWallet native Android clone — figma-style mapping & build blueprint, Hardened NodeApi from apks/pandadex (+3 more)

### Community 20 - "App Shell & Chain Events Spec"
Cohesion: 0.20
Nodes (11): App shell — nav + tabs, Explorer deep link (explorer.minima.global/transactions/<txid>), History screen (history-row + details), Live block indicator (pulsing accent dot + #chainBlock), NEWBLOCK event handler, ORIGINAL design language (brutalist/terminal), signAndPost, History status buckets (posting/finalizing → Sending, etc.) (+3 more)

### Community 22 - "NFT Wallet Feature Map"
Cohesion: 0.31
Nodes (10): Balances screen (per-token bal-card), NEWBALANCE event handler (debounced), Balances tab, Gallery tab (two-column NFT grid), Mint tab (Token · NFT · State NFT collection), NFT Wallet (native Android), Resumable CREATE→MOVE→SPLIT→STAMP mint engine, StateNFT protocol (locked-edition) (+2 more)

### Community 23 - "Design Language System"
Cohesion: 0.22
Nodes (10): Build order (tokens → shell → wallet → send → … → release), Component catalog (btn, field, card, utxo-row, modal, toast, pills), CURRENT design language (native dark, Material), Runtime design-language toggle (ORIGINAL / CURRENT), Central Design token object, Design tokens (colors, type, metrics), Distribute affordability rule fix (strict >, allow change 0), Status pill (coin + history) (+2 more)

### Community 24 - "Transaction Building & 64KB Guard"
Cohesion: 0.24
Nodes (10): buildTransaction, Distribute flow (auto-chained batches), estimateTxPowBytes, MAX_ADDR_OUTPUTS_PER_BATCH = 14, oversizedPostError, 64KB post-size guard, Split flow (2–15 coins), State & persistence (SharedPreferences + SQLite history) (+2 more)

### Community 25 - "Distribute Job Persistence"
Cohesion: 0.42
Nodes (3): DistributeJob, Context, JSONObject

### Community 26 - "Minima App Icon SVG"
Cohesion: 0.33
Nodes (7): Minima Brand Ink #17191C, Minima Brand Orange #FFA010, Minima App Icon Artwork, Full-Bleed Mask Clipping Pattern, Minima Logo Mark (zig-zag chevron glyph), SVG Shipped in res/raw (not vector drawable), Rounded-Square Icon Tile (542x527, r=57)

### Community 27 - "Mint Screen States"
Cohesion: 0.33
Nodes (6): Screen, COLLECTION, HUB, NFT, PROGRESS, TOKEN

### Community 29 - "Minima Coin Logomark"
Cohesion: 0.50
Nodes (5): minima_coin.png (Minima 'M' Logomark Asset), Minima Brand Mark (Angular Zig-Zag 'M'), Monochrome Near-Black on Transparent Palette, drawable-nodpi Resource Bucket (No Density Scaling), Default Token/Coin Icon Placeholder

### Community 30 - "Gradle Wrapper"
Cohesion: 0.60
Nodes (3): gradlew script, die(), warn()

### Community 31 - "Node IPC Transport"
Cohesion: 0.50
Nodes (4): Command reference over MinimaAPI.Command, apks/base/dist minimaapi.aar (content:// large-response hand-off), Minima Core node APK (org.minimarex.minimacore), minimaapi.aar broadcast-Intent IPC

### Community 32 - "Coin Selection & Consolidate"
Cohesion: 0.50
Nodes (4): Consolidate flow, Selection model (selected, selectedTokenid, sendableCoinIds), Wallet screen (address cards + coin rows), Coins tab (UTXO coin picker)

### Community 33 - "Minima Logo Asset"
Cohesion: 0.67
Nodes (4): Minima Brand Mark: Four White Arrows on Blue Rounded Square, Minima Logo Asset (minima_logo.png), NFTwallet Companion App Minima Branding, drawable-nodpi Density-Independent Asset Placement

## Ambiguous Edges - Review These
- `utxoWallet index.html v1.0.52 (source of truth)` → `NFT Wallet (native Android)`  [AMBIGUOUS]
  apks/NFTwallet/DESIGN_MAP.md · relation: conceptually_related_to
- `Resumable CREATE→MOVE→SPLIT→STAMP mint engine` → `txndelete on every path + ≤3 token outputs with halving`  [AMBIGUOUS]
  apks/NFTwallet/README.md · relation: conceptually_related_to

## Knowledge Gaps
- **27 isolated node(s):** `ORIGINAL_LIGHT`, `ORIGINAL_DARK`, `CURRENT`, `CLEAN_LIGHT`, `HUB` (+22 more)
  These have ≤1 connection - possible missing edges or undocumented components.
- **3 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **What is the exact relationship between `utxoWallet index.html v1.0.52 (source of truth)` and `NFT Wallet (native Android)`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._
- **What is the exact relationship between `Resumable CREATE→MOVE→SPLIT→STAMP mint engine` and `txndelete on every path + ≤3 token outputs with halving`?**
  _Edge tagged AMBIGUOUS (relation: conceptually_related_to) - confidence is low._
- **Why does `MainActivity` connect `Main Activity Shell` to `Command Chain Runner`, `Balances Screen`, `History Screen`, `Background Mint Lifecycle`, `Coin Model & Wallet State`, `Gallery Grid & Collections`, `Modal Sheet System`, `Tab View Framework`, `Amount Formatting & Decimals`, `Distribute Batch Manager`, `Activity Lifecycle & Theming`, `History Database`, `Coin Detail Modal`, `Distribute Job Persistence`?**
  _High betweenness centrality (0.266) - this node is a cross-community bridge._
- **Why does `MintView` connect `Image Pipeline & Design Tokens` to `Balances Screen`, `Gallery Grid & Collections`, `StateNFT Protocol & Metadata`, `Modal Sheet System`, `Tab View Framework`, `Main Activity Shell`, `Mint Screen States`?**
  _High betweenness centrality (0.064) - this node is a cross-community bridge._
- **Why does `Cb` connect `Command Chain Runner` to `Balances Screen`, `History Screen`, `Coin Model & Wallet State`, `Image Pipeline & Design Tokens`, `Gallery Grid & Collections`, `Modal Sheet System`, `Coin Detail Modal`?**
  _High betweenness centrality (0.062) - this node is a cross-community bridge._
- **Are the 26 inferred relationships involving `Cb` (e.g. with `.loadCoins()` and `.loadContract()`) actually correct?**
  _`Cb` has 26 INFERRED edges - model-reasoned connections that need verification._
- **What connects `ORIGINAL_LIGHT`, `ORIGINAL_DARK`, `CURRENT` to the rest of the system?**
  _27 weakly-connected nodes found - possible documentation gaps or missing edges._