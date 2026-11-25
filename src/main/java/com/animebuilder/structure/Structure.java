package com.animebuilder.structure;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.EmptyBlockGetter;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.SlabType;

import java.util.*;

/** Immutable schematic. Coordinates are 0-based from the min corner. y = 0 is the ground layer. */
public final class Structure {
    public record Entry(int x, int y, int z, BlockState state) {}
    /** Entries relative to the anchor (centre of the footprint), already rotated. */
    public record Placement(List<Entry> all, List<Entry> visible, int minX, int minZ, int maxX, int maxZ) {}

    private static final Rotation[] ROTS = {Rotation.NONE, Rotation.CLOCKWISE_90, Rotation.CLOCKWISE_180, Rotation.COUNTERCLOCKWISE_90};

    public final String id;
    public final String category;
    public final Component name;
    public final boolean custom;
    public final int sizeX, sizeY, sizeZ;
    private final List<Entry> entries;
    private final List<Entry> visibleEntries;
    private final Map<Item, Integer> materials;
    private final Placement[] cache = new Placement[4];

    private Structure(String id, String category, Component name, boolean custom, int sx, int sy, int sz,
                      List<Entry> entries, List<Entry> visible, Map<Item, Integer> materials) {
        this.id = id; this.category = category; this.name = name; this.custom = custom;
        this.sizeX = sx; this.sizeY = sy; this.sizeZ = sz;
        this.entries = entries; this.visibleEntries = visible; this.materials = materials;
    }

    public static Structure fromMap(String id, String category, Component name, boolean custom, Map<BlockPos, BlockState> map) {
        int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE, minZ = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE, maxY = Integer.MIN_VALUE, maxZ = Integer.MIN_VALUE;
        for (Map.Entry<BlockPos, BlockState> e : map.entrySet()) {
            if (e.getValue().isAir()) continue;
            BlockPos p = e.getKey();
            minX = Math.min(minX, p.getX()); minY = Math.min(minY, p.getY()); minZ = Math.min(minZ, p.getZ());
            maxX = Math.max(maxX, p.getX()); maxY = Math.max(maxY, p.getY()); maxZ = Math.max(maxZ, p.getZ());
        }
        if (minX == Integer.MAX_VALUE) throw new IllegalArgumentException("Structure " + id + " is empty");
        int sx = maxX - minX + 1, sy = maxY - minY + 1, sz = maxZ - minZ + 1;
        List<Entry> list = new ArrayList<>();
        boolean[] solid = new boolean[sx * sy * sz];
        for (Map.Entry<BlockPos, BlockState> e : map.entrySet()) {
            BlockState s = e.getValue();
            if (s.isAir()) continue;
            BlockPos p = e.getKey();
            int x = p.getX() - minX, y = p.getY() - minY, z = p.getZ() - minZ;
            list.add(new Entry(x, y, z, s));
            if (s.isSolidRender(EmptyBlockGetter.INSTANCE, BlockPos.ZERO)) solid[(y * sz + z) * sx + x] = true;
        }
        list.sort(Comparator.comparingInt(Entry::y).thenComparingInt(Entry::x).thenComparingInt(Entry::z));
        List<Entry> vis = new ArrayList<>();
        for (Entry e : list) {
            if (e.state().getRenderShape() != RenderShape.MODEL) continue;
            if (exposed(e, solid, sx, sy, sz)) vis.add(e);
        }
        return new Structure(id, category, name, custom, sx, sy, sz,
                Collections.unmodifiableList(list), Collections.unmodifiableList(vis), computeMaterials(list));
    }

    private static boolean exposed(Entry e, boolean[] solid, int sx, int sy, int sz) {
        int[][] d = {{1, 0, 0}, {-1, 0, 0}, {0, 1, 0}, {0, -1, 0}, {0, 0, 1}, {0, 0, -1}};
        for (int[] o : d) {
            int x = e.x() + o[0], y = e.y() + o[1], z = e.z() + o[2];
            if (x < 0 || y < 0 || z < 0 || x >= sx || y >= sy || z >= sz) return true;
            if (!solid[(y * sz + z) * sx + x]) return true;
        }
        return false;
    }

    public static Map<Item, Integer> computeMaterials(List<Entry> list) {
        Map<Item, Integer> m = new LinkedHashMap<>();
        for (Entry e : list) {
            BlockState s = e.state();
            Item it = s.getBlock().asItem();
            if (it == Items.AIR && s.getBlock() instanceof net.minecraft.world.level.block.AbstractCauldronBlock) it = Items.CAULDRON;
            if (it == Items.AIR) continue;
            int n = 1;
            if (s.hasProperty(BlockStateProperties.SLAB_TYPE) && s.getValue(BlockStateProperties.SLAB_TYPE) == SlabType.DOUBLE) n = 2;
            if (s.hasProperty(BlockStateProperties.DOUBLE_BLOCK_HALF) && s.getValue(BlockStateProperties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.UPPER) continue;
            if (s.hasProperty(BlockStateProperties.BED_PART) && s.getValue(BlockStateProperties.BED_PART) == BedPart.HEAD) continue;
            m.merge(it, n, Integer::sum);
        }
        return Collections.unmodifiableMap(m);
    }

    public Map<Item, Integer> materials() { return materials; }
    public int blockCount() { return entries.size(); }
    public List<Entry> originalVisible() { return visibleEntries; }

    public Placement get(int rot) {
        rot = Math.floorMod(rot, 4);
        Placement p = cache[rot];
        if (p == null) { p = compute(ROTS[rot]); cache[rot] = p; }
        return p;
    }

    private Placement compute(Rotation rot) {
        int cx = sizeX / 2, cz = sizeZ / 2;
        List<Entry> all = rotateList(entries, rot, cx, cz);
        List<Entry> vis = rotateList(visibleEntries, rot, cx, cz);
        int minX = Integer.MAX_VALUE, minZ = Integer.MAX_VALUE, maxX = Integer.MIN_VALUE, maxZ = Integer.MIN_VALUE;
        for (Entry e : all) {
            minX = Math.min(minX, e.x()); maxX = Math.max(maxX, e.x());
            minZ = Math.min(minZ, e.z()); maxZ = Math.max(maxZ, e.z());
        }
        return new Placement(all, vis, minX, minZ, maxX, maxZ);
    }

    private static List<Entry> rotateList(List<Entry> src, Rotation rot, int cx, int cz) {
        List<Entry> out = new ArrayList<>(src.size());
        for (Entry e : src) {
            int x = e.x() - cx, z = e.z() - cz;
            int nx = x, nz = z;
            switch (rot) {
                case CLOCKWISE_90 -> { nx = -z; nz = x; }
                case CLOCKWISE_180 -> { nx = -x; nz = -z; }
                case COUNTERCLOCKWISE_90 -> { nx = z; nz = -x; }
                default -> { }
            }
            out.add(new Entry(nx, e.y(), nz, e.state().rotate(rot)));
        }
        return Collections.unmodifiableList(out);
    }
}
