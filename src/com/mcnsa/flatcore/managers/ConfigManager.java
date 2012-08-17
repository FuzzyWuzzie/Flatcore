package com.mcnsa.flatcore.managers;

import org.bukkit.configuration.file.FileConfiguration;

import com.mcnsa.flatcore.Flatcore;

public class ConfigManager {
	// store the main plugin for later access
	static Flatcore plugin = null;
	public ConfigOptions options = new ConfigOptions();
	public ConfigManager(Flatcore instance) {
		plugin = instance;
	}

	// load the configuration
	public Boolean load(FileConfiguration config) {
		// load the spawn center
		options.spawnX = config.getInt("spawn-center-x", 0);
		options.spawnZ = config.getInt("spawn-center-z", 0);
		
		// load the spawn radius
		options.spawnRadius = config.getInt("spawn-radius", 10000);
		
		// should we disable PVP altogether?
		options.disablePVP = config.getBoolean("disable-pvp", true);
		
		// should the player's inventory be destroyed upon death?
		options.loseInventoryOnDeath = config.getBoolean("lose-inventory-on-death", true);
		
		// parse the deathban time
		String deathBanTime = config.getString("death-ban-time", "12h");
		options.deathBanTime = plugin.parseTime(deathBanTime);
		if(options.deathBanTime < 0) {
			options.deathBanTime = 43200L;
		}
		
		// parse the mod deathban time
		String modDeathBanTime = config.getString("mod-death-ban-time", "10m");
		options.modDeathBanTime = plugin.parseTime(modDeathBanTime);
		if(options.modDeathBanTime < 0) {
			options.modDeathBanTime = 600L;
		}
		
		// should lightning strike when someone dies?
		options.thunderDeath = config.getBoolean("thunder-death", true);
		
		// should we broadcast deaths to the entire server?
		options.broadcastDeath = config.getBoolean("broadcast-death", true);
		
		// get the death messages
		options.broadcastDeathMessage = config.getString("broadcast-death-message", "&4#player was killed by &c#deathreason");
		options.privateDeathMessage = config.getString("private-death-message", "&4You've been killed by &c#deathreason &4and are now death-banned for &9#deathbantime&4. We'll see you then!");
		
		// get the deathban message (for when they try to join and are still banned)
		options.deathbanMessage = config.getString("deathban-message", "Sorry, #deathreason killed you and you're still banned for #deathbantime");
		
		// parse the spawn immortality time
		String spawnImmortalityTime = config.getString("spawn-immortality-time", "30s");
		options.spawnImmortalityTime = plugin.parseTime(spawnImmortalityTime);
		if(options.spawnImmortalityTime < 0) {
			options.spawnImmortalityTime = 30L;
		}

		// parse the spawn immortality reminder
		String spawnImmortalityReminder = config.getString("spawn-immortality-reminder", "5s");
		options.spawnImmortalityReminder = plugin.parseTime(spawnImmortalityReminder);
		if(options.spawnImmortalityReminder < 0) {
			options.spawnImmortalityReminder = 5L;
		}
		
		// get the spawn immortality message
		options.spawnImmortalityMessage = config.getString("spawn-immortality-message", "&aYou're immortal for #immortaltime!");
		
		// get the spawn mortality message
		options.spawnMortalityMessage = config.getString("spawn-mortality-message", "&cYou're no longer immortal! Good Luck!");
		
		// successful
		return true;
	}

	// create a "class" in here to store config options!
	public class ConfigOptions {
		public Integer spawnX = new Integer(0);
		public Integer spawnZ = new Integer(0);
		public Integer spawnRadius = new Integer(10000);
		public Boolean disablePVP = new Boolean(true);
		public Boolean loseInventoryOnDeath = new Boolean(true);
		public Long deathBanTime = new Long(43200);
		public Long modDeathBanTime = new Long(600);
		public Boolean thunderDeath = new Boolean(true);
		public Boolean broadcastDeath = new Boolean(true);
		public String broadcastDeathMessage = new String("&4#player was killed by #deathreason");
		public String privateDeathMessage = new String("&4You've been killed by #deathreason &4and are now death-banned for &9#deathbantime&4. We'll see you then!");
		public String deathbanMessage = new String("Sorry, #deathreason killed you and you're still banned for #deathbantime");
		public Long spawnImmortalityTime = new Long(30);
		public Long spawnImmortalityReminder = new Long(5);
		public String spawnImmortalityMessage = new String("&aYou're immortal for #immortaltime!");
		public String spawnMortalityMessage = new String("&cYou're no longer immortal! Good Luck!");
	}
}