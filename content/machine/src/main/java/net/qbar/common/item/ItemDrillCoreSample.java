package net.qbar.common.item;

import com.google.common.collect.Sets;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.qbar.common.init.QBarItems;
import net.qbar.common.ore.QBarMineral;
import net.qbar.common.ore.QBarOres;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

public class ItemDrillCoreSample extends ItemBase
{
    public ItemDrillCoreSample()
    {
        super("drillcoresample");
        this.setMaxStackSize(1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        if (stack.hasTagCompound() && stack.getTagCompound().hasKey("minerals"))
        {
            tooltip.add("Position: x=" + stack.getTagCompound().getInteger("xpos") + " y="
                    + stack.getTagCompound().getInteger("ypos") + " z=" + stack.getTagCompound().getInteger("zpos"));
            tooltip.add("Minerals:");
            for (int i = 0; i < stack.getTagCompound().getInteger("minerals"); i++)
            {
                float quantity = stack.getTagCompound().getFloat("quantity" + i);
                QBarOres.getMineralFromName(stack.getTagCompound().getString("mineral" + i))
                        .ifPresent(mineral -> tooltip.add(
                                mineral.getRarity().rarityColor + I18n.format(mineral.getName()) + " " + quantity));
            }
        }
    }

    public static ItemStack getSample(BlockPos pos, Map<QBarMineral, Float> results)
    {
        ItemStack stack = new ItemStack(QBarItems.DRILL_CORE_SAMPLE);
        NBTTagCompound tag = new NBTTagCompound();
        stack.setTagCompound(tag);

        tag.setInteger("xpos", pos.getX());
        tag.setInteger("ypos", pos.getY());
        tag.setInteger("zpos", pos.getZ());

        int i = 0;
        for (QBarMineral mineral : Sets.newTreeSet(results.keySet()))
        {
            tag.setString("mineral" + i, mineral.getName());
            tag.setFloat("quantity" + i, results.get(mineral));
            i++;
        }
        tag.setInteger("minerals", i);

        return stack;
    }
}
