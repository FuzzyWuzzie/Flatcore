# Flatcore
A plugin for implementing all the various things necessary for flatcore

## Functionality
* Whenever a player spawns (either for first time, or after a death), they're placed in a random location (configurable)
* Beds only skip night - they don't have anything to do with respawning
* When a player dies, their entire inventory is destroyed (configurable)
* When a player dies, they are death-banned for 12h (configurable)
* When a player dies, custom messages are sent to the player, other players, and the console informing them about how their died
* When a player tries to join when death-banned, they will be rejected and told how long until they can rejoin
* For 30s after respawn (configurable), players have immortality - they cannot be damaged in any way, and mobs will not target them
* When a mob dies, if it wasn't killed by a player, it doesn't drop any loot
* Players, mods, and admins all have different death-ban times (admins cannot be death-banned)
* Players can read the weekly challenges in-game (both previous and current challenges)
* Mods can set the weekly challenges in-game (both previous and current challenges)
* The entire state of the plugin saves itself across /reloads, so /reloads are invisible to users
* Console can use all the commands
* Plugin configuration can be reloaded with a command
* Challenge presentation template is configurable

## Commands
* `/flathelp [page]` - lists all the "flatcore plugin" commands you have access to, and what they do
* `/challenge [id]` - tells you what the current weekly challenge is if you omit the [id], or tells you the weekly challenge for your specified id
* `/deathban <player> <time>` - set's a player's deathban. If they're not currently death-banned, it will death-ban them for a given time. Time in XwXdXhXmXs format
* `/startchallenge [id]` - starts you writing a weekly challenge ([id] is optional, if excluded a new challenge will be inserted). All chat you send to the server after sending this command will be appended to the challenge
* `/appendchallenge <text>` - explicitely adds text to the currently edited challenge (implemented for console use)
* `/stopchallenge` - stops writing a weekly challenge. All the chat text you've been accumulating in the challenge thing gets collected, and inserted as either an edited or new weekly challenge based on what ID you were editing
* `/flatreload` - reloads the flatcore config file

## Permissions
* flatcore.admin - cannot be deathbanned
* flatcore.mod - has a modified deathban time
* flatcore.help - can use the `/flathelp` command
* flatcore.challenge - can use the `/challenge` command
* flatcore.setchallenge - can use the `/startchallenge`, `/appendchallenge`, and `/stopchallenge` commands
* flatcore.deathban - can use the `/deathban` command
* flatcore.reload - can use the `/flatreload` command

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
	# how long normal players are death-banned for. Time in XwXdXhXmXs format
	death-ban-time: 12h
	# how long mods are death-banned for. Time in XwXdXhXmXs format
	mod-death-ban-time: 5m
	# should lightning strike where a player dies?
	thunder-death: true
	# should the server be made aware of their failure?
	broadcast-death: true
	# What to say to the person who dies
	private-death-message: "&4You've been killed by #deathreason &4and are now death-banned for &9#deathbantime&4. We'll see you then!"
	# What to say when someone tries to join the server but is still deathbanned
	deathban-message: "Sorry, #deathreason killed you and you're still banned for #deathbantime"
	# How long players are immortal for after spawning. Time in XwXdXhXmXs format
	spawn-immortality-time: 30s
	# How often to remind players of how much immortality they have left. Time in XwXdXhXmXs format
	spawn-immortality-reminder: 5s
	# The message that gets sent to a player when they're immortal
	spawn-immortality-message: "&aYou're immortal for #immortaltime!"
	# The message the gets sent to a player when they become mortal
	spawn-mortality-message: "&cYou''re no longer immortal! Good Luck!"
	# templates for the /challenge text
	challenge-header-template: |
	  &nFlatcore Challenge Week #week
	challenge-line-template: "&a> &f#text"
	challenge-footer-template: ""
	# the various death messages, a random one from each list will be picked when a player dies
	# Must contain at least 1 line. If there are more, it will appear randomly when a person dies.
	# %n for player who died
	# %a name of player who attacked in pvp deaths
	# %i for item a player was using to kill someone else
	death-messages:
	  block_explosion:
		- "&3%n&c was last seen playing with dynamite."
		- "&3%n&c blew the fuck up."
		- "&3%n&c cut the wrong wire."
		- "&3%n&c just lost a game of Minesweeper."
	  entity-explosion:
		- "&3%n&c blew the fuck up."
	  cavespider:
		- "&cCave Johnson here, &3%n&c."
		- "&3%n&c just took a small eight-legged beating."
		- "&3%n&c found out that following a spider's web isn't the best idea."
	  contact:
		- "&c1 &3%n&c 1 cactus."
		- "&3%n&c poked a cactus, but the cactus poked back."
		- "&3%n&c sucks at lying on a bed of nails."
	  creeper:
		- "&cKch-tsssssssssssss. &3%n&c never saw it coming."
		- "&3%n&c hugged a creeper, and is now a convicted felon."
		- "&cCreeper used SELFDESTRUCT! &3%n&c fainted!"
	  drowning:
		- "&3%n&c forgot about air."
		- "&3%n&c is swimming with the fishes."
		- "&3%n&c can't find the fucking spacebar."
		- "&3%n&c met the Kraken."
		- "&cOcean: 1. &3%n&c: 0."
		- "&3%n&c doesn't have gills"
	  enderman:
		- "&cAn enderman borrowed &3%n&c's head."
		- "&3%n&c looked at an Enderman the wrong way."
	  fall:
		- "&3%n&c took a leap of faith for nsanidy."
		- "&3%n&c went over 9000."
		- "&3%n&c believed they could fly."
		- "&cI believe I can fly! - No you can not, &3%n&c!"
		- "&3%n&c is not thinking with portals."
		- "&3%n&c just left a human-shaped crater in the countryside."
		- "&cNewton's Laws were not kind to &3%n&c."
	  fire:
		- "&3%n&c didn't start the fire, but couldn't stop it either."
		- "&3%n&c will be baked, and then there will be cake."
		- "&cToday's special: &3%n&c, extra crispy!"
		- "&3%n&c exited this mortal coil via self immolation."
		- "&cGo die in a fire, &3%n&c. Oh wait."
	  fire_tick:
		- "&3%n&c didn't start the fire, but couldn't stop it either."
		- "&3%n&c will be baked, and then there will be cake."
		- "&cToday's special: &3%n&c, extra crispy!"
		- "&3%n&c exited this mortal coil via self immolation."
		- "&cGo die in a fire, &3%n&c. Oh wait."
	  ghast:
		- "&3%n&c died in the loving tentacles of a screaming ghast."
		- "&cWhy did &3%n&c have to die? Oh, right, ghast fireball."
		- "&cDon't mourn &3%n&c's death. It was for the good of ghastkind."
		- "&cPoor &3%n&c... that ghast only wanted to play ping pong with you."
		- "&cThose aren't babies you hear, &3%n&c!"
	  giant:
		- "&3%n&c got curbstomped!"
		- "&3%n&c met a member of the band 'They are Definitely Giants.'"
		- "&3%n&c shouldn't have climbed that beanstalk."
		- "&3%n&c made their new home under the foot of a giant."
		- "&3%n&c met my little friend, Timmy."
	  lava:
		- "&3%n&c became obsidian."
		- "&3%n&c lost all of their items!"
		- "&cThat's not tomato juice, &3%n&c!"
		- "&3%n&c took a dip in the wrong kind of pool!"
		- "&3%n&c found out how to encase himself in carbonite."
		- "&3%n&c played The Floor is Lava. Literally."
	  lightning:
		- "&3%n&c had an unfortunate weather incident."
		- "&3%n&c heard the thunder too late."
		- "&3%n&c probably pissed the mods off."
		- "&3%n&c probably didn't piss off the mods at all."
		- "&cThey say lightning never strikes in the same place twice... it didn't need to for &3%n&c."
		- "&cThere's a good chance &3%n&c met Sirenfal."
	  pigzombie:
		- "&3%n&c took a golden pork sword to the sphincter."
		- "&3%n&c played six degrees of Zombie Bacon."
		- "&3%n&c tried to steal the pork. Don't do that."
		- "&3%n&c seems to have encountered the result of an Ion Cannon applied to a helpless pig."
	  pvp:
		- "&3%a&c killed &3%n&c wielding &3%i&c, do not fuck with him."
		- "&3%a&c wanted to know how sharp their &3%i&c was... so they tested it on &3%n&c."
		- "&3%a&c wanted to tell &3%n&c about forums.mcnsa.com."
		- "&3%a&c put a &3%i&c to &3%n&c's head."
		- "&cThat PVP to &3%n&c better have been mutual, &3%a&c."
	  pvp-fists:
		- "&3%a&c pummeled &3%n&c to death."
		- "&3%a&c crusted &3%n&c with their bare hands."
	  pvp-tamed:
		- "&3%n&c was mauled by &3%a&c's &3%i&c."
		- "&3%n&c's hand was bitten by &3%a&c's &3%i&c."
	  silverfish:
		- "&3%n&c found something hidden below a rock."
		- "&3%n&c, you can't stuff that many fish into your mouth!"
		- "&3%n&c was sodomized by gray insects."
		- "&3%n&c's last words 'Oh god they're coming out of the walls!'"
	  skeleton:
		- "&3%n&c just got boned by a skeleton."
		- "&3%n&c caught an arrow with their face."
		- "&3%n&c took an arrow to the knee. Skeletonry ensued."
		- "&cKnow who is a skeleton's pincushion now? &3%n&c."
		- "&cI know, &3%n&c, who gives a skeleton a bow anyway?"
	  slime:
		- "&3%n&c played with slime and is now all gooey."
		- "&cThat slime has your balls right here, &3%n&c."
		- "&3%n&c is all sticky and naked."
		- "&3%n&c saw this green mushy cushion. Well it was green and mushy..."
	  spider:
		- "&3%n&c failed to kill it with fire. It being that large spider."
		- "&3%n&c found out that the 'she' monster's lair is a bad place."
		- "&3%n&c forgot that spiders climb walls."
		- "&3%n&c just took an eight-legged beating."
	  starvation:
		- "&3%n&c could have used a royale with cheese."
		- "&3%n&c should have packed a lunch."
		- "&cYou can't eat grass and live, &3%n&c."
	  suffocation:
		- "&3%n&c took a swim in a cement lake."
		- "&3%n&c lost all their stuff in a wall."
		- "&3%n&c discovered solids aren't breathable."
		- "&3%n&c choked on a gravel sandwich."
	  suicide:
		- "&3%n&c took matters into his own hands."
	  unknown:
		- "&cWe don't know why &3%n&c died. Honest. No idea."
		- "&3%n&c asked if they could join Avolition."
		- "&3%n&c was vaporized."
		- "&3%n&c probably asked to be promoted."
		- "&3%n&c died from explosive diarrhea."
		- "&3%n&c was hit by a falling piano."
	  void:
		- "&3%n&c fell into the Gap."
		- "&cHey, where did the world go, &3%n&c?"
		- "&3%n&c found purgatory."
		- "&cAchievement Unlocked. &3%n&c somehow fell through the map."
	  wolf:
		- "&3%n&c pissed off the wrong puppy."
		- "&cThere's only so much poking with sticks that a wolf will take, &3%n&c."
		- "&cPetting and heavy petting are different, and &3%n&c picked the wrong one."
		- "&3%n&c told the wolf to sit. Didn't work out so well now did it, &3%n&c."
		- "&3%n&c let the dogs out."
		- "&3%n&c must have played with too many pigs before punching that wolf."
	  zombie:
		- "&3%n&c just got their face ripped off by a Zombie. Well, it's an improvement."
		- "&3%n&c just got killed by a Zombie. Well, that's a no-brainer."
		- "&3%n&c doesn't watch enough Zombie films."
		- "&3%n&c will be in the barn for the whole fucking season too."
	  blaze:
		- "&3%n&c couldn't get the blaze under control."
		- "&3%n&c was airbombed!"
		- "&3%n&c nope, that wasn't a rocket."
	  magmacube:
		- "&3%n&c didn't expect this kind of slinky!"
		- "&3%n&c is all sticky and burning."
	  enderdragon:
		- "&3%n&c died at the end... IN the end."
		- "&3%n&c, looking up would have helped."
		- "&cNo egg for you, &3%n&c."
		- "&3%n&c will never get to read that end poem!"
		- "&3%n&c made a generous donation to the Ender Dragon's Club."
	  dispenser:
		- "&3%n&c did not need a dispenser there."
		- "&3%n&c found out that the machines are learning..."
		- "&3%n&c did not follow proper Aperture Science procedures."
		- "&3%n&c thinks he is Indiana Jones."
	  poison:
		- "&cThat one time when &3%n&c should have spit not swallowed."
		- "&cThere was a reason the bottle had a skull on it, &3%n&c."
	  magic:
		- "&3%n&c felt the force."
		- "&3%n&c should ask Rincewind the next time."