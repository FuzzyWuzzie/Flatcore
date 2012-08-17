package com.mcnsa.flatcore.commands;

import org.bukkit.entity.Player;

import com.mcnsa.flatcore.Flatcore;
import com.mcnsa.flatcore.util.*;

@CommandInfo(alias = "stopchallenge", permission = "setchallenge", usage = "", description = "stops writing weekly challenges")
public class StopChallenge implements Command {
	private static Flatcore plugin = null;
	public StopChallenge(Flatcore instance) {
		plugin = instance;
	}
	
	@Override
	public Boolean handle(Player player, String sArgs) {
		// check to see if they're editing a challenge or not
		if(!plugin.challengeManager.isEditingChallenge(player)) {
			ColourHandler.sendMessage(player, "&cError - you weren't writing a challenge!");
			return true;
		}
		
		// ok, save the challenge
		int id = plugin.challengeManager.stopChallenge(player);
		if(id < 0) {
			// an error occurred!
			ColourHandler.sendMessage(player, "&cError - something went wrong! Editing stopped!");
		}
		
		// send them a message
		ColourHandler.sendMessage(player, "&aCongratulations, your challenge &f#" + (id + 1) + " &ahas been saved!");
		
		return true;
	}
}