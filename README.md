# Geyser Parity
Spigot Plugin for GeyserMC Servers that bridges the Parity between bedrock and java exclusive features.
Developing based off my personal needs. Please go ahead and fork to contribute to bring parity between the platforms.


# Parity Fixes:
Sweeping edge now works in the anvil because this fix adds unbreaking 1 to the second slot in the anvil if that slot contains a sweeping edge book.
For Geyser Bedrock Players connecting with ViaVersion, There is now an option to enable the full functionality of the Smithing Table for these players. Previously without this fix, a client limitation is present where a bedrock player is presented a Furnace GUI but cannot put the netherite ingot in the fuel slot. This fix makes that possible.


# List of Solutions that made it to Geyser:
- Fixed https://github.com/GeyserMC/Geyser/issues/1741 by adding the sweeping edge level to the lore of the item. 
- - This https://github.com/GeyserMC/Geyser/commit/7474d2c74565823842dbc251f75736bdbd4119ef fixes this issue now and implements it better than I did.
-
- -

# Disclaimer: 
This Repository used https://github.com/GeyserMC/Hurricane as a base to contribute my fixes; ie sweeping edge. But understandably, my pull request was denied for it. I recommend using this plugin as well.
