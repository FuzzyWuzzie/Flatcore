package com.mcnsa.flatcore.commands;

import org.bukkit.command.CommandSender;

import com.mcnsa.flatcore.Flatcore;
import com.mcnsa.flatcore.util.*;

@CommandInfo(alias = "challenge", permission = "challenge", usage = "[number]", description = "lists weekly challenges")
public class Challenge implements Command {
	private static Flatcore plugin = null;
	public Challenge(Flatcore instance) {
		plugin = instance;
	}
	
	@Override
	public Boolean handle(CommandSender player, String sArgs) {
		// store the challenge number that we want to access
		Integer challengeNumber = -1;
	
		// check to see if they gave any arguments
		if(sArgs.length() > 0) {
			// they did, they did!
			// attempt to parse it into a number!
			try {
				challengeNumber = Integer.parseInt(sArgs);
			}
			catch(Exception e) {
				// uh-oh.. not a number!
				return false;
			}
		}
		
		// figure out the latest week number if necessary
		if(challengeNumber < 1) {
			challengeNumber = plugin.challengeManager.latestChallengeNumber();
		}
		
		// ok, now access the challenge given by this week
		String challengeText = plugin.challengeManager.getChallenge(challengeNumber);
		// massage the text to eliminate multiple consequetive spaces
		challengeText = challengeText.trim().replaceAll("\\s+", " ");
		
		// and tell them about it!
		// format the week number
		String weekString = "";
		if(challengeNumber < 10) {
			weekString = "0" + challengeNumber;
		}
		else {
			weekString = "" + challengeNumber;
		}
		// send the header
		// split it into lines though
		String[] headerLines = plugin.config.options.challengeHeaderTemplate.split("\n");
		for(int i = 0; i < headerLines.length; i++) {
			ColourHandler.sendMessage(player, headerLines[i].replaceAll("#week", weekString));
		}
		
		// send the text
		int maxChars = 50;
		int textPos = 0;
		while(textPos < challengeText.length()) {
			// start building the line
			String line = "";
			// get the remaining text
			String textLeft = challengeText.substring(textPos);
			// and break it into tokens
			String wordsLeft[] = textLeft.split(" ");
			
			// and add things one word at a time
			int linePos = 0;
			int wordIndex = 0;
			while(linePos < maxChars && wordIndex < wordsLeft.length) {
				// add the word and space
				line += wordsLeft[wordIndex] + " ";
				// keep track of how much we added
				linePos += wordsLeft[wordIndex].length() + 1;
				textPos += wordsLeft[wordIndex].length() + 1;
				// increase the word index
				wordIndex++;
			}
			
			// finish the line
			// and send it!
			ColourHandler.sendMessage(player, plugin.config.options.challengeLineTemplate.replaceAll("#text", line));
		}
		
		// send the footer
		// split it into lines though
		String[] footerLines = plugin.config.options.challengeFooterTemplate.split("\n");
		for(int i = 0; i < headerLines.length; i++) {
			ColourHandler.sendMessage(player, footerLines[i].replaceAll("#week", weekString));
		}
		
		return true;
	}
}