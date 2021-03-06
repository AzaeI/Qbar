package net.qbar.common.recipe;

import net.minecraft.block.BlockPlanks;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;
import net.qbar.common.QBarConstants;
import net.qbar.common.block.BlockMetal;
import net.qbar.common.init.QBarBlocks;
import net.qbar.common.init.QBarItems;
import net.qbar.common.recipe.category.FurnaceRecipeCategory;
import net.qbar.common.recipe.category.OreWasherRecipeCategory;
import net.qbar.common.recipe.category.QBarRecipeCategory;
import net.qbar.common.recipe.category.SortingMachineRecipeCategory;
import net.qbar.common.recipe.type.SludgeRecipe;
import org.apache.commons.lang3.StringUtils;

public class QBarRecipes
{
    public void registerRecipes()
    {
        QBarRecipeHandler.RECIPES.put(QBarRecipeHandler.ROLLINGMILL_UID,
                new QBarRecipeCategory(QBarRecipeHandler.ROLLINGMILL_UID));
        QBarRecipeHandler.RECIPES.put(QBarRecipeHandler.LIQUIDBOILER_UID,
                new QBarRecipeCategory(QBarRecipeHandler.LIQUIDBOILER_UID));
        QBarRecipeHandler.RECIPES.put(QBarRecipeHandler.ORE_WASHER_UID,
                new OreWasherRecipeCategory(QBarRecipeHandler.ORE_WASHER_UID));
        QBarRecipeHandler.RECIPES.put(QBarRecipeHandler.FURNACE_UID,
                new FurnaceRecipeCategory(QBarRecipeHandler.FURNACE_UID));
        QBarRecipeHandler.RECIPES.put(QBarRecipeHandler.SORTING_MACHINE_UID,
                new SortingMachineRecipeCategory(QBarRecipeHandler.SORTING_MACHINE_UID));
        QBarRecipeHandler.RECIPES.put(QBarRecipeHandler.SAW_MILL_UID,
                new QBarRecipeCategory(QBarRecipeHandler.SAW_MILL_UID));
        QBarRecipeHandler.RECIPES.put(QBarRecipeHandler.MELTING_UID,
                new QBarRecipeCategory(QBarRecipeHandler.MELTING_UID));
        QBarRecipeHandler.RECIPES.put(QBarRecipeHandler.ALLOY_UID,
                new QBarRecipeCategory(QBarRecipeHandler.ALLOY_UID));

        QBarMaterials.metals.forEach(metalName ->
        {
            QBarRecipeHelper.addIngotToPlateRecipe(metalName);
            QBarRecipeHelper.addBlockToPlateRecipe(metalName);

            if (BlockMetal.VARIANTS.getAllowedValues().contains(metalName))
            {
                QBarRecipeHelper.addBlockToIngotRecipe(metalName);
                QBarRecipeHelper.addIngotToBlockRecipe(metalName);
            }
            if (QBarItems.METALGEAR.hasMetalVariant(metalName))
                QBarRecipeHelper.addIngotToGearRecipe(metalName);
        });

        QBarRecipeHelper.addLiquidBoilerRecipe(FluidRegistry.LAVA, 2, 1200);

        QBarRecipeHelper.addSawMillRecipe(new ItemStack(Blocks.LOG, 1, BlockPlanks.EnumType.OAK.getMetadata()),
                new ItemStack(Blocks.PLANKS, 5, BlockPlanks.EnumType.OAK.getMetadata()));
        QBarRecipeHelper.addSawMillRecipe(new ItemStack(Blocks.LOG, 1, BlockPlanks.EnumType.SPRUCE.getMetadata()),
                new ItemStack(Blocks.PLANKS, 5, BlockPlanks.EnumType.SPRUCE.getMetadata()));
        QBarRecipeHelper.addSawMillRecipe(new ItemStack(Blocks.LOG, 1, BlockPlanks.EnumType.BIRCH.getMetadata()),
                new ItemStack(Blocks.PLANKS, 5, BlockPlanks.EnumType.BIRCH.getMetadata()));
        QBarRecipeHelper.addSawMillRecipe(new ItemStack(Blocks.LOG, 1, BlockPlanks.EnumType.JUNGLE.getMetadata()),
                new ItemStack(Blocks.PLANKS, 5, BlockPlanks.EnumType.JUNGLE.getMetadata()));
        QBarRecipeHelper.addSawMillRecipe(new ItemStack(Blocks.LOG2, 1, BlockPlanks.EnumType.ACACIA.getMetadata() - 4),
                new ItemStack(Blocks.PLANKS, 5, BlockPlanks.EnumType.ACACIA.getMetadata()));
        QBarRecipeHelper.addSawMillRecipe(new ItemStack(Blocks.LOG2, 1, BlockPlanks.EnumType.DARK_OAK.getMetadata() -
                        4),
                new ItemStack(Blocks.PLANKS, 5, BlockPlanks.EnumType.DARK_OAK.getMetadata()));

        QBarRecipeHandler.CRAFTING_RECIPES.add(new SludgeRecipe().setRegistryName(new ResourceLocation(QBarConstants
                .MODID,
                "compressedsludge")));

        QBarRecipeHelper.addMeltingRecipe("iron", 1204, 1204 * 1.25f, 35);
        QBarRecipeHelper.addMeltingRecipe("gold", 1064, 1064 * 1.25f, 20);
        QBarRecipeHelper.addMeltingRecipe("copper", 1085, 1085 * 1.25f, 30);
        QBarRecipeHelper.addMeltingRecipe("bronze", 950, 950 * 1.25f, 25);
        QBarRecipeHelper.addMeltingRecipe("brass", 927, 927 * 1.25f, 25);
        QBarRecipeHelper.addMeltingRecipe("tin", 232, 232 * 1.25f, 10);
        QBarRecipeHelper.addMeltingRecipe("zinc", 419, 419 * 1.25f, 15);
        QBarRecipeHelper.addMeltingRecipe("nickel", 1455, 1455 * 1.25f, 40);
        QBarRecipeHelper.addMeltingRecipe("lead", 327, 327 * 1.25f, 15);
        QBarRecipeHelper.addMeltingRecipe("steel", 1371, 1371 * 1.25f, 35);

        QBarRecipeHelper.addAlloyRecipe("tin", "copper", 3, "bronze");
        QBarRecipeHelper.addAlloyRecipe("zinc", "copper", 3, "brass");
    }

    public void registerOreDict()
    {
        QBarItems.METALGEAR.getMetals().forEach(metal ->
        {
            ItemStack gear = new ItemStack(QBarItems.METALGEAR, 1, QBarMaterials.metals.indexOf(metal));
            OreDictionary.registerOre("gear" + StringUtils.capitalize(metal), gear);
        });

        QBarItems.METALPLATE.getMetals().forEach(metal ->
        {
            ItemStack gear = new ItemStack(QBarItems.METALPLATE, 1, QBarMaterials.metals.indexOf(metal));
            OreDictionary.registerOre("plate" + StringUtils.capitalize(metal), gear);
        });

        QBarItems.METALINGOT.getMetals().forEach(metal ->
        {
            ItemStack gear = new ItemStack(QBarItems.METALINGOT, 1, QBarMaterials.metals.indexOf(metal));
            OreDictionary.registerOre("ingot" + StringUtils.capitalize(metal), gear);
        });

        BlockMetal.VARIANTS.getAllowedValues().forEach(metal -> OreDictionary.registerOre("block" + StringUtils
                        .capitalize(metal),
                new ItemStack(QBarBlocks.METALBLOCK, 1, QBarMaterials.metals.indexOf(metal))));
    }

    @SubscribeEvent
    public void onRecipeRegister(RegistryEvent.Register<IRecipe> event)
    {
        this.registerOreDict();
        this.registerRecipes();

        event.getRegistry().registerAll(QBarRecipeHandler.CRAFTING_RECIPES.toArray(
                new IRecipe[QBarRecipeHandler.CRAFTING_RECIPES.size()]));
    }
}
