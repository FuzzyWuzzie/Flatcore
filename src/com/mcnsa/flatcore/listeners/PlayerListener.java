package com.mcnsa.flatcore.listeners;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerRespawnEvent;

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
	
	private Location randomSpawn(Player player) {
		// find a new spot for them to respawn!
		Location location = new Location(player.getWorld(), 0, 0, 0);
		
		// get a random x and y
		location.setX(((plugin.random.nextDouble() * 2.0) - 1.0) * (double)plugin.config.options.spawnRadius + plugin.config.options.spawnX);
		location.setZ(((plugin.random.nextDouble() * 2.0) - 1.0) * (double)plugin.config.options.spawnRadius + plugin.config.options.spawnZ);
		
		// now find the highest block at that location
		location.setY(player.getWorld().getHighestBlockYAt(location));
		
		// and return it!
		return location;
	}
	
	// handle pre-logging in (before actually joining)
	@EventHandler(priority = EventPriority.LOWEST)
	public void playerLogin(PlayerLoginEvent event) {
		// check to see if they're currently deathbanned
		Long deathBanTime = plugin.stateManager.deathBanTime(event.getPlayer());
		if(deathBanTime > 0) {
			// yup, they're death-banned!			
			// get the text
			String message = plugin.config.options.deathbanMessage;
			// figure out why
			message = message.replaceAll("#deathreason", plugin.stateManager.lastDamage(event.getPlayer()));
			// add the time
			message = message.replaceAll("#deathbantime", plugin.formatTime(deathBanTime));
			message = ColourHandler.stripColours(message);
			
			// now stop them!
			event.disallow(Result.KICK_BANNED, message);
		}
	}
	
	// handle joining
	@EventHandler(priority = EventPriority.LOWEST)
	public void playerJoin(PlayerJoinEvent event) {
		// see if they're a new player or not
		if(plugin.stateManager.newPlayer(event.getPlayer())) {
			// randomize their spawning!
			event.getPlayer().teleport(randomSpawn(event.getPlayer()));
			
			// and make them immortal for a bit
			plugin.stateManager.immortalize(event.getPlayer());
		}
	}
	
	// handle spawning
	@EventHandler(priority = EventPriority.LOWEST)
	public void playerSpawn(PlayerRespawnEvent event) {
		// get them a random spawn!
		event.setRespawnLocation(randomSpawn(event.getPlayer()));
		
		// and make them immortal for a bit
		plugin.stateManager.immortalize(event.getPlayer());
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void playerDied(PlayerDeathEvent event) {
		// set their death ban
		plugin.stateManager.deathBan(event.getEntity());
		
		// should we lose everything when we die?
		if(plugin.config.options.loseInventoryOnDeath) {
			// reset all XP
			event.setDroppedExp(0);
			event.setKeepLevel(false);
			event.setNewExp(0);
			event.setNewTotalExp(0);
			event.setNewLevel(0);
			
			// remove all our drops
			event.getDrops().clear();
		}
		
		// handle our own death message
		event.setDeathMessage("");
		
		// broadcast?
		if(plugin.config.options.broadcastDeath) {
			// format the message
			String message = plugin.config.options.broadcastDeathMessage.replaceAll("#player", event.getEntity().getName());
			message = message.replaceAll("#deathreason", plugin.stateManager.lastDamage(event.getEntity()));
			message = message.replaceAll("#deathbantime", plugin.formatTime(plugin.stateManager.deathBanTime(event.getEntity())));
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
		
		// send some thunder and lightning
		if(plugin.config.options.thunderDeath) {
			// strike that shit!
			event.getEntity().getWorld().strikeLightningEffect(event.getEntity().getLocation());
		}
		
		// send a private message
		String message = plugin.config.options.privateDeathMessage.replaceAll("#player", event.getEntity().getName());
		message = message.replaceAll("#deathreason", plugin.stateManager.lastDamage(event.getEntity()));
		message = message.replaceAll("#deathbantime", plugin.formatTime(plugin.stateManager.deathBanTime(event.getEntity())));
		message = ColourHandler.processColours(message);
		event.getEntity().sendMessage(message);
		
		// and kick them
		event.getEntity().kickPlayer(ColourHandler.stripColours(message));
	}
	
	// handle damage / disabled pvp
	@EventHandler(priority = EventPriority.LOWEST)
	public void onDamage(EntityDamageEvent event) {
		// make sure a player got damaged 
		if(event.getEntity() instanceof Player) {
			// before we go anywhere, see if they're immortal first
			if(!plugin.stateManager.isMortal((Player)event.getEntity())) {
				// cancel the damage
				event.setCancelled(true);
				return;
			}
			
			// set that player's last damage
			plugin.stateManager.setLastDamage((Player)event.getEntity(), event);
			
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
	
	// make sure we can't be targeted if we are immortal
	@EventHandler(priority = EventPriority.LOWEST)
	public void onEntityTarget(EntityTargetEvent event) {
		if((event.getTarget() instanceof Player) && !plugin.stateManager.isMortal((Player)event.getTarget())) {
			// nope, we're immortal. Can't target us!
			event.setCancelled(true);
		}
	}
	
	// handle chatting (when editing challenges)
	@EventHandler(priority = EventPriority.LOWEST)
	public void chatHandler(AsyncPlayerChatEvent event) {
		// see if they're editing a challenge
		if(plugin.challengeManager.isEditingChallenge(event.getPlayer())) {
			// capture their chat
			plugin.challengeManager.appendChallenge(event.getPlayer(), event.getMessage());
			
			// cancel it
			event.setCancelled(true);
			
			// and send it back to them in green so they know it was captured
			ColourHandler.sendMessage(event.getPlayer(), "&a> &f" + event.getMessage());
		}
	}
	
	// disable quit messages if they're death-banned
	@EventHandler
	public void onPlayerKick(PlayerKickEvent event) {
		// determine if they're death-banned
		if(plugin.stateManager.deathBanTime(event.getPlayer()) > 0L) {
			// yup, death-banned!
			// cancel the quit message
			event.setLeaveMessage("");
		}
	}
	
	// handle entities dying
	@EventHandler
	public void onEntityDeath(EntityDeathEvent event) {
		// prevent drops from non-player-killed mobs
		if(!(event.getEntity() instanceof Player) && !(event.getEntity().getLastDamageCause() instanceof EntityDamageByEntityEvent)) {
			event.setDroppedExp(0);
			event.getDrops().clear();
		}
	}
}