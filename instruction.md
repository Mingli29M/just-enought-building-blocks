# Just Enought Building Blocks — developer instructions

This project is a **Fabric** mod for **Minecraft 1.20.1**, using **Gradle** and **Fabric Loom**. Commands below use Windows `gradlew.bat`; on macOS or Linux use `./gradlew` instead.

---

## Test run (development client)

From the project root:

```powershell
.\gradlew.bat runClient
```

This starts Minecraft with the mod on the **client** classpath (normal in-game testing).

Optional: regenerate data-driven assets (models, recipes, tags, loot tables) after you change registration or filters:

```powershell
.\gradlew.bat runDatagen
```

---

## Build (produce the mod JAR)

```powershell
.\gradlew.bat build
```

The remapped mod JAR is typically under:

`build/libs/`

Look for a file named like `just-enought-building-blocks-<version>.jar` (exact name depends on `mod_version` in `gradle.properties`).

---

## Clean build

A clean build removes Gradle output, then compiles from scratch:

```powershell
.\gradlew.bat clean build
```

Use this when caches or generated files seem wrong, or after large refactors.

---

## Add or remove blocks from the variant list (vertical slab / corner pillar / quarter)

Variants are created automatically for **vanilla full cube blocks** that pass `JebbBlockFilter.qualifies()`.

- **Main filter logic:** `src/main/java/mingli29/jebb/util/JebbBlockFilter.java`  
  - **`DENY`:** block IDs (path only, `minecraft:` namespace) that must **never** get variants.  
  - **`ORE_PATHS`:** ore blocks excluded from variants.  
  - **`TEMP_NATURAL_EXTRA`:** extra “natural terrain” style blocks excluded.  
  - **`isTemporaryNaturalBlock()`:** excludes blocks in certain vanilla tags (stone, dirt, sand, etc.).

- **Log columns (no quarter / ¼ block):** same file, method **`skipsQuarterVariants()`** — parents that match here still get vertical slabs and corner pillars, but **not** quarter blocks.

- **Top/side/bottom textures** for non-cube parents (bookshelf-style): `src/main/java/mingli29/jebb/util/JebbTextureMap.java` — add or adjust entries, or rely on the automatic column rule for `_log` / `_stem` / bamboo paths.

After you change which blocks qualify, run **`runDatagen`** so generated JSON under `src/main/generated/` stays in sync.

---

## Where to add custom (mod-owned) blocks

- **Register blocks and items:** `src/main/java/mingli29/jebb/block/JebbBlocks.java`  
  Example: `OAK_MUZHUAN` is a simple mod block registered there and picked up by `registerVariantsForParent()` so it also gets vertical slab / quarter / pillar variants unless you special-case it.

- **Mod entrypoint (init order):** `src/main/java/mingli29/jebb/JustEnoughtBuildingBlocks.java` — calls `JebbBlocks.init()` and `JebbItemGroups.register()`.

- **Client-only setup (rendering, etc.):** `src/client/java/mingli29/jebb/client/JustEnoughtBuildingBlocksClient.java`

- **Data generation** (recipes, models, tags, loot): under `src/client/java/mingli29/jebb/client/datagen/` — extend providers there and ensure your data generator entry is wired in the Fabric data gen setup for this template.

- **Translations:** `src/main/resources/assets/just-enought-building-blocks/lang/` (e.g. `en_us.json`).

---

## Checklist for adding more blocks

Use this checklist when adding another family such as square bricks, chiseled planks, triangle blocks, striped blocks, or a new specific type.

1. **Choose the block id and type**
   - Use a stable lowercase id such as `oak_triangle_block` or `striped_acacia_plank`.
   - Decide whether it is a simple parent block only, or whether it should also get JEBB variants: vertical slab, horizontal slab, quarter, and corner pillar.

2. **Register the block**
   - Add a public field in `JebbBlocks.java`.
   - Create it in `init()` with `registerSimpleBlock("<id>", <template parent>)`.
   - Add it to the custom parent list passed to `registerVariantsForParent(parent)` if it should receive variants.
   - If vanilla already has a matching slab, `JebbSlabs.vanillaSlabForParent()` will use it. Otherwise `registerVariantsForParent()` creates a mod horizontal slab named `slab_<id>`.

3. **Add model and texture information**
   - Add a base block model under `src/main/resources/assets/just-enought-building-blocks/models/block/<id>.json`.
   - Add the item model under `models/item/<id>.json`, usually with `"parent": "just-enought-building-blocks:block/<id>"`.
   - If you add real mod texture PNGs, place them under `textures/block/` and point the model at `just-enought-building-blocks:block/<texture>`.
   - If the block reuses a vanilla texture, point the model at `minecraft:block/<texture>`.
   - Add or update `JebbTextureMap.java` for any custom parent whose generated variants need special top/side/bottom textures. This is what vertical slabs, horizontal slabs, quarters, corner pillars, and item models use during datagen.

4. **Add blockstates and translations**
   - Add `blockstates/<id>.json` for the parent block.
   - Add display names in each language file under `assets/just-enought-building-blocks/lang/`.
   - Variant item names are composed by `JebbVariantBlockItem`, so the parent translation must exist.

5. **Add recipes**
   - In `JebbRecipeProvider.java`, add a `registerWoodRecipe(output, JebbBlocks.<FIELD>, Blocks.<PARENT>)` or another recipe for the new parent.
   - Blocks in `registerVariantsForParent()` automatically get stonecutting/shaped/shapeless recipes for vertical slabs, horizontal slabs, quarters, and corner pillars.

6. **Add loot tables**
   - In `JebbBlockLootProvider.java`, call `addSimpleWoodBlockLoot(JebbBlocks.<FIELD>)` or the correct loot method for the parent block.
   - Variant loot tables are generated automatically for vertical slabs, horizontal slabs, quarters, and corner pillars.

7. **Add tags**
   - In `JebbBlockTagProvider.java`, include the block in `woodVariantParents()` when it behaves like wood.
   - Update `tagSourceForParent()` so the new custom block mirrors the correct vanilla block tags such as `mineable/axe` and tool requirements.

8. **Regenerate and check output**
   - Run `./gradlew runDatagen`.
   - Confirm generated models under `src/main/generated/assets/just-enought-building-blocks/models/` use existing textures.
   - Confirm generated data includes recipes, loot tables, and tags under `src/main/generated/data/just-enought-building-blocks/`.

---

## How to add or change creative “banners” (section headers)

Section headers are the **striped bars with a title** above each group in the creative inventory.

1. **Which tab and which blocks sit in each section**  
   `src/main/java/mingli29/jebb/item/JebbItemGroups.java`  
   - **`MAIN_SECTIONS`** — *JEBB block variants* tab (corner pillars, vertical slabs, quarters, etc.).  
   - **`BLOCKS_SECTIONS`** — *JEBB blocks* tab (e.g. wood-style custom blocks).  
   Each `Section` has a **translation key** (`labelKey`) and a **`SectionBannerStyle`**.

2. **Banner look (height, backdrop block texture)**  
   `src/client/java/mingli29/jebb/client/render/JebbBannerRenderer.java` — `bannerHeight()` and `backdropState()` per style.

3. **Section title text**  
   Language files, e.g. `src/main/resources/assets/just-enought-building-blocks/lang/en_us.json` — keys like `jebb.section.vertical_slabs`.

4. **Layout injection (spacer row + row index for drawing)**  
   `src/main/java/mingli29/jebb/mixin/CreativeModeTabMixin.java`

5. **Drawing banners on screen**  
   `src/client/java/mingli29/jebb/client/mixin/CreativeModeInventoryScreenMixin.java`

---

## Push to GitHub

1. **Create a repository** on GitHub (empty repo, no need to add a README if you already have one locally).

2. **From the project root**, if Git is not initialized yet:

   ```powershell
   git init
   git add .
   git commit -m "Initial commit"
   ```

3. **Add the remote** (replace `YOUR_USER` and `YOUR_REPO`):

   ```powershell
   git remote add origin https://github.com/YOUR_USER/YOUR_REPO.git
   ```

4. **Push** (first time, set upstream):

   ```powershell
   git branch -M main
   git push -u origin main
   ```

Later changes:

```powershell
git add .
git commit -m "Describe your change"
git push
```

**Tip:** Add a `.gitignore` that excludes `build/`, `.gradle/`, `run/` (world and logs), and IDE folders if you do not want them in the repo. This template may already ignore some of these; adjust as needed before the first push.

---

## Pull from GitHub

### If you do NOT have the project locally yet

```powershell
git clone https://github.com/YOUR_USER/YOUR_REPO.git
cd YOUR_REPO
```

### If you already have the project locally

From the project root:

```powershell
git status
git pull
```

If you work on a specific branch, replace it as needed:

```powershell
git pull origin main
```

If you see conflicts, resolve them, then commit the merge.

