package com.animebuilder.client;

import com.animebuilder.network.Net;
import com.animebuilder.structure.Structure;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;

public final class ClientState {
    private ClientState() {}

    /** Structure being previewed in the world, or null. */
    public static String placingId;
    public static int rot;
    public static boolean locked;
    public static BlockPos lockedPos;
    /** Block the player was aiming at when the GUI was opened. */
    public static BlockPos lastTarget;
    public static final Map<String, Map<String, Integer>> SCAN = new HashMap<>();

    public static void onScan(Net.ScanResult m) { SCAN.put(m.id, m.have); }

    public static void stopPlacing() { placingId = null; locked = false; lockedPos = null; }

    /** Where the ghost / build should currently go. */
    public static BlockPos anchor() {
        if (locked && lockedPos != null) return lockedPos;
        HitResult hr = Minecraft.getInstance().hitResult;
        if (hr instanceof BlockHitResult bhr && hr.getType() == HitResult.Type.BLOCK) return bhr.getBlockPos();
        return null;
    }

    public static int have(String id, Item item) {
        Map<String, Integer> m = SCAN.get(id);
        if (m == null) return -1;
        ResourceLocation k = ForgeRegistries.ITEMS.getKey(item);
        return k == null ? 0 : m.getOrDefault(k.toString(), 0);
    }

    /** null = unknown yet. */
    public static Boolean hasEnough(Structure s) {
        if (!SCAN.containsKey(s.id)) return null;
        for (Map.Entry<Item, Integer> e : s.materials().entrySet())
            if (have(s.id, e.getKey()) < e.getValue()) return false;
        return true;
    }
}
