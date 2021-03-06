package net.qbar.client.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.qbar.client.gui.util.GuiMachineBase;
import net.qbar.client.gui.util.GuiProgress;
import net.qbar.client.gui.util.GuiSpace;
import net.qbar.client.gui.util.GuiTexturedSpace;
import net.qbar.common.QBarConstants;
import net.qbar.common.tile.machine.TileSolidBoiler;

import java.util.Collections;

public class GuiBoiler extends GuiMachineBase<TileSolidBoiler>
{
    private static final ResourceLocation BACKGROUND = new ResourceLocation(QBarConstants.MODID, "textures/gui/boiler.png");

    public GuiBoiler(final EntityPlayer player, final TileSolidBoiler boiler)
    {
        super(player, boiler, BACKGROUND);

        this.addFluidTank(boiler.getWaterTank(), 128, 7, 18, 73);
        this.addSteamTank(boiler.getSteamTank(), 151, 7, 18, 73);

        this.addAnimatedSprite(this::getBurnLeftScaled,
                GuiProgress.builder().space(GuiTexturedSpace.builder().x(81).y(38).width(14).height(13).u(176).v(12)
                        .s(190).t(25).build()).direction(GuiProgress.StartDirection.TOP).revert(false).build());

        this.addAnimatedSprite(this::getHeatScaled,
                GuiProgress.builder().space(GuiTexturedSpace.builder().x(10).y(79).width(12).height(71).u(176).v(84)
                        .s(176 + 12).t(85 + 71).build()).direction(GuiProgress.StartDirection.TOP).revert(false)
                        .build());

        this.addTooltip(new GuiSpace(10, 8, 12, 71), () ->
                Collections.singletonList(this.getHeatTooltip(this.getMachine()::getHeat, this.getMachine()::getMaxHeat)));
    }

    private int getHeatScaled(final int pixels)
    {
        final int i = (int) this.getMachine().getMaxHeat();

        if (i == 0)
            return 0;

        return (int) Math.min(this.getMachine().getHeat() * pixels / i, pixels);
    }

    private int getBurnLeftScaled(final int pixels)
    {
        final int i = this.getMachine().getMaxBurnTime();

        if (i == 0)
            return 0;

        return this.getMachine().getCurrentBurnTime() * pixels / i;
    }
}
