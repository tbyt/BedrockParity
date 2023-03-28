package com.tbyt;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;

@ConfigSerializable
public final class GeyserParityConfiguration {
	
	@Comment("\nFor Bedrock Players, This fix adds unbreaking 1 to a sweeping edge only book if it is found in the second slot of the anvil."
			+ "\nSweeping Edge Lore setting must be true for this to work.")
	private boolean sweepingEdgeBookAnvilFix = true;
	
	public boolean sweepingEdgeBookAnvilFix() {
		return sweepingEdgeBookAnvilFix;
	}
}
