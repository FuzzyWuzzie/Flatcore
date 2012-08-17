# Flatcore
A plugin for implementing all the various things necessary for flatcore

## Functionality
* Whenever a player spawns (either for first time, or after a death), they're placed in a random location (configurable)
* Beds only skip night - they don't have anything to do with respawning
* When a player dies, their entire inventory is destroyed (configurable)
* When a player dies, they are death-banned for 12h (configurable)
* When a player dies, custom messages are sent to the player, other players, and the console informing them about how their died and how long they're death-banned for
* When a player tries to join when death-banned, they will be rejected and told how long until they can rejoin
* For 30s after respawn (configurable), players have immortality - they cannot be damaged in any way, and mobs will not target them
* When a mob dies, if it wasn't killed by a player, it doesn't drop any loot
* Players, mods, and admins all have different death-ban times (admins cannot be death-banned)
* Players can read the weekly challenges in-game (both previous and current challenges)
* Mods can set the weekly challenges in-game (both previous and current challenges)
* The entire state of the plugin saves itself across /reloads, so /reloads are invisible to users

## Commands
* `/flathelp [page]` - lists all the "flatcore plugin" commands you have access to, and what they do
* `/challenge [id]` - tells you what the current weekly challenge is if you omit the [id], or tells you the weekly challenge for your specified id
* `/deathban <player> <time>` - set's a player's deathban. If they're not currently death-banned, it will death-ban them for a given time. Time in XwXdXhXmXs format
* `/startchallenge [id]` - starts you writing a weekly challenge ([id] is optional, if excluded a new challenge will be inserted). All chat you send to the server after sending this command will be appended to the challenge
* `/stopchallenge` - stops writing a weekly challenge. All the chat text you've been accumulating in the challenge thing gets collected, and inserted as either an edited or new weekly challenge based on what ID you were editing

## Permissions
* flatcore.admin - cannot be deathbanned
* flatcore.mod - has a modified deathban time
* flatcore.help - can use the `/flathelp` command
* flatcore.challenge - can use the `/challenge` command
* flatcore.setchallenge - can use the `/startchallenge` and `/stopchallenge` commands
* flatcore.deathban - can use the `/deathban` command

## Config
	# define the center of spawn
	spawn-center-x: 0
	spawn-center-z: 0
	# define the spawn radius (in a square)
	spawn-radius: 10000
	# prevent pvp?
	disable-pvp: true
	# does the player's inventory get destroyed when they die?
	lose-inventory-on-death: true
	# how long normal players are death-banned for
	death-ban-time: 12h
	# how long mods are death-banned for
	mod-death-ban-time: 5m
	# should lightning strike where a player dies?
	thunder-death: true
	# should the server be made aware of their failure?
	broadcast-death: true
	# What to say to the server when someone dies
	broadcast-death-message: "&4#player was killed by #deathreason and is death-banned for &9#deathbantime"
	# What to say to the person who dies
	private-death-message: "&4You've been killed by #deathreason &4and are now death-banned for &9#deathbantime&4. We'll see you then!"
	# What to say when someone tries to join the server but is still deathbanned
	deathban-message: "Sorry, #deathreason killed you and you're still banned for #deathbantime"
	# How long players are immortal for after spawning
	spawn-immortality-time: 30s
	# How often to remind players of how much immortality they have left
	spawn-immortality-reminder: 5s
	# The message that gets sent to a player when they're immortal
	spawn-immortality-message: "&aYou're immortal for #immortaltime!"
	# The message the gets sent to a player when they become mortal
	spawn-mortality-message: "&cYou''re no longer immortal! Good Luck!"
