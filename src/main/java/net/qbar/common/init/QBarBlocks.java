package net.qbar.common.init;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.qbar.QBar;
import net.qbar.common.block.*;
import net.qbar.common.block.creative.BlockCreativeSteamGenerator;
import net.qbar.common.block.item.ItemBlockMetadata;
import net.qbar.common.multiblock.*;
import net.qbar.common.tile.TileFluidPipe;
import net.qbar.common.tile.TileSteamPipe;
import net.qbar.common.tile.TileStructure;
import net.qbar.common.tile.creative.TileCreativeSteamGenerator;
import net.qbar.common.tile.machine.*;

import java.util.function.Function;

@ObjectHolder(QBar.MODID)
public class QBarBlocks
{
    @ObjectHolder("keypunch")
    public static final BlockMachineBase PUNCHING_MACHINE   = null;
    @ObjectHolder("fluidtank")
    public static final BlockMachineBase FLUID_TANK         = null;
    @ObjectHolder("solid_boiler")
    public static final BlockMachineBase SOLID_BOILER       = null;
    @ObjectHolder("fluidpipe")
    public static final BlockMachineBase FLUID_PIPE         = null;
    @ObjectHolder("steampipe")
    public static final BlockMachineBase STEAM_PIPE         = null;
    @ObjectHolder("fluidpump")
    public static final BlockMachineBase FLUID_PUMP         = null;
    @ObjectHolder("offshorepump")
    public static final BlockMachineBase OFFSHORE_PUMP      = null;
    @ObjectHolder("assembler")
    public static final BlockMachineBase ASSEMBLER          = null;

    // Creative
    @ObjectHolder("creative_steam_generator")
    public static final BlockMachineBase CREATIVE_BOILER    = null;

    @ObjectHolder("belt")
    public static final BlockMachineBase BELT               = null;
    @ObjectHolder("itemextractor")
    public static final BlockMachineBase ITEM_EXTRACTOR     = null;
    @ObjectHolder("itemsplitter")
    public static final BlockMachineBase ITEM_SPLITTER      = null;

    @ObjectHolder("structure")
    public static final BlockMachineBase STRUCTURE          = null;
    @ObjectHolder("steamfurnace")
    public static final BlockMachineBase STEAM_FURNACE      = null;
    @ObjectHolder("rollingmill")
    public static final BlockMachineBase ROLLING_MILL       = null;
    @ObjectHolder("solar_mirror")
    public static final BlockMachineBase SOLAR_MIRROR       = null;
    @ObjectHolder("solar_boiler")
    public static final BlockMachineBase SOLAR_BOILER       = null;
    @ObjectHolder("liquidfuel_boiler")
    public static final BlockMachineBase LIQUID_FUEL_BOILER = null;
    @ObjectHolder("steamfurnacemk2")
    public static final BlockMachineBase STEAM_FURNACE_MK2  = null;

    @ObjectHolder("orewasher")
    public static final BlockMachineBase ORE_WASHER         = null;
    @ObjectHolder("sortingmachine")
    public static final BlockMachineBase SORTING_MACHINE    = null;
    @ObjectHolder("smallminingdrill")
    public static final BlockMachineBase SMALL_MINING_DRILL = null;
    @ObjectHolder("tinyminingdrill")
    public static final BlockMachineBase TINY_MINING_DRILL  = null;

    public static final void registerBlocks()
    {
        QBarBlocks.registerBlock(
                new BlockMultiblockMachine("keypunch", Material.IRON, Multiblocks.KEYPUNCH, TileKeypunch::new));
        QBarBlocks.registerBlock(
                new BlockTank("fluidtank_small", Multiblocks.SMALL_FLUID_TANK, Fluid.BUCKET_VOLUME * 48, 0));
        QBarBlocks.registerBlock(
                new BlockTank("fluidtank_medium", Multiblocks.MEDIUM_FLUID_TANK, Fluid.BUCKET_VOLUME * 128, 1));
        QBarBlocks.registerBlock(
                new BlockTank("fluidtank_big", Multiblocks.BIG_FLUID_TANK, Fluid.BUCKET_VOLUME * 432, 2));
        QBarBlocks.registerBlock(new BlockSolidBoiler());
        QBarBlocks.registerBlock(new BlockFluidPipe(), block -> new ItemBlockMetadata(block, "valve"));
        QBarBlocks.registerBlock(new BlockSteamPipe(), block -> new ItemBlockMetadata(block, "valve"));
        QBarBlocks.registerBlock(new BlockFluidPump());
        QBarBlocks.registerBlock(new BlockOffshorePump());
        QBarBlocks.registerBlock(
                new BlockMultiblockMachine("assembler", Material.IRON, Multiblocks.ASSEMBLER, TileAssembler::new));
        QBarBlocks.registerBlock(new BlockBelt());
        QBarBlocks.registerBlock(new BlockExtractor(), block -> new ItemBlockMetadata(block, "filter"));
        QBarBlocks.registerBlock(new BlockSplitter(), block -> new ItemBlockMetadata(block, "filter"));

        QBarBlocks.registerBlock(new BlockCreativeSteamGenerator());

        QBarBlocks.registerBlock(new BlockStructure());
        QBarBlocks.registerBlock(new BlockMultiblockMachine("steamfurnacemk1", Material.IRON,
                Multiblocks.STEAM_FURNACE_MK1, TileSteamFurnace::new));
        QBarBlocks.registerBlock(new BlockSolarBoiler());
        QBarBlocks.registerBlock(new BlockSolarMirror());
        QBarBlocks.registerBlock(new BlockMultiblockMachine("rollingmill", Material.IRON, Multiblocks.ROLLING_MILL,
                TileRollingMill::new));
        QBarBlocks.registerBlock(new BlockLiquidBoiler());
        QBarBlocks.registerBlock(new BlockMultiblockMachine("steamfurnacemk2", Material.IRON,
                Multiblocks.STEAM_FURNACE_MK2, TileSteamFurnaceMK2::new));

        QBarBlocks.registerBlock(
                new BlockMultiblockMachine("orewasher", Material.IRON, Multiblocks.ORE_WASHER, TileOreWasher::new));
        QBarBlocks.registerBlock(new BlockMultiblockMachine("sortingmachine", Material.IRON,
                Multiblocks.SORTING_MACHINE, TileSortingMachine::new));
        QBarBlocks.registerBlock(new BlockMultiblockMachine("smallminingdrill", Material.IRON,
                Multiblocks.SMALL_MINING_DRILL, TileSmallMiningDrill::new));
        QBarBlocks.registerBlock(new BlockMultiblockMachine("tinyminingdrill", Material.IRON,
                Multiblocks.TINY_MINING_DRILL, TileTinyMiningDrill::new));

        QBarBlocks.registerTile(TileTank.class, "tank");
        QBarBlocks.registerTile(TileKeypunch.class, "keypunch");
        QBarBlocks.registerTile(TileSolidBoiler.class, "boiler");
        QBarBlocks.registerTile(TileFluidPipe.class, "fluidpipe");
        QBarBlocks.registerTile(TileSteamPipe.class, "steampipe");
        QBarBlocks.registerTile(TileFluidPump.class, "fluidpump");
        QBarBlocks.registerTile(TileOffshorePump.class, "offshore_pump");
        QBarBlocks.registerTile(TileAssembler.class, "assembler");
        QBarBlocks.registerTile(TileBelt.class, "belt");
        QBarBlocks.registerTile(TileExtractor.class, "itemextractor");
        QBarBlocks.registerTile(TileSplitter.class, "itemsplitter");
        QBarBlocks.registerTile(TileMultiblockGag.class, "multiblockgag");
        QBarBlocks.registerTile(TileCreativeSteamGenerator.class, "creative_steam_generator");
        QBarBlocks.registerTile(TileStructure.class, "structure");
        QBarBlocks.registerTile(TileSteamFurnace.class, "steamfurnace");
        QBarBlocks.registerTile(TileSolarBoiler.class, "solarboiler");
        QBarBlocks.registerTile(TileSolarMirror.class, "solarmirror");
        QBarBlocks.registerTile(TileRollingMill.class, "rollingmill");
        QBarBlocks.registerTile(TileLiquidBoiler.class, "liquidfuelboiler");
        QBarBlocks.registerTile(TileSteamFurnaceMK2.class, "steamfurnacemk2");
        QBarBlocks.registerTile(TileOreWasher.class, "orewasher");
        QBarBlocks.registerTile(TileSortingMachine.class, "sortingmachine");
        QBarBlocks.registerTile(TileSmallMiningDrill.class, "smallminingdrill");
        QBarBlocks.registerTile(TileTinyMiningDrill.class, "tinyminingdrill");
    }

    public static final void registerBlock(final Block block, final String name)
    {
        if (block instanceof BlockMultiblockBase)
            QBarBlocks.registerBlock(block, ItemBlockMultiblockBase::new, name);
        else
            QBarBlocks.registerBlock(block, ItemBlock::new, name);
    }

    public static final void registerBlock(final Block block, final Function<Block, ItemBlock> supplier,
            final String name)
    {
        final ItemBlock supplied = supplier.apply(block);
        GameRegistry.register(block.setRegistryName(QBar.MODID, name));
        GameRegistry.register(supplied, block.getRegistryName());

        QBar.proxy.registerItemRenderer(supplied, 0, name);
    }

    public static final void registerBlock(final BlockBase block)
    {
        QBarBlocks.registerBlock(block, block.name);
    }

    public static final void registerBlock(final BlockMachineBase block)
    {
        QBarBlocks.registerBlock(block, block.name);
    }

    public static final void registerBlock(final BlockMachineBase block, final Function<Block, ItemBlock> supplier)
    {
        QBarBlocks.registerBlock(block, supplier, block.name);
    }

    public static final void registerTile(final Class<? extends TileEntity> c, final String name)
    {
        GameRegistry.registerTileEntity(c, QBar.MODID + ":" + name);
    }
}