package ninuna.losttales.config.custom;

import net.neoforged.neoforge.common.ModConfigSpec;

public class LostTalesClientConfig {
    public final ModConfigSpec.BooleanValue showLostTalesHud;

    public final ModConfigSpec.BooleanValue showQuickLootHud;
    public final ModConfigSpec.BooleanValue linkShowQuickLootHud;
    public final ModConfigSpec.IntValue maxRowsPerQuickLootScreen;
    public final ModConfigSpec.IntValue quickLootHudOffsetX;
    public final ModConfigSpec.IntValue quickLootHudOffsetY;


    public final ModConfigSpec.BooleanValue showCompassHud;
    public final ModConfigSpec.BooleanValue linkShowCompassHud;

    public LostTalesClientConfig(ModConfigSpec.Builder builder) {
        this.showLostTalesHud = builder.define("showLostTalesHud", true);

        builder.push("quickLootHud");
        this.showQuickLootHud = builder.define("showQuickLootHud", true);
        this.linkShowQuickLootHud = builder.define("linkShowQuickLootHud", true);
        this.maxRowsPerQuickLootScreen = builder.defineInRange("maxRowsPerQuickLootScreen", 5, 1, 12);
        this.quickLootHudOffsetX = builder.defineInRange("quickLootHudOffsetX", 24, 0, 100);
        this.quickLootHudOffsetY = builder.defineInRange("quickLootHudOffsetY", 32, 0, 100);
        builder.pop();

        builder.push("compassHud");
        this.showCompassHud = builder.define("showCompassHud", true);
        this.linkShowCompassHud = builder.define("linkShowCompassHud", true);
        builder.pop();
    }
}
