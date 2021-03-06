package net.qbar.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.qbar.client.render.model.obj.QBarStateProperties;
import net.qbar.common.tile.TilePipeBase;

public abstract class BlockPipeBase extends BlockMachineBase<TilePipeBase>
{
    public BlockPipeBase(final String name)
    {
        super(name, Material.IRON, TilePipeBase.class);
    }

    @Override
    public void getSubBlocks(final CreativeTabs tab, final NonNullList<ItemStack> stacks)
    {
        stacks.add(new ItemStack(this, 1, 0));
        stacks.add(new ItemStack(this, 1, 1));
    }

    @Override
    public boolean isOpaqueCube(final IBlockState state)
    {
        return false;
    }

    @Override
    public boolean isFullCube(final IBlockState state)
    {
        return false;
    }

    @Override
    public boolean causesSuffocation(final IBlockState state)
    {
        return false;
    }

    @Override
    public IBlockState getExtendedState(final IBlockState state, final IBlockAccess world, final BlockPos pos)
    {
        if (this.checkWorldTile(world, pos))
        {
            return ((IExtendedBlockState) state).withProperty(QBarStateProperties.VISIBILITY_PROPERTY,
                    this.getWorldTile(world, pos).getVisibilityState());
        }
        return state;
    }

    @Override
    public BlockStateContainer createBlockState()
    {
        return new ExtendedBlockState(this, new IProperty[0],
                new IUnlistedProperty[]{QBarStateProperties.VISIBILITY_PROPERTY});
    }

    @Override
    public void neighborChanged(final IBlockState state, final World w, final BlockPos pos, final Block block,
                                final BlockPos posNeighbor)
    {
        if (!w.isRemote)
            ((TilePipeBase<?, ?>) w.getTileEntity(pos)).scanHandlers(posNeighbor);
    }

    @Override
    public void breakBlock(final World w, final BlockPos pos, final IBlockState state)
    {
        ((TilePipeBase<?, ?>) w.getTileEntity(pos)).disconnectItself();

        super.breakBlock(w, pos, state);
    }
}
