package com.mcnsa.flatcore.commands;

import org.bukkit.entity.Player;

import com.mcnsa.flatcore.Flatcore;
import com.mcnsa.flatcore.util.*;

@CommandInfo(alias = "startchallenge", permission = "setchallenge", usage = "[number]", description = "starts writing weekly challenges")
public class StartChallenge implements Command {
	private static Flatcore plugin = null;
	public StartChallenge(Flatcore instance) {
		plugin = instance;
	}
	
	@Override
	public Boolean handle(Player player, String sArgs) {
		// store the challenge number that we want to access
		Integer challengeNumber = -1;
	
		// check to see if they gave any arguments
		if(sArgs.length() > 0) {
			// they did, they did!
			// attempt to parse it into a number!
			try {
				challengeNumber = Integer.parseInt(sArgs);
			}
			catch(Exception e) {
				// uh-oh.. not a number!
				return false;
			}
		}
		
		// figure out the latest week number if necessary
		if(challengeNumber < 1) {
			challengeNumber = plugin.challengeManager.latestChallengeNumber() + 1;
		}
		
		// and start editing the challenge
		if(!plugin.challengeManager.startChallenge(player, challengeNumber)) {
			// uh-oh, we can't edit it for some reason
			ColourHandler.sendMessage(player, "&cError: could not edit challenge &f#" + challengeNumber);
			return true;
		}
		
		// ok, tell them that they're now editing the challenge
		ColourHandler.sendMessage(player, "&aYou are now editing the challenge for week " + challengeNumber + "!");
		ColourHandler.sendMessage(player, "&aEverything you type in chat will be added to the challenge until you issue a &f/stopchallenge &acommand!");
		
		return true;
	}
}