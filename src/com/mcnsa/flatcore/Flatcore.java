package com.mcnsa.flatcore;

import com.mcnsa.flatcore.listeners.PlayerListener;
import com.mcnsa.flatcore.managers.CommandManager;
import com.mcnsa.flatcore.managers.ConfigManager;
import com.mcnsa.flatcore.managers.PersistanceManager;
import com.mcnsa.flatcore.managers.StateManager;
import com.mcnsa.flatcore.util.*;

import java.util.Timer;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Player;

import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class Flatcore extends JavaPlugin {
	// load the minecraft logger
	Logger log = Logger.getLogger("Minecraft");

	// keep track of permissions
	public PermissionManager permissions = null;

	// keep track of configuration options
	public ConfigManager config = null;

	// and commands
	public CommandManager commandManager = null;
	
	// listeners
	public PlayerListener playerListener = null;
	
	// persistance
	public PersistanceManager persistanceManager = null;
	
	// now everything else
	public StateManager stateManager = null;
	public Timer tickerTimer = null;

	public void onEnable() {
		// set up permissions
		this.setupPermissions();
		
		// set up
		//debug("loading configuration manager..");
		config = new ConfigManager(this);
		//debug("loading command manager..");
		commandManager = new CommandManager(this);

		// load configuration
		// and save it again (for defaults)
		this.getConfig().options().copyDefaults(true);
		if(!config.load(getConfig())) {
			// shit
			// BAIL
			error("configuration failed - bailing");
			getServer().getPluginManager().disablePlugin(this);
		}
		this.saveConfig();
		
		// load stuff up
		playerListener = new PlayerListener(this);
		stateManager = new StateManager(this);
		
		// load persistance
		persistanceManager = new PersistanceManager(this);
		persistanceManager.readPersistance();
		
		// start the ticker timer
		tickerTimer = new Timer();
		tickerTimer.scheduleAtFixedRate(stateManager.new TickerTask(stateManager), 0, 1000);
		
		// routines for when the plugin gets enabled
		log("plugin enabled!");
	}

	public void onDisable() {
		// disable the timer
		tickerTimer.cancel();
		
		// save persistance
		persistanceManager.writePersistance();
		
		// shut the plugin down
		log("plugin disabled!");
	}
	
	public Logger log() {
		return log;
	}

	// for simpler logging
	public void log(String info) {
		//log.info("[Flatcore] " + info);
		ColourHandler.consoleMessage("[Flatcore] " + info);
	}

	// for error reporting
	public void error(String info) {
		//log.info("[Flatcore] <ERROR> " + info);
		ColourHandler.consoleMessage("[Flatcore] &c<ERROR> " + info);
	}

	// for debugging
	// (disable for final release)
	public void debug(String info) {
		//log.info("[Flatcore] <DEBUG> " + info);
	}

	// load the permissions plugin
	public void setupPermissions() {
		if(Bukkit.getServer().getPluginManager().isPluginEnabled("PermissionsEx")) {
			this.permissions = PermissionsEx.getPermissionManager();
			log("permissions successfully loaded!");
		}
		else {
			error("PermissionsEx not found!");
		}
	}

	// just an interface function for checking permissions
	// if permissions are down, default to OP status.
	public boolean hasPermission(Player player, String permission) {
		if(permissions != null) {
			return permissions.has(player, "flatcore." + permission);
		}
		else {
			return player.isOp();
		}
	}
	
	public String formatTime(long time) {
		long weeks = time / 604800;
		time -= (weeks * 604800);
		long days = time / 86400;
		time -= (days * 86400);
		long hours = time / 3600;
		time -= (hours * 3600);
		long minutes = time / 60;
		time -= (minutes * 60);
		long seconds = time;
		
		String str = "";
		if(weeks > 0) str += weeks + "w";
		if(days > 0) str += days + "d";
		if(hours > 0) str += hours + "h";
		if(minutes > 0) str += minutes + "m";
		if(seconds > 0) str += seconds + "s";
		if(str.length() == 0) str = "0s";
		
		return str;
	}
	
	private boolean isLong(String str) {
		try {
			Long.parseLong(str);
		}
		catch(NumberFormatException e) {
			return false;
		}
		return true;
	}
	
	public long parseTime(String time) {
		long secondsTime = -1;

		try {
			long weeks = 0;
			long days = 0;
			long hours = 0;
			long mins = 0;
			long secs = 0;

			String nums = "";
			for(int j = 0; j < time.length(); j++) {
				String c = time.substring(j, j+1);
				if(isLong(c)) {
					nums += c;
					continue;
				}

				long num = Long.parseLong(nums);
				if(c.equals("w")) weeks = num;
				else if(c.equals("d")) days = num;
				else if(c.equals("h")) hours = num;
				else if(c.equals("m")) mins = num;
				else if(c.equals("s")) secs = num;
				else throw new IllegalArgumentException("invalid time measurement: " + c);
				nums = "";
			}

			// now get the total time
			secondsTime = secs + (mins * 60) + (hours * 3600) + (days * 86400) + (weeks * 604800);
		}
		catch(Exception e) {
			error("could not parse time ('" + time + "'): " + e.getMessage());
		}
		
		return secondsTime;
	}
}