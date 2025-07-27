package ninuna.losttales.config.custom;

import net.neoforged.neoforge.common.ModConfigSpec;

public class LostTalesClientConfig {
    public final ModConfigSpec.BooleanValue showLostTalesHud;

    public final ModConfigSpec.BooleanValue showQuickLootHud;
    public final ModConfigSpec.BooleanValue toggleLostTalesHudAffectsQuickLootHud;

    public LostTalesClientConfig(ModConfigSpec.Builder builder) {
        this.showLostTalesHud = builder.define("showLostTalesHud", true);

        builder.push("quickLoot");
        this.showQuickLootHud = builder.define("showQuickLootHud", true);
        this.toggleLostTalesHudAffectsQuickLootHud = builder.define("toggleLostTalesHudAffectsQuickLootHud", true);
        builder.pop();
    }
}
