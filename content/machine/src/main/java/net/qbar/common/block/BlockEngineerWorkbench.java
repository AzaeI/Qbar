package net.qbar.common.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.qbar.common.multiblock.BlockMultiblockBase;
import net.qbar.common.tile.machine.TileEngineerWorkbench;

public class BlockEngineerWorkbench extends BlockMultiblockBase<TileEngineerWorkbench>
{
    public BlockEngineerWorkbench()
    {
        super("engineerworkbench", Material.WOOD, TileEngineerWorkbench.class);
    }

    @Override
    public TileEngineerWorkbench getTile(World w, IBlockState state)
    {
        return new TileEngineerWorkbench();
    }

    @Override
    public void breakBlock(final World w, final BlockPos pos, final IBlockState state)
    {
        if (this.isWordTileCore(w, pos))
            this.getWorldTile(w, pos).disconnectItself();
        super.breakBlock(w, pos, state);
    }
}
