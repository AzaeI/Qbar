package net.qbar.client.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.qbar.common.QBarConstants;
import net.qbar.common.container.BuiltContainer;
import net.qbar.common.tile.machine.TileEngineerWorkbench;
import org.yggard.brokkgui.paint.Background;
import org.yggard.brokkgui.paint.Texture;
import org.yggard.brokkgui.panel.GuiAbsolutePane;
import org.yggard.brokkgui.panel.GuiRelativePane;
import org.yggard.brokkgui.wrapper.container.BrokkGuiContainer;

public class GuiEngineerWorkbench extends BrokkGuiContainer<BuiltContainer>
{
    private static final int xSize = 176, ySize = 204;

    private static final Texture BACKGROUND = new Texture(QBarConstants.MODID + ":textures/gui/engineerworkbench.png",
            0, 0,
            xSize / 256.0f, ySize / 256.0f);

    private final TileEngineerWorkbench engineerWorkbench;

    public GuiEngineerWorkbench(final EntityPlayer player, final TileEngineerWorkbench engineerWorkbench)
    {
        super(engineerWorkbench.createContainer(player));

        this.setWidth(xSize);
        this.setHeight(ySize);
        this.setxRelativePos(0.5f);
        this.setyRelativePos(0.5f);

        this.engineerWorkbench = engineerWorkbench;

        GuiRelativePane mainPanel = new GuiRelativePane();
        this.setMainPanel(mainPanel);

        GuiAbsolutePane body = new GuiAbsolutePane();
        body.setWidthRatio(1);
        body.setHeightRatio(1);
        body.setBackground(new Background(BACKGROUND));
        mainPanel.addChild(body, 0.5f, 0.5f);
    }
}
