package com.mcnsa.flatcore.managers;

import java.util.ArrayList;
import java.util.HashMap;

import com.mcnsa.flatcore.Flatcore;

public class ChallengeManager {
	public ArrayList<String> challenges = new ArrayList<String>();
	public HashMap<String, String> editingChallenges = new HashMap<String, String>();
	
	Flatcore plugin = null;
	public ChallengeManager(Flatcore instance) {
		plugin = instance;
	}
	
	// figure out how many challenges we actually have
	public Integer latestChallengeNumber() {
		return challenges.size();
	}
	
	// get the actual challenge text
	public String getChallenge(Integer id) {
		// let them know if there aren't any challenges yet
		if(challenges.size() < 1) {
			return "Woops, there are no challenges yet!";
		}
		// make sure we have an appropriate ID
		id--;
		if(id < 0 || id >= challenges.size()) {
			return "Woops, that challenge doesn't exist!";
		}
		return challenges.get(id);
	}
	
	// start editing a challenge
	public Boolean startChallenge(String player, Integer id) {
		// make sure we have a valid id
		if(id < 1) {
			return false;
		}
		// add it to the list
		editingChallenges.put(player, (id-1) + ":");
		return true;
	}
	
	public Boolean isEditingChallenge(String player) {
		return editingChallenges.containsKey(player);
	}
	
	public void appendChallenge(String player, String text) {
		// make sure we're actually editing something
		if(!isEditingChallenge(player)) {
			return;
		}
		
		// ok, append it
		editingChallenges.put(player, editingChallenges.get(player) + text + " ");
	}
	
	// stop editing a challenge
	public Integer stopChallenge(String player) {
		// make sure we're actually editing something
		if(!isEditingChallenge(player)) {
			return -1;
		}
		
		// strip out the id and ":" at the beginning
		String[] parts = editingChallenges.get(player).split(":", 2);
		if(parts.length != 2) {
			// stop editing
			editingChallenges.remove(player);
			return -2;
		}

		// grab the id
		int id = -1;
		try {
			id = Integer.parseInt(parts[0]);
		}
		catch(Exception e) {
			id = challenges.size();
		}
		
		// ok, now save it
		if(id >= challenges.size()) {
			// we're inserting
			challenges.add(parts[1]);
		}
		else {
			// we're updating
			challenges.set(id, parts[1]);
		}
		
		// stop editing
		editingChallenges.remove(player);
		
		return id;
	}
}
