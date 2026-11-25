package com.animebuilder.build;

import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Mod.EventBusSubscriber(modid = "animebuilder")
public final class BuildManager {
    private static final List<BuildJob> JOBS = new ArrayList<>();

    private BuildManager() {}

    static void add(BuildJob j) { JOBS.add(j); }

    @SubscribeEvent
    public static void onTick(TickEvent.ServerTickEvent e) {
        if (e.phase != TickEvent.Phase.END || JOBS.isEmpty()) return;
        Iterator<BuildJob> it = JOBS.iterator();
        while (it.hasNext()) {
            BuildJob j = it.next();
            boolean done;
            try { done = j.tick(); } catch (Exception ex) { ex.printStackTrace(); done = true; }
            if (done) it.remove();
        }
    }
}
