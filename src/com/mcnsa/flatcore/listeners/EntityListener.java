package com.mcnsa.flatcore.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetEvent;

import com.mcnsa.flatcore.Flatcore;
import com.mcnsa.flatcore.util.ColourHandler;

public class EntityListener implements Listener {
	Flatcore plugin = null;
	public EntityListener(Flatcore instance) {
		plugin = instance;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	// make sure we can't be targeted if we are immortal
	@EventHandler(priority = EventPriority.LOWEST)
	public void onEntityTarget(EntityTargetEvent event) {
		if((event.getTarget() instanceof Player) && !plugin.stateManager.isMortal((Player)event.getTarget())) {
			// nope, we're immortal. Can't target us!
			event.setCancelled(true);
		}
	}
	
	// handle entities dying
	/*@EventHandler
	public void onEntityDeath(EntityDeathEvent event) {
		// prevent drops from non-player-killed mobs
		if(!(event.getEntity() instanceof Player) && !(event.getEntity().getLastDamageCause() instanceof EntityDamageByEntityEvent)) {
			event.setDroppedExp(0);
			event.getDrops().clear();
		}
	}*/
	
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
}
