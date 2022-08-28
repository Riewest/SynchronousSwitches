package com.suicidesquid.syncswitch.datagen;

import java.util.function.Consumer;

import com.suicidesquid.syncswitch.SynchronousSwitches;
import com.suicidesquid.syncswitch.init.BlockInit;

import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder{

    private final String recipeLocation;

    public ModRecipeProvider(DataGenerator dataGenerator) {
        super(dataGenerator);
        recipeLocation = SynchronousSwitches.MODID + ":";
    }


    private ShapelessRecipeBuilder cycleRecipe(ItemLike input, ItemLike output){
        return ShapelessRecipeBuilder.shapeless(output)
                                        .requires(input)
                                        .unlockedBy("has_sync_switch", inventoryTrigger(ItemPredicate.Builder.item().of(BlockInit.SWITCH_BLOCK.get()).build()))
                                        .group(SynchronousSwitches.MODID);
    }


    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> finishedRecipeConsumer) {



        ShapedRecipeBuilder.shaped(BlockInit.SWITCH_BLOCK.get())
                                    .define('E', Tags.Items.ENDER_PEARLS)
                                    .define('S', Tags.Items.RODS_WOODEN)
                                    .define('C', Tags.Items.STONE)
                                    .pattern("E")
                                    .pattern("S")
                                    .pattern("C")
                                    .group(SynchronousSwitches.MODID)
                                    .unlockedBy("has_ender_pearl", inventoryTrigger(ItemPredicate.Builder.item().of(Items.ENDER_PEARL).build()))
                                    .save(finishedRecipeConsumer);
        ShapedRecipeBuilder.shaped(BlockInit.CHANNEL_OUTPUT_BLOCK.get())
                                    .define('E', Tags.Items.ENDER_PEARLS)
                                    .define('S', Tags.Items.STONE)
                                    .define('R', Tags.Items.DUSTS_REDSTONE)
                                    .pattern("RSR")
                                    .pattern("SES")
                                    .pattern("RSR")
                                    .group(SynchronousSwitches.MODID)
                                    .unlockedBy("has_ender_pearl", inventoryTrigger(ItemPredicate.Builder.item().of(Items.ENDER_PEARL).build()))
                                    .save(finishedRecipeConsumer);
        ShapedRecipeBuilder.shaped(BlockInit.CHANNEL_INPUT_BLOCK.get())
                                    .define('E', Tags.Items.ENDER_PEARLS)
                                    .define('S', Tags.Items.STONE)
                                    .define('R', Tags.Items.DUSTS_REDSTONE)
                                    .pattern("SRS")
                                    .pattern("RER")
                                    .pattern("SRS")
                                    .group(SynchronousSwitches.MODID)
                                    .unlockedBy("has_ender_pearl", inventoryTrigger(ItemPredicate.Builder.item().of(Items.ENDER_PEARL).build()))
                                    .save(finishedRecipeConsumer);
        ShapedRecipeBuilder.shaped(BlockInit.LIGHT_BLOCK.get(), 4)
                                    .define('E', Tags.Items.ENDER_PEARLS)
                                    .define('S', Items.SEA_LANTERN)
                                    .pattern(" S ")
                                    .pattern("SES")
                                    .pattern(" S ")
                                    .group(SynchronousSwitches.MODID)
                                    .unlockedBy("has_sea_lantern", inventoryTrigger(ItemPredicate.Builder.item().of(Items.SEA_LANTERN).build()))
                                    .save(finishedRecipeConsumer);

        ShapedRecipeBuilder.shaped(BlockInit.LIGHT_BLOCK.get())
                                    .define('P', BlockInit.LIGHT_PANEL_BLOCK.get())
                                    .pattern("PP")
                                    .pattern("PP")
                                    .group(SynchronousSwitches.MODID)
                                    .unlockedBy("has_light_block", inventoryTrigger(ItemPredicate.Builder.item().of(BlockInit.LIGHT_BLOCK.get()).build()))
                                    .save(finishedRecipeConsumer, new ResourceLocation(recipeLocation + "light_block_1"));

        ShapelessRecipeBuilder.shapeless(BlockInit.LIGHT_PANEL_BLOCK.get(), 4)
                                    .requires(BlockInit.LIGHT_BLOCK.get())
                                    .unlockedBy("has_light_block", inventoryTrigger(ItemPredicate.Builder.item().of(BlockInit.LIGHT_BLOCK.get()).build()))
                                    .group(SynchronousSwitches.MODID)
                                    .save(finishedRecipeConsumer);

        cycleRecipe(BlockInit.SWITCH_BLOCK.get(), BlockInit.VANILLA_SWITCH_BLOCK.get()).save(finishedRecipeConsumer);
        cycleRecipe(BlockInit.VANILLA_SWITCH_BLOCK.get(), BlockInit.IO_SWITCH_BLOCK.get()).save(finishedRecipeConsumer);
        cycleRecipe(BlockInit.IO_SWITCH_BLOCK.get(), BlockInit.ESTOP_BUTTON_BLOCK.get()).save(finishedRecipeConsumer);
        cycleRecipe(BlockInit.ESTOP_BUTTON_BLOCK.get(), BlockInit.BIG_BUTTON_BLOCK.get()).save(finishedRecipeConsumer);
        cycleRecipe(BlockInit.BIG_BUTTON_BLOCK.get(), BlockInit.SWITCH_BLOCK.get()).save(finishedRecipeConsumer, new ResourceLocation(recipeLocation + "switch_block_1"));
    }
    
}
