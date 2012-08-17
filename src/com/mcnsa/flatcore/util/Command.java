package com.mcnsa.flatcore.util;

import org.bukkit.command.CommandSender;

public interface Command {
	public Boolean handle(CommandSender player, String sArgs);
}