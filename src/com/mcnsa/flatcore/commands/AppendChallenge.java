package com.mcnsa.flatcore.commands;

import org.bukkit.command.CommandSender;

import com.mcnsa.flatcore.Flatcore;
import com.mcnsa.flatcore.util.*;

@CommandInfo(alias = "appendchallenge", permission = "setchallenge", usage = "<text>", description = "adds to the currently edited weekly challenges")
public class AppendChallenge implements Command {
	private static Flatcore plugin = null;
	public AppendChallenge(Flatcore instance) {
		plugin = instance;
	}
	
	@Override
	public Boolean handle(CommandSender player, String sArgs) {
		// check to see if they're editing a challenge or not
		if(!plugin.challengeManager.isEditingChallenge(player.getName())) {
			ColourHandler.sendMessage(player, "&cError - you weren't writing a challenge!");
			return true;
		}
		
		// make sure they gave text
		if(sArgs.length() < 1) {
			ColourHandler.sendMessage(player, "&cError - you need to specify text to write!");
			return true;
		}

		// ok, append the text
		plugin.challengeManager.appendChallenge(player.getName(), sArgs);
		
		// and send it back to them in green so they know it was captured
		ColourHandler.sendMessage(player, "&a> &f" + sArgs);
		
		return true;
	}
}