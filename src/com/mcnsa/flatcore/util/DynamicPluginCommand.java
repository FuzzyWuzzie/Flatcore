package com.mcnsa.flatcore.util;

import java.util.Arrays;

import org.bukkit.command.CommandSender;

import com.mcnsa.flatcore.Flatcore;

public class DynamicPluginCommand extends org.bukkit.command.Command {
	private Flatcore plugin = null;
    public DynamicPluginCommand(String alias, String desc, String usage, Flatcore instance) {
        super(alias, desc, usage, Arrays.asList(alias));
        plugin = instance;
    }

	@Override
	public boolean execute(CommandSender sender, String label, String[] args) {
		String sArgs = "";
		for(int i = 0; i < args.length; i++) {
			sArgs += " " + args[i];
		}
		return plugin.commandManager.handleCommand(sender, "/" + label + sArgs);
	}
}