package net.qbar.common.recipe.type;

import lombok.Getter;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.qbar.common.recipe.QBarRecipe;
import net.qbar.common.recipe.ingredient.FluidStackRecipeIngredient;
import net.qbar.common.recipe.ingredient.SteamStackRecipeIngredient;
import net.qbar.common.steam.SteamStack;

public class LiquidBoilerRecipe extends QBarRecipe
{
    @Getter
    private int time;

    public LiquidBoilerRecipe(Fluid fluid, int heatPerMb, int timePerBucket)
    {
        this.inputs.put(FluidStack.class, NonNullList.withSize(1, new FluidStackRecipeIngredient(fluid)));
        this.outputs.put(SteamStack.class, NonNullList.withSize(1, new SteamStackRecipeIngredient(heatPerMb)));

        this.time = timePerBucket;
    }
}
