package dev.ninuna.losttales.client.gui.mapmarker;

import dev.ninuna.losttales.common.mapmarker.LostTalesMapMarkerData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class LostTalesClientMapMarker {
    private static final List<LostTalesMapMarkerData.Entry> SHARED = new ArrayList<>();
    private static final List<LostTalesMapMarkerData.Entry> PERSONAL = new ArrayList<>();

    public static synchronized void setShared(Collection<LostTalesMapMarkerData.Entry> entries) {
        SHARED.clear();
        SHARED.addAll(entries);
    }

    public static synchronized void setPersonal(Collection<LostTalesMapMarkerData.Entry> entries) {
        PERSONAL.clear();
        PERSONAL.addAll(entries);
    }

    public static synchronized List<LostTalesMapMarkerData.Entry> shared() {
        return Collections.unmodifiableList(SHARED);
    }

    public static synchronized List<LostTalesMapMarkerData.Entry> personal() {
        return Collections.unmodifiableList(PERSONAL);
    }

    public static synchronized void clearAll() {
        SHARED.clear();
        PERSONAL.clear();
    }
}
