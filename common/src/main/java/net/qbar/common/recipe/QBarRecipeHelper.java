package net.qbar.common.recipe;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.oredict.OreIngredient;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import net.qbar.common.QBarConstants;
import net.qbar.common.init.QBarBlocks;
import net.qbar.common.init.QBarItems;
import net.qbar.common.recipe.ingredient.FluidStackRecipeIngredient;
import net.qbar.common.recipe.ingredient.ItemStackRecipeIngredient;
import net.qbar.common.recipe.type.*;
import org.apache.commons.lang3.StringUtils;

public class QBarRecipeHelper
{
    public static void addBlockToPlateRecipe(final String metalName)
    {
        final ItemStack plate = new ItemStack(QBarItems.METALPLATE, 9, QBarMaterials.metals.indexOf(metalName));

        QBarRecipeHandler.RECIPES.get(QBarRecipeHandler.ROLLINGMILL_UID)
                .add(new RollingMillRecipe(
                        new ItemStackRecipeIngredient("block" + StringUtils.capitalize(metalName), 1),
                        new ItemStackRecipeIngredient(plate)));
    }

    public static void addIngotToPlateRecipe(final String metalName)
    {
        final ItemStack plate = new ItemStack(QBarItems.METALPLATE, 1, QBarMaterials.metals.indexOf(metalName));

        QBarRecipeHandler.RECIPES.get(QBarRecipeHandler.ROLLINGMILL_UID)
                .add(new RollingMillRecipe(
                        new ItemStackRecipeIngredient("ingot" + StringUtils.capitalize(metalName), 1),
                        new ItemStackRecipeIngredient(plate)));
    }

    public static void addIngotToGearRecipe(String metalName)
    {
        ItemStack gearStack = new ItemStack(QBarItems.METALGEAR, 1, QBarMaterials.metals.indexOf(metalName));

        QBarRecipeHandler.CRAFTING_RECIPES.add(new ShapedOreRecipe(new ResourceLocation(QBarConstants.MODID, "gear" +
                metalName), gearStack, " X ", "XOX", " X ", 'X',
                new OreIngredient("ingot" + StringUtils.capitalize(metalName)), 'O', new ItemStack(Items.IRON_INGOT))
                .setRegistryName(new ResourceLocation(QBarConstants.MODID, "gear" + metalName)));
    }

    public static void addBlockToIngotRecipe(String metalName)
    {
        ItemStack ingotStack = new ItemStack(QBarItems.METALINGOT, 9, QBarMaterials.metals.indexOf(metalName));

        QBarRecipeHandler.CRAFTING_RECIPES
                .add(new ShapelessOreRecipe(new ResourceLocation(QBarConstants.MODID, "block" + metalName), ingotStack,
                        new OreIngredient("block" + StringUtils.capitalize(metalName)))
                        .setRegistryName(new ResourceLocation(QBarConstants.MODID, "block" + metalName)));
    }

    public static void addIngotToBlockRecipe(String metalName)
    {
        ItemStack blockStack = new ItemStack(QBarBlocks.METALBLOCK, 1, QBarMaterials.metals.indexOf(metalName));

        QBarRecipeHandler.CRAFTING_RECIPES
                .add(new ShapedOreRecipe(new ResourceLocation(QBarConstants.MODID, "block_ingot" + metalName),
                        blockStack, "XXX", "XXX", "XXX", 'X',
                        new OreIngredient("ingot" + StringUtils.capitalize(metalName)))
                        .setRegistryName(new ResourceLocation(QBarConstants.MODID, "block_ingot" + metalName)));
    }

    public static void addLiquidBoilerRecipe(Fluid fuel, int heatPerMb, int timePerBucket)
    {
        QBarRecipeHandler.RECIPES.get(QBarRecipeHandler.LIQUIDBOILER_UID)
                .add(new LiquidBoilerRecipe(fuel, heatPerMb, timePerBucket));
    }

    public static void addSawMillRecipe(ItemStack input, ItemStack output)
    {
        QBarRecipeHandler.RECIPES.get(QBarRecipeHandler.SAW_MILL_UID).add(
                new SawMillRecipe(new ItemStackRecipeIngredient(input), new ItemStackRecipeIngredient(output)));
    }

    public static void addMeltingRecipe(String metalName, float lowMeltingPoint, float hightMeltingPoint, int
            baseMeltingTime)
    {
        QBarRecipeHandler.RECIPES.get(QBarRecipeHandler.MELTING_UID).add(
                new MeltRecipe(new ItemStackRecipeIngredient("ingot" + StringUtils.capitalize(metalName), 1),
                        new FluidStackRecipeIngredient(FluidRegistry.getFluidStack("molten" + metalName, 144)),
                        lowMeltingPoint, hightMeltingPoint, baseMeltingTime)
        );

        QBarRecipeHandler.RECIPES.get(QBarRecipeHandler.MELTING_UID).add(
                new MeltRecipe(new ItemStackRecipeIngredient("block" + StringUtils.capitalize(metalName), 1),
                        new FluidStackRecipeIngredient(FluidRegistry.getFluidStack("molten" + metalName, 1296)),
                        lowMeltingPoint, hightMeltingPoint, baseMeltingTime));

        QBarRecipeHandler.RECIPES.get(QBarRecipeHandler.MELTING_UID).add(
                new MeltRecipe(new ItemStackRecipeIngredient("plate" + StringUtils.capitalize(metalName), 1),
                        new FluidStackRecipeIngredient(FluidRegistry.getFluidStack("molten" + metalName, 144)),
                        lowMeltingPoint, hightMeltingPoint, baseMeltingTime)
        );
    }

    public static void addAlloyRecipe(String firstMetal, String secondMetal, int secondCount, String resultMetal)
    {
        QBarRecipeHandler.RECIPES.get(QBarRecipeHandler.ALLOY_UID).add(
                new AlloyRecipe(new FluidStackRecipeIngredient(FluidRegistry.getFluidStack("molten" + firstMetal, 1)),
                        new FluidStackRecipeIngredient(FluidRegistry.getFluidStack("molten" + secondMetal,
                                secondCount)),
                        new FluidStackRecipeIngredient(FluidRegistry.getFluidStack("molten" + resultMetal, 1 +
                                secondCount)))
        );
    }
}
