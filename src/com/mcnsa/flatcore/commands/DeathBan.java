package com.mcnsa.flatcore.commands;

import java.util.Arrays;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.mcnsa.flatcore.Flatcore;
import com.mcnsa.flatcore.util.*;

@CommandInfo(alias = "deathban", permission = "setdeathban", usage = "<player> <time>", description = "changes a player's deathban")
public class DeathBan implements Command {
	private static Flatcore plugin = null;
	public DeathBan(Flatcore instance) {
		plugin = instance;
	}
	
	@Override
	public Boolean handle(CommandSender player, String sArgs) {
		// extract the args
		String[] args = sArgs.split("\\s+");
		if(args.length != 2) {
			// we need 2 arguments!
			return false;
		}
		
		// try to get the target player
		String targetName = args[0];
		if(!plugin.stateManager.deathBanTimes.containsKey(targetName)) {
			// tell them we don't know who that is
			ColourHandler.sendMessage(player, "&cError: '&f" + targetName + "&c' isn't deathbanned!");
			return true;
		}
		
		// try to parse the time
		long parsedTime = plugin.parseTime(args[1]);
		if(parsedTime < 0) {
			// tell them we couldn't parse it
			ColourHandler.sendMessage(player, "&cError: failed to parse your time: '&f" + args[1] + "&c'");
			return true;
		}
		
		// set their new deathban time
		plugin.stateManager.deathBan(targetName, parsedTime);
		
		// echo the information back
		ColourHandler.sendMessage(player, "&f" + targetName + "'s &adeathban has been set to: &f" + plugin.formatTime(parsedTime));
		
		// if the player is online and we have a positive time, we need to kick them!
		Player targetPlayer = plugin.getServer().getPlayer(targetName);
		if(targetPlayer != null && Arrays.asList(plugin.getServer().getOnlinePlayers()).contains(targetPlayer) && parsedTime > 0) {
			// broadcast?
			if(plugin.config.options.broadcastDeath) {
				// format the message
				String message = plugin.config.options.broadcastDeathMessage.replaceAll("#player", targetPlayer.getName());
				message = message.replaceAll("#deathreason", "an angry mod");
				message = message.replaceAll("#deathbantime", plugin.formatTime(plugin.stateManager.deathBanTime(targetPlayer)));
				message = ColourHandler.processColours(message);
				
				// loop through all players
				Player[] onlinePlayers = plugin.getServer().getOnlinePlayers();
				for(int i = 0; i < onlinePlayers.length; i++) {
					// skip the current player
					if(onlinePlayers[i].equals(targetPlayer)) {
						continue;
					}
					
					// now send the message out
					onlinePlayers[i].sendMessage(message);
				}
				
				// and log it to console
				plugin.log(message);
			}
			
			// send some thunder and lightning
			if(plugin.config.options.thunderDeath) {
				// strike that shit!
				targetPlayer.getWorld().strikeLightningEffect(targetPlayer.getLocation());
			}
			
			// send a private message
			String message = plugin.config.options.privateDeathMessage.replaceAll("#player", targetPlayer.getName());
			message = message.replaceAll("#deathreason", plugin.stateManager.lastDamage(targetPlayer));
			message = message.replaceAll("#deathbantime", plugin.formatTime(plugin.stateManager.deathBanTime(targetPlayer)));
			message = ColourHandler.processColours(message);
			targetPlayer.sendMessage(message);
			
			// and kick them
			targetPlayer.kickPlayer(ColourHandler.stripColours(message));
		}
		
		return true;
	}
}