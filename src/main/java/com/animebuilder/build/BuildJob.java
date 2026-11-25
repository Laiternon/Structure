package com.animebuilder.build;

import com.animebuilder.structure.Structure;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.UUID;

/** One queued construction. Places blocks gradually, then fixes connections (fences, panes, stairs, walls). */
public final class BuildJob {
    private static final int PER_TICK = 48;
    private static final int FLAGS = Block.UPDATE_CLIENTS | Block.UPDATE_KNOWN_SHAPE;

    final ServerLevel level;
    final UUID owner;
    private final List<Structure.Entry> entries;
    private final List<BlockPos> targets;
    private int index = 0;
    private int fixIndex = 0;
    private boolean placing = true;

    BuildJob(ServerLevel level, UUID owner, List<Structure.Entry> entries, List<BlockPos> targets) {
        this.level = level; this.owner = owner; this.entries = entries; this.targets = targets;
    }

    /** @return true when the job is finished */
    boolean tick() {
        if (placing) {
            int n = 0;
            while (index < entries.size() && n < PER_TICK) {
                BlockPos pos = targets.get(index);
                BlockState st = entries.get(index).state();
                if (level.hasChunkAt(pos)) level.setBlock(pos, st, FLAGS);
                index++; n++;
            }
            if (index >= entries.size()) placing = false;
            return false;
        }
        int n = 0;
        while (fixIndex < entries.size() && n < PER_TICK * 2) {
            BlockPos pos = targets.get(fixIndex++);
            if (!level.hasChunkAt(pos)) continue;
            BlockState cur = level.getBlockState(pos);
            Block b = cur.getBlock();
            if (b instanceof CrossCollisionBlock || b instanceof WallBlock || b instanceof StairBlock) {
                BlockState upd = Block.updateFromNeighbourShapes(cur, level, pos);
                if (upd != cur) level.setBlock(pos, upd, Block.UPDATE_CLIENTS | Block.UPDATE_KNOWN_SHAPE);
            }
            n++;
        }
        if (fixIndex >= entries.size()) {
            // notify neighbours once at the end so lighting/redstone etc. settle
            for (BlockPos p : targets) level.getChunkSource().blockChanged(p);
            return true;
        }
        return false;
    }
}
