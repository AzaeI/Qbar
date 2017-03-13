package net.qbar.client;

import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.client.model.pipeline.LightUtil;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.qbar.common.item.ItemBlueprint;
import net.qbar.common.multiblock.BlockMultiblockBase;
import net.qbar.common.multiblock.Blueprints;
import net.qbar.common.multiblock.Blueprints.Blueprint;
import net.qbar.common.util.ItemUtils;

public class ClientEventManager
{
    private final BlockRendererDispatcher blockRender = Minecraft.getMinecraft().getBlockRendererDispatcher();

    @SubscribeEvent
    public void onDrawblockHightlight(final DrawBlockHighlightEvent e)
    {
        if (e.getTarget().typeOfHit.equals(RayTraceResult.Type.BLOCK))
        {
            if (e.getPlayer().getHeldItemMainhand().getItem() instanceof ItemBlueprint)
            {
                if (e.getPlayer().getHeldItemMainhand().hasTagCompound()
                        && e.getPlayer().getHeldItemMainhand().getTagCompound().hasKey("blueprint"))
                {
                    final String name = e.getPlayer().getHeldItemMainhand().getTagCompound().getString("blueprint");
                    final Blueprint blueprint = Blueprints.getInstance().getBlueprints()
                            .get(e.getPlayer().getHeldItemMainhand().getTagCompound().getString("blueprint"));
                    final BlockMultiblockBase base = (BlockMultiblockBase) Block.getBlockFromName("qbar:" + name);
                    final World w = Minecraft.getMinecraft().world;
                    if (base != null)
                    {
                        if (base.canPlaceBlockAt(w, e.getTarget().getBlockPos().offset(e.getTarget().sideHit)))
                        {
                            GlStateManager.enableBlend();
                            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
                                    GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,
                                    GlStateManager.DestFactor.ZERO);
                            GlStateManager.glLineWidth(2.0F);
                            GlStateManager.disableTexture2D();
                            GlStateManager.depthMask(false);
                            final BlockPos pos = e.getTarget().getBlockPos().offset(e.getTarget().sideHit);

                            if (e.getPlayer().world.getWorldBorder().contains(pos))
                            {
                                final double x = e.getPlayer().lastTickPosX
                                        + (e.getPlayer().posX - e.getPlayer().lastTickPosX) * e.getPartialTicks();
                                final double y = e.getPlayer().lastTickPosY
                                        + (e.getPlayer().posY - e.getPlayer().lastTickPosY) * e.getPartialTicks();
                                final double z = e.getPlayer().lastTickPosZ
                                        + (e.getPlayer().posZ - e.getPlayer().lastTickPosZ) * e.getPartialTicks();
                                RenderGlobal.drawSelectionBoundingBox(
                                        base.getDefaultState().getSelectedBoundingBox(e.getPlayer().world, pos)
                                                .expandXyz(0.0020000000949949026D).offset(-x, -y, -z),
                                        0.0F, 0.0F, 0.0F, 0.4F);

                                GlStateManager.depthMask(true);
                                GlStateManager.enableTexture2D();
                                GlStateManager.disableBlend();

                                this.renderMultiblock(
                                        base.getStateForPlacement(w, pos, e.getTarget().sideHit, 0, 0, 0, 0,
                                                e.getPlayer(), e.getPlayer().getActiveHand()),
                                        pos, e.getPartialTicks(), e.getPlayer());

                                GlStateManager.pushMatrix();
                                GlStateManager.translate(pos.getX() - x + 0.5, pos.getY() - y, pos.getZ() - z + .5);
                                GlStateManager.rotate(180, 1, 0, 0);
                                GlStateManager.rotate(e.getPlayer().getHorizontalFacing().getHorizontalAngle() - 180, 0,
                                        1, 0);
                                GlStateManager.translate(0.15, -1.4, -.69);
                                GlStateManager.scale(0.625f / 32, 0.625f / 32, 0.625f / 32);
                                GlStateManager.disableLighting();

                                Minecraft.getMinecraft().fontRendererObj.drawString(
                                        String.valueOf(blueprint.getRodAmount()),
                                        -Minecraft.getMinecraft().fontRendererObj
                                                .getStringWidth(String.valueOf(blueprint.getRodAmount())) / 2,
                                        0, ItemUtils.hasPlayerEnough(e.getPlayer().inventory, blueprint.getRodStack(),
                                                false) ? 38400 : 9830400);

                                GlStateManager.enableLighting();
                                GlStateManager.popMatrix();

                                GlStateManager.pushMatrix();
                                GlStateManager.translate(pos.getX() - x + 0.5, pos.getY() - y, pos.getZ() - z + .5);

                                GlStateManager.rotate(90, 1, 0, 0);
                                GlStateManager.rotate(-90, 0, 1, 0);
                                GlStateManager.rotate(e.getPlayer().getHorizontalFacing().getHorizontalAngle(), 1, 0,
                                        0);
                                GlStateManager.translate(-1.5, -0.5, 0);

                                RenderUtil.handleRenderItem(blueprint.getRodStack());
                                GlStateManager.popMatrix();
                                e.setCanceled(true);
                            }
                        }
                    }
                }
            }
        }
    }

    private void renderMultiblock(final IBlockState state, final BlockPos pos, final float partialTicks,
            final EntityPlayer player)
    {
        final double dx = player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks;
        final double dy = player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks;
        final double dz = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks;
        final Minecraft minecraft = Minecraft.getMinecraft();
        final World world = player.world;

        minecraft.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        ForgeHooksClient.setRenderLayer(BlockRenderLayer.CUTOUT);

        GlStateManager.pushMatrix();
        GlStateManager.translate(pos.getX() - dx, pos.getY() - dy, pos.getZ() - dz);

        RenderHelper.disableStandardItemLighting();
        GlStateManager.color(1f, 1f, 1f, 1f);
        final int alpha = (int) (0.5 * 0xFF) << 24;
        GlStateManager.enableBlend();
        GlStateManager.enableTexture2D();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        GlStateManager.colorMask(true, true, true, true);
        GlStateManager.depthFunc(GL11.GL_LEQUAL);
        this.renderModel(world, pos, alpha, state);
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    private void renderModel(final World world, final BlockPos pos, final int alpha, final IBlockState state)
    {
        final IBakedModel model = this.blockRender.getModelForState(state);
        final IBlockState extendedState = state.getBlock().getExtendedState(state, world, pos);

        this.renderQuads(world, state, pos, model.getQuads(extendedState, null, 0), alpha);
    }

    private void renderQuads(final World world, final IBlockState actualState, final BlockPos pos,
            final List<BakedQuad> quads, final int alpha)
    {
        final Tessellator tessellator = Tessellator.getInstance();
        final VertexBuffer buffer = tessellator.getBuffer();

        if (quads == null || quads.isEmpty())
            return;
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
        quads.forEach(quad -> LightUtil.renderQuadColor(buffer, quad, alpha | 0xffffff));
        tessellator.draw();
    }
}
