package net.qbar.common.recipe.type;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.qbar.common.recipe.QBarRecipe;
import net.qbar.common.recipe.ingredient.ItemStackRecipeIngredient;

public class RollingMillRecipe extends QBarRecipe
{
    public RollingMillRecipe(final ItemStackRecipeIngredient metal, final ItemStackRecipeIngredient plate)
    {
        this.inputs.put(ItemStack.class, NonNullList.withSize(1, metal));
        this.outputs.put(ItemStack.class, NonNullList.withSize(1, plate));
    }

    @Override
    public int getTime()
    {
        return 40;
    }
}
