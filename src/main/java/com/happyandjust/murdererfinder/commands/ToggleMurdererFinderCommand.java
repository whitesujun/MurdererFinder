package com.happyandjust.murdererfinder.commands;

import com.happyandjust.murdererfinder.handlers.ConfigHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

public class ToggleMurdererFinderCommand extends CommandBase {
    public static boolean toggled;

    @Override
    public String getCommandName() {
        return "murdererfinder";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return null;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        toggled = !toggled;
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("Toggled : " + toggled));
        ConfigHandler.writeBooleanConfig("murderer", "toggle", toggled);
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }
}
