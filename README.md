# Flatcore
A plugin for implementing all the various things necessary for flatcore

## Functionality
* Whenever a player spawns (either for first time, or after a death), they're placed in a random location (configurable)
* Beds only skip night - they don't have anything to do with respawning
* When a player dies, their entire inventory is destroyed (configurable)
* When a player dies, they are death-banned for 12h (configurable)
* When a player dies, custom messages are sent to the player, other players, and the console informing them about how their died and how long they're death-banned for
* When a player tries to join when death-banned, they will be rejected and told how long until they can rejoin
* For 30s after respawn (configurable), players have immortality