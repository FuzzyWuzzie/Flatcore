package com.mcnsa.flatcore.commands;

import org.bukkit.command.CommandSender;

import com.mcnsa.flatcore.Flatcore;
import com.mcnsa.flatcore.util.*;

@CommandInfo(alias = "flatreload", permission = "reload", usage = "", description = "reloads the flatcore config")
public class Reload implements Command {
	private static Flatcore plugin = null;
	public Reload(Flatcore instance) {
		plugin = instance;
	}
	
	@Override
	public Boolean handle(CommandSender player, String sArgs) {
		plugin.reloadConfig();
		if(!plugin.config.load(plugin.getConfig())) {
			// shit
			// BAIL
			plugin.error("configuration failed");
			ColourHandler.sendMessage(player, "&cError - flatcore configuration reload railed!");
		}
		plugin.saveConfig();
		
		// send them a message
		ColourHandler.sendMessage(player, "&aConfiguration reloaded successfully!");
		
		return true;
	}
}