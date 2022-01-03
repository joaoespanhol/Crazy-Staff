## Version Support
Only support 1.13 and above, I do ASK that you update to 1.18 as old versions are just not optimal to play in.

## Want to contribute?
Create a fork of StaffX and submit a pull request ( Please review your code, Any pull request in Java will be closed but I'll merge it manually in kotlin)

## Description
Spent quite a bit of my time looking around on Mc-Market/Spigot which were either not updated, updated but not what I wanted, updated but just terrible. I decided that enough was enough and wrote a simplistic version in Kotlin. 

## Features

- Random Teleport Menu bound to Random Tool, It only works for Online Players at the moment.
- A special effect you get when turning on/off Vanish, Toggled off by default in the config.json.
- A freeze tool that will keep players frozen past restart.
- Commands to run if a player logs out while frozen, Can be disabled/enabled in the config.json
- An endless ( up to the max on the hotbar ) configurable list of items that can run commands, Can be toggled off in the config.json.
- Saving your location on Staff Enter to teleport you back when you leave /Staff ( Inventory saving next )
- Hook into EssentialsX to use their vanish instead of my own, Mine is really just a fallback system if you want it vanilla
- Join/Quit messages are empty if in Vanish.
- /staff setspawn - Sets the location for where freezed players will go.
- Set slot items to 100 for pre-defined items to disable them.
- Using paper lib to enable extra paper listeners like denying Mob Targeting for both Frozen & Staff
- Can toggle off metrics in the config.json
- Chat messages instead of Title messages in case you aren't using Paper, It will auto change regardless of the value of sendTitles likewise you can set it to false if you prefer the old messages.
- GUI Customization of the Random Teleport Menu
- Everything locale wise is customizable.

## Support
Create an issue, Supply an error log / description of how to reproduce ( including high mspt issues )

## Usage
Everything is configurable if you open config.json

## Suggestions
Create an issue and follow this format below.

What is your suggestion? (goes here)

## Roadmap
Saving your inventory and re-adding your inventory when you go back to your saved location.
Adding booleans for Titles to switch to messages or to have no messages at all.
Particle effects and booleans when frozen.
