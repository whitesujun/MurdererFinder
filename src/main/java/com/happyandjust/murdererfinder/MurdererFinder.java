package com.happyandjust.murdererfinder;

import com.happyandjust.murdererfinder.commands.ToggleMurdererFinderCommand;
import com.happyandjust.murdererfinder.handlers.ConfigHandler;
import com.happyandjust.murdererfinder.handlers.ScoreboardHandler;
import com.happyandjust.murdererfinder.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Mod(modid = "murdererfinder", name = "MurdererFinder", version = "1.2")
public class MurdererFinder {
    public final List<Item> sword_lists = new ArrayList<>();
    public String alpha = null;
    public Set<EntityPlayer> murderers = new HashSet<>();
    private int tick;
    private boolean isMurderer;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) {
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
    }

    @SubscribeEvent
    public void onWorldRender(RenderWorldLastEvent e) {
        if (Utils.checkForMurderMystery() && ToggleMurdererFinderCommand.toggled) {
            for (Entity en : Minecraft.getMinecraft().theWorld.loadedEntityList) {
                int color = 2147483647;

                if (en instanceof EntityPlayer) {
                    if (!isMurderer) {
                        EntityPlayer ep = (EntityPlayer) en;
                        for (ItemStack inv : ep.inventory.mainInventory) {
                            Item inv_item = inv == null ? null : inv.getItem();
                            for (Item item : sword_lists) {
                                if (inv_item == item) {
                                    ItemStack itemStack = ep.getEquipmentInSlot(3);
                                    if (itemStack != null) {
                                        if (itemStack.getItem() == Items.iron_chestplate) {
                                            alpha = ep.getName();
                                        }
                                    }
                                    murderers.add(ep);
                                }
                            }
                        }
                        if (murderers.contains(ep)) {
                            if (ep.isInvisible()) {
                                ep.setInvisible(false);
                            }
                            color = new Color(0, 0, 255).getRGB();
                        }
                        if (alpha != null) {
                            if (ep.getName().equalsIgnoreCase(alpha)) {
                                color = new Color(83, 0, 165).getRGB();
                            }
                        }
                    } else {
                        EntityPlayer ep = (EntityPlayer) en;
                        ScorePlayerTeam scorePlayerTeam = (ScorePlayerTeam) ep.getTeam();
                        if (scorePlayerTeam != null) {
                            if (!scorePlayerTeam.getColorPrefix().contains("[M]")) {
                                color = new Color(40, 220, 248).getRGB();
                            }
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
                                Utils.draw3DString(new BlockPos(en).add(0, 1, 0), "Bow", new Color(229, 109, 5).getRGB(), e.partialTicks);
                            }
                        }
                    }
                }
                if (color != 2147483647) {
                    Utils.draw3DBox(en.getEntityBoundingBox(), color, e.partialTicks);
                }
            }
        } else {
            murderers.clear();
            alpha = null;
        }
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent e) {
        if (e.phase == TickEvent.Phase.END) {
            if (Utils.checkForMurderMystery()) {
                tick++;
                if (tick == 30) {
                    tick = 0;
                    for (String line : ScoreboardHandler.getSidebarLines()) {
                        line = ScoreboardHandler.cleanSB(line).trim();
                        if (line.equalsIgnoreCase("Role: Murderer")) {
                            isMurderer = true;
                        }
                    }
                }
            } else {
                isMurderer = false;
            }
        }
    }

}
