package com.mcnsa.flatcore.managers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.TimerTask;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import com.mcnsa.flatcore.Flatcore;
import com.mcnsa.flatcore.util.ColourHandler;

public class StateManager {
	// keep track of the last damage each player recieved
	public HashMap<String, String> lastPlayerDamage = new HashMap<String, String>();
	
	// keep track of deathban times
	public HashMap<String, Long> deathBanTimes = new HashMap<String, Long>();
	
	// keep track of immortality times
	public HashMap<String, Long> immortalityTimes = new HashMap<String, Long>();
	
	Flatcore plugin = null;
	public StateManager(Flatcore instance) {
		plugin = instance;
	}
	
	// access our last damage
	public String lastDamage(Player player) {
		if(lastPlayerDamage.containsKey(player.getName())) {
			return lastPlayerDamage.get(player.getName());
		}
		else {
			return "nothing";
		}
	}
	
	// check whether a player has ever logged in
	// and update their login status
	public Boolean newPlayer(Player player) {
		boolean exists = deathBanTimes.containsKey(player.getName());
		deathBanTimes.put(player.getName(), 0L);
		return !exists;
	}
	
	// check how long a player is deathbanned for
	public Long deathBanTime(Player player) {
		if(deathBanTimes.containsKey(player.getName())) {
			return deathBanTimes.get(player.getName());
		}
		return 0L;
	}
	
	// apply a deathban to a player
	public void deathBan(Player player) {
		// check player permissions
		if(plugin.hasPermission(player, "admin")) {
			deathBanTimes.put(player.getName(), 0L); // don't ban admins
		}
		if(plugin.hasPermission(player, "mod")) {
			deathBanTimes.put(player.getName(), plugin.config.options.modDeathBanTime);
		}
		else {
			deathBanTimes.put(player.getName(), plugin.config.options.deathBanTime);
		}
	}
	
	// apply a specific deathban to a player
	public void deathBan(String player, Long time) {
		// check player permissions
		deathBanTimes.put(player, time);
	}
	
	// update the timers on death bans
	// (subtract 1 each second that the server is running if a player has a positive
	// death-ban time)
	public void updateDeathBans() {
		for(String player: deathBanTimes.keySet()) {
			if(deathBanTimes.get(player) > 0L) {
				deathBanTimes.put(player, deathBanTimes.get(player) - 1);
				
				// make sure it sticks at 0
				if(deathBanTimes.get(player) < 0L) {
					deathBanTimes.put(player, 0L);
				}
			}
		}
	}
	
	// check to see if a player is mortal or not
	public boolean isMortal(Player player) {
		// default to mortal
		if(!immortalityTimes.containsKey(player.getName())) {
			return true;
		}
		return (immortalityTimes.get(player.getName()) <= 0L);
	}
	
	// make a player immortal
	public void immortalize(Player player) {
		// set them as "immortal"
		immortalityTimes.put(player.getName(), plugin.config.options.spawnImmortalityTime);
		// send them a message
		String message = plugin.config.options.spawnImmortalityMessage;
		message = message.replaceAll("#immortaltime", plugin.formatTime(plugin.config.options.spawnImmortalityTime));
		message = ColourHandler.processColours(message);
		player.sendMessage(message);
	}
	
	// make a player mortal again
	public void mortalize(Player player) {
		// set them as "mortal"
		immortalityTimes.put(player.getName(), 0L);
		// send them a message
		String message = plugin.config.options.spawnMortalityMessage;
		message = ColourHandler.processColours(message);
		player.sendMessage(message);
	}
	
	// update timers on immortality
	public void updateImmortality() {
		for(String playerName: immortalityTimes.keySet()) {
			// only update their immortality if they're online
			Player player = plugin.getServer().getPlayer(playerName);
			if(player != null && Arrays.asList(plugin.getServer().getOnlinePlayers()).contains(player)) {
				if(immortalityTimes.get(playerName) > 0L) {
					// update their timer
					immortalityTimes.put(playerName, immortalityTimes.get(playerName) - 1);
					
					// check to see if we should be reminding them
					if(immortalityTimes.get(playerName) > 0 && (plugin.config.options.spawnImmortalityTime - immortalityTimes.get(playerName)) % plugin.config.options.spawnImmortalityReminder == 0) {
						String message = plugin.config.options.spawnImmortalityMessage;
						message = message.replaceAll("#immortaltime", plugin.formatTime(immortalityTimes.get(playerName)));
						message = ColourHandler.processColours(message);
						player.sendMessage(message);
					}
					
					// now check to see if the player has gone mortal
					if(immortalityTimes.get(playerName) <= 0L) {
						// they've gone mortal!
						mortalize(player);
					}
				}
			}
		}
	}
	
	// update whatever hit the player last
	public void setLastDamage(Player player, EntityDamageEvent event) {
		if(event.getCause() == DamageCause.BLOCK_EXPLOSION) {
			lastPlayerDamage.put(player.getName(), "an &6EXPLOSION");
		}
		else if(event.getCause() == DamageCause.CONTACT) {
			lastPlayerDamage.put(player.getName(), "a &acactus");
		}
		else if(event.getCause() == DamageCause.DROWNING) {
			lastPlayerDamage.put(player.getName(), "&9drowning");
		}
		else if(event.getCause() == DamageCause.ENTITY_ATTACK) {
			lastPlayerDamage.put(player.getName(), "an &cangry mob");
			if(event instanceof EntityDamageByEntityEvent) {
				EntityDamageByEntityEvent mobEvent = (EntityDamageByEntityEvent)event;
				if(mobEvent.getDamager().getType() == EntityType.ARROW) {
					lastPlayerDamage.put(player.getName(), "an &7arrow");
				}
				else if(mobEvent.getDamager().getType() == EntityType.BLAZE) {
					lastPlayerDamage.put(player.getName(), "a &cblaze");
				}
				else if(mobEvent.getDamager().getType() == EntityType.CAVE_SPIDER) {
					lastPlayerDamage.put(player.getName(), "a &acave spider");
				}
				else if(mobEvent.getDamager().getType() == EntityType.CREEPER) {
					lastPlayerDamage.put(player.getName(), "a &acreeper");
				}
				else if(mobEvent.getDamager().getType() == EntityType.ENDER_DRAGON) {
					lastPlayerDamage.put(player.getName(), "an &5ender dragon");
				}
				else if(mobEvent.getDamager().getType() == EntityType.ENDERMAN) {
					lastPlayerDamage.put(player.getName(), "an &5enderman");
				}
				else if(mobEvent.getDamager().getType() == EntityType.FALLING_BLOCK) {
					lastPlayerDamage.put(player.getName(), "&bsuffocation");
				}
				else if(mobEvent.getDamager().getType() == EntityType.FIREBALL) {
					lastPlayerDamage.put(player.getName(), "a &4fireball");
				}
				else if(mobEvent.getDamager().getType() == EntityType.GHAST) {
					lastPlayerDamage.put(player.getName(), "a &fghast");
				}
				else if(mobEvent.getDamager().getType() == EntityType.GIANT) {
					lastPlayerDamage.put(player.getName(), "a &2GIANT");
				}
				else if(mobEvent.getDamager().getType() == EntityType.IRON_GOLEM) {
					lastPlayerDamage.put(player.getName(), "an &7iron golem");
				}
				else if(mobEvent.getDamager().getType() == EntityType.LIGHTNING) {
					lastPlayerDamage.put(player.getName(), "&elightning");
				}
				else if(mobEvent.getDamager().getType() == EntityType.MAGMA_CUBE) {
					lastPlayerDamage.put(player.getName(), "a &4magma cube");
				}
				else if(mobEvent.getDamager().getType() == EntityType.PIG_ZOMBIE) {
					lastPlayerDamage.put(player.getName(), "a &cpig zombie");
				}
				else if(mobEvent.getDamager().getType() == EntityType.PLAYER) {
					lastPlayerDamage.put(player.getName(), ((Player)mobEvent.getDamager()).getName());
				}
				else if(mobEvent.getDamager().getType() == EntityType.SILVERFISH) {
					lastPlayerDamage.put(player.getName(), "a &7silverfish");
				}
				else if(mobEvent.getDamager().getType() == EntityType.SKELETON) {
					lastPlayerDamage.put(player.getName(), "a &fskeleton");
				}
				else if(mobEvent.getDamager().getType() == EntityType.SLIME) {
					lastPlayerDamage.put(player.getName(), "a &aslime");
				}
				else if(mobEvent.getDamager().getType() == EntityType.SMALL_FIREBALL) {
					lastPlayerDamage.put(player.getName(), "a &4fireball");
				}
				else if(mobEvent.getDamager().getType() == EntityType.SPIDER) {
					lastPlayerDamage.put(player.getName(), "a &cspider");
				}
				else if(mobEvent.getDamager().getType() == EntityType.SPLASH_POTION) {
					lastPlayerDamage.put(player.getName(), "a &dpotion");
				}
				else if(mobEvent.getDamager().getType() == EntityType.WOLF) {
					lastPlayerDamage.put(player.getName(), "a &7wolf");
				}
				else if(mobEvent.getDamager().getType() == EntityType.ZOMBIE) {
					lastPlayerDamage.put(player.getName(), "a &2zombie");
				}
			}
		}
		else if(event.getCause() == DamageCause.ENTITY_EXPLOSION) {
			lastPlayerDamage.put(player.getName(), "an &aEXPLOSION");
		}
		else if(event.getCause() == DamageCause.FALL) {
			lastPlayerDamage.put(player.getName(), "a high fall");
		}
		else if(event.getCause() == DamageCause.FIRE || event.getCause() == DamageCause.FIRE_TICK) {
			lastPlayerDamage.put(player.getName(), "&cfire");
		}
		else if(event.getCause() == DamageCause.LAVA) {
			lastPlayerDamage.put(player.getName(), "&4lava");
		}
		else if(event.getCause() == DamageCause.LIGHTNING) {
			lastPlayerDamage.put(player.getName(), "&elightning");
		}
		else if(event.getCause() == DamageCause.MAGIC) {
			lastPlayerDamage.put(player.getName(), "&dmagic");
		}
		else if(event.getCause() == DamageCause.POISON) {
			lastPlayerDamage.put(player.getName(), "&apoison");
		}
		else if(event.getCause() == DamageCause.STARVATION) {
			lastPlayerDamage.put(player.getName(), "&6starvation");
		}
		else if(event.getCause() == DamageCause.SUFFOCATION) {
			lastPlayerDamage.put(player.getName(), "&7suffocation");
		}
		else if(event.getCause() == DamageCause.SUICIDE) {
			lastPlayerDamage.put(player.getName(), "&3suicide");
		}
		else if(event.getCause() == DamageCause.VOID) {
			lastPlayerDamage.put(player.getName(), "the &3void");
		}
		else if(event.getCause() == DamageCause.PROJECTILE) {
			lastPlayerDamage.put(player.getName(), "a &fskeleton");
		}
		else {
			lastPlayerDamage.put(player.getName(), "&5mysterious forces");
		}
	}
	
	// an internal class for ticking away deathbans
	public class TickerTask extends TimerTask {
		private StateManager stateManager = null;
		
		public TickerTask(StateManager stateManager) {
			this.stateManager = stateManager;
		}
		
		@Override
		public void run() {
			try {
				// update things every second
				stateManager.updateDeathBans();
				stateManager.updateImmortality();
			}
			catch(Exception e) {
				stateManager.plugin.error("TickerTask crashed: " + e.getMessage());
			}
		}
	}
}
