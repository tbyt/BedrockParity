[![modrinth](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/available/modrinth_64h.png)](https://modrinth.com/plugin/bedrockparity/)
# Bedrock Parity for GeyserMC
A Third-Party Bukkit Plugin for GeyserMC Servers that bridges the Parity between bedrock and java exclusive features.
Developing based off my personal needs. Please go ahead and fork to contribute to bring parity between the platforms.

If any problems arise while trying to enable plugin, please compare your config.yml file in the BedrockParity folder with [Config](https://github.com/TBYT/BedrockParity/blob/master/src/main/resources/config.yml).

# Parity Fixes:
- Sweeping edge now works in the anvil because this fix adds unbreaking 1 to the second slot in the anvil if that slot contains a sweeping edge book.
- - Minecraft Server Versions 1.11.1 - latest
- - Fixes https://github.com/GeyserMC/Geyser/issues/3661
- Bedrock Players can now see the Dragon Head Mouth Animation and the Piglin Head Ear Animation when they are powered by redstone.
- - Minecraft Server Versions 1.16.5 - 1.20.1 (with Geyser-Spigot Plugin)
- - Fixes https://github.com/GeyserMC/Geyser/issues/1764

# List of Parities Geyser implemented afterwards:
- I fixed https://github.com/GeyserMC/Geyser/issues/1741 by adding the sweeping edge level to the lore of the item. 
- - This [commit](https://github.com/GeyserMC/Geyser/commit/7474d2c74565823842dbc251f75736bdbd4119ef) fixes this issue and implements it better [than when I did.](https://github.com/TBYT/BedrockParity/blob/a76b40297fac529aa9609554caf91e88ff3bf078/src/main/java/com/tbyt/SweepingEdgeFix.java)
- I fixed https://github.com/GeyserMC/Geyser/issues/3866 and https://github.com/ViaVersion/ViaVersion/issues/3358 by having the 1st slot forwarded to the 2nd slot if it is a netherite ingot.
- - This [commit](https://github.com/GeyserMC/Geyser/commit/706d1b96270df004b8dda74f0611e28d689747ff) fixes this issue and implements it better [than when I did.](https://github.com/TBYT/BedrockParity/blob/4ed46cac4972ce4e296d60b391889912cc292f6c/src/main/java/com/tbyt/ViaVersionLegacySmithing.java)

# Disclaimer: 
This Repository used [Geyser Hurricane](https://github.com/GeyserMC/Hurricane) as a base to contribute my fixes; ie sweeping edge. But understandably, my pull request was denied for it. I recommend using this plugin as well for its collision fixes.
