package com.mcnsa.flatcore.managers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.mcnsa.flatcore.Flatcore;

@SuppressWarnings("unchecked")
public class PersistanceManager {
	private Flatcore plugin = null;
	
	public PersistanceManager(Flatcore instance) {
		plugin = instance;
	}
	
	public void writePersistance() {
		FileWriter outFile;
		try {
			// make sure the data folder exists
			File dataFolder = new File(plugin.getDataFolder().toString());
			if(!dataFolder.exists()) {
				dataFolder.mkdirs();
			}
			
			outFile = new FileWriter(plugin.getDataFolder().toString() + "/persist.json");
			PrintWriter out = new PrintWriter(outFile);
			
			JSONObject obj = new JSONObject();
			
			// last player damage
			obj.put("lastDamage", plugin.stateManager.lastPlayerDamage);
			
			// death bans
			obj.put("deathBans", plugin.stateManager.deathBanTimes);
			
			// immortalities
			obj.put("immortality", plugin.stateManager.immortalityTimes);
			
			// challenges
			obj.put("challenges", plugin.challengeManager.challenges);
			
			// challenges currently being edited
			obj.put("editingChallenges", plugin.challengeManager.editingChallenges);
			
			// and save it!
			out.print(obj);			
			out.close();
		} catch (IOException e) {
			plugin.error("failed to write persistance: " + e.getMessage());
		}
	}
	
	public void readPersistance() {
		try {
			// load the file
			String lineSep = System.getProperty("line.separator");
			FileInputStream fin = new FileInputStream(plugin.getDataFolder().toString() + "/persist.json");
			BufferedReader input = new BufferedReader(new InputStreamReader(fin));
			String nextLine = "";
			StringBuffer sb = new StringBuffer();
			while((nextLine = input.readLine()) != null) {
				sb.append(nextLine);
				sb.append(lineSep);
			}
			
			// and parse it!
			Map<String, Object> obj = (HashMap<String, Object>)JSONValue.parse(sb.toString());
			
			// grab the objects
			if(obj != null) {
				plugin.stateManager.lastPlayerDamage = (HashMap<String, String>)obj.get("lastDamage");
				if(plugin.stateManager.lastPlayerDamage == null) {
					plugin.stateManager.lastPlayerDamage = new HashMap<String, String>();
				}
				plugin.stateManager.deathBanTimes = (HashMap<String, Long>)obj.get("deathBans");
				if(plugin.stateManager.deathBanTimes == null) {
					plugin.stateManager.deathBanTimes = new HashMap<String, Long>();
				}
				plugin.stateManager.immortalityTimes = (HashMap<String, Long>)obj.get("immortality");
				if(plugin.stateManager.immortalityTimes == null) {
					plugin.stateManager.immortalityTimes = new HashMap<String, Long>();
				}
				plugin.challengeManager.challenges = (ArrayList<String>)obj.get("challenges");
				if(plugin.challengeManager.challenges == null) {
					plugin.challengeManager.challenges = new ArrayList<String>();
				}
				plugin.challengeManager.editingChallenges = (HashMap<String, String>)obj.get("editingChallenges");
				if(plugin.challengeManager.editingChallenges == null) {
					plugin.challengeManager.editingChallenges = new HashMap<String, String>();
				}
			}
		}
		catch(Exception e) {
			plugin.error("failed to read persistance: " + e.getMessage());
		}
	}
}