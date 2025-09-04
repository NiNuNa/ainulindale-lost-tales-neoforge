package dev.ninuna.losttales.common.mapmarker;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.ninuna.losttales.client.gui.LostTalesGuiColor;
import dev.ninuna.losttales.client.gui.mapmarker.LostTalesMapMarkerIcon;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

import java.util.*;

public class LostTalesMapMarkerData {

    public static final class Entry {
        public final UUID id;
        public final String name;
        public final LostTalesMapMarkerIcon icon;
        public final LostTalesGuiColor color;
        public final ResourceKey<Level> dimension;
        public final double x, y, z;
        public final double fadeInRadius, unlockRadius;

        public Entry(UUID id, String name, LostTalesMapMarkerIcon icon, LostTalesGuiColor color, ResourceKey<Level> dimension, double x, double y, double z, double fadeInRadius, double unlockRadius) {
            this.id = id;
            this.name = name;
            this.icon = icon;this.color = color;
            this.dimension = dimension;
            this.x = x; this.y = y; this.z = z;
            this.fadeInRadius = fadeInRadius; this.unlockRadius = unlockRadius;
        }

        public static final Codec<Entry> CODEC = RecordCodecBuilder.create(entryInstance -> entryInstance.group(
                UUIDUtil.STRING_CODEC.fieldOf("id").forGetter(entry -> entry.id),
                Codec.STRING.fieldOf("name").forGetter(entry -> entry.name),
                LostTalesMapMarkerIcon.CODEC.fieldOf("icon").forGetter(entry -> entry.icon),
                LostTalesGuiColor.CODEC.fieldOf("color").forGetter(entry -> entry.color),
                ResourceKey.codec(Registries.DIMENSION).fieldOf("dimension").forGetter(entry -> entry.dimension),
                Codec.DOUBLE.fieldOf("x").forGetter(entry -> entry.x),
                Codec.DOUBLE.fieldOf("y").forGetter(entry -> entry.y),
                Codec.DOUBLE.fieldOf("z").forGetter(entry -> entry.z),
                Codec.DOUBLE.fieldOf("fadeInRadius").forGetter(entry -> entry.fadeInRadius),
                Codec.DOUBLE.fieldOf("unlockRadius").forGetter(entry -> entry.unlockRadius)
        ).apply(entryInstance, Entry::new));
    }

    public static final MapCodec<LostTalesMapMarkerData> MAP_CODEC = RecordCodecBuilder.mapCodec(dataInstance -> dataInstance.group(
            Entry.CODEC.listOf().fieldOf("map_markers").forGetter(data -> List.copyOf(
                    data.byId.values()))).apply(dataInstance, list -> {
                        LostTalesMapMarkerData data = new LostTalesMapMarkerData();
                        for (var entry : list) data.byId.put(entry.id, entry);
                        return data;
                    })
    );

    private final Map<UUID, Entry> byId = new LinkedHashMap<>();

    public Collection<Entry> all() {
        return Collections.unmodifiableCollection(byId.values());
    }

    public Optional<Entry> byNameFirst(String name) {
        return byId.values().stream().filter(entry -> entry.name.equalsIgnoreCase(name)).findFirst();
    }

    public boolean add(Entry entry) {
        return byId.put(entry.id, entry) == null;
    }

    public void addAll(Collection<Entry> entries) {
        entries.forEach(this::add);
    }

    public void clear() {
        byId.clear();
    }

    public boolean remove(UUID id) {
        return byId.remove(id) != null;
    }

    public boolean removeByNameFirst(String name) {
        return byNameFirst(name).map(entry -> remove(entry.id)).orElse(false);
    }
}
