package geyserparity;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;

@ConfigSerializable
@SuppressWarnings("FieldMayBeFinal")
public final class GeyserParityConfiguration {
    @Comment("Fixes Bedrock players being unable to use any item with the sweeping edge enchant.\n"
    		+ "For Bedrock Players, this adds Unbreaking i enchant to item if no other enchants, and will change the name of the item to reflect the level of sweeping edge.\n" +
            "This option should be relatively safe but does modify server behavior. Geyser or Floodgate must be installed, as well as a recent server version.")
    private boolean sweepingEdgeFix = true;
    
    public boolean sweepingEdgeFix() {
        return sweepingEdgeFix;
    }
}
