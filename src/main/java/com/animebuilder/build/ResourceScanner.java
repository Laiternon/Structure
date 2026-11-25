package com.animebuilder.build;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.entity.BarrelBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;

import java.util.*;

/** Finds the player's inventory and nearby chests/barrels/shulker boxes and counts/takes items. */
public final class ResourceScanner {
    public static final int RADIUS = 16;

    private ResourceScanner() {}

    public static List<Container> sources(ServerPlayer player) {
        List<Container> out = new ArrayList<>();
        out.add(player.getInventory());
        ServerLevel level = player.serverLevel();
        BlockPos c = player.blockPosition();
        int minCx = (c.getX() - RADIUS) >> 4, maxCx = (c.getX() + RADIUS) >> 4;
        int minCz = (c.getZ() - RADIUS) >> 4, maxCz = (c.getZ() + RADIUS) >> 4;
        for (int cx = minCx; cx <= maxCx; cx++) for (int cz = minCz; cz <= maxCz; cz++) {
            LevelChunk chunk = level.getChunkSource().getChunkNow(cx, cz);
            if (chunk == null) continue;
            for (BlockEntity be : chunk.getBlockEntities().values()) {
                if (!(be instanceof ChestBlockEntity || be instanceof BarrelBlockEntity || be instanceof ShulkerBoxBlockEntity)) continue;
                BlockPos p = be.getBlockPos();
                if (Math.abs(p.getX() - c.getX()) > RADIUS || Math.abs(p.getY() - c.getY()) > RADIUS || Math.abs(p.getZ() - c.getZ()) > RADIUS) continue;
                if (be instanceof Container cont) out.add(cont);
            }
        }
        return out;
    }

    public static Map<Item, Integer> count(List<Container> sources, Collection<Item> items) {
        Map<Item, Integer> have = new LinkedHashMap<>();
        for (Item i : items) have.put(i, 0);
        for (Container c : sources) {
            for (int s = 0; s < c.getContainerSize(); s++) {
                ItemStack st = c.getItem(s);
                if (st.isEmpty()) continue;
                Integer cur = have.get(st.getItem());
                if (cur != null) have.put(st.getItem(), cur + st.getCount());
            }
        }
        return have;
    }

    /** Removes the given items. Caller must have verified availability. */
    public static void take(List<Container> sources, Map<Item, Integer> need) {
        for (Map.Entry<Item, Integer> e : need.entrySet()) {
            int left = e.getValue();
            for (Container c : sources) {
                if (left <= 0) break;
                boolean changed = false;
                for (int s = 0; s < c.getContainerSize() && left > 0; s++) {
                    ItemStack st = c.getItem(s);
                    if (st.isEmpty() || st.getItem() != e.getKey()) continue;
                    int n = Math.min(left, st.getCount());
                    st.shrink(n);
                    if (st.isEmpty()) c.setItem(s, ItemStack.EMPTY);
                    left -= n;
                    changed = true;
                }
                if (changed) c.setChanged();
            }
        }
    }
}
