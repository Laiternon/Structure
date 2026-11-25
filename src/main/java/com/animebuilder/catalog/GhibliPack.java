package com.animebuilder.catalog;

import com.animebuilder.structure.St;
import com.animebuilder.structure.Structure;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Blocks;

/** Cozy / magical-fairytale buildings (Ghibli-like). */
final class GhibliPack {
    private GhibliPack() {}
    static final String CAT = "Волшебные сказки";

    static void register() {
        Catalog.register(CAT, "spirit_bathhouse", "Купальня духов", GhibliPack::bathhouse);
        Catalog.register(CAT, "forest_spirit_tree", "Дом в дереве лесного духа", GhibliPack::treeHouse);
        Catalog.register(CAT, "witch_bakery", "Пекарня юной ведьмы", GhibliPack::bakery);
        Catalog.register(CAT, "wizard_cottage", "Коттедж странствующего мага", GhibliPack::cottage);
    }

    private static Structure bathhouse() {
        B b = new B(CAT, "spirit_bathhouse");
        b.fill(0, 0, 0, 22, 0, 14, Blocks.STONE_BRICKS);
        // ground floor
        b.walls(1, 1, 1, 21, 6, 13, Blocks.RED_NETHER_BRICKS);
        b.fill(1, 7, 1, 21, 7, 13, Blocks.DARK_OAK_PLANKS);
        for (int x = 3; x <= 19; x += 4) for (int y : new int[]{3, 4}) { b.set(x, y, 1, Blocks.GLASS_PANE); b.set(x, y, 13, Blocks.GLASS_PANE); }
        b.fill(-1, 7, -1, 23, 7, 15, Blocks.CUT_COPPER);
        b.roofX(-2, 24, -2, 16, 8, Blocks.CUT_COPPER_STAIRS, Blocks.CUT_COPPER_SLAB);
        // second tier
        b.walls(4, 8, 4, 18, 13, 10, Blocks.RED_CONCRETE);
        for (int x = 6; x <= 16; x += 2) { b.set(x, 10, 4, Blocks.GLASS_PANE); b.set(x, 10, 10, Blocks.GLASS_PANE); b.set(x, 11, 4, Blocks.GLASS_PANE); b.set(x, 11, 10, Blocks.GLASS_PANE); }
        b.fill(3, 14, 3, 19, 14, 11, Blocks.CUT_COPPER);
        b.roofX(2, 20, 2, 12, 15, Blocks.CUT_COPPER_STAIRS, Blocks.CUT_COPPER_SLAB);
        // top tower
        b.walls(8, 16, 5, 14, 20, 9, Blocks.RED_TERRACOTTA);
        for (int[] p : new int[][]{{11, 5}, {11, 9}, {8, 7}, {14, 7}}) b.set(p[0], 18, p[1], Blocks.GLASS_PANE);
        b.pyramid(6, 3, 16, 11, 21, Blocks.CUT_COPPER_STAIRS, Blocks.CUT_COPPER_SLAB);
        b.set(11, 24, 7, Blocks.GOLD_BLOCK);
        // entrance bridge
        b.fill(9, 0, -6, 13, 0, -1, Blocks.RED_TERRACOTTA);
        for (int z = -6; z <= -1; z++) { b.set(9, 1, z, Blocks.CRIMSON_FENCE); b.set(13, 1, z, Blocks.CRIMSON_FENCE); }
        for (int z : new int[]{-6, -3}) { b.set(9, 2, z, St.lantern(false)); b.set(13, 2, z, St.lantern(false)); }
        b.clear(10, 1, 1, 12, 4, 1);
        b.fill(10, 1, 1, 12, 4, 1, Blocks.AIR);
        b.door(11, 1, 1, Blocks.CRIMSON_DOOR, Direction.NORTH);
        // lanterns along eaves
        for (int x = 2; x <= 20; x += 3) b.set(x, 6, 0, St.lantern(true));
        // interior
        b.fill(3, 1, 3, 19, 1, 11, Blocks.SPRUCE_PLANKS);
        b.fill(14, 1, 4, 19, 1, 8, Blocks.WATER);
        b.walls(13, 1, 3, 20, 1, 9, Blocks.POLISHED_ANDESITE);
        b.fill(14, 1, 4, 19, 1, 8, Blocks.WATER);
        return b.build("Купальня духов");
    }

    private static Structure treeHouse() {
        B b = new B(CAT, "forest_spirit_tree");
        int c = 8;
        // roots
        for (int[] p : new int[][]{{3, 0}, {-3, 0}, {0, 3}, {0, -3}}) {
            b.fill(c + p[0], 0, c + p[1], c + p[0], 1, c + p[1], Blocks.OAK_LOG);
        }
        // trunk
        b.cylinder(c, 0, 14, c, 2.2, Blocks.OAK_LOG, false);
        // hollow room
        b.cylinder(c, 1, 4, c, 1.2, Blocks.AIR, false);
        b.clear(c, 1, c - 3, c, 2, c - 2);
        b.door(c, 1, c - 2, Blocks.OAK_DOOR, Direction.NORTH);
        b.fill(c - 1, 0, c - 1, c + 1, 0, c + 1, Blocks.MOSS_BLOCK);
        b.set(c, 1, c + 1, St.facing(Blocks.CRAFTING_TABLE, Direction.NORTH));
        b.bed(c - 1, 1, c, Blocks.GREEN_BED, Direction.SOUTH);
        b.set(c + 1, 3, c, St.lantern(false));
        // canopy
        b.sphere(c, 17, c, 7, Blocks.OAK_LEAVES, false);
        b.sphere(c + 5, 14, c + 2, 4, Blocks.OAK_LEAVES, false);
        b.sphere(c - 5, 14, c - 2, 4, Blocks.OAK_LEAVES, false);
        b.cylinder(c, 9, 17, c, 1.2, Blocks.OAK_LOG, false);
        // branch platform & rope
        b.fill(c + 3, 7, c - 1, c + 6, 7, c + 1, Blocks.OAK_PLANKS);
        b.set(c + 6, 8, c - 1, Blocks.OAK_FENCE);
        b.set(c + 6, 8, c + 1, Blocks.OAK_FENCE);
        b.set(c + 6, 9, c + 1, St.lantern(false));
        // ground details
        b.set(c + 4, 0, c - 4, Blocks.MOSS_BLOCK);
        b.set(c + 4, 1, c - 4, Blocks.AZALEA);
        b.set(c - 4, 1, c + 4, Blocks.FLOWERING_AZALEA);
        return b.build("Дом в дереве лесного духа");
    }

    private static Structure bakery() {
        B b = new B(CAT, "witch_bakery");
        b.fill(0, 0, 0, 8, 0, 8, Blocks.COBBLESTONE);
        b.walls(0, 1, 0, 8, 4, 8, Blocks.WHITE_TERRACOTTA);
        b.fill(0, 1, 0, 8, 1, 0, Blocks.STONE_BRICKS);
        for (int[] c : new int[][]{{0, 0}, {8, 0}, {0, 8}, {8, 8}}) b.column(c[0], 1, 4, c[1], Blocks.STRIPPED_OAK_LOG);
        b.fill(0, 5, 0, 8, 5, 8, Blocks.OAK_PLANKS);
        b.roofX(-1, 9, -1, 9, 6, Blocks.RED_NETHER_BRICK_STAIRS, Blocks.RED_NETHER_BRICK_SLAB);
        // storefront
        b.fill(2, 2, 0, 6, 3, 0, Blocks.GLASS_PANE);
        b.door(4, 1, 0, Blocks.OAK_DOOR, Direction.NORTH);
        b.set(3, 2, 0, Blocks.GLASS_PANE);
        b.fill(1, 2, 0, 1, 3, 0, Blocks.WHITE_TERRACOTTA);
        b.set(2, 4, -1, St.trapdoor(Blocks.OAK_TRAPDOOR, Direction.NORTH, true, false));
        b.set(6, 4, -1, St.trapdoor(Blocks.OAK_TRAPDOOR, Direction.NORTH, true, false));
        for (int x = 1; x <= 7; x++) b.set(x, 4, -1, (x % 2 == 0) ? Blocks.RED_WOOL : Blocks.WHITE_WOOL);
        // inside: counter, ovens
        for (int x = 2; x <= 6; x++) b.set(x, 1, 2, St.stair(Blocks.OAK_STAIRS, Direction.SOUTH, false));
        b.set(1, 1, 6, St.facing(Blocks.FURNACE, Direction.EAST));
        b.set(1, 1, 7, St.facing(Blocks.SMOKER, Direction.EAST));
        b.set(7, 1, 6, St.facing(Blocks.BARREL, Direction.WEST));
        b.set(7, 1, 7, St.facing(Blocks.CHEST, Direction.WEST));
        b.set(4, 1, 6, St.facing(Blocks.CRAFTING_TABLE, Direction.NORTH));
        b.set(4, 4, 4, St.lantern(true));
        b.set(4, 5, 4, Blocks.CHAIN);
        // chimney
        b.fill(7, 6, 6, 7, 10, 6, Blocks.BRICKS);
        b.set(7, 11, 6, Blocks.CAMPFIRE);
        // flower boxes
        b.set(0, 2, -1, Blocks.FLOWER_POT);
        b.set(8, 2, -1, Blocks.FLOWER_POT);
        // delivery broom stand
        b.fill(10, 0, 1, 10, 3, 1, Blocks.OAK_FENCE);
        b.set(10, 4, 1, St.lantern(false));
        return b.build("Пекарня юной ведьмы");
    }

    private static Structure cottage() {
        B b = new B(CAT, "wizard_cottage");
        b.fill(0, 0, 0, 6, 0, 6, Blocks.MOSSY_COBBLESTONE);
        b.walls(0, 1, 0, 6, 3, 6, Blocks.OAK_PLANKS);
        for (int[] c : new int[][]{{0, 0}, {6, 0}, {0, 6}, {6, 6}}) b.column(c[0], 1, 3, c[1], Blocks.OAK_LOG);
        b.fill(0, 4, 0, 6, 4, 6, Blocks.SPRUCE_PLANKS);
        b.roofZ(-1, 7, -1, 7, 5, Blocks.SPRUCE_STAIRS, Blocks.SPRUCE_SLAB);
        b.door(3, 1, 0, Blocks.SPRUCE_DOOR, Direction.NORTH);
        b.set(1, 2, 0, Blocks.GLASS_PANE); b.set(5, 2, 0, Blocks.GLASS_PANE);
        b.set(0, 2, 3, Blocks.GLASS_PANE); b.set(6, 2, 3, Blocks.GLASS_PANE);
        // magic tower
        b.walls(7, 1, 3, 10, 9, 6, Blocks.STONE_BRICKS);
        b.set(8, 4, 3, Blocks.GLASS_PANE); b.set(9, 7, 3, Blocks.GLASS_PANE); b.set(10, 5, 5, Blocks.GLASS_PANE);
        b.fill(7, 10, 3, 10, 10, 6, Blocks.DARK_OAK_PLANKS);
        b.pyramid(6, 2, 11, 7, 11, Blocks.DARK_OAK_STAIRS, Blocks.DARK_OAK_SLAB);
        b.set(8, 1, 4, St.facing(Blocks.ENCHANTING_TABLE, Direction.NORTH));
        b.set(9, 1, 4, Blocks.BOOKSHELF);
        b.set(9, 1, 5, Blocks.BOOKSHELF);
        b.set(8, 1, 5, Blocks.BREWING_STAND);
        b.set(1, 1, 5, St.facing(Blocks.CHEST, Direction.EAST));
        b.set(1, 1, 4, St.facing(Blocks.CRAFTING_TABLE, Direction.NORTH));
        b.bed(5, 1, 4, Blocks.PURPLE_BED, Direction.NORTH);
        b.set(3, 3, 3, St.lantern(true));
        return b.build("Коттедж странствующего мага");
    }
}
