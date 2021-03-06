package net.qbar.common.init;

import net.qbar.common.block.*;
import net.qbar.common.block.item.ItemBlockMetadata;
import net.qbar.common.tile.TileFluidPipe;
import net.qbar.common.tile.TileSteamPipe;
import net.qbar.common.tile.TileFluidPump;
import net.qbar.common.tile.TileOffshorePump;
import net.qbar.common.tile.machine.TileBelt;
import net.qbar.common.tile.machine.TileExtractor;
import net.qbar.common.tile.machine.TileSplitter;

public class LogisticBlocks
{
    public static void init()
    {
        QBarBlocks.registerBlock(new BlockFluidPipe(), block -> new ItemBlockMetadata(block, "valve"));
        QBarBlocks.registerBlock(new BlockSteamPipe(), block -> new ItemBlockMetadata(block, "valve"));
        QBarBlocks.registerBlock(new BlockFluidPump());
        QBarBlocks.registerBlock(new BlockOffshorePump());

        QBarBlocks.registerBlock(new BlockBelt());
        QBarBlocks.registerBlock(new BlockExtractor(), block -> new ItemBlockMetadata(block, "filter"));
        QBarBlocks.registerBlock(new BlockSplitter(), block -> new ItemBlockMetadata(block, "filter"));

        QBarBlocks.registerTile(TileFluidPipe.class, "fluidpipe");
        QBarBlocks.registerTile(TileSteamPipe.class, "steampipe");
        QBarBlocks.registerTile(TileFluidPump.class, "fluidpump");
        QBarBlocks.registerTile(TileOffshorePump.class, "offshore_pump");
        QBarBlocks.registerTile(TileBelt.class, "belt");
        QBarBlocks.registerTile(TileExtractor.class, "itemextractor");
        QBarBlocks.registerTile(TileSplitter.class, "itemsplitter");
    }
}
