package net.qbar.common.tile.machine;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import net.qbar.common.container.BuiltContainer;
import net.qbar.common.container.ContainerBuilder;
import net.qbar.common.container.IContainerProvider;
import net.qbar.common.tile.TileInventoryBase;

import javax.annotation.Nullable;
import java.util.EnumMap;
import java.util.stream.IntStream;

public class TileEngineerStorage extends TileInventoryBase implements IContainerProvider, ISidedInventory
{
    private final EnumMap<EnumFacing, SidedInvWrapper> inventoryWrapperCache;

    public TileEngineerStorage()
    {
        super("engineerstorage", 32);

        this.inventoryWrapperCache = new EnumMap<>(EnumFacing.class);
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side)
    {
        return IntStream.range(0, 32).toArray();
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction)
    {
        return index >= 0 && index <= 31;
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction)
    {
        return index >= 0 && index <= 31;
    }

    @Override
    public BuiltContainer createContainer(EntityPlayer player)
    {
        return new ContainerBuilder("engineerstorage", player).player(player.inventory).inventory(8, 84).hotbar(8, 142)
                .addInventory().tile(this).slotLine(0, 16, 8, 8, EnumFacing.Axis.X)
                .slotLine(8, 16, 26, 8, EnumFacing.Axis.X).slotLine(16, 16, 44, 8, EnumFacing.Axis.X)
                .slotLine(24, 16, 62, 8, EnumFacing.Axis.X).addInventory().create();
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
    {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return true;
        return super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
    {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return (T) this.getInventoryWrapper(facing);
        return super.getCapability(capability, facing);
    }

    protected SidedInvWrapper getInventoryWrapper(EnumFacing side)
    {
        if (!this.inventoryWrapperCache.containsKey(side))
            this.inventoryWrapperCache.put(side, new SidedInvWrapper(this, side));
        return this.inventoryWrapperCache.get(side);
    }
}
