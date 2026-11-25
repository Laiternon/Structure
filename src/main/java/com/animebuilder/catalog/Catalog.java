package com.animebuilder.catalog;

import com.animebuilder.structure.Structure;

import java.util.*;
import java.util.function.Supplier;

public final class Catalog {
    private Catalog() {}

    private static final Map<String, Supplier<Structure>> SUPPLIERS = new LinkedHashMap<>();
    private static final Map<String, Structure> BUILT = new LinkedHashMap<>();
    private static final Map<String, String> CATEGORY = new LinkedHashMap<>();
    private static final Map<String, String> NAMES = new LinkedHashMap<>();
    private static boolean init;

    public static void register(String category, String id, String displayName, Supplier<Structure> s) {
        SUPPLIERS.put(id, s);
        CATEGORY.put(id, category);
        NAMES.put(id, displayName);
    }

    private static synchronized void init() {
        if (init) return;
        init = true;
        NarutoPack.register();
        GhibliPack.register();
        FantasyPack.register();
        ScienceAndForgePack.register();
        ModernPack.register();
    }

    public static List<String> ids() { init(); return new ArrayList<>(SUPPLIERS.keySet()); }
    public static List<String> categories() {
        init();
        return new ArrayList<>(new LinkedHashSet<>(CATEGORY.values()));
    }
    public static List<String> idsIn(String category) {
        init();
        List<String> out = new ArrayList<>();
        for (var e : CATEGORY.entrySet()) if (e.getValue().equals(category)) out.add(e.getKey());
        return out;
    }
    public static String categoryOf(String id) { init(); return CATEGORY.get(id); }
    public static String nameOf(String id) { init(); return NAMES.getOrDefault(id, id); }

    public static Structure get(String id) {
        init();
        Structure s = BUILT.get(id);
        if (s != null) return s;
        Supplier<Structure> sup = SUPPLIERS.get(id);
        if (sup == null) return null;
        s = sup.get();
        BUILT.put(id, s);
        return s;
    }
}
