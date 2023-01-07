package com.tbyt;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;

@ConfigSerializable
public final class GeyserParityConfiguration {
	@Comment("\nFixes Bedrock players being unable to see the enchant description 'sweeping edge' of any item.\n"
			+ "For Bedrock Players, this adds the sweeping edge level to the lore of the item."
			+ "\nThis setting does not add any enchants.")
	private boolean sweepingEdgeLore = true;
	
	@Comment("\nFor Bedrock Players, This fix adds unbreaking 1 to a sweeping edge only book if it is found in the second slot of the anvil."
			+ "\nSweeping Edge Lore setting must be true for this to work.")
	private boolean sweepingEdgeAnvilBookFix = true;
	
	//private boolean bannerDetachFromShield = true;

	public boolean sweepingEdgeLore() {
		return sweepingEdgeLore;
	}
	
	public boolean sweepingEdgeAnvilBookFix() {
		return sweepingEdgeAnvilBookFix;
	}
	
	/*
	 * public boolean bannerDetachFromShield() { return bannerDetachFromShield; }
	 */
}
