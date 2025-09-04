package dev.ninuna.losttales.common.mapmarker;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class LostTalesMapMarkerDataStore {
    private static final AtomicReference<List<LostTalesMapMarkerData.Entry>> SHARED = new AtomicReference<>(List.of());
    private static final AtomicReference<List<LostTalesMapMarkerData.Entry>> PERSONAL = new AtomicReference<>(List.of());

    public static void setShared(Collection<LostTalesMapMarkerData.Entry> entries) {
        SHARED.set(List.copyOf(entries));
    }

    public static void setPersonal(Collection<LostTalesMapMarkerData.Entry> entries) {
        PERSONAL.set(List.copyOf(entries));
    }

    public static List<LostTalesMapMarkerData.Entry> shared() {
        return SHARED.get();
    }

    public static List<LostTalesMapMarkerData.Entry> personal() {
        return PERSONAL.get();
    }

    public static void clearAll() {
        SHARED.set(List.of());
        PERSONAL.set(List.of());
    }
}
