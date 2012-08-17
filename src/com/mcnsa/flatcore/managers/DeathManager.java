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
	private HashMap<Player, String> lastPlayerDamage = new HashMap<Player, String>();
	
	Flatcore plugin = null;
	public DeathManager(Flatcore instance) {
		plugin = instance;
	}
	
	// access our last damage
	public String lastDamage(Player player) {
		if(lastPlayerDamage.containsKey(player)) {
			return lastPlayerDamage.get(player);
		}
		else {
			return "nothing";
		}
	}
	
	public void setLastDamage(Player player, EntityDamageEvent event) {
		if(event.getCause() == DamageCause.BLOCK_EXPLOSION) {
			lastPlayerDamage.put(player, "an &6EXPLOSION");
		}
		else if(event.getCause() == DamageCause.CONTACT) {
			lastPlayerDamage.put(player, "a &acactus");
		}
		else if(event.getCause() == DamageCause.DROWNING) {
			lastPlayerDamage.put(player, "&9drowning");
		}
		else if(event.getCause() == DamageCause.ENTITY_ATTACK) {
			lastPlayerDamage.put(player, "an &cangry mob");
			if(event instanceof EntityDamageByEntityEvent) {
				EntityDamageByEntityEvent mobEvent = (EntityDamageByEntityEvent)event;
				if(mobEvent.getDamager().getType() == EntityType.ARROW) {
					lastPlayerDamage.put(player, "an &7arrow");
				}
				else if(mobEvent.getDamager().getType() == EntityType.BLAZE) {
					lastPlayerDamage.put(player, "a &cblaze");
				}
				else if(mobEvent.getDamager().getType() == EntityType.CAVE_SPIDER) {
					lastPlayerDamage.put(player, "a &acave spider");
				}
				else if(mobEvent.getDamager().getType() == EntityType.CREEPER) {
					lastPlayerDamage.put(player, "a &acreeper");
				}
				else if(mobEvent.getDamager().getType() == EntityType.ENDER_DRAGON) {
					lastPlayerDamage.put(player, "an &5ender dragon");
				}
				else if(mobEvent.getDamager().getType() == EntityType.ENDERMAN) {
					lastPlayerDamage.put(player, "an &5enderman");
				}
				else if(mobEvent.getDamager().getType() == EntityType.FALLING_BLOCK) {
					lastPlayerDamage.put(player, "&bsuffocation");
				}
				else if(mobEvent.getDamager().getType() == EntityType.FIREBALL) {
					lastPlayerDamage.put(player, "a &4fireball");
				}
				else if(mobEvent.getDamager().getType() == EntityType.GHAST) {
					lastPlayerDamage.put(player, "a &fghast");
				}
				else if(mobEvent.getDamager().getType() == EntityType.GIANT) {
					lastPlayerDamage.put(player, "a &2GIANT");
				}
				else if(mobEvent.getDamager().getType() == EntityType.IRON_GOLEM) {
					lastPlayerDamage.put(player, "an &7iron golem");
				}
				else if(mobEvent.getDamager().getType() == EntityType.LIGHTNING) {
					lastPlayerDamage.put(player, "&elightning");
				}
				else if(mobEvent.getDamager().getType() == EntityType.MAGMA_CUBE) {
					lastPlayerDamage.put(player, "a &4magma cube");
				}
				else if(mobEvent.getDamager().getType() == EntityType.PIG_ZOMBIE) {
					lastPlayerDamage.put(player, "a &cpig zombie");
				}
				else if(mobEvent.getDamager().getType() == EntityType.PLAYER) {
					lastPlayerDamage.put(player, "a player");//((Player)mobEvent.getDamager()).getName());
				}
				else if(mobEvent.getDamager().getType() == EntityType.SILVERFISH) {
					lastPlayerDamage.put(player, "a &7silverfish");
				}
				else if(mobEvent.getDamager().getType() == EntityType.SKELETON) {
					lastPlayerDamage.put(player, "a &fskeleton");
				}
				else if(mobEvent.getDamager().getType() == EntityType.SLIME) {
					lastPlayerDamage.put(player, "a &aslime");
				}
				else if(mobEvent.getDamager().getType() == EntityType.SMALL_FIREBALL) {
					lastPlayerDamage.put(player, "a &4fireball");
				}
				else if(mobEvent.getDamager().getType() == EntityType.SPIDER) {
					lastPlayerDamage.put(player, "a &cspider");
				}
				else if(mobEvent.getDamager().getType() == EntityType.SPLASH_POTION) {
					lastPlayerDamage.put(player, "a &dpotion");
				}
				else if(mobEvent.getDamager().getType() == EntityType.WOLF) {
					lastPlayerDamage.put(player, "a &7wolf");
				}
				else if(mobEvent.getDamager().getType() == EntityType.ZOMBIE) {
					lastPlayerDamage.put(player, "a &2zombie");
				}
			}
		}
		else if(event.getCause() == DamageCause.ENTITY_EXPLOSION) {
			lastPlayerDamage.put(player, "an &aEXPLOSION");
		}
		else if(event.getCause() == DamageCause.FALL) {
			lastPlayerDamage.put(player, "a high fall");
		}
		else if(event.getCause() == DamageCause.FIRE || event.getCause() == DamageCause.FIRE_TICK) {
			lastPlayerDamage.put(player, "&cfire");
		}
		else if(event.getCause() == DamageCause.LAVA) {
			lastPlayerDamage.put(player, "&4lava");
		}
		else if(event.getCause() == DamageCause.LIGHTNING) {
			lastPlayerDamage.put(player, "&elightning");
		}
		else if(event.getCause() == DamageCause.MAGIC) {
			lastPlayerDamage.put(player, "&dmagic");
		}
		else if(event.getCause() == DamageCause.POISON) {
			lastPlayerDamage.put(player, "&apoison");
		}
		else if(event.getCause() == DamageCause.STARVATION) {
			lastPlayerDamage.put(player, "&6starvation");
		}
		else if(event.getCause() == DamageCause.SUFFOCATION) {
			lastPlayerDamage.put(player, "&7suffocation");
		}
		else if(event.getCause() == DamageCause.SUICIDE) {
			lastPlayerDamage.put(player, "&3suicide");
		}
		else if(event.getCause() == DamageCause.VOID) {
			lastPlayerDamage.put(player, "the &3void");
		}
		else if(event.getCause() == DamageCause.PROJECTILE) {
			lastPlayerDamage.put(player, "a &fskeleton");
		}
		else {
			lastPlayerDamage.put(player, "&5mysterious forces");
		}
	}
}
