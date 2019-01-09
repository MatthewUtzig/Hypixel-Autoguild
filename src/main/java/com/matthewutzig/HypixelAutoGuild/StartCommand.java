package com.matthewutzig.HypixelAutoGuild;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

public class StartCommand extends CommandBase {

    @Override
    public String getCommandName() {
        return "executescript";
    }

    @Override
    public String getCommandUsage(ICommandSender var1) {
        return "starts the auto guild invite script";
    }

    @Override
    public void processCommand(ICommandSender icommandsender, String[] vars) {
        AutoGuildTickHandler.scriptEnabled = !AutoGuildTickHandler.scriptEnabled;
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }
}