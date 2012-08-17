package com.mcnsa.flatcore.listeners;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.inventory.ItemStack;

import com.mcnsa.flatcore.Flatcore;
import com.mcnsa.flatcore.util.ColourHandler;

public class PlayerListener implements Listener {
	
	Flatcore plugin = null;
	public PlayerListener(Flatcore instance) {
		plugin = instance;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void preprocessHandler(PlayerCommandPreprocessEvent event) {
		// if the command is cancelled, back out
		if(event.isCancelled()) return;
		
		// intercept the command
		if(plugin.commandManager.handleCommand(event.getPlayer(), event.getMessage())) {
			// we handled it, cancel it
			event.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void playerDied(PlayerDeathEvent event) {
		// should we lose everything when we die?
		if(plugin.config.options.loseInventoryOnDeath) {
			// reset all XP
			event.setDroppedExp(0);
			event.setKeepLevel(false);
			event.setNewExp(0);
			event.setNewTotalExp(0);
			event.setNewLevel(0);
			
			// remove all our drops
			List<ItemStack> drops = event.getDrops();
			for(int i = 0; i < drops.size(); i++) {
				drops.get(i).setAmount(0);
			}
			
			// handle our own death message
			event.setDeathMessage("");
		}
		
		// broadcast?
		if(plugin.config.options.broadcastDeath) {
			// format the message
			String message = plugin.config.options.broadcastDeathMessage.replaceAll("#player", event.getEntity().getName());
			message = message.replaceAll("#deathreason", plugin.deathManager.lastDamage(event.getEntity()));
			message = message.replaceAll("#deathbantime", plugin.formatTime(plugin.config.options.deathBanTime));
			message = ColourHandler.processColours(message);
			
			// loop through all players
			Player[] onlinePlayers = plugin.getServer().getOnlinePlayers();
			for(int i = 0; i < onlinePlayers.length; i++) {
				// skip the current player
				if(onlinePlayers[i].equals(event.getEntity())) {
					continue;
				}
				
				// now send the message out
				onlinePlayers[i].sendMessage(message);
			}
			
			// and log it to console
			plugin.log(message);
		}
		
		// send a private message
		String message = plugin.config.options.privateDeathMessage.replaceAll("#player", event.getEntity().getName());
		message = message.replaceAll("#deathreason", plugin.deathManager.lastDamage(event.getEntity()));
		message = message.replaceAll("#deathbantime", plugin.formatTime(plugin.config.options.deathBanTime));
		message = ColourHandler.processColours(message);
		event.getEntity().sendMessage(message);
		
		// deathban
		// TODO:
	}
	
	// handle damage / disabled pvp
	@EventHandler(priority = EventPriority.LOWEST)
	public void onEntityDamage(EntityDamageEvent event) {
		// make sure a player got damaged 
		if(event.getEntity() instanceof Player) {
			// set that player's last damage
			plugin.deathManager.setLastDamage((Player)event.getEntity(), event);
			
			// see if another entity damaged them
			if(event instanceof EntityDamageByEntityEvent) {
				EntityDamageByEntityEvent mobevent = (EntityDamageByEntityEvent)event;
				if(mobevent.getDamager() instanceof Player) {
					// check to see if PvP is disabled or not
					if(plugin.config.options.disablePVP) {
						// cancel the event and warn the attacker
						event.setCancelled(true);
						ColourHandler.sendMessage((Player)mobevent.getDamager(), "&cPvP is disabled!");
					}
				}
			}
		}
	}
}