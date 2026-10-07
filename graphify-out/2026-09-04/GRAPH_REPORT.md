# Graph Report - .  (2026-08-23)

## Corpus Check
- 11 files · ~62,015 words
- Verdict: corpus is large enough that graph structure adds value.

## Summary
- 576 nodes · 1112 edges · 151 communities (17 shown, 134 thin omitted)
- Extraction: 97% EXTRACTED · 3% INFERRED · 0% AMBIGUOUS · INFERRED: 31 edges (avg confidence: 0.8)
- Token cost: 31,299 input · 0 output

## Community Hubs (Navigation)
- Mint Screen & Android Widgets
- Mint Driver & Activity Lifecycle
- Modal Sheet System
- Mint Engine Phase Machine
- Node API Callbacks
- Image Compression Pipeline
- Background Mint Lifecycle
- SVG Sanitising & MIME
- Amount Formatting & Decimals
- Receive Screen & Addresses
- Version-Bump Guardrail & RULE 0
- Buried Coin Tests
- Multi-Image Fill Tests
- Icon Budget Tests
- Address Ownership Badges
- Mint Screen States
- Tab Scroll Affordance
- StateNFT Metadata Tests
- Community 20
- Community 21
- Community 22
- Community 23
- Community 24
- Community 25
- Community 27
- Community 28
- Community 29
- Community 30
- Community 31
- Community 32
- Community 33
- Community 34
- Community 35
- Community 36
- Community 37
- Community 38
- Community 39
- Community 40
- Community 41
- Community 42
- Community 43
- Community 44
- Community 45
- Community 46
- Community 48
- Community 49
- Community 50
- Community 51
- Community 52
- Community 53
- Community 54
- Community 55
- Community 56
- Community 57
- Community 58
- Community 59
- Community 60
- Community 61
- Community 62
- Community 63
- Community 64
- Community 65
- Community 66
- Community 67
- Community 68
- Community 69
- Community 70
- Community 71
- Community 72
- Community 73
- Community 74
- Community 75
- Community 76
- Community 77
- Community 78
- Community 79
- Community 80
- Community 81
- Community 82
- Community 83
- Community 84
- Community 85
- Community 86
- Community 87
- Community 88
- Community 89
- Community 90
- Community 91
- Community 92
- Community 93
- Community 94
- Community 95
- Community 96
- Community 97
- Community 98
- Community 99
- Community 100
- Community 101
- Community 102
- Community 103
- Community 104
- Community 105
- Community 106
- Community 107
- Community 108
- Community 109
- Community 110
- Community 111
- Community 112
- Community 113
- Community 114
- Community 115
- Community 116
- Community 117
- Community 118
- Community 119
- Community 120
- Community 121
- Community 122
- Community 123
- Community 124
- Community 125
- Community 126
- Community 127
- Community 128
- Community 129
- Community 130
- Community 131
- Community 132
- Community 133
- Community 134
- Community 135
- Community 136
- Community 137
- Community 138
- Community 139
- Community 140
- Community 141
- Community 142
- Community 143
- Community 144
- Community 145
- Community 146
- Community 147
- Community 148
- Community 149
- Community 150

## God Nodes (most connected - your core abstractions)
1. `MintView` - 62 edges
2. `MintEngine` - 37 edges
3. `StateNft` - 29 edges
4. `Sheet` - 21 edges
5. `Cb` - 21 edges
6. `MintService` - 18 edges
7. `ReceiveView` - 16 edges
8. `MintDriver` - 15 edges
9. `Done` - 14 edges
10. `ImageFormatTest` - 11 edges

## Surprising Connections (you probably didn't know these)
- `MintView` --inherits--> `BaseView`  [EXTRACTED]
  app/src/main/java/com/eurobuddha/nftwallet/MintView.java →   _Bridges community 9 → community 0_
- `MintView` --references--> `Screen`  [EXTRACTED]
  app/src/main/java/com/eurobuddha/nftwallet/MintView.java → app/src/main/java/com/eurobuddha/nftwallet/MintView.java  _Bridges community 0 → community 15_

## Import Cycles
- None detected.

## Hyperedges (group relationships)
- **The four sub-principles that operationalize RULE 0** — claude_rule_0_follow_explicit_instructions, claude_reuse_before_you_reinvent, claude_blocking_instructions, claude_never_silently_substitute, claude_disagree_openly_never_disobey_quietly [EXTRACTED 1.00]
- **Version-bump enforcement chain (policy → gradle → hook → installer → no bypass)** — claude_versioning_guardrail_every_code_change_ships_with_a_version_bump, claude_version_bump, claude_app_build_gradle, claude_pre_commit_hook, claude_githooks_install_sh, claude_no_verify_prohibition [EXTRACTED 1.00]
- **Commit discipline for a real-funds app** — claude_real_funds_real_chain, claude_one_change_one_version_one_commit_one_push, claude_docs_config_commit_exemption, claude_versioning_guardrail_every_code_change_ships_with_a_version_bump [INFERRED 0.85]

## Communities (151 total, 134 thin omitted)

### Community 0 - "Mint Screen & Android Widgets"
Cohesion: 0.09
Nodes (20): android.view.View, android.widget.Button, android.widget.CheckBox, android.widget.EditText, android.widget.ImageView, android.widget.LinearLayout, android.widget.TextView, ImageView (+12 more)

### Community 1 - "Mint Driver & Activity Lifecycle"
Cohesion: 0.07
Nodes (28): Override, Done, Context, JSONObject, NodeApi, MintDriver, Result, BUSY (+20 more)

### Community 2 - "Modal Sheet System"
Cohesion: 0.10
Nodes (16): Context, Dialog, LinearLayout, TextView, View, OnTap, Progress, Sheet (+8 more)

### Community 3 - "Mint Engine Phase Machine"
Cohesion: 0.11
Nodes (9): JSONArray, JSONObject, Item, JSONObject, Meta, StateNft, java.util.regex.Pattern, org.json.JSONArray (+1 more)

### Community 4 - "Node API Callbacks"
Cohesion: 0.23
Nodes (6): android.content.Context, Cb, CoinsCb, Done, MintEngine, NodeApi

### Community 5 - "Image Compression Pipeline"
Cohesion: 0.12
Nodes (10): android.graphics.Bitmap, android.net.Uri, ImageTools, DefBudgetTest, ShrinkLadderTest, org.junit.runner.RunWith, org.junit.Test, org.robolectric.annotation.Config (+2 more)

### Community 6 - "Background Mint Lifecycle"
Cohesion: 0.16
Nodes (12): BootReceiver, Context, Intent, Override, HeartbeatReceiver, Context, Intent, Override (+4 more)

### Community 7 - "SVG Sanitising & MIME"
Cohesion: 0.21
Nodes (4): Pattern, SvgSanitizer, ImageFormatTest, Test

### Community 8 - "Amount Formatting & Decimals"
Cohesion: 0.18
Nodes (4): Format, Context, FormatTest, Test

### Community 9 - "Receive Screen & Addresses"
Cohesion: 0.21
Nodes (6): LinearLayout, MainActivity, Override, TextView, ReceiveView, BaseView

### Community 10 - "Version-Bump Guardrail & RULE 0"
Cohesion: 0.18
Nodes (16): app/build.gradle (version source of truth), Blocking instruction forms (Look at X / use Y / do Z first / don't do W), Disagree openly; never disobey quietly, Docs/config-only commits need no bump, .githooks/install.sh (one-time hook installer), Never silently substitute your own approach, Do NOT bypass with --no-verify, One logical change = one version = one commit = one push (+8 more)

### Community 11 - "Buried Coin Tests"
Cohesion: 0.38
Nodes (3): BuriedCoinTest, Test, Coin

### Community 15 - "Mint Screen States"
Cohesion: 0.33
Nodes (6): Screen, COLLECTION, HUB, NFT, PROGRESS, TOKEN

## Knowledge Gaps
- **17 isolated node(s):** `RULE 0 (highest priority) — Follow the user's explicit instructions. They are BLOCKING, not suggestions.`, `STARTED`, `BUSY`, `NEEDS_IMAGES`, `NOTHING_TO_DO` (+12 more)
  These have ≤1 connection - possible missing edges or undocumented components.
- **134 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `MintView` connect `Mint Screen & Android Widgets` to `Receive Screen & Addresses`, `Mint Engine Phase Machine`, `Image Compression Pipeline`, `Mint Screen States`?**
  _High betweenness centrality (0.052) - this node is a cross-community bridge._
- **Why does `StateNft` connect `Mint Engine Phase Machine` to `Buried Coin Tests`, `Image Compression Pipeline`?**
  _High betweenness centrality (0.029) - this node is a cross-community bridge._
- **Why does `Cb` connect `Node API Callbacks` to `Mint Screen & Android Widgets`, `Receive Screen & Addresses`, `Mint Engine Phase Machine`?**
  _High betweenness centrality (0.023) - this node is a cross-community bridge._
- **What connects `RULE 0 (highest priority) — Follow the user's explicit instructions. They are BLOCKING, not suggestions.`, `STARTED`, `BUSY` to the rest of the system?**
  _17 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `Mint Screen & Android Widgets` be split into smaller, more focused modules?**
  _Cohesion score 0.09218807848944835 - nodes in this community are weakly interconnected._
- **Should `Mint Driver & Activity Lifecycle` be split into smaller, more focused modules?**
  _Cohesion score 0.06605222734254992 - nodes in this community are weakly interconnected._
- **Should `Modal Sheet System` be split into smaller, more focused modules?**
  _Cohesion score 0.10121951219512196 - nodes in this community are weakly interconnected._