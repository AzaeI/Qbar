package net.qbar.common.tile.machine;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.qbar.common.container.BuiltContainer;
import net.qbar.common.fluid.MultiFluidTank;
import net.qbar.common.recipe.QBarRecipeHandler;
import net.qbar.common.recipe.type.MeltRecipe;
import net.qbar.common.tile.TileMultiblockInventoryBase;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class TileAlloyCauldron extends TileMultiblockInventoryBase implements ITickable
{
    private final MultiFluidTank tanks;

    private final float maxHeat;
    private       float heat;
    private       float remainingHeat;

    private MeltRecipe currentRecipe;
    private float      meltProgress;

    public TileAlloyCauldron()
    {
        super("alloycauldron", 5);
        this.tanks = new MultiFluidTank(16* Fluid.BUCKET_VOLUME);
        this.maxHeat = 1500;
    }

    @Override
    public void update()
    {
        if (this.isClient())
            return;

        this.heatLogic();
        this.meltLogic();
        this.alloyLogic();
    }

    private void heatLogic()
    {
        if (this.heat > this.getMinimumTemp())
            this.heat -= 0.5f;
        if (this.heat >= this.maxHeat)
            return;

        if (!this.getStackInSlot(4).isEmpty() && this.getStackInSlot(5).isEmpty())
        {
            this.setInventorySlotContents(5, this.decrStackSize(4, 1));
            this.remainingHeat = TileEntityFurnace.getItemBurnTime(this.getStackInSlot(5));
        }

        this.remainingHeat--;
        this.heat++;

        if (this.remainingHeat <= 0)
            this.setInventorySlotContents(5, ItemStack.EMPTY);
    }

    private void meltLogic()
    {
        if (this.heat == this.getMinimumTemp() || this.isEmpty())
            return;

        if (!this.getStackInSlot(0).isEmpty() && this.getStackInSlot(1).isEmpty())
        {
            this.setInventorySlotContents(1, this.decrStackSize(0, 1));

            QBarRecipeHandler.getRecipe(QBarRecipeHandler.MELTING_UID,
                    this.getStackInSlot(1))
                    .ifPresent(qBarRecipe -> this.currentRecipe = (MeltRecipe) qBarRecipe);
        }

        if (this.currentRecipe != null)
        {
            float efficiency = (this.currentRecipe.getHighMeltingPoint() - this.currentRecipe.getLowMeltingPoint()) /
                    (this.heat - this.currentRecipe.getLowMeltingPoint());
            this.meltProgress += (1 / this.currentRecipe.getTime()) * efficiency;

            if (this.meltProgress > 1 && this.fillTanks(this.currentRecipe.getOutput().copy()))
            {
                this.currentRecipe = null;
                this.meltProgress = 0;
            }
        }
    }

    private void alloyLogic()
    {
        if (this.tanks.getFluids().size() < 2)
            return;
    }

    private boolean fillTanks(FluidStack fluid)
    {
        // TODO: Multi tank filling
        return false;
    }

    private int getMinimumTemp()
    {
        return (int) (this.world.getBiome(this.getPos()).getTemperature(this.pos) * 200);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag)
    {

        return super.writeToNBT(tag);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        super.readFromNBT(tag);
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side)
    {
        return new int[0];
    }

    @Override
    public boolean canInsertItem(int index, ItemStack stack, EnumFacing side)
    {
        return false;
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing side)
    {
        return false;
    }

    @Override
    public BuiltContainer createContainer(EntityPlayer player)
    {
        return null;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, BlockPos from, @Nullable EnumFacing facing)
    {
        return false;
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, BlockPos from, @Nullable EnumFacing facing)
    {
        return null;
    }
}
