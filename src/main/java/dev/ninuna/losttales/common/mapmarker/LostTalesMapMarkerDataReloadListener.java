package dev.ninuna.losttales.common.mapmarker;

import com.mojang.serialization.Codec;
import dev.ninuna.losttales.common.LostTales;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class LostTalesMapMarkerDataReloadListener extends SimpleJsonResourceReloadListener<LostTalesMapMarkerData> {
    public static final Map<ResourceLocation, LostTalesMapMarkerData> RAW_FILES = new ConcurrentHashMap<>();
    public static final Map<ResourceLocation, List<LostTalesMapMarkerData.Entry>> RAW_ENTRIES = new ConcurrentHashMap<>();
    public static final Map<ResourceLocation, List<LostTalesMapMarkerData.Entry>> BY_DIMENSION = new ConcurrentHashMap<>();

    public LostTalesMapMarkerDataReloadListener(Codec<LostTalesMapMarkerData> codec, FileToIdConverter fileToId) {
        super(codec, fileToId);
    }

    @Override
    public void apply(Map<ResourceLocation, LostTalesMapMarkerData> resources, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        RAW_FILES.clear();
        RAW_ENTRIES.clear();
        BY_DIMENSION.clear();

        RAW_FILES.putAll(resources);
        for (var entry : resources.entrySet()) {
            RAW_ENTRIES.put(entry.getKey(), new ArrayList<>(entry.getValue().all()));
        }

        // Flatten and group by dimension registry name (minecraft:overworld, minecraft:the_nether, …)
        var grouped = resources.values().stream()
                .flatMap(data -> data.all().stream())
                .collect(Collectors.groupingBy(entry -> entry.dimension.location()));

        BY_DIMENSION.putAll(grouped);

        // Debug/Log
        int total = grouped.values().stream().mapToInt(List::size).sum();
        LostTales.LOGGER.info("[LostTales] Loaded {} map markers from datapacks across {} dimensions", total, grouped.size());
    }
}
