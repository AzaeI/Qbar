package net.qbar.common.init;

import java.util.ArrayList;
import java.util.List;

import net.qbar.common.block.INamedBlock;
import org.apache.commons.lang3.StringUtils;

import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.oredict.OreDictionary;
import net.qbar.QBar;
import net.qbar.common.item.*;
import net.qbar.common.recipe.QBarRecipeHandler;

@ObjectHolder(QBar.MODID)
public class QBarItems
{
    @ObjectHolder("punched_card")
    public static final ItemBase PUNCHED_CARD              = null;

    @ObjectHolder("wrench")
    public static final ItemBase WRENCH                    = null;

    @ObjectHolder("blueprint")
    public static final ItemBase BLUEPRINT                 = null;
    @ObjectHolder("ironrod")
    public static final ItemBase IRON_ROD                  = null;
    @ObjectHolder("metalplate")
    public static final ItemBase METALPLATE                = null;
    @ObjectHolder("solarreflector")
    public static final ItemBase SOLAR_REFLECTOR           = null;

    @ObjectHolder("mineralsludge")
    public static final ItemBase MINERAL_SLUDGE            = null;
    @ObjectHolder("compressedmineralsludge")
    public static final ItemBase COMPRESSED_MINERAL_SLUDGE = null;
    @ObjectHolder("mixedrawore")
    public static final ItemBase MIXED_RAW_ORE             = null;
    @ObjectHolder("rawore")
    public static final ItemBase RAW_ORE                   = null;

    public static List<Item>    ITEMS;

    public static final void init()
    {
        ITEMS = new ArrayList<>();

        QBarItems.registerMetal("iron");
        QBarItems.registerMetal("gold");

        QBarItems.registerItem(new ItemPunchedCard());
        QBarItems.registerItem(new ItemWrench());

        QBarItems.registerItem(new ItemBlueprint());
        QBarItems.registerItem(new ItemBase("ironrod"));
        QBarItems.registerItem(new ItemPlate());
        QBarItems.registerItem(new ItemBase("solarreflector"));

        QBarItems.registerItem(new ItemSludge("mineralsludge"));
        QBarItems.registerItem(new ItemSludge("compressedmineralsludge"));
        QBarItems.registerItem(new ItemRawOre());
        QBarItems.registerItem(new ItemMixedRawOre());

        ITEMS.addAll(QBarBlocks.BLOCKS.values());
    }

    @SubscribeEvent
    public void onItemRegister(RegistryEvent.Register<Item> event)
    {
        event.getRegistry().registerAll(ITEMS.stream().toArray(Item[]::new));
    }

    private static final void registerItem(final ItemBase item)
    {
        ITEMS.add(item);
    }

    private static final void registerMetal(final String metalName)
    {
        if (OreDictionary.doesOreNameExist("ingot" + StringUtils.capitalize(metalName)))
            QBarRecipeHandler.metals.add(metalName);
    }
}
