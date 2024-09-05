package com.suicidesquid.syncswitch.datagen;

import java.util.function.Consumer;

import com.suicidesquid.syncswitch.SynchronousSwitches;
import com.suicidesquid.syncswitch.setup.Registration;

import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder{

    private final String recipeLocation;

    public ModRecipeProvider(PackOutput packOutput) {
        super(packOutput);
        recipeLocation = SynchronousSwitches.MODID + ":";
    }


    private ShapelessRecipeBuilder cycleRecipe(ItemLike input, ItemLike output){
        return ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, output)
                                        .requires(input)
                                        .unlockedBy("has_sync_switch", inventoryTrigger(ItemPredicate.Builder.item().of(Registration.SWITCH_BLOCK.get()).build()))
                                        .group(SynchronousSwitches.MODID);
    }


    @Override
    protected void buildRecipes(RecipeOutput consumer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Registration.SWITCH_BLOCK.get())
                                    .define('E', Tags.Items.ENDER_PEARLS)
                                    .define('S', Tags.Items.RODS_WOODEN)
                                    .define('C', Tags.Items.STONE)
                                    .pattern("E")
                                    .pattern("S")
                                    .pattern("C")
                                    .group(SynchronousSwitches.MODID)
                                    .unlockedBy("has_ender_pearl", inventoryTrigger(ItemPredicate.Builder.item().of(Items.ENDER_PEARL).build()))
                                    .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Registration.CHANNEL_OUTPUT_BLOCK.get())
                                    .define('E', Tags.Items.ENDER_PEARLS)
                                    .define('S', Tags.Items.STONE)
                                    .define('R', Tags.Items.DUSTS_REDSTONE)
                                    .pattern("RSR")
                                    .pattern("SES")
                                    .pattern("RSR")
                                    .group(SynchronousSwitches.MODID)
                                    .unlockedBy("has_ender_pearl", inventoryTrigger(ItemPredicate.Builder.item().of(Items.ENDER_PEARL).build()))
                                    .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Registration.CHANNEL_INPUT_BLOCK.get())
                                    .define('E', Tags.Items.ENDER_PEARLS)
                                    .define('S', Tags.Items.STONE)
                                    .define('R', Tags.Items.DUSTS_REDSTONE)
                                    .pattern("SRS")
                                    .pattern("RER")
                                    .pattern("SRS")
                                    .group(SynchronousSwitches.MODID)
                                    .unlockedBy("has_ender_pearl", inventoryTrigger(ItemPredicate.Builder.item().of(Items.ENDER_PEARL).build()))
                                    .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Registration.LIGHT_BLOCK.get(), 4)
                                    .define('E', Tags.Items.ENDER_PEARLS)
                                    .define('S', Items.SEA_LANTERN)
                                    .pattern(" S ")
                                    .pattern("SES")
                                    .pattern(" S ")
                                    .group(SynchronousSwitches.MODID)
                                    .unlockedBy("has_sea_lantern", inventoryTrigger(ItemPredicate.Builder.item().of(Items.SEA_LANTERN).build()))
                                    .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Registration.LIGHT_BLOCK.get())
                                    .define('P', Registration.LIGHT_PANEL_BLOCK.get())
                                    .pattern("PP")
                                    .pattern("PP")
                                    .group(SynchronousSwitches.MODID)
                                    .unlockedBy("has_light_block", inventoryTrigger(ItemPredicate.Builder.item().of(Registration.LIGHT_BLOCK.get()).build()))
                                    .save(consumer, new ResourceLocation(recipeLocation + "light_block_1"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Registration.LIGHT_PANEL_BLOCK.get(), 4)
                                    .requires(Registration.LIGHT_BLOCK.get())
                                    .unlockedBy("has_light_block", inventoryTrigger(ItemPredicate.Builder.item().of(Registration.LIGHT_BLOCK.get()).build()))
                                    .group(SynchronousSwitches.MODID)
                                    .save(consumer);

        cycleRecipe(Registration.SWITCH_BLOCK.get(), Registration.VANILLA_SWITCH_BLOCK.get()).save(consumer);
        cycleRecipe(Registration.VANILLA_SWITCH_BLOCK.get(), Registration.IO_SWITCH_BLOCK.get()).save(consumer);
        cycleRecipe(Registration.IO_SWITCH_BLOCK.get(), Registration.ESTOP_BUTTON_BLOCK.get()).save(consumer);
        cycleRecipe(Registration.ESTOP_BUTTON_BLOCK.get(), Registration.BIG_BUTTON_BLOCK.get()).save(consumer);
        cycleRecipe(Registration.BIG_BUTTON_BLOCK.get(), Registration.SWITCH_BLOCK.get()).save(consumer, new ResourceLocation(recipeLocation + "switch_block_1"));
    }
    
}
