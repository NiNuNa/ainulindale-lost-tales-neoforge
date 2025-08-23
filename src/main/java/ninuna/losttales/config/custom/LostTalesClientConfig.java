package ninuna.losttales.config.custom;

import net.neoforged.neoforge.common.ModConfigSpec;

public class LostTalesClientConfig {
    public final ModConfigSpec.BooleanValue showLostTalesHud;

    public final ModConfigSpec.BooleanValue showQuickLootHud;
    public final ModConfigSpec.BooleanValue linkShowQuickLootHud;

    public LostTalesClientConfig(ModConfigSpec.Builder builder) {
        this.showLostTalesHud = builder.define("showLostTalesHud", true);

        builder.push("quickLootHud");
        this.showQuickLootHud = builder.define("showQuickLootHud", true);
        this.linkShowQuickLootHud = builder.define("linkShowQuickLootHud", true);
        builder.pop();
    }
}
