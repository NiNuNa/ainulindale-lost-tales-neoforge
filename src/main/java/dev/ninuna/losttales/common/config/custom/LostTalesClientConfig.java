package dev.ninuna.losttales.common.config.custom;

import net.neoforged.neoforge.common.ModConfigSpec;

public class LostTalesClientConfig {
    public final ModConfigSpec.BooleanValue showLostTalesHud;

    public final ModConfigSpec.BooleanValue showQuickLootHud;
    public final ModConfigSpec.BooleanValue linkShowQuickLootHud;
    public final ModConfigSpec.IntValue quickLootHudMaxRows;
    public final ModConfigSpec.IntValue quickLootHudOffsetX;
    public final ModConfigSpec.IntValue quickLootHudOffsetY;

    public final ModConfigSpec.BooleanValue showCompassHud;
    public final ModConfigSpec.BooleanValue linkShowCompassHud;
    public final ModConfigSpec.IntValue compassHudOffsetX;
    public final ModConfigSpec.IntValue compassHudOffsetY;
    public final ModConfigSpec.IntValue compassHudDisplayRadius;

    public LostTalesClientConfig(ModConfigSpec.Builder builder) {
        this.showLostTalesHud = builder.define("showLostTalesHud", true);

        builder.push("quickLootHud");
        this.showQuickLootHud = builder.define("showQuickLootHud", true);
        this.linkShowQuickLootHud = builder.define("linkShowQuickLootHud", true);
        this.quickLootHudOffsetX = builder.defineInRange("quickLootHudOffsetX", 24, 0, 100);
        this.quickLootHudOffsetY = builder.defineInRange("quickLootHudOffsetY", 32, 0, 100);
        this.quickLootHudMaxRows = builder.defineInRange("quickLootHudMaxRows", 5, 1, 12);
        builder.pop();

        builder.push("compassHud");
        this.showCompassHud = builder.define("showCompassHud", true);
        this.linkShowCompassHud = builder.define("linkShowCompassHud", true);
        this.compassHudOffsetX = builder.defineInRange("compassHudOffsetX", 50, 0, 100);
        this.compassHudOffsetY = builder.defineInRange("compassHudOffsetY", 2, 0, 100);
        this.compassHudDisplayRadius = builder.defineInRange("compassHudDisplayRadius", 90, 45, 225);
        builder.pop();
    }
}
