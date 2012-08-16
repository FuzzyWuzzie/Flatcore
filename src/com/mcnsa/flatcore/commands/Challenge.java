package com.mcnsa.flatcore.commands;

import org.bukkit.entity.Player;

import com.mcnsa.flatcore.Flatcore;
import com.mcnsa.flatcore.util.*

@CommandInfo(alias = "challenge", permission = "flatcore.challenge", usage = "[number]", description = "lists weekly challenges")
public class Challenge implements Command {
	private static Flatcore plugin = null;
	public Challenge(Flatcore instance) {
		plugin = instance;
	}
	
	@Override
	public Boolean handle(Player player, String sArgs) {
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
		if(challengeNumber < 0) {
			// TODO
			challengeNumber = 1;
		}
		
		// ok, now access the challenge given by this week
		// ?
		String challengeText = "Open an inter-dimensional rift (Nether Portal), eliminate 64 aliens (Collect 32 Ender Pearls, and 32 Gunpowder), collapse the rift (How you do this is up to you!), and go into stasis (sleep in a bed). Put a satellite in orbit to prevent any other portals forming (Build a small structure 250 blocks in the air).";
		// massage the text to eliminate multiple consequetive spaces
		challengeText = challengeText.trim().replaceAll("\\s+", " ");
		
		// and tell them about it!
		// send the header
		ColourHandler.sendMessage(player, "&a################################################");
		String weekString = "";
		if(challengeNumber < 10) {
			weekString = "0" + challengeNumber;
		}
		else {
			weekString = "" + challengeNumber;
		}
		ColourHandler.sendMessage(player, "&a########## &fFlatcore Challenge Week &7" + weekString + " ##########");
		ColourHandler.sendMessage(player, "&a################################################");
		
		// send the text
		int maxChars = 44;
		int textPos = 0;
		while(textPos < challengeText.length()) {
			// start building the line
			String line = "&a# &f";
			// get the remaining text
			String textLeft = challengeText.substring(textPos);
			// and break it into tokens
			String wordsLeft[] = textLeft.split(" ");
			
			// and add things one word at a time
			int linePos = 0;
			int wordIndex = 0;
			while(linePos < maxChars) {
				// add the word and space
				line += wordsLeft[wordIndex] + " ";
				// keep track of how much we added
				linePos += wordsLeft[wordIndex].length() + 1;
				textPos += wordsLeft[wordIndex].length() + 1;
				// increase the word index
				wordIndex++;
			}
			
			// finish the line
			line += " &a#";
			// and send it!
			ColourHandler.sendMessage(player, line);
		}
		
		// send the footer
		ColourHandler.sendMessage(player, "&a################################################");
		
		return true;
	}
}