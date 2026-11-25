package com.animebuilder.catalog;

import com.animebuilder.structure.Structure;
import com.animebuilder.structure.St;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.Map;

/** Tiny DSL for describing structures. Coordinates are arbitrary, normalised on build(). */
public final class B {
    private final Map<BlockPos, BlockState> map = new HashMap<>();
    private final String id, category;

    public B(String category, String id) { this.category = category; this.id = id; }

    public B set(int x, int y, int z, BlockState s) { map.put(new BlockPos(x, y, z), s); return this; }
    public B set(int x, int y, int z, Block b) { return set(x, y, z, b.defaultBlockState()); }

    public B fill(int x1, int y1, int z1, int x2, int y2, int z2, BlockState s) {
        for (int x = Math.min(x1, x2); x <= Math.max(x1, x2); x++)
            for (int y = Math.min(y1, y2); y <= Math.max(y1, y2); y++)
                for (int z = Math.min(z1, z2); z <= Math.max(z1, z2); z++) set(x, y, z, s);
        return this;
    }
    public B fill(int x1, int y1, int z1, int x2, int y2, int z2, Block b) { return fill(x1, y1, z1, x2, y2, z2, b.defaultBlockState()); }

    /** Walls only (no floor/ceiling). */
    public B walls(int x1, int y1, int z1, int x2, int y2, int z2, BlockState s) {
        for (int x = x1; x <= x2; x++) for (int y = y1; y <= y2; y++) for (int z = z1; z <= z2; z++)
            if (x == x1 || x == x2 || z == z1 || z == z2) set(x, y, z, s);
        return this;
    }
    public B walls(int x1, int y1, int z1, int x2, int y2, int z2, Block b) { return walls(x1, y1, z1, x2, y2, z2, b.defaultBlockState()); }

    /** Solid shell: floor, ceiling and walls. */
    public B shell(int x1, int y1, int z1, int x2, int y2, int z2, Block b) {
        for (int x = x1; x <= x2; x++) for (int y = y1; y <= y2; y++) for (int z = z1; z <= z2; z++)
            if (x == x1 || x == x2 || y == y1 || y == y2 || z == z1 || z == z2) set(x, y, z, b);
        return this;
    }

    public B clear(int x1, int y1, int z1, int x2, int y2, int z2) {
        for (int x = x1; x <= x2; x++) for (int y = y1; y <= y2; y++) for (int z = z1; z <= z2; z++)
            map.remove(new BlockPos(x, y, z));
        return this;
    }

    public B disc(int cx, int y, int cz, double r, Block b) {
        int ri = (int) Math.ceil(r);
        for (int x = -ri; x <= ri; x++) for (int z = -ri; z <= ri; z++)
            if (x * x + z * z <= r * r + 0.5) set(cx + x, y, cz + z, b);
        return this;
    }

    public B ring(int cx, int y, int cz, double r, Block b) {
        int ri = (int) Math.ceil(r) + 1;
        for (int x = -ri; x <= ri; x++) for (int z = -ri; z <= ri; z++) {
            double d = Math.sqrt(x * x + z * z);
            if (d <= r + 0.5 && d > r - 0.7) set(cx + x, y, cz + z, b);
        }
        return this;
    }

    public B cylinder(int cx, int y1, int y2, int cz, double r, Block b, boolean hollow) {
        for (int y = y1; y <= y2; y++) {
            if (hollow) ring(cx, y, cz, r, b); else disc(cx, y, cz, r, b);
        }
        return this;
    }

    public B sphere(int cx, int cy, int cz, double r, Block b, boolean hollow) {
        int ri = (int) Math.ceil(r) + 1;
        for (int x = -ri; x <= ri; x++) for (int y = -ri; y <= ri; y++) for (int z = -ri; z <= ri; z++) {
            double d = Math.sqrt(x * x + y * y + z * z);
            if (hollow ? (d <= r + 0.3 && d > r - 1.0) : d <= r + 0.3) set(cx + x, cy + y, cz + z, b);
        }
        return this;
    }

    public B column(int x, int y1, int y2, int z, Block b) { return fill(x, y1, z, x, y2, z, b); }

    /** Stair-stepped pitched roof along X axis (ridge runs along X). */
    public B roofX(int x1, int x2, int zMin, int zMax, int y, Block stair, Block slab) {
        int z1 = zMin, z2 = zMax, yy = y;
        while (z1 < z2) {
            for (int x = x1; x <= x2; x++) {
                set(x, yy, z1, St.stair(stair, Direction.SOUTH, false));
                set(x, yy, z2, St.stair(stair, Direction.NORTH, false));
            }
            z1++; z2--; yy++;
        }
        if (z1 == z2) for (int x = x1; x <= x2; x++) set(x, yy, z1, St.slab(slab, false));
        return this;
    }

    /** Stair-stepped pitched roof along Z axis (ridge runs along Z). */
    public B roofZ(int z1, int z2, int xMin, int xMax, int y, Block stair, Block slab) {
        int a = xMin, b = xMax, yy = y;
        while (a < b) {
            for (int z = z1; z <= z2; z++) {
                set(a, yy, z, St.stair(stair, Direction.EAST, false));
                set(b, yy, z, St.stair(stair, Direction.WEST, false));
            }
            a++; b--; yy++;
        }
        if (a == b) for (int z = z1; z <= z2; z++) set(a, yy, z, St.slab(slab, false));
        return this;
    }

    /** Four-sided stepped pyramid roof (pagoda style) with overhang. */
    public B pyramid(int x1, int z1, int x2, int z2, int y, Block stair, Block top) {
        int yy = y;
        while (x1 <= x2 && z1 <= z2) {
            for (int x = x1; x <= x2; x++) {
                set(x, yy, z1, St.stair(stair, Direction.SOUTH, false));
                set(x, yy, z2, St.stair(stair, Direction.NORTH, false));
            }
            for (int z = z1; z <= z2; z++) {
                set(x1, yy, z, St.stair(stair, Direction.EAST, false));
                set(x2, yy, z, St.stair(stair, Direction.WEST, false));
            }
            // fix corners to be plain stairs facing diagonal-ish (approximation: keep as is)
            x1++; z1++; x2--; z2--; yy++;
        }
        return this;
    }

    public B door(int x, int y, int z, Block door, Direction facing) {
        set(x, y, z, St.door(door, facing, false));
        set(x, y + 1, z, St.door(door, facing, true));
        return this;
    }

    /** Ladder column with a solid support block behind it (so it is never floating). */
    public B ladder(int x, int y1, int y2, int z, Direction facing) {
        BlockPos behind = new BlockPos(x, 0, z).relative(facing.getOpposite());
        for (int y = y1; y <= y2; y++) {
            set(x, y, z, St.facing(Blocks.LADDER, facing));
            BlockPos sp = new BlockPos(behind.getX(), y, behind.getZ());
            if (!map.containsKey(sp)) map.put(sp, Blocks.SPRUCE_PLANKS.defaultBlockState());
        }
        return this;
    }

    public B bed(int x, int y, int z, Block bed, Direction facing) {
        set(x, y, z, St.bed(bed, facing, false));
        BlockPos head = new BlockPos(x, y, z).relative(facing);
        set(head.getX(), y, head.getZ(), St.bed(bed, facing, true));
        return this;
    }

    public Structure build(String displayName) {
        return Structure.fromMap(id, category, Component.literal(displayName), false, map);
    }

    public Structure build(Component name) { return Structure.fromMap(id, category, name, false, map); }

    public static BlockState air() { return Blocks.AIR.defaultBlockState(); }
}
