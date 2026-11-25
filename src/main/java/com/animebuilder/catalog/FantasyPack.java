package com.animebuilder.catalog;

import com.animebuilder.structure.St;
import com.animebuilder.structure.Structure;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Blocks;

/** Isekai / RPG / magic guild buildings. */
final class FantasyPack {
    private FantasyPack() {}
    static final String CAT = "Фэнтези и гильдии";

    static void register() {
        Catalog.register(CAT, "mage_guild_hall", "Зал гильдии магов", FantasyPack::guildHall);
        Catalog.register(CAT, "adventurer_tavern", "Таверна искателей приключений", FantasyPack::tavern);
        Catalog.register(CAT, "demon_lord_castle", "Замок Повелителя демонов", FantasyPack::demonCastle);
        Catalog.register(CAT, "floating_castle_tower", "Башня парящего замка", FantasyPack::floatingTower);
        Catalog.register(CAT, "magic_academy", "Академия магии", FantasyPack::academy);
    }

    private static Structure guildHall() {
        B b = new B(CAT, "mage_guild_hall");
        b.fill(0, 0, 0, 16, 0, 12, Blocks.STONE_BRICKS);
        b.walls(0, 1, 0, 16, 6, 12, Blocks.WHITE_CONCRETE);
        for (int[] c : new int[][]{{0, 0}, {16, 0}, {0, 12}, {16, 12}, {8, 0}, {8, 12}}) b.column(c[0], 1, 6, c[1], Blocks.QUARTZ_PILLAR);
        for (int x : new int[]{3, 5, 11, 13}) for (int y : new int[]{3, 4}) { b.set(x, y, 0, Blocks.GLASS_PANE); b.set(x, y, 12, Blocks.GLASS_PANE); }
        b.fill(0, 7, 0, 16, 7, 12, Blocks.BLUE_CONCRETE);
        b.roofX(-1, 17, -1, 13, 8, Blocks.PRISMARINE_BRICK_STAIRS, Blocks.PRISMARINE_BRICK_SLAB);
        b.fill(7, 1, 0, 9, 3, 0, Blocks.AIR);
        b.door(8, 1, 0, Blocks.WARPED_DOOR, Direction.NORTH);
        // central tower with emblem
        b.walls(6, 8, 4, 10, 14, 8, Blocks.WHITE_CONCRETE);
        b.set(8, 11, 4, Blocks.SEA_LANTERN);
        b.set(8, 11, 8, Blocks.SEA_LANTERN);
        b.fill(5, 15, 3, 11, 15, 9, Blocks.BLUE_CONCRETE);
        b.pyramid(5, 3, 11, 9, 16, Blocks.PRISMARINE_BRICK_STAIRS, Blocks.PRISMARINE_BRICK_SLAB);
        b.set(8, 19, 6, Blocks.LIGHTNING_ROD);
        // interior: quest board, bar, library
        b.fill(1, 1, 1, 15, 1, 11, Blocks.SPRUCE_PLANKS);
        for (int x = 2; x <= 6; x++) b.set(x, 1, 3, St.stair(Blocks.DARK_OAK_STAIRS, Direction.SOUTH, false));
        for (int z = 6; z <= 10; z++) { b.set(15, 1, z, Blocks.BOOKSHELF); b.set(15, 2, z, Blocks.BOOKSHELF); b.set(15, 3, z, Blocks.BOOKSHELF); }
        b.set(13, 1, 10, St.facing(Blocks.LECTERN, Direction.WEST));
        b.set(12, 1, 2, Blocks.ENCHANTING_TABLE);
        b.set(14, 1, 2, Blocks.BREWING_STAND);
        b.set(1, 1, 8, St.facing(Blocks.CHEST, Direction.EAST));
        b.set(1, 1, 9, St.facing(Blocks.CHEST, Direction.EAST));
        b.set(1, 1, 10, St.facing(Blocks.CARTOGRAPHY_TABLE, Direction.EAST));
        for (int x : new int[]{4, 8, 12}) { b.set(x, 6, 6, St.lantern(true)); b.set(x, 7, 6, Blocks.CHAIN); }
        return b.build("Зал гильдии магов");
    }

    private static Structure tavern() {
        B b = new B(CAT, "adventurer_tavern");
        b.fill(0, 0, 0, 14, 0, 10, Blocks.COBBLESTONE);
        b.walls(0, 1, 0, 14, 3, 10, Blocks.STRIPPED_SPRUCE_LOG);
        b.walls(-1, 4, -1, 15, 7, 11, Blocks.WHITE_TERRACOTTA);
        for (int[] c : new int[][]{{0, 0}, {14, 0}, {0, 10}, {14, 10}}) b.column(c[0], 1, 7, c[1], Blocks.DARK_OAK_LOG);
        for (int x = 2; x <= 12; x += 3) { b.set(x, 2, 0, Blocks.GLASS_PANE); b.set(x, 2, 10, Blocks.GLASS_PANE); b.set(x, 5, -1, Blocks.GLASS_PANE); b.set(x, 5, 11, Blocks.GLASS_PANE); }
        b.fill(-1, 4, -1, 15, 4, 11, Blocks.DARK_OAK_PLANKS);
        b.fill(1, 4, 1, 13, 4, 9, Blocks.AIR);
        b.fill(1, 4, 1, 13, 4, 9, Blocks.SPRUCE_PLANKS);
        b.roofX(-2, 16, -2, 12, 8, Blocks.DARK_OAK_STAIRS, Blocks.DARK_OAK_SLAB);
        b.fill(-1, 8, -1, 15, 8, 11, Blocks.DARK_OAK_PLANKS);
        b.door(7, 1, 0, Blocks.DARK_OAK_DOOR, Direction.NORTH);
        b.set(5, 3, -1, Blocks.OAK_WALL_SIGN);
        b.set(9, 3, -1, St.lantern(false));
        b.set(5, 3, -1, St.lantern(false));
        // bar & kitchen
        for (int x = 2; x <= 9; x++) b.set(x, 1, 7, St.stair(Blocks.SPRUCE_STAIRS, Direction.NORTH, false));
        for (int x = 2; x <= 9; x += 2) b.set(x, 1, 9, Blocks.BARREL);
        b.set(11, 1, 9, St.facing(Blocks.SMOKER, Direction.WEST));
        b.set(12, 1, 9, St.facing(Blocks.FURNACE, Direction.WEST));
        b.set(13, 1, 9, St.facing(Blocks.CHEST, Direction.WEST));
        // tables
        for (int[] t : new int[][]{{3, 3}, {7, 3}, {11, 3}, {5, 5}, {10, 5}}) {
            b.set(t[0], 1, t[1], Blocks.OAK_FENCE);
            b.set(t[0], 2, t[1], Blocks.OAK_PRESSURE_PLATE);
        }
        // fireplace
        b.fill(1, 1, 2, 1, 2, 4, Blocks.BRICKS);
        b.set(1, 1, 3, Blocks.CAMPFIRE);
        // upstairs rooms: beds
        b.bed(2, 5, 2, Blocks.RED_BED, Direction.EAST);
        b.bed(2, 5, 8, Blocks.BLUE_BED, Direction.EAST);
        b.bed(11, 5, 2, Blocks.GREEN_BED, Direction.WEST);
        b.bed(11, 5, 8, Blocks.YELLOW_BED, Direction.WEST);
        b.ladder(13, 1, 4, 6, Direction.WEST);
        b.set(7, 3, 5, St.lantern(true));
        b.set(7, 4, 5, Blocks.CHAIN);
        return b.build("Таверна искателей приключений");
    }

    private static Structure demonCastle() {
        B b = new B(CAT, "demon_lord_castle");
        b.fill(0, 0, 0, 24, 0, 24, Blocks.BLACKSTONE);
        b.walls(0, 1, 0, 24, 10, 24, Blocks.POLISHED_BLACKSTONE_BRICKS);
        b.fill(0, 11, 0, 24, 11, 24, Blocks.POLISHED_BLACKSTONE);
        b.fill(1, 11, 1, 23, 11, 23, Blocks.AIR);
        b.fill(1, 11, 1, 23, 11, 23, Blocks.POLISHED_BLACKSTONE);
        // battlements
        for (int i = 0; i <= 24; i += 2) {
            b.set(i, 12, 0, Blocks.POLISHED_BLACKSTONE_BRICK_WALL);
            b.set(i, 12, 24, Blocks.POLISHED_BLACKSTONE_BRICK_WALL);
            b.set(0, 12, i, Blocks.POLISHED_BLACKSTONE_BRICK_WALL);
            b.set(24, 12, i, Blocks.POLISHED_BLACKSTONE_BRICK_WALL);
        }
        // corner towers
        for (int[] c : new int[][]{{0, 0}, {24, 0}, {0, 24}, {24, 24}}) {
            b.cylinder(c[0], 1, 18, c[1], 3.5, Blocks.POLISHED_BLACKSTONE_BRICKS, false);
            b.disc(c[0], 19, c[1], 4.5, Blocks.NETHER_BRICKS);
            b.disc(c[0], 20, c[1], 3.2, Blocks.PURPLE_CONCRETE);
            b.disc(c[0], 21, c[1], 2.2, Blocks.PURPLE_CONCRETE);
            b.disc(c[0], 22, c[1], 1.2, Blocks.PURPLE_CONCRETE);
            b.set(c[0], 23, c[1], Blocks.END_ROD);
            b.set(c[0], 24, c[1], Blocks.END_ROD);
            b.set(c[0] + 4, 10, c[1], Blocks.SOUL_LANTERN);
        }
        // central keep
        b.cylinder(12, 12, 24, 12, 6, Blocks.DEEPSLATE_BRICKS, false);
        b.disc(12, 25, 12, 7.5, Blocks.NETHER_BRICKS);
        for (int r = 6; r >= 1; r--) b.disc(12, 31 - r, 12, r, Blocks.PURPLE_CONCRETE);
        b.set(12, 32, 12, Blocks.END_ROD);
        // gate
        b.fill(10, 1, 0, 14, 6, 0, Blocks.AIR);
        b.fill(10, 6, 0, 14, 6, 0, Blocks.CRYING_OBSIDIAN);
        b.fill(11, 1, 0, 13, 5, 0, Blocks.IRON_BARS);
        // throne room
        b.fill(8, 1, 14, 16, 1, 20, Blocks.RED_CARPET);
        b.fill(10, 1, 20, 14, 2, 21, Blocks.CRIMSON_PLANKS);
        b.set(12, 3, 21, St.stair(Blocks.CRIMSON_STAIRS, Direction.NORTH, false));
        b.set(11, 3, 21, Blocks.CRIMSON_FENCE);
        b.set(13, 3, 21, Blocks.CRIMSON_FENCE);
        for (int z : new int[]{4, 10, 16}) { b.set(9, 3, z, Blocks.SOUL_LANTERN); b.set(15, 3, z, Blocks.SOUL_LANTERN); b.set(9, 2, z, Blocks.POLISHED_BLACKSTONE_BRICK_WALL); b.set(15, 2, z, Blocks.POLISHED_BLACKSTONE_BRICK_WALL); }
        return b.build("Замок Повелителя демонов");
    }

    private static Structure floatingTower() {
        B b = new B(CAT, "floating_castle_tower");
        int c = 9;
        // floating island
        for (int y = 0; y >= -6; y--) b.disc(c, y, c, 9 + y * 1.2, y > -4 ? Blocks.DIRT : Blocks.STONE);
        b.disc(c, 1, c, 9, Blocks.GRASS_BLOCK);
        // tower levels
        b.cylinder(c, 2, 30, c, 6, Blocks.QUARTZ_BRICKS, true);
        for (int lvl = 5; lvl <= 29; lvl += 6) { b.disc(c, lvl, c, 5.5, Blocks.SMOOTH_QUARTZ); b.ring(c, lvl + 1, c, 7, Blocks.CHISELED_QUARTZ_BLOCK); }
        for (int y = 3; y <= 29; y += 6) for (int[] p : new int[][]{{c + 6, c}, {c - 6, c}, {c, c + 6}, {c, c - 6}}) { b.set(p[0], y, p[1], Blocks.GLASS_PANE); b.set(p[0], y + 1, p[1], Blocks.GLASS_PANE); }
        b.disc(c, 31, c, 7, Blocks.LIGHT_BLUE_CONCRETE);
        for (int r = 6; r >= 1; r--) b.disc(c, 38 - r, c, r, Blocks.LIGHT_BLUE_CONCRETE);
        b.set(c, 39, c, Blocks.BEACON);
        b.door(c, 2, c - 6, Blocks.IRON_DOOR, Direction.NORTH);
        b.clear(c, 2, c - 6, c, 3, c - 6);
        b.door(c, 2, c - 6, Blocks.IRON_DOOR, Direction.NORTH);
        b.ladder(c + 4, 2, 30, c + 4, Direction.NORTH);
        return b.build("Башня парящего замка");
    }

    private static Structure academy() {
        B b = new B(CAT, "magic_academy");
        b.fill(0, 0, 0, 30, 0, 18, Blocks.STONE_BRICKS);
        b.walls(0, 1, 4, 30, 8, 14, Blocks.STONE_BRICKS);
        b.fill(0, 9, 4, 30, 9, 14, Blocks.DEEPSLATE_TILES);
        b.roofX(-1, 31, 3, 15, 10, Blocks.DEEPSLATE_TILE_STAIRS, Blocks.DEEPSLATE_TILE_SLAB);
        for (int x = 3; x <= 27; x += 4) for (int y : new int[]{3, 4, 6, 7}) { b.set(x, y, 4, Blocks.GLASS_PANE); b.set(x, y, 14, Blocks.GLASS_PANE); }
        b.fill(13, 1, 4, 17, 4, 4, Blocks.AIR);
        b.door(15, 1, 4, Blocks.DARK_OAK_DOOR, Direction.NORTH);
        for (int[] c : new int[][]{{1, 9}, {29, 9}}) {
            b.cylinder(c[0], 1, 20, c[1], 4, Blocks.STONE_BRICKS, false);
            b.disc(c[0], 21, c[1], 5, Blocks.DEEPSLATE_TILES);
            for (int r = 4; r >= 1; r--) b.disc(c[0], 26 - r, c[1], r, Blocks.DEEPSLATE_TILES);
            b.set(c[0], 26, c[1], Blocks.LIGHTNING_ROD);
            b.set(c[0] + 4, 8, c[1], Blocks.GLASS_PANE);
        }
        b.cylinder(15, 9, 22, 9, 3, Blocks.STONE_BRICKS, false);
        for (int r = 4; r >= 1; r--) b.disc(15, 27 - r, 9, r, Blocks.DEEPSLATE_TILES);
        b.set(15, 28, 9, Blocks.LIGHTNING_ROD);
        // hall
        b.fill(1, 1, 5, 29, 1, 13, Blocks.POLISHED_ANDESITE);
        for (int x = 4; x <= 26; x += 6) { b.set(x, 7, 9, St.lantern(true)); b.set(x, 8, 9, Blocks.CHAIN); }
        for (int z = 6; z <= 12; z += 3) for (int x = 4; x <= 26; x += 11) { b.set(x, 1, z, Blocks.OAK_FENCE); b.set(x, 2, z, Blocks.OAK_PRESSURE_PLATE); }
        for (int x = 20; x <= 28; x++) { b.set(x, 1, 13, Blocks.BOOKSHELF); b.set(x, 2, 13, Blocks.BOOKSHELF); b.set(x, 3, 13, Blocks.BOOKSHELF); }
        return b.build("Академия магии");
    }
}
