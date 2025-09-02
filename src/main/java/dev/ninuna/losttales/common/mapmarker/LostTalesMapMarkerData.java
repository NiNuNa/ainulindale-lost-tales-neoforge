package dev.ninuna.losttales.common.mapmarker;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

import java.util.*;

public class LostTalesMapMarkerData {

    public static final class Entry {
        public final UUID id;
        public final String name;
        public final int color;
        public final ResourceKey<Level> dim;
        public final double x, y, z;
        public final double fadeInRadius;
        public final double unlockRadius;

        public Entry(UUID id, String name, int color, ResourceKey<Level> dim, double x, double y, double z, double fadeInRadius, double unlockRadius) {
            this.id = id;
            this.name = name;
            this.color = color;
            this.dim = dim;
            this.x = x; this.y = y; this.z = z;
            this.fadeInRadius = fadeInRadius;
            this.unlockRadius = unlockRadius;
        }

        public static final Codec<Entry> CODEC = RecordCodecBuilder.create(entryInstance -> entryInstance.group(
                UUIDUtil.CODEC.fieldOf("id").forGetter(entry -> entry.id),
                Codec.STRING.fieldOf("name").forGetter(entry -> entry.name),
                Codec.INT.fieldOf("color").forGetter(entry -> entry.color),
                ResourceKey.codec(Registries.DIMENSION).fieldOf("dim").forGetter(entry -> entry.dim),
                Codec.DOUBLE.fieldOf("x").forGetter(entry -> entry.x),
                Codec.DOUBLE.fieldOf("y").forGetter(entry -> entry.y),
                Codec.DOUBLE.fieldOf("z").forGetter(entry -> entry.z),
                Codec.DOUBLE.fieldOf("fadeInRadius").forGetter(entry -> entry.fadeInRadius),
                Codec.DOUBLE.fieldOf("unlockRadius").forGetter(entry -> entry.unlockRadius)
        ).apply(entryInstance, Entry::new));
    }

    public static final Codec<LostTalesMapMarkerData> CODEC = RecordCodecBuilder.create(i -> i.group(
            Entry.CODEC.listOf().fieldOf("mapMarkers").forGetter(data -> List.copyOf(
                    data.byId.values()))).apply(i, list -> {
                        LostTalesMapMarkerData s = new LostTalesMapMarkerData();
                        for (var e : list) s.byId.put(e.id, e);
                        return s;
                    })
    );

    private final Map<UUID, Entry> byId = new LinkedHashMap<>();

    public Collection<Entry> all() {
        return Collections.unmodifiableCollection(byId.values());
    }

    public Optional<Entry> byNameFirst(String name) {
        return byId.values().stream().filter(e -> e.name.equalsIgnoreCase(name)).findFirst();
    }

    public boolean add(Entry e) {
        return byId.put(e.id, e) == null;
    }

    public boolean remove(UUID id) {
        return byId.remove(id) != null;
    }

    public boolean removeByNameFirst(String name) {
        return byNameFirst(name).map(e -> remove(e.id)).orElse(false);
    }
}
