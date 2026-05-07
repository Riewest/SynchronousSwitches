package com.suicidesquid.syncswitch.datagen;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.suicidesquid.syncswitch.SynchronousSwitches;
import com.suicidesquid.syncswitch.setup.ModRegistration;

import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.Tags;
public class ModRecipeProvider extends RecipeProvider {

    public ModRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output);
    }

    private void buildCycleRecipes(HolderGetter<Item> items, List<ModRegistration.BlockWithEntity<?, ?>> list, ItemLike firstItem) {
        for (int i = 0; i < list.size() - 1; i++) {
            cycleRecipe(items, list.get(i).get(), list.get(i + 1).get()).save(output);
        }
        String wrapKey = BuiltInRegistries.ITEM.getKey(firstItem.asItem()).getPath() + "_1";
        cycleRecipe(items, list.get(list.size() - 1).get(), firstItem)
            .save(output, ResourceKey.create(Registries.RECIPE, Identifier.fromNamespaceAndPath(SynchronousSwitches.MODID, wrapKey)));
    }

    private ShapelessRecipeBuilder cycleRecipe(HolderGetter<Item> items, ItemLike input, ItemLike outputItem) {
        return ShapelessRecipeBuilder.shapeless(items, RecipeCategory.MISC, outputItem)
                                        .requires(input)
                                        .unlockedBy("has_sync_switch", inventoryTrigger(ItemPredicate.Builder.item().of(items, ModRegistration.SWITCH_BLOCK.get()).build()))
                                        .group(SynchronousSwitches.MODID);
    }

    @Override
    protected void buildRecipes() {
        HolderGetter<Item> items = this.registries.lookupOrThrow(Registries.ITEM);
        ShapedRecipeBuilder.shaped(items, RecipeCategory.MISC, ModRegistration.SWITCH_BLOCK.get())
                                    .define('E', Tags.Items.ENDER_PEARLS)
                                    .define('S', Tags.Items.RODS_WOODEN)
                                    .define('C', Tags.Items.STONES)
                                    .pattern("E")
                                    .pattern("S")
                                    .pattern("C")
                                    .group(SynchronousSwitches.MODID)
                                    .unlockedBy("has_ender_pearl", inventoryTrigger(ItemPredicate.Builder.item().of(items, Items.ENDER_PEARL).build()))
                                    .save(output);
        ShapedRecipeBuilder.shaped(items, RecipeCategory.MISC, ModRegistration.CHANNEL_OUTPUT_BLOCK.get())
                                    .define('E', Tags.Items.ENDER_PEARLS)
                                    .define('S', Tags.Items.STONES)
                                    .define('R', Tags.Items.DUSTS_REDSTONE)
                                    .pattern("RSR")
                                    .pattern("SES")
                                    .pattern("RSR")
                                    .group(SynchronousSwitches.MODID)
                                    .unlockedBy("has_ender_pearl", inventoryTrigger(ItemPredicate.Builder.item().of(items, Items.ENDER_PEARL).build()))
                                    .save(output);
        ShapedRecipeBuilder.shaped(items, RecipeCategory.MISC, ModRegistration.CHANNEL_INPUT_BLOCK.get())
                                    .define('E', Tags.Items.ENDER_PEARLS)
                                    .define('S', Tags.Items.STONES)
                                    .define('R', Tags.Items.DUSTS_REDSTONE)
                                    .pattern("SRS")
                                    .pattern("RER")
                                    .pattern("SRS")
                                    .group(SynchronousSwitches.MODID)
                                    .unlockedBy("has_ender_pearl", inventoryTrigger(ItemPredicate.Builder.item().of(items, Items.ENDER_PEARL).build()))
                                    .save(output);
        ShapedRecipeBuilder.shaped(items, RecipeCategory.MISC, ModRegistration.LIGHT_BLOCK.get(), 4)
                                    .define('E', Tags.Items.ENDER_PEARLS)
                                    .define('S', Items.SEA_LANTERN)
                                    .pattern(" S ")
                                    .pattern("SES")
                                    .pattern(" S ")
                                    .group(SynchronousSwitches.MODID)
                                    .unlockedBy("has_sea_lantern", inventoryTrigger(ItemPredicate.Builder.item().of(items, Items.SEA_LANTERN).build()))
                                    .save(output);

        ShapedRecipeBuilder.shaped(items, RecipeCategory.MISC, ModRegistration.LIGHT_BLOCK.get())
                                    .define('P', ModRegistration.LIGHT_PANEL_BLOCK.get())
                                    .pattern("PP")
                                    .pattern("PP")
                                    .group(SynchronousSwitches.MODID)
                                    .unlockedBy("has_light_block", inventoryTrigger(ItemPredicate.Builder.item().of(items, ModRegistration.LIGHT_BLOCK.get()).build()))
                                    .save(output, ResourceKey.create(Registries.RECIPE, Identifier.fromNamespaceAndPath(SynchronousSwitches.MODID, "light_block_1")));

        ShapelessRecipeBuilder.shapeless(items, RecipeCategory.MISC, ModRegistration.LIGHT_PANEL_BLOCK.get(), 4)
                                    .requires(ModRegistration.LIGHT_BLOCK.get())
                                    .unlockedBy("has_light_block", inventoryTrigger(ItemPredicate.Builder.item().of(items, ModRegistration.LIGHT_BLOCK.get()).build()))
                                    .group(SynchronousSwitches.MODID)
                                    .save(output);

        ShapelessRecipeBuilder.shapeless(items, RecipeCategory.MISC, ModRegistration.BIG_BUTTON_BLOCK.get())
                                        .requires(Items.STONE_BUTTON)
                                        .requires(Tags.Items.ENDER_PEARLS)
                                        .unlockedBy("has_sync_switch", inventoryTrigger(ItemPredicate.Builder.item().of(items, ModRegistration.SWITCH_BLOCK.get()).build()))
                                        .group(SynchronousSwitches.MODID)
                                        .save(output);

        
        // Switches
        buildCycleRecipes(items, ModRegistration.getSwitches(), ModRegistration.SWITCH_BLOCK.get());

        // Buttons
        buildCycleRecipes(items, ModRegistration.getButtons(), ModRegistration.BIG_BUTTON_BLOCK.get());
    }

    public static class Runner extends RecipeProvider.Runner {
        public Runner(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
            super(output, registries);
        }

        @Override
        protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
            return new ModRecipeProvider(registries, output);
        }

        @Override
        public String getName() {
            return "SynchronousSwitches Recipes";
        }
    }
}
