package com.happyandjust.murdererfinder;

import com.happyandjust.murdererfinder.commands.ToggleMurdererFinderCommand;
import com.happyandjust.murdererfinder.handlers.ConfigHandler;
import com.happyandjust.murdererfinder.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Mod(modid = "murdererfinder", name = "MurdererFinder", version = "1.5")
public class MurdererFinder {
    public final List<Item> sword_lists = new ArrayList<>();
    public String alpha = null;
    public Set<EntityPlayer> murderers = new HashSet<>();
    public Set<EntityPlayer> hasBow = new HashSet<>();

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        ClientCommandHandler.instance.registerCommand(new ToggleMurdererFinderCommand());
        if (ConfigHandler.hasKey("murderer", "toggle")) {
            ToggleMurdererFinderCommand.toggled = ConfigHandler.getBoolean("murderer", "toggle");
        }
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
        MinecraftForge.EVENT_BUS.register(this);
        sword_lists.add(Items.iron_sword);
        sword_lists.add(Items.stone_sword);
        sword_lists.add(Items.iron_shovel);
        sword_lists.add(Items.stick);
        sword_lists.add(Items.wooden_axe);
        sword_lists.add(Items.wooden_sword);
        sword_lists.add(Items.stone_shovel);
        sword_lists.add(Items.blaze_rod);
        sword_lists.add(Items.diamond_shovel);
        sword_lists.add(Items.feather);
        sword_lists.add(Items.pumpkin_pie);
        sword_lists.add(Items.golden_pickaxe);
        sword_lists.add(Items.apple);
        sword_lists.add(Items.name_tag);
        sword_lists.add(Items.carrot_on_a_stick);
        sword_lists.add(Items.bone);
        sword_lists.add(Items.carrot);
        sword_lists.add(Items.golden_carrot);
        sword_lists.add(Items.cookie);
        sword_lists.add(Items.diamond_axe);
        sword_lists.add(Items.prismarine_shard);
        sword_lists.add(Items.golden_sword);
        sword_lists.add(Items.diamond_sword);
        sword_lists.add(Items.diamond_hoe);
        sword_lists.add(Items.shears);
        sword_lists.add(Items.fish);
        sword_lists.add(Items.boat);
        sword_lists.add(Items.cookie);
        sword_lists.add(Item.getItemFromBlock(Blocks.torch));
        sword_lists.add(Item.getItemFromBlock(Blocks.redstone_torch));
        sword_lists.add(Item.getItemFromBlock(Blocks.sponge));
        sword_lists.add(Items.cooked_beef);
        sword_lists.add(Item.getItemFromBlock(Blocks.double_plant));
        sword_lists.add(Items.speckled_melon);
    }

    @SubscribeEvent
    public void onWorldRender(RenderWorldLastEvent e) {
        if (Utils.checkForMurderMystery() && ToggleMurdererFinderCommand.toggled) {
            Entity render = Minecraft.getMinecraft().getRenderViewEntity();
            WorldRenderer vb = Tessellator.getInstance().getWorldRenderer();
            Tessellator ts = Tessellator.getInstance();
            Color colour;

            double realX = render.lastTickPosX + (render.posX - render.lastTickPosX) * e.partialTicks;
            double realY = render.lastTickPosY + (render.posY - render.lastTickPosY) * e.partialTicks;
            double realZ = render.lastTickPosZ + (render.posZ - render.lastTickPosZ) * e.partialTicks;

            GlStateManager.pushMatrix();
            GlStateManager.translate(-realX, -realY, -realZ);
            GlStateManager.disableTexture2D();
            GlStateManager.enableBlend();
            GlStateManager.disableDepth();
            GlStateManager.disableAlpha();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GL11.glLineWidth(2);
            for (Entity en : Minecraft.getMinecraft().theWorld.loadedEntityList) {
                int color = 2147483647;
                if (en instanceof EntityPlayer) {
                    EntityPlayer ep = (EntityPlayer) en;
                    if (ep != Minecraft.getMinecraft().thePlayer) {
                        ItemStack inv = ep.getEquipmentInSlot(0);
                        Item inv_item = inv == null ? null : inv.getItem();
                        ScorePlayerTeam scorePlayerTeam = (ScorePlayerTeam) ep.getTeam();
                        if (scorePlayerTeam != null) {
                            if (!scorePlayerTeam.getColorPrefix().contains("[M]")) {
                                color = Color.green.getRGB();
                            }
                        }
                        for (Item item : sword_lists) {
                            if (inv_item == item) {
                                ItemStack itemStack = ep.getEquipmentInSlot(3);
                                if (itemStack != null) {
                                    if (itemStack.getItem() == Items.iron_chestplate) {
                                        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§7[§cALERT§7] §e" + en.getName() + " §3is a alpha!"));
                                        alpha = ep.getName();
                                    }
                                }
                                if (!murderers.contains(ep)) {
                                    Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§7[§cALERT§7] §e" + en.getName() + " §3is a murderer!"));
                                    murderers.add(ep);
                                }

                            }

                        }
                        if (murderers.contains(ep)) {
                            if (ep.isInvisible()) {
                                ep.setInvisible(false);
                            }
                            color = Color.red.getRGB();
                        }
                        if (alpha != null) {
                            if (ep.getName().equalsIgnoreCase(alpha)) {
                                color = Color.MAGENTA.getRGB();
                            }
                        }

                        if (inv != null) {
                            if (inv.getItem() == Items.bow) {
                                ItemStack cp = ep.getEquipmentInSlot(3);
                                if (cp == null) {
                                    if (!hasBow.contains(ep)) {
                                        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§7[§cALERT§7] §e" + en.getName() + " §3has a bow!"));
                                        hasBow.add(ep);
                                    }
                                }
                            }
                        }
                        if (hasBow.contains(ep)) {
                            color = Color.orange.getRGB();
                        }
                    }
                } else if (en instanceof EntityItem) {
                    EntityItem entityItem = (EntityItem) en;
                    if (entityItem.getEntityItem().getItem() == Items.gold_ingot) {
                        color = new Color(0xFFAA00).getRGB();
                    }
                } else if (en instanceof EntityArmorStand) {
                    ItemStack itemStack = ((EntityArmorStand) en).getEquipmentInSlot(0);
                    if (itemStack != null) {
                        if (itemStack.getItem() == Items.bow) {
                            if (en.isInvisible()) {
                                color = new Color(229, 109, 5).getRGB();
                            }
                        }
                    }
                }
                if (color != 2147483647) {
                    colour = new Color(color);
                    AxisAlignedBB aabb = en.getEntityBoundingBox();
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
                }
            }
            GlStateManager.translate(realX, realY, realZ);
            GlStateManager.disableBlend();
            GlStateManager.enableAlpha();
            GlStateManager.enableDepth();
            GlStateManager.enableTexture2D();
            GlStateManager.popMatrix();
        } else {
            murderers.clear();
            alpha = null;
        }
    }


}
