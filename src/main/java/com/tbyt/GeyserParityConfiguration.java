package com.tbyt;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;

@ConfigSerializable
public final class GeyserParityConfiguration {
	
	@Comment("\nFor Geyser Bedrock Players, This fix adds Unbreaking 1 to a sweeping edge only book if it is found in the second slot of the Anvil."
			+ "\nDefault: true")
	private boolean sweepingEdgeBookAnvilFix = true;
	
	@Comment("\nFor Geyser ViaVersion Bedrock Players, This fixes the smithing table."
			+ "\nPreviously without this fix, a client limitation is present where a bedrock player is presented a Furnace GUI but cannot put the netherite ingot in the fuel slot."
			+ "\nDefault: false")
	private boolean viaVersionLegacySmithing = false;
	
	public boolean viaVersionLegacySmithing() {
		return viaVersionLegacySmithing;
	}

	public boolean sweepingEdgeBookAnvilFix() {
		return sweepingEdgeBookAnvilFix;
	}
}
