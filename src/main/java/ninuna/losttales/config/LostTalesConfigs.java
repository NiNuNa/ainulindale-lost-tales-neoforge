package ninuna.losttales.config;

import net.neoforged.neoforge.common.ModConfigSpec;
import ninuna.losttales.config.custom.LostTalesClientConfig;
import ninuna.losttales.config.custom.LostTalesCommonConfig;
import ninuna.losttales.config.custom.LostTalesServerConfig;
import org.apache.commons.lang3.tuple.Pair;

public class LostTalesConfigs {
    public static final LostTalesClientConfig CLIENT;
    public static final ModConfigSpec CLIENT_SPEC;
    public static final LostTalesCommonConfig COMMON;
    public static final ModConfigSpec COMMON_SPEC;
    public static final LostTalesServerConfig SERVER;
    public static final ModConfigSpec SERVER_SPEC;

    static {
        Pair<LostTalesServerConfig, ModConfigSpec> serverPair = new ModConfigSpec.Builder().configure(LostTalesServerConfig::new);
        SERVER = serverPair.getLeft();
        SERVER_SPEC = serverPair.getRight();

        Pair<LostTalesClientConfig, ModConfigSpec> clientPair = new ModConfigSpec.Builder().configure(LostTalesClientConfig::new);
        CLIENT = clientPair.getLeft();
        CLIENT_SPEC = clientPair.getRight();

        Pair<LostTalesCommonConfig, ModConfigSpec> commonPair = new ModConfigSpec.Builder().configure(LostTalesCommonConfig::new);
        COMMON = commonPair.getLeft();
        COMMON_SPEC = commonPair.getRight();
    }
}
