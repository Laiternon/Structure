package com.animebuilder.catalog;

import com.animebuilder.structure.St;
import com.animebuilder.structure.Structure;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Blocks;

/** Slice-of-life and school buildings. */
final class ModernPack {
    private ModernPack() {}
    static final String CAT = "Повседневная жизнь";

    static void register() {
        Catalog.register(CAT, "high_school", "Школа", ModernPack::school);
        Catalog.register(CAT, "shinto_shrine", "Синтоистское святилище", ModernPack::shrine);
        Catalog.register(CAT, "hot_spring_inn", "Рёкан с горячим источником", ModernPack::ryokan);
        Catalog.register(CAT, "maid_cafe", "Тематическое кафе", ModernPack::cafe);
    }

    private static Structure school() {
        B b = new B(CAT, "high_school");
        b.fill(0, 0, 0, 30, 0, 12, Blocks.SMOOTH_STONE);
        b.walls(0, 1, 0, 30, 12, 12, Blocks.WHITE_CONCRETE);
        for (int f = 0; f < 3; f++) {
            int y = 1 + f * 4;
            b.fill(0, y + 3, 0, 30, y + 3, 12, Blocks.LIGHT_GRAY_CONCRETE);
            for (int x = 2; x <= 28; x += 2) for (int yy : new int[]{y + 1, y + 2}) {
                if (f == 0 && x >= 14 && x <= 16) continue;
                b.set(x, yy, 0, Blocks.GLASS_PANE); b.set(x, yy, 12, Blocks.GLASS_PANE);
            }
        }
        b.fill(0, 13, 0, 30, 13, 12, Blocks.GRAY_CONCRETE);
        b.fill(1, 13, 1, 29, 13, 11, Blocks.GRAY_CONCRETE);
        // entrance
        b.fill(14, 1, 0, 16, 3, 0, Blocks.AIR);
        b.door(15, 1, 0, Blocks.OAK_DOOR, Direction.NORTH);
        // clock tower
        b.walls(13, 14, 4, 17, 20, 8, Blocks.WHITE_CONCRETE);
        b.set(15, 18, 4, Blocks.GLOWSTONE);
        b.set(15, 18, 8, Blocks.GLOWSTONE);
        b.fill(13, 21, 4, 17, 21, 8, Blocks.GRAY_CONCRETE);
        b.pyramid(13, 4, 17, 8, 22, Blocks.STONE_BRICK_STAIRS, Blocks.STONE_BRICK_SLAB);
        // inner corridor floor & stairs
        b.fill(1, 1, 1, 29, 1, 11, Blocks.SMOOTH_QUARTZ);
        b.ladder(2, 1, 12, 6, Direction.EAST);
        // classroom desks (first floor)
        for (int x = 5; x <= 25; x += 4) for (int z = 4; z <= 8; z += 2) b.set(x, 2, z, Blocks.OAK_PRESSURE_PLATE);
        // rooftop fence
        for (int x = 0; x <= 30; x++) { b.set(x, 14, 0, Blocks.IRON_BARS); b.set(x, 14, 12, Blocks.IRON_BARS); }
        return b.build("Школа");
    }

    private static Structure shrine() {
        B b = new B(CAT, "shinto_shrine");
        // torii gate
        b.column(0, 0, 6, 0, Blocks.RED_TERRACOTTA);
        b.column(6, 0, 6, 0, Blocks.RED_TERRACOTTA);
        b.fill(-1, 6, 0, 7, 6, 0, Blocks.BLACK_CONCRETE);
        b.fill(0, 5, 0, 6, 5, 0, Blocks.RED_TERRACOTTA);
        // stairs path
        for (int z = 1; z <= 8; z++) { b.fill(2, 0, z, 4, 0, z, Blocks.STONE_BRICKS); }
        for (int i = 0; i < 4; i++) b.fill(2, i, 9 + i, 4, i, 9 + i, Blocks.STONE_BRICK_STAIRS);
        // main hall
        b.fill(-2, 4, 13, 8, 4, 22, Blocks.STONE_BRICKS);
        b.walls(0, 5, 14, 6, 8, 21, Blocks.WHITE_TERRACOTTA);
        for (int[] c : new int[][]{{0, 14}, {6, 14}, {0, 21}, {6, 21}}) b.column(c[0], 5, 8, c[1], Blocks.RED_TERRACOTTA);
        b.fill(-1, 9, 13, 7, 9, 22, Blocks.DARK_OAK_PLANKS);
        b.roofX(-2, 8, 12, 23, 10, Blocks.DARK_OAK_STAIRS, Blocks.DARK_OAK_SLAB);
        b.fill(2, 5, 14, 4, 7, 14, Blocks.AIR);
        b.fill(3, 5, 14, 3, 6, 14, Blocks.IRON_BARS);
        b.set(3, 5, 17, Blocks.OAK_FENCE);
        b.set(3, 6, 17, St.lantern(false));
        for (int x : new int[]{-1, 7}) b.set(x, 5, 13, St.lantern(false));
        // komainu
        for (int x : new int[]{0, 6}) {
            b.set(x, 1, 6, Blocks.STONE_BRICKS);
            b.set(x, 2, 6, Blocks.STONE_BRICK_STAIRS);
        }
        // stone lanterns
        for (int x : new int[]{1, 5}) for (int z : new int[]{3, 6}) {
            b.set(x, 1, z, Blocks.COBBLESTONE_WALL);
            b.set(x, 2, z, St.lantern(false));
        }
        return b.build("Синтоистское святилище");
    }

    private static Structure ryokan() {
        B b = new B(CAT, "hot_spring_inn");
        b.fill(0, 0, 0, 16, 0, 10, Blocks.SPRUCE_PLANKS);
        b.walls(0, 1, 0, 16, 4, 10, Blocks.BAMBOO_PLANKS);
        for (int x = 0; x <= 16; x += 4) { b.column(x, 1, 4, 0, Blocks.DARK_OAK_LOG); b.column(x, 1, 4, 10, Blocks.DARK_OAK_LOG); }
        for (int x : new int[]{2, 6, 10, 14}) { b.set(x, 2, 0, Blocks.WHITE_STAINED_GLASS_PANE); b.set(x, 3, 0, Blocks.WHITE_STAINED_GLASS_PANE); }
        b.fill(-1, 5, -1, 17, 5, 11, Blocks.DARK_OAK_PLANKS);
        b.roofX(-2, 18, -2, 12, 6, Blocks.DARK_OAK_STAIRS, Blocks.DARK_OAK_SLAB);
        b.fill(7, 1, 0, 9, 3, 0, Blocks.AIR);
        b.door(8, 1, 0, Blocks.BAMBOO_DOOR, Direction.NORTH);
        // rooms with futon
        b.fill(1, 1, 1, 7, 1, 9, Blocks.WHITE_CARPET);
        b.bed(2, 1, 3, Blocks.WHITE_BED, Direction.EAST);
        b.bed(2, 1, 7, Blocks.WHITE_BED, Direction.EAST);
        b.set(5, 1, 5, Blocks.OAK_FENCE);
        b.set(5, 2, 5, Blocks.OAK_PRESSURE_PLATE);
        // onsen outside
        b.fill(18, 0, 2, 26, 0, 9, Blocks.STONE_BRICKS);
        b.walls(18, 1, 2, 26, 1, 9, Blocks.POLISHED_ANDESITE);
        b.fill(19, 0, 3, 25, 0, 8, Blocks.WATER);
        b.fill(19, 1, 3, 25, 1, 8, Blocks.AIR);
        b.fill(19, 0, 3, 25, 0, 8, Blocks.WATER);
        b.fill(27, 0, 1, 27, 3, 1, Blocks.BAMBOO_BLOCK);
        for (int z : new int[]{2, 5, 8}) { b.set(18, 2, z, St.lantern(false)); }
        return b.build("Рёкан с горячим источником");
    }

    private static Structure cafe() {
        B b = new B(CAT, "maid_cafe");
        b.fill(0, 0, 0, 12, 0, 9, Blocks.PINK_CONCRETE);
        b.walls(0, 1, 0, 12, 4, 9, Blocks.WHITE_CONCRETE);
        b.fill(0, 5, 0, 12, 5, 9, Blocks.PINK_TERRACOTTA);
        b.fill(0, 1, 0, 12, 1, 0, Blocks.PINK_TERRACOTTA);
        for (int x = 2; x <= 10; x += 2) { b.set(x, 2, 0, Blocks.PINK_STAINED_GLASS_PANE); b.set(x, 3, 0, Blocks.PINK_STAINED_GLASS_PANE); }
        b.fill(5, 1, 0, 7, 3, 0, Blocks.AIR);
        b.door(6, 1, 0, Blocks.CHERRY_DOOR, Direction.NORTH);
        for (int x = -1; x <= 13; x++) b.set(x, 4, -1, (x % 2 == 0) ? Blocks.PINK_WOOL : Blocks.WHITE_WOOL);
        b.fill(0, 5, 0, 12, 5, 9, Blocks.PINK_CONCRETE);
        // counter & kitchen
        for (int x = 2; x <= 10; x++) b.set(x, 1, 7, St.stair(Blocks.CHERRY_STAIRS, Direction.NORTH, false));
        b.set(1, 1, 8, St.facing(Blocks.SMOKER, Direction.EAST));
        b.set(11, 1, 8, St.facing(Blocks.CHEST, Direction.WEST));
        b.set(2, 1, 8, Blocks.CAULDRON);
        for (int[] t : new int[][]{{3, 3}, {6, 3}, {9, 3}, {4, 5}, {8, 5}}) {
            b.set(t[0], 1, t[1], Blocks.CHERRY_FENCE);
            b.set(t[0], 2, t[1], Blocks.CHERRY_PRESSURE_PLATE);
        }
        b.set(6, 4, 5, St.lantern(true));
        b.set(6, 5, 5, Blocks.CHAIN);
        return b.build("Тематическое кафе");
    }
}
