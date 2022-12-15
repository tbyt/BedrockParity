package com.tbyt;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;

@ConfigSerializable
@SuppressWarnings("FieldMayBeFinal")
public final class GeyserParityConfiguration {
	@Comment("Fixes Bedrock players being unable to see the enchant description 'sweeping edge' of any item.\n"
			+ "For Bedrock Players, this adds the sweeping edge level to the lore of the item. "
			+ "Also sweeping edge now works in the anvil because this fix adds unbreaking 1 to the 2nd slot in the anvil if that slot contains a sweeping edge only book.")
	private boolean sweepingEdgeFix = true;
	
	@Comment("\nCosmetically display banners on shields?")
	private boolean bannerOnShieldFix = true;

	public boolean sweepingEdgeFix() {
		return sweepingEdgeFix;
	}
	
	public boolean bannerOnShieldFix() {
		return bannerOnShieldFix;
	}
}
