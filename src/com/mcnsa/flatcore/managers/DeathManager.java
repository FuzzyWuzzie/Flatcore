package com.mcnsa.flatcore.managers;

import java.util.HashMap;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import com.mcnsa.flatcore.Flatcore;

public class DeathManager {
	// keep track of the last damage each player recieved
	private HashMap<String, String> lastPlayerDamage = new HashMap<String, String>();
	
	// keep track of deathban times
	private HashMap<String, Long> deathBanTimes = new HashMap<String, Long>();
	
	Flatcore plugin = null;
	public DeathManager(Flatcore instance) {
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
}
