**This is a simple plugin which can remove or replace items dropped by mobs.** It was originally created for use with Cauldron servers and mods that replace vanilla items with modded ones (on some versions of Cauldron this will result in both the modded and vanilla drop rather than a replacement - for example cows dropping both raw hides and leather when only hides are intended). However, this could be used on other types of servers to removal of a certain mob drop is desired. Due to a bug it does not work on flying mobs.

This plugin is back, and should now be fixed.

Changes include:

* commands have been added so that you no longer have to edit the config by hand

* It will now edit drops for any entiry except a player or vehicle (this allow it to work with modded on Cauldron mobs, though non-player living entities would be better for vanilla orient Craftbukkit / Spigot servers). 

As far as I know, everything works now - no deleting player drops, no crashing when reading the config, etc. Being a small, simple plugin I don't think much else can go wrong (or needs to be added).

http://dev.bukkit.org/bukkit-plugins/mob-drop-stopper/

MobDropStopper by is now licensed under a Creative Commons Attribution 4.0 International License: http://creativecommons.org/licenses/by/4.0/


