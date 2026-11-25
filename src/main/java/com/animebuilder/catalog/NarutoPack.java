package com.animebuilder.catalog;

import com.animebuilder.structure.St;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Blocks;

/** Ninja village style buildings (Hidden Leaf inspired). */
final class NarutoPack {
    private NarutoPack() {}
    static final String CAT = "Деревня ниндзя";

    static void register() {
        Catalog.register(CAT, "konoha_house", "Дом в Скрытом Листе", NarutoPack::house);
        Catalog.register(CAT, "hokage_tower", "Башня Хокаге", NarutoPack::hokageTower);
        Catalog.register(CAT, "ichiraku_ramen", "Лавка рамэна Ичираку", NarutoPack::ramen);
        Catalog.register(CAT, "ninja_dojo", "Додзё ниндзя", NarutoPack::dojo);
    }

    private static com.animebuilder.structure.Structure house() {
        B b = new B(CAT, "konoha_house");
        b.fill(0, 0, 0, 10, 0, 8, Blocks.SPRUCE_PLANKS);
        b.fill(-1, 0, -1, 11, 0, -1, Blocks.COBBLESTONE);
        b.walls(0, 1, 0, 10, 4, 8, Blocks.WHITE_TERRACOTTA);
        for (int[] c : new int[][]{{0, 0}, {10, 0}, {0, 8}, {10, 8}}) b.column(c[0], 1, 4, c[1], Blocks.DARK_OAK_LOG);
        // windows
        for (int x : new int[]{2, 4, 6, 8}) { b.set(x, 3, 0, Blocks.GLASS_PANE); b.set(x, 3, 8, Blocks.GLASS_PANE); }
        for (int z : new int[]{3, 5}) { b.set(0, 3, z, Blocks.GLASS_PANE); b.set(10, 3, z, Blocks.GLASS_PANE); }
        b.fill(0, 5, 0, 10, 5, 8, Blocks.DARK_OAK_PLANKS);
        b.fill(1, 5, 1, 9, 5, 7, Blocks.AIR);
        b.fill(0, 5, 0, 10, 5, 0, Blocks.DARK_OAK_PLANKS);
        b.pyramid(-1, -1, 11, 9, 6, Blocks.ACACIA_STAIRS, Blocks.ACACIA_SLAB);
        b.fill(1, 5, 1, 9, 5, 7, Blocks.DARK_OAK_PLANKS);
        b.door(5, 1, 0, Blocks.SPRUCE_DOOR, Direction.NORTH);
        b.set(4, 1, -1, St.stair(Blocks.COBBLESTONE_STAIRS, Direction.NORTH, false));
        b.set(5, 1, -1, St.stair(Blocks.COBBLESTONE_STAIRS, Direction.NORTH, false));
        b.set(6, 1, -1, St.stair(Blocks.COBBLESTONE_STAIRS, Direction.NORTH, false));
        b.set(4, 3, -1, St.lantern(false));
        b.set(6, 3, -1, St.lantern(false));
        // interior
        b.bed(2, 1, 6, Blocks.ORANGE_BED, Direction.NORTH);
        b.set(8, 1, 2, St.facing(Blocks.CRAFTING_TABLE, Direction.NORTH));
        b.set(8, 1, 3, St.facing(Blocks.FURNACE, Direction.WEST));
        b.set(8, 1, 6, St.facing(Blocks.CHEST, Direction.WEST));
        b.set(5, 4, 4, St.lantern(true));
        b.set(5, 5, 4, Blocks.CHAIN);
        return b.build("Дом в Скрытом Листе");
    }

    private static com.animebuilder.structure.Structure hokageTower() {
        B b = new B(CAT, "hokage_tower");
        int c = 8;
        b.disc(c, 0, c, 9, Blocks.STONE_BRICKS);
        // tier 1
        b.cylinder(c, 1, 6, c, 7, Blocks.WHITE_CONCRETE, true);
        b.disc(c, 7, c, 8, Blocks.ORANGE_TERRACOTTA);
        b.ring(c, 8, c, 8, Blocks.ORANGE_TERRACOTTA);
        // tier 2
        b.cylinder(c, 8, 12, c, 5, Blocks.WHITE_CONCRETE, true);
        b.disc(c, 13, c, 6, Blocks.ORANGE_TERRACOTTA);
        b.ring(c, 14, c, 6, Blocks.ORANGE_TERRACOTTA);
        // tier 3 / office
        b.cylinder(c, 14, 17, c, 4, Blocks.WHITE_CONCRETE, true);
        b.disc(c, 18, c, 5, Blocks.ORANGE_TERRACOTTA);
        b.disc(c, 19, c, 4, Blocks.ORANGE_TERRACOTTA);
        b.disc(c, 20, c, 3, Blocks.ORANGE_TERRACOTTA);
        b.disc(c, 21, c, 2, Blocks.ORANGE_TERRACOTTA);
        b.set(c, 22, c, Blocks.GOLD_BLOCK);
        // windows
        for (int y : new int[]{3, 4}) for (int[] p : new int[][]{{c + 7, c}, {c - 7, c}, {c, c + 7}, {c, c - 7}}) b.set(p[0], y, p[1], Blocks.GLASS_PANE);
        for (int[] p : new int[][]{{c + 5, c}, {c - 5, c}, {c, c + 5}, {c, c - 5}}) b.set(p[0], 10, p[1], Blocks.GLASS_PANE);
        for (int[] p : new int[][]{{c + 4, c}, {c - 4, c}, {c, c + 4}, {c, c - 4}}) b.set(p[0], 16, p[1], Blocks.GLASS_PANE);
        // entrance
        b.clear(c - 1, 1, c - 8, c + 1, 3, c - 6);
        b.door(c, 1, c - 7, Blocks.SPRUCE_DOOR, Direction.NORTH);
        b.fill(c - 1, 1, c - 9, c + 1, 1, c - 8, Blocks.STONE_BRICK_STAIRS);
        // interior
        b.fill(c - 3, 1, c - 3, c + 3, 1, c + 3, Blocks.SPRUCE_PLANKS);
        b.set(c, 15, c + 3, St.facing(Blocks.LECTERN, Direction.NORTH));
        b.set(c + 1, 15, c + 3, Blocks.BOOKSHELF);
        b.set(c - 1, 15, c + 3, Blocks.BOOKSHELF);
        b.set(c, 15, c - 2, Blocks.SPRUCE_PLANKS);
        b.ladder(c + 2, 1, 12, c + 2, Direction.NORTH);
        return b.build("Башня Хокаге");
    }

    private static com.animebuilder.structure.Structure ramen() {
        B b = new B(CAT, "ichiraku_ramen");
        b.fill(0, 0, 0, 8, 0, 6, Blocks.SPRUCE_PLANKS);
        b.fill(0, 1, 6, 8, 3, 6, Blocks.WHITE_TERRACOTTA);
        b.fill(0, 1, 0, 0, 3, 6, Blocks.WHITE_TERRACOTTA);
        b.fill(8, 1, 0, 8, 3, 6, Blocks.WHITE_TERRACOTTA);
        for (int z : new int[]{2, 4}) { b.set(0, 2, z, Blocks.GLASS_PANE); b.set(8, 2, z, Blocks.GLASS_PANE); }
        b.fill(-1, 4, -1, 9, 4, 7, Blocks.DARK_OAK_PLANKS);
        for (int x = -1; x <= 9; x++) b.set(x, 3, -1, (x % 2 == 0) ? Blocks.RED_WOOL : Blocks.WHITE_WOOL);
        for (int x = 0; x <= 8; x++) b.set(x, 1, 1, St.stair(Blocks.SPRUCE_STAIRS, Direction.SOUTH, false));
        for (int x = 1; x <= 7; x += 2) b.set(x, 1, 0, Blocks.BARREL);
        b.set(2, 2, 5, Blocks.CAULDRON);
        b.set(3, 2, 5, St.waterCauldron(3));
        b.set(5, 1, 5, St.facing(Blocks.SMOKER, Direction.NORTH));
        b.set(6, 1, 5, St.facing(Blocks.BARREL, Direction.NORTH));
        b.set(7, 1, 5, St.facing(Blocks.CHEST, Direction.NORTH));
        b.set(1, 1, 5, St.facing(Blocks.CRAFTING_TABLE, Direction.NORTH));
        for (int x : new int[]{1, 4, 7}) b.set(x, 3, 0, St.lantern(true));
        b.fill(0, 1, 1, 0, 1, 1, Blocks.AIR);
        b.fill(3, 1, 6, 5, 2, 6, Blocks.AIR);
        b.set(4, 2, 6, Blocks.WHITE_STAINED_GLASS_PANE);
        return b.build("Лавка рамэна Ичираку");
    }

    private static com.animebuilder.structure.Structure dojo() {
        B b = new B(CAT, "ninja_dojo");
        b.fill(0, 0, 0, 14, 0, 10, Blocks.STONE_BRICKS);
        b.fill(1, 1, 1, 13, 1, 9, Blocks.BAMBOO_PLANKS);
        b.fill(2, 1, 2, 12, 1, 8, Blocks.WHITE_CARPET);
        b.walls(1, 2, 1, 13, 5, 9, Blocks.BAMBOO_PLANKS);
        for (int[] c : new int[][]{{1, 1}, {13, 1}, {1, 9}, {13, 9}, {7, 1}, {7, 9}}) b.column(c[0], 2, 5, c[1], Blocks.DARK_OAK_LOG);
        for (int x = 3; x <= 11; x += 2) { b.set(x, 4, 1, Blocks.WHITE_STAINED_GLASS_PANE); b.set(x, 4, 9, Blocks.WHITE_STAINED_GLASS_PANE); }
        b.clear(6, 2, 1, 8, 3, 1);
        b.fill(6, 2, 1, 8, 3, 1, Blocks.AIR);
        b.roofX(-1, 15, -1, 11, 6, Blocks.DARK_OAK_STAIRS, Blocks.DARK_OAK_SLAB);
        b.fill(0, 6, 0, 14, 6, 10, Blocks.DARK_OAK_PLANKS);
        b.fill(2, 6, 2, 12, 6, 8, Blocks.AIR);
        b.fill(2, 6, 2, 12, 6, 8, Blocks.DARK_OAK_PLANKS);
        // training dummies
        for (int x : new int[]{4, 7, 10}) {
            b.set(x, 2, 6, Blocks.OAK_FENCE);
            b.set(x, 3, 6, Blocks.HAY_BLOCK);
            b.set(x, 4, 6, Blocks.CARVED_PUMPKIN);
        }
        b.set(2, 4, 5, St.lantern(true)); b.set(12, 4, 5, St.lantern(true));
        b.fill(2, 5, 5, 2, 5, 5, Blocks.CHAIN);
        b.fill(12, 5, 5, 12, 5, 5, Blocks.CHAIN);
        b.set(7, 0, 0, Blocks.STONE_BRICK_STAIRS);
        return b.build("Додзё ниндзя");
    }
}
