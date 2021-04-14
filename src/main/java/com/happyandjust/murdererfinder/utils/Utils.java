package com.happyandjust.murdererfinder.utils;

import com.happyandjust.murdererfinder.handlers.ScoreboardHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class Utils {
    public static boolean checkForMurderMystery() {
        try {
            boolean a = ScoreboardHandler.cleanSB(ScoreboardHandler.getSidebarDisplayName())
                    .equalsIgnoreCase("MURDER MYSTERY");
            boolean b = false;
            for (ScoreObjective sc : Minecraft.getMinecraft().theWorld.getScoreboard().getScoreObjectives()) {
                if (sc.getName().equalsIgnoreCase("MurderMystery")) {
                    b = true;
                }
            }

            return a && b;
        } catch (Exception ignored) {

        }
        return false;
    }

    public static void draw3DString(BlockPos pos, String text, int colour, float partialTicks) {
        try {
            Minecraft mc = Minecraft.getMinecraft();
            EntityPlayer player = Minecraft.getMinecraft().thePlayer;
            double x = (pos.getX() - player.lastTickPosX)
                    + ((pos.getX() - player.posX) - (pos.getX() - player.lastTickPosX)) * partialTicks;
            double y = (pos.getY() - player.lastTickPosY)
                    + ((pos.getY() - player.posY) - (pos.getY() - player.lastTickPosY)) * partialTicks;
            double z = (pos.getZ() - player.lastTickPosZ)
                    + ((pos.getZ() - player.posZ) - (pos.getZ() - player.lastTickPosZ)) * partialTicks;
            RenderManager renderManager = mc.getRenderManager();

            float f = 1.6F;
            float f1 = 0.016666668F * f;
            int width = mc.fontRendererObj.getStringWidth(text) / 2;
            GlStateManager.pushMatrix();
            GlStateManager.translate(x, y, z);
            GL11.glNormal3f(0f, 1f, 0f);
            GlStateManager.rotate(-renderManager.playerViewY, 0f, 1f, 0f);
            GlStateManager.rotate(renderManager.playerViewX, 1f, 0f, 0f);
            GlStateManager.scale(-f1, -f1, -f1);
            GlStateManager.enableBlend();
            GlStateManager.disableDepth();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            mc.fontRendererObj.drawString(text, -width, 0, colour);
            GlStateManager.disableBlend();
            GlStateManager.enableDepth();
            GlStateManager.popMatrix();
        } catch (Exception ignored) {

        }
    }

    public static void draw3DBox(AxisAlignedBB aabb, int colourInt, float partialTicks) {
        Entity render = Minecraft.getMinecraft().getRenderViewEntity();
        WorldRenderer vb = Tessellator.getInstance().getWorldRenderer();
        Tessellator ts = Tessellator.getInstance();
        Color colour = new Color(colourInt);

        double realX = render.lastTickPosX + (render.posX - render.lastTickPosX) * partialTicks;
        double realY = render.lastTickPosY + (render.posY - render.lastTickPosY) * partialTicks;
        double realZ = render.lastTickPosZ + (render.posZ - render.lastTickPosZ) * partialTicks;

        GlStateManager.pushMatrix();
        GlStateManager.translate(-realX, -realY, -realZ);
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GL11.glLineWidth(2);
        float r = colour.getRed() / 255f;
        float g = colour.getGreen() / 255f;
        float b = colour.getBlue() / 255f;
        float a = 0.25F;
        GlStateManager.color(r, g, b, colour.getAlpha() / 255f);
        RenderGlobal.drawSelectionBoundingBox(aabb);
        // bottom
        vb.begin(7, DefaultVertexFormats.POSITION_COLOR);
        vb.pos(aabb.minX, aabb.minY, aabb.minZ).color(r, g, b, a).endVertex();
        vb.pos(aabb.minX, aabb.maxY, aabb.minZ).color(r, g, b, a).endVertex();
        vb.pos(aabb.maxX, aabb.minY, aabb.minZ).color(r, g, b, a).endVertex();
        vb.pos(aabb.maxX, aabb.maxY, aabb.minZ).color(r, g, b, a).endVertex();
        vb.pos(aabb.maxX, aabb.minY, aabb.maxZ).color(r, g, b, a).endVertex();
        vb.pos(aabb.maxX, aabb.maxY, aabb.maxZ).color(r, g, b, a).endVertex();
        vb.pos(aabb.minX, aabb.minY, aabb.maxZ).color(r, g, b, a).endVertex();
        vb.pos(aabb.minX, aabb.maxY, aabb.maxZ).color(r, g, b, a).endVertex();
        ts.draw();
        vb.begin(7, DefaultVertexFormats.POSITION_COLOR);
        vb.pos(aabb.maxX, aabb.maxY, aabb.minZ).color(r, g, b, a).endVertex();
        vb.pos(aabb.maxX, aabb.minY, aabb.minZ).color(r, g, b, a).endVertex();
        vb.pos(aabb.minX, aabb.maxY, aabb.minZ).color(r, g, b, a).endVertex();
        vb.pos(aabb.minX, aabb.minY, aabb.minZ).color(r, g, b, a).endVertex();
        vb.pos(aabb.minX, aabb.maxY, aabb.maxZ).color(r, g, b, a).endVertex();
        vb.pos(aabb.minX, aabb.minY, aabb.maxZ).color(r, g, b, a).endVertex();
        vb.pos(aabb.maxX, aabb.maxY, aabb.maxZ).color(r, g, b, a).endVertex();
        vb.pos(aabb.maxX, aabb.minY, aabb.maxZ).color(r, g, b, a).endVertex();
        ts.draw();
        vb.begin(7, DefaultVertexFormats.POSITION_COLOR);
        vb.pos(aabb.minX, aabb.maxY, aabb.minZ).color(r, g, b, a).endVertex();
        vb.pos(aabb.maxX, aabb.maxY, aabb.minZ).color(r, g, b, a).endVertex();
        vb.pos(aabb.maxX, aabb.maxY, aabb.maxZ).color(r, g, b, a).endVertex();
        vb.pos(aabb.minX, aabb.maxY, aabb.maxZ).color(r, g, b, a).endVertex();
        vb.pos(aabb.minX, aabb.maxY, aabb.minZ).color(r, g, b, a).endVertex();
        vb.pos(aabb.minX, aabb.maxY, aabb.maxZ).color(r, g, b, a).endVertex();
        vb.pos(aabb.maxX, aabb.maxY, aabb.maxZ).color(r, g, b, a).endVertex();
        vb.pos(aabb.maxX, aabb.maxY, aabb.minZ).color(r, g, b, a).endVertex();
        ts.draw();
        vb.begin(7, DefaultVertexFormats.POSITION_COLOR);
        vb.pos(aabb.minX, aabb.minY, aabb.minZ).color(r, g, b, a).endVertex();
        vb.pos(aabb.maxX, aabb.minY, aabb.minZ).color(r, g, b, a).endVertex();
        vb.pos(aabb.maxX, aabb.minY, aabb.maxZ).color(r, g, b, a).endVertex();
        vb.pos(aabb.minX, aabb.minY, aabb.maxZ).color(r, g, b, a).endVertex();
        vb.pos(aabb.minX, aabb.minY, aabb.minZ).color(r, g, b, a).endVertex();
        vb.pos(aabb.minX, aabb.minY, aabb.maxZ).color(r, g, b, a).endVertex();
        vb.pos(aabb.maxX, aabb.minY, aabb.maxZ).color(r, g, b, a).endVertex();
        vb.pos(aabb.maxX, aabb.minY, aabb.minZ).color(r, g, b, a).endVertex();
        ts.draw();
        vb.begin(7, DefaultVertexFormats.POSITION_COLOR);
        vb.pos(aabb.minX, aabb.minY, aabb.minZ).color(r, g, b, a).endVertex();
        vb.pos(aabb.minX, aabb.maxY, aabb.minZ).color(r, g, b, a).endVertex();
        vb.pos(aabb.minX, aabb.minY, aabb.maxZ).color(r, g, b, a).endVertex();
        vb.pos(aabb.minX, aabb.maxY, aabb.maxZ).color(r, g, b, a).endVertex();
        vb.pos(aabb.maxX, aabb.minY, aabb.maxZ).color(r, g, b, a).endVertex();
        vb.pos(aabb.maxX, aabb.maxY, aabb.maxZ).color(r, g, b, a).endVertex();
        vb.pos(aabb.maxX, aabb.minY, aabb.minZ).color(r, g, b, a).endVertex();
        vb.pos(aabb.maxX, aabb.maxY, aabb.minZ).color(r, g, b, a).endVertex();
        ts.draw();
        vb.begin(7, DefaultVertexFormats.POSITION_COLOR);
        vb.pos(aabb.minX, aabb.maxY, aabb.maxZ).color(r, g, b, a).endVertex();
        vb.pos(aabb.minX, aabb.minY, aabb.maxZ).color(r, g, b, a).endVertex();
        vb.pos(aabb.minX, aabb.maxY, aabb.minZ).color(r, g, b, a).endVertex();
        vb.pos(aabb.minX, aabb.minY, aabb.minZ).color(r, g, b, a).endVertex();
        vb.pos(aabb.maxX, aabb.maxY, aabb.minZ).color(r, g, b, a).endVertex();
        vb.pos(aabb.maxX, aabb.minY, aabb.minZ).color(r, g, b, a).endVertex();
        vb.pos(aabb.maxX, aabb.maxY, aabb.maxZ).color(r, g, b, a).endVertex();
        vb.pos(aabb.maxX, aabb.minY, aabb.maxZ).color(r, g, b, a).endVertex();
        ts.draw();

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.translate(realX, realY, realZ);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();

        GlStateManager.popMatrix();

    }
}
