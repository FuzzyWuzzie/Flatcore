package com.mcnsa.flatcore.managers;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.entity.Player;

import com.mcnsa.flatcore.commands.*;
import com.mcnsa.flatcore.util.ColourHandler;
import com.mcnsa.flatcore.util.Command;
import com.mcnsa.flatcore.util.CommandInfo;
import com.mcnsa.flatcore.util.DynamicPluginCommand;
import com.mcnsa.flatcore.util.ReflectionUtil;

import com.mcnsa.flatcore.Flatcore;

public class CommandManager {
	// keep track of the plugin
	public Flatcore plugin = null;

	// and the commands
	public HashMap<String, InternalCommand> commands = new HashMap<String, InternalCommand>();

	public CommandManager(Flatcore instance) {
		plugin = instance;		
		registerCommand(new Challenge(plugin));
		registerCommand(new DeathBan(plugin));
		registerCommand(new StartChallenge(plugin));
		registerCommand(new AppendChallenge(plugin));
		registerCommand(new StopChallenge(plugin));
		registerCommand(new Help(plugin));
		registerCommand(new Reload(plugin));
	}
	
	// get bukkit's command map
	public CommandMap getCommandMap() {
		CommandMap commandMap = ReflectionUtil.getField(plugin.getServer().getPluginManager(), "commandMap");
		if (commandMap == null) {
			plugin.error("Could not retrieve server CommandMap! Console cannot use commands!");
			commandMap = new SimpleCommandMap(Bukkit.getServer());
		}
		return commandMap;
	}

	// register new command
	public void registerCommand(Command command) {
		// get the class
		Class<? extends Command> cls = command.getClass();
		//plugin.debug("registering command: " + cls.getSimpleName());
		
		// get the class's annotations
		Annotation[] annotations = cls.getAnnotations();
		for(int i = 0; i < annotations.length; i++) {
			if(annotations[i] instanceof CommandInfo) {
				// we found our annotation!
				CommandInfo ci = (CommandInfo)annotations[i];
				
				// create the internal command
				InternalCommand ic = new InternalCommand(ci.alias(), ci.permission(), ci.usage(), ci.description(), ci.visible(), command);
				commands.put(ci.alias(), ic);
				
				// prepare to register bukkit commands
				DynamicPluginCommand cmd = new DynamicPluginCommand(ci.alias(), ci.usage(), ci.description(), plugin);
				// get the command map
				CommandMap commandMap = getCommandMap();
				// and register the bukkit command
				commandMap.register(ci.alias(), cmd);
				
				// we're done
				return;
			}
		}
	}
	
	// handle commands
	public Boolean handleCommand(CommandSender sender, String command) {
		// get the actual command
		//plugin.debug(player.getName() + " sent command: " + command);
		
		// strip off the proceeding "/"
		command = command.substring(1);
		
		// tokenize it
		String[] tokens = command.split("\\s");
		
		// get the command
		if(tokens.length < 1) {
			// we're not handling it
			return false;
		}
		tokens[0] = tokens[0].toLowerCase();
		
		// find the command
		if(!commands.containsKey(tokens[0])) {
			// we're not handling it
			//plugin.debug("not handling command: " + tokens[0]);
			return false;
		}
		
		// make sure they have permission first
		// they definitely have permission on the console
		if(!(sender instanceof ConsoleCommandSender)) {
			if(!commands.get(tokens[0]).permissions.equals("") && !plugin.hasPermission((Player)sender, commands.get(tokens[0]).permissions)) {
				// return a message if they don't have permission
				plugin.log(((Player)sender).getName() + " attempted to use command: " + tokens[0] + " without permission!");
				ColourHandler.sendMessage((Player)sender, "&cYou don't have permission to do that!");
				// we handled it, but they don't have perms
				return true;
			}
		}
		
		// we have the command, send it in!
		String sArgs = new String("");
		//plugin.debug("handling command: " + tokens[0]);
		// make sure we have args
		if(command.length() > (1 + tokens[0].length())) {
			// substring out the args
			sArgs = command.substring(1 + tokens[0].length());
			//plugin.debug("with arguments: " + sArgs);
		}
		
		// and handle the command!
		if(commands.get(tokens[0]).command.handle(sender, sArgs.trim())) {
			// we handled it!
			//plugin.debug("command " + tokens[0] + " handled successfully!");
			return true;
		}
		
		// they didn't use it properly! let them know!
		//plugin.debug("command " + tokens[0] + " NOT handled successfully");
		ColourHandler.sendMessage(sender, "&cInvalid usage! &aCorrect usage: &3/" + commands.get(tokens[0]).alias + " &b" + commands.get(tokens[0]).usage + " &7(" + commands.get(tokens[0]).description + ")");
		return true;
	}
	
	// return a sorted list of commands
	public InternalCommand[] listCommands() {
		// count the number of invisible commands
		//plugin.debug("counting number of invisible commands");
		int numInvisible = 0;
		for(String cmd: commands.keySet()) {
			if(!commands.get(cmd).visible) {
				numInvisible++;
			}
		}
		
		// create the list
		//plugin.debug("creating command list array");
		InternalCommand[] cList = new InternalCommand[commands.size() - numInvisible];
		
		// get them all!
		int i = 0;
		//plugin.debug("getting all visible commands");
		for(String cmd: commands.keySet()) {
			// add only the visible ones!
			if(commands.get(cmd).visible) {
				cList[i] = commands.get(cmd);
				i += 1;
			}
		}
		
		// sort the array
		//plugin.debug("sorting command list");
		Arrays.sort(cList, new CommandComp());
		
		// and return!
		return cList;
	}

	public class InternalCommand {
		public String alias = new String("");
		public String permissions = new String("");
		public String usage = new String("");
		public String description = new String("");
		public Boolean visible = new Boolean(true);
		public Command command = null;
	
		public InternalCommand(String _alias, String _perms, String _usage, String _desc, boolean _visible, Command _command) {
			alias = _alias;
			permissions = _perms;
			usage = _usage;
			description = _desc;
			visible = _visible;
			command = _command;
		}
	}
	
	class CommandComp implements Comparator<InternalCommand> {
		public int compare(InternalCommand a, InternalCommand b) {
			return a.alias.compareTo(b.alias);
		}
	}
}