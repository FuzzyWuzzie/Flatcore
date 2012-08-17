package com.mcnsa.flatcore.managers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.TimerTask;

import org.bukkit.Material;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.CaveSpider;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.Giant;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.MagmaCube;
import org.bukkit.entity.MushroomCow;
import org.bukkit.entity.Pig;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Silverfish;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Snowman;
import org.bukkit.entity.Spider;
import org.bukkit.entity.Squid;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.entity.Tameable;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Wolf;
import org.bukkit.entity.Zombie;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import com.mcnsa.flatcore.Flatcore;
import com.mcnsa.flatcore.util.ColourHandler;

public class StateManager {
	// keep track of the last damage each player recieved
	public HashMap<String, String> lastPlayerDamage = new HashMap<String, String>();
	
	// keep track of deathban times
	public HashMap<String, Long> deathBanTimes = new HashMap<String, Long>();
	
	// keep track of immortality times
	public HashMap<String, Long> immortalityTimes = new HashMap<String, Long>();
	
	// enumerate all the ways we can die
	public static enum DeathEventType {
		BLOCK_EXPLOSION, ENTITY_EXPLOSION, CAVE_SPIDER, CONTACT, CREEPER, DROWNING, ENDERMAN, FALL, FIRE, FIRE_TICK, GHAST, GIANT, LAVA, LIGHTNING, MONSTER, PIG_ZOMBIE, PVP, PVP_FISTS, PVP_TAMED, SILVERFISH, SKELETON, SLIME, SPIDER, STARVATION, SUFFOCATION, SUICIDE, UNKNOWN, VOID, WOLF, ZOMBIE, BLAZE, MAGMA_CUBE, ENDERDRAGON, DISPENSER, POISON, MAGIC, IRON_GOLEM
	}
	
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
		String causeOfDeath = new String("");
		Player killer = player;
		String murderWeapon = new String("");
		if(event instanceof EntityDamageByEntityEvent) {
			Entity damager = ((EntityDamageByEntityEvent)event).getDamager();
			if(damager instanceof Player) {
				if(((Player)damager).getItemInHand().getType().equals(Material.AIR)) {
					causeOfDeath = "pvp";
					murderWeapon = "fists";
				}
				else {
					causeOfDeath = "pvp";
				}
				murderWeapon = ((Player)damager).getItemInHand().getType().toString();
				killer = (Player)damager;
			}
			else if (damager instanceof Creature || damager instanceof Slime) {
				if (damager instanceof Tameable && ((Tameable)damager).isTamed()) {
					causeOfDeath = "pvp";
					murderWeapon = getEntityType(damager).toString();
					killer = (Player)((Tameable) damager).getOwner();
				}
				else {
					try {
						causeOfDeath = getEntityType(damager);
					}
					catch (IllegalArgumentException iae) {
						causeOfDeath = "unknown";
					}
				}
			}
			else if (damager instanceof Projectile) {
				if (((Projectile) damager).getShooter() instanceof Player) {
					causeOfDeath = "pvp";
					murderWeapon = ((Projectile) damager).toString().replace("Craft", "");
					killer = (Player) ((Projectile) damager).getShooter();
				}
				if (((Projectile) damager).getShooter() == null) {
					//let's assume that null will only be caused by a dispenser!
					causeOfDeath = "dispenser";
					murderWeapon = ((Projectile) damager).toString().replace("Craft", "");
				}
				if (((Projectile) damager).getShooter().toString().equalsIgnoreCase("CraftSkeleton")) {
					causeOfDeath = "skeleton";
					murderWeapon = ((Projectile) damager).toString().replace("Craft", "");
				}

			}
			else if (damager instanceof TNTPrimed) {
				causeOfDeath = "block_explosion";
			}
		}
		else if (event != null) {
			try {
				causeOfDeath = event.getCause().toString();
			}
			catch (IllegalArgumentException e) {
				causeOfDeath = "unknown";
			}
		}

		// update their damage
		String damage = causeOfDeath + ":" + murderWeapon + ":" + killer.getName();
		lastPlayerDamage.put(player.getName(), damage);
	}
	
	public static String getEntityType(Entity entity) {
		if (entity instanceof Blaze) {
			return "blaze";
		}
		if (entity instanceof CaveSpider) {
			return "cave_spider";
		}
		if (entity instanceof Chicken) {
			return "chicken";
		}
		if (entity instanceof Cow) {
			return "cow";
		}
		if (entity instanceof Creeper) {
			return "creeper";
		}
		if (entity instanceof EnderDragon) {
			return "ender_dragon";
		}
		if (entity instanceof Enderman) {
			return "enderman";
		}
		if (entity instanceof Ghast) {
			return "ghast";
		}
		if (entity instanceof Giant) {
			return "giant";
		}
		if (entity instanceof MagmaCube) {
			return "magma_cube";
		}
		if (entity instanceof MushroomCow) {
			return "mushroom_cow";
		}
		if (entity instanceof Pig) {
			return "pig";
		}
		if (entity instanceof PigZombie) {
			return "pig_zombie";
		}
		if (entity instanceof Sheep) {
			return "sheep";
		}
		if (entity instanceof Skeleton) {
			return "skeleton";
		}
		if (entity instanceof Slime) {
			return "slime";
		}
		if (entity instanceof Silverfish) {
			return "silverfish";
		}
		if (entity instanceof Snowman) {
			return "snowman";
		}
		if (entity instanceof Spider) {
			return "spider";
		}
		if (entity instanceof Squid) {
			return "squid";
		}
		if (entity instanceof Villager) {
			return "villager";
		}
		if (entity instanceof Zombie) {
			return "zombie";
		}
		if (entity instanceof Wolf) {
			return "wolf";
		}
		if (entity instanceof IronGolem) {
			return "iron_golem";
		}
		
		return "";
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
