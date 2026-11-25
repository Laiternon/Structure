package com.animebuilder.build;

import com.animebuilder.structure.Structure;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;

public final class BuildService {
    private BuildService() {}

    public static final int MAX_DISTANCE = 160;

    public static void tryBuild(ServerPlayer player, Structure s, BlockPos origin, int rot) {
        ServerLevel level = player.serverLevel();
        Structure.Placement pl = s.get(rot);
        boolean creative = player.getAbilities().instabuild;

        if (origin.getY() + s.sizeY >= level.getMaxBuildHeight() || origin.getY() < level.getMinBuildHeight()) {
            player.sendSystemMessage(Component.literal("§cПостройка не помещается по высоте."));
            return;
        }

        // Decide which blocks actually need placing
        List<Structure.Entry> todo = new ArrayList<>();
        List<BlockPos> targets = new ArrayList<>();
        int skippedProtected = 0;
        for (Structure.Entry e : pl.all()) {
            BlockPos pos = origin.offset(e.x(), e.y(), e.z());
            if (!level.hasChunkAt(pos)) { skippedProtected++; continue; }
            BlockState cur = level.getBlockState(pos);
            if (cur == e.state()) continue;
            if (!level.mayInteract(player, pos) || !player.mayBuild()) { skippedProtected++; continue; }
            if (cur.getDestroySpeed(level, pos) < 0) { skippedProtected++; continue; }
            if (level.getBlockEntity(pos) != null) { skippedProtected++; continue; } // never overwrite chests etc.
            todo.add(e);
            targets.add(pos);
        }
        if (todo.isEmpty()) {
            player.sendSystemMessage(Component.literal("§eНечего строить: всё уже на месте или место защищено."));
            return;
        }

        Map<Item, Integer> need = Structure.computeMaterials(todo);
        List<Container> sources = ResourceScanner.sources(player);

        if (!creative) {
            Map<Item, Integer> have = ResourceScanner.count(sources, need.keySet());
            List<String> missing = new ArrayList<>();
            for (Map.Entry<Item, Integer> e : need.entrySet()) {
                int h = have.getOrDefault(e.getKey(), 0);
                if (h < e.getValue()) {
                    missing.add(e.getKey().getDescription().getString() + " ×" + (e.getValue() - h));
                }
            }
            if (!missing.isEmpty()) {
                player.sendSystemMessage(Component.literal("§cНе хватает ресурсов (инвентарь + сундуки в радиусе "
                        + ResourceScanner.RADIUS + "): §f" + String.join(", ", missing.subList(0, Math.min(8, missing.size())))
                        + (missing.size() > 8 ? " …и ещё " + (missing.size() - 8) : "")));
                return;
            }
            ResourceScanner.take(sources, need);
        }

        BuildManager.add(new BuildJob(level, player.getUUID(), todo, targets));
        player.sendSystemMessage(Component.literal("§aСтроительство «" + s.name.getString() + "» началось: "
                + todo.size() + " блоков" + (skippedProtected > 0 ? " (пропущено защищённых/занятых: " + skippedProtected + ")" : "") + "."));
    }

    /** Used by the scan packet. */
    public static Map<Item, Integer> scan(ServerPlayer player, Structure s) {
        return ResourceScanner.count(ResourceScanner.sources(player), s.materials().keySet());
    }

    public static String keyOf(Item i) {
        var k = ForgeRegistries.ITEMS.getKey(i);
        return k == null ? "minecraft:air" : k.toString();
    }
}
