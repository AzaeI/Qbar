package net.qbar.common.tile.machine;

import com.google.common.collect.Lists;
import fr.ourten.teabeans.value.BaseListProperty;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import net.qbar.common.card.FilterCard;
import net.qbar.common.card.IPunchedCard;
import net.qbar.common.card.CardDataStorage;
import net.qbar.common.card.CardDataStorage.ECardType;
import net.qbar.common.container.BuiltContainer;
import net.qbar.common.container.ContainerBuilder;
import net.qbar.common.container.IContainerProvider;
import net.qbar.common.grid.IBelt;
import net.qbar.common.init.QBarItems;
import net.qbar.common.network.action.ActionSender;
import net.qbar.common.network.action.IActionReceiver;
import net.qbar.common.tile.TileInventoryBase;
import net.qbar.common.util.ItemUtils;

import javax.annotation.Nullable;
import java.util.List;

public class TileSplitter extends TileInventoryBase
        implements IContainerProvider, ITickable, ISidedInventory, IActionReceiver
{
    private EnumFacing facing;

    private       boolean                   hasFilter;
    private final BaseListProperty<Boolean> whitelistProperty;

    private ItemStack cachedWest, cachedNorth, cachedEast;
    private FilterCard filterWest, filterNorth, filterEast;

    public TileSplitter(final boolean hasFilter)
    {
        super("itemsplitter", 4);

        this.hasFilter = hasFilter;

        this.whitelistProperty = new BaseListProperty<>(null, "whitelistProperty");
        this.whitelistProperty.add(true);
        this.whitelistProperty.add(true);
        this.whitelistProperty.add(true);
        this.facing = EnumFacing.UP;

        this.cachedEast = this.cachedNorth = this.cachedWest = ItemStack.EMPTY;
    }

    public TileSplitter()
    {
        this(false);
    }

    private final List<EnumFacing> choices   = Lists.newArrayList();
    private       int              lastSplit = 0;

    @Override
    public void update()
    {
        if (this.isServer() && !this.getStackInSlot(3).isEmpty())
        {
            if (this.hasFilter())
            {
                if (!ItemUtils.deepEquals(this.cachedWest, this.getStackInSlot(0)))
                {
                    this.filterWest = null;
                    this.cachedWest = this.getStackInSlot(0).copy();
                    if (this.cachedWest.hasTagCompound())
                    {
                        final IPunchedCard card = CardDataStorage.instance()
                                .read(this.getStackInSlot(0).getTagCompound());
                        if (card.getID() == ECardType.FILTER.getID())
                            this.filterWest = (FilterCard) card;
                    }
                }
                if (!ItemUtils.deepEquals(this.cachedNorth, this.getStackInSlot(1)))
                {
                    this.filterNorth = null;
                    this.cachedNorth = this.getStackInSlot(1).copy();
                    if (this.cachedNorth.hasTagCompound())
                    {
                        final IPunchedCard card = CardDataStorage.instance()
                                .read(this.getStackInSlot(1).getTagCompound());
                        if (card.getID() == ECardType.FILTER.getID())
                            this.filterNorth = (FilterCard) card;
                    }
                }
                if (!ItemUtils.deepEquals(this.cachedEast, this.getStackInSlot(2)))
                {
                    this.filterEast = null;
                    this.cachedEast = this.getStackInSlot(2).copy();
                    if (this.cachedEast.hasTagCompound())
                    {
                        final IPunchedCard card = CardDataStorage.instance()
                                .read(this.getStackInSlot(2).getTagCompound());
                        if (card.getID() == ECardType.FILTER.getID())
                            this.filterEast = (FilterCard) card;
                    }
                }
            }

            final boolean left = this.hasBelt(this.getFacing().rotateY());
            final boolean front = this.hasBelt(this.getFacing().getOpposite());
            final boolean right = this.hasBelt(this.getFacing().rotateY().getOpposite());

            int split = 0;
            this.choices.clear();

            if (left && this.checkFilter(this.getFacing().rotateY(), this.getStackInSlot(3)))
            {
                split++;
                this.choices.add(this.getFacing().rotateY());
            }
            if (front && this.checkFilter(this.getFacing().getOpposite(), this.getStackInSlot(3)))
            {
                split++;
                this.choices.add(this.getFacing().getOpposite());
            }
            if (right && this.checkFilter(this.getFacing().rotateY().getOpposite(), this.getStackInSlot(3)))
            {
                split++;
                this.choices.add(this.getFacing().rotateY().getOpposite());
            }

            this.lastSplit += 1;
            if (split > 0)
                this.lastSplit %= split;

            for (int i = 0; i < split; i++)
            {
                if (this.canInsert(this.getStackInSlot(3), this.choices.get(i == 0 ? this.lastSplit : 0)))
                {
                    this.insert(this.getStackInSlot(3), this.choices.get(i == 0 ? this.lastSplit : 0));
                    this.setInventorySlotContents(3, ItemStack.EMPTY);
                    break;
                }
                this.choices.remove(i == 0 ? this.lastSplit : 0);
            }
        }
    }

    private boolean checkFilter(final EnumFacing facing, final ItemStack stack)
    {
        if (!this.hasFilter())
            return true;

        if (facing == this.getFacing().getOpposite())
        {
            if (this.filterNorth != null && (stack.isEmpty() || (this.getWhitelistProperty().get(1)
                    ? !this.filterNorth.filter(stack) : this.filterNorth.filter(stack))))
                return false;
        }
        else if (facing == this.getFacing().rotateY())
        {
            if (this.filterWest != null && (stack.isEmpty() || (this.getWhitelistProperty().get(0)
                    ? !this.filterWest.filter(stack) : this.filterWest.filter(stack))))
                return false;
        }
        else if (this.filterEast != null && (stack.isEmpty() || (this.getWhitelistProperty().get(2)
                ? !this.filterEast.filter(stack) : this.filterEast.filter(stack))))
            return false;
        return true;
    }

    private void insert(final ItemStack stack, final EnumFacing facing)
    {
        ((IBelt) this.world.getTileEntity(this.getPos().down().offset(facing))).insert(stack, true);
    }

    private boolean canInsert(final ItemStack stack, final EnumFacing facing)
    {
        final IBelt belt = (IBelt) this.world.getTileEntity(this.getPos().down().offset(facing));

        return belt.insert(stack, false);
    }

    private boolean hasBelt(final EnumFacing facing)
    {
        final TileEntity tile = this.world.getTileEntity(this.getPos().down().offset(facing));

        return tile != null && tile instanceof IBelt;
    }

    @Override
    public void addInfo(final List<String> lines)
    {
        lines.add("Orientation: " + this.getFacing());
        lines.add("Input: " + this.hasBelt(this.getFacing()));
        lines.add("Output: " + (this.hasBelt(this.getFacing().getOpposite()) ? "FRONT " : "")
                + (this.hasBelt(this.getFacing().rotateY()) ? "LEFT " : "")
                + (this.hasBelt(this.getFacing().rotateY().getOpposite()) ? "RIGHT " : ""));
        lines.add("Buffer: " + this.getStackInSlot(3));
    }

    @Override
    public NBTTagCompound writeToNBT(final NBTTagCompound tag)
    {
        tag.setInteger("facing", this.facing.ordinal());
        tag.setBoolean("filtered", this.hasFilter);

        for (int i = 0; i < this.whitelistProperty.size(); i++)
            tag.setBoolean("whitelist" + i, this.whitelistProperty.get(i));
        tag.setInteger("whitelistSize", this.whitelistProperty.size());
        return super.writeToNBT(tag);
    }

    @Override
    public void readFromNBT(final NBTTagCompound tag)
    {
        this.facing = EnumFacing.VALUES[tag.getInteger("facing")];
        this.hasFilter = tag.getBoolean("filtered");

        for (int i = 0; i < tag.getInteger("whitelistSize"); i++)
            this.whitelistProperty.set(i, tag.getBoolean("whitelist" + i));
        super.readFromNBT(tag);
    }

    public void setFacing(final EnumFacing facing)
    {
        this.facing = facing;
    }

    public EnumFacing getFacing()
    {
        return this.facing;
    }

    @Override
    public BuiltContainer createContainer(final EntityPlayer player)
    {
        return new ContainerBuilder("itemsplitter", player).player(player.inventory).inventory(8, 103).hotbar(8, 161)
                .addInventory().tile(this)
                .filterSlot(0, 8, -5, stack -> !stack.isEmpty() && stack.getItem().equals(QBarItems.PUNCHED_CARD))
                .filterSlot(1, 8, 34, stack -> !stack.isEmpty() && stack.getItem().equals(QBarItems.PUNCHED_CARD))
                .filterSlot(2, 8, 73, stack -> !stack.isEmpty() && stack.getItem().equals(QBarItems.PUNCHED_CARD))
                .syncBooleanValue(() -> this.getWhitelistProperty().get(0),
                        bool -> this.getWhitelistProperty().set(0, bool))
                .syncBooleanValue(() -> this.getWhitelistProperty().get(1),
                        bool -> this.getWhitelistProperty().set(1, bool))
                .syncBooleanValue(() -> this.getWhitelistProperty().get(2),
                        bool -> this.getWhitelistProperty().set(2, bool))
                .addInventory().create();
    }

    @Override
    public void onLoad()
    {
        if (this.isClient())
            this.forceSync();
    }

    public boolean hasFilter()
    {
        return this.hasFilter;
    }

    public void setHasFilter(final boolean hasFilter)
    {
        this.hasFilter = hasFilter;
    }

    public BaseListProperty<Boolean> getWhitelistProperty()
    {
        return this.whitelistProperty;
    }

    public boolean isWhitelist(final EnumFacing facing)
    {
        return this.getWhitelistProperty()
                .get(facing == this.getFacing().getOpposite() ? 1 : facing == this.getFacing().rotateY() ? 0 : 2);
    }

    public void setWhitelist(final EnumFacing facing, final boolean isWhitelist)
    {
        this.getWhitelistProperty().set(
                facing == this.getFacing().getOpposite() ? 1 : facing == this.getFacing().rotateY() ? 0 : 2,
                isWhitelist);
    }

    public FilterCard getFilter(final EnumFacing facing)
    {
        if (facing == this.facing.getOpposite())
            return this.filterNorth;
        if (facing == this.facing.rotateY())
            return this.filterEast;
        if (facing == this.facing.rotateY().getOpposite())
            return this.filterWest;
        return null;
    }

    private final int[] inputSlot = new int[]{3};

    @Override
    public int[] getSlotsForFace(final EnumFacing side)
    {
        return this.inputSlot;
    }

    @Override
    public boolean canInsertItem(final int index, final ItemStack itemStackIn, final EnumFacing direction)
    {
        if (index == 3)
            return this.isItemValidForSlot(index, itemStackIn);
        return false;
    }

    @Override
    public boolean canExtractItem(final int index, final ItemStack stack, final EnumFacing direction)
    {
        return false;
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 1;
    }

    IItemHandler inventoryHandler = new SidedInvWrapper(this, EnumFacing.NORTH);

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getCapability(final Capability<T> capability, @Nullable final EnumFacing facing)
    {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && facing == this.getFacing())
            return (T) this.inventoryHandler;
        return super.getCapability(capability, facing);
    }

    @Override
    public boolean hasCapability(final Capability<?> capability, @Nullable final EnumFacing facing)
    {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && facing == this.getFacing())
            return true;
        return super.hasCapability(capability, facing);
    }

    @Override
    public void handle(ActionSender sender, String actionID, NBTTagCompound payload)
    {
        if ("WHITELIST".equals(actionID))
        {
            this.setWhitelist(EnumFacing.values()[payload.getInteger("facing")],
                    payload.getBoolean("whitelist"));
            this.markDirty();
        }
    }
}
