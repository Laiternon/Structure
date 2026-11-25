package com.animebuilder.catalog;

import com.animebuilder.structure.St;
import com.animebuilder.structure.Structure;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Blocks;

/** Laboratories, forges, workshops. */
final class ScienceAndForgePack {
    private ScienceAndForgePack() {}
    static final String CAT = "Лаборатории и кузницы";

    static void register() {
        Catalog.register(CAT, "gadget_lab", "Лаборатория гаджетов", ScienceAndForgePack::gadgetLab);
        Catalog.register(CAT, "alchemist_workshop", "Мастерская алхимика", ScienceAndForgePack::alchemist);
        Catalog.register(CAT, "swordsmith_forge", "Кузница мастера мечей", ScienceAndForgePack::swordForge);
        Catalog.register(CAT, "stone_age_science_lab", "Лаборатория науки из камня", ScienceAndForgePack::scienceLab);
        Catalog.register(CAT, "mecha_hangar", "Ангар боевых мехов", ScienceAndForgePack::hangar);
    }

    private static Structure gadgetLab() {
        B b = new B(CAT, "gadget_lab");
        b.fill(0, 0, 0, 10, 0, 8, Blocks.SMOOTH_STONE);
        b.walls(0, 1, 0, 10, 4, 8, Blocks.LIGHT_GRAY_CONCRETE);
        b.fill(0, 5, 0, 10, 5, 8, Blocks.GRAY_CONCRETE);
        b.fill(0, 6, 0, 10, 6, 8, Blocks.GRAY_CONCRETE_POWDER);
        b.walls(0, 6, 0, 10, 6, 8, Blocks.IRON_BARS);
        b.door(5, 1, 0, Blocks.IRON_DOOR, Direction.NORTH);
        for (int x : new int[]{2, 3, 7, 8}) { b.set(x, 3, 0, Blocks.LIGHT_BLUE_STAINED_GLASS_PANE); }
        b.set(10, 3, 4, Blocks.LIGHT_BLUE_STAINED_GLASS_PANE);
        // workstations along back wall
        for (int x = 1; x <= 9; x++) b.set(x, 1, 7, Blocks.IRON_BLOCK);
        b.set(2, 2, 7, Blocks.OBSERVER); b.set(3, 2, 7, Blocks.REDSTONE_LAMP);
        b.set(4, 2, 7, Blocks.DISPENSER); b.set(6, 2, 7, Blocks.REDSTONE_LAMP);
        b.set(7, 2, 7, Blocks.TARGET); b.set(8, 2, 7, Blocks.NOTE_BLOCK);
        b.set(2, 3, 7, Blocks.GLASS); b.set(8, 3, 7, Blocks.GLASS);
        // central lab table
        b.fill(4, 1, 3, 6, 1, 4, Blocks.SMOOTH_QUARTZ);
        b.set(4, 2, 3, Blocks.BREWING_STAND); b.set(6, 2, 4, Blocks.CAULDRON);
        b.set(5, 2, 3, Blocks.LEVER);
        // storage, bed, microwave analog
        b.set(1, 1, 1, St.facing(Blocks.SMOKER, Direction.SOUTH));
        b.set(9, 1, 1, St.facing(Blocks.BLAST_FURNACE, Direction.SOUTH));
        b.set(1, 1, 3, St.facing(Blocks.CHEST, Direction.EAST));
        b.set(1, 1, 4, St.facing(Blocks.CHEST, Direction.EAST));
        b.bed(9, 1, 3, Blocks.WHITE_BED, Direction.SOUTH);
        b.set(5, 4, 4, Blocks.SEA_LANTERN);
        b.set(1, 4, 1, Blocks.SEA_LANTERN); b.set(9, 4, 1, Blocks.SEA_LANTERN);
        // antenna
        b.fill(5, 7, 4, 5, 12, 4, Blocks.IRON_BARS);
        b.set(5, 13, 4, Blocks.LIGHTNING_ROD);
        b.set(6, 9, 4, Blocks.IRON_BARS); b.set(4, 9, 4, Blocks.IRON_BARS);
        return b.build("Лаборатория гаджетов");
    }

    private static Structure alchemist() {
        B b = new B(CAT, "alchemist_workshop");
        int c = 8;
        b.disc(c, 0, c, 9, Blocks.STONE_BRICKS);
        b.ring(c, 0, c, 6, Blocks.CHISELED_STONE_BRICKS);
        b.ring(c, 0, c, 4, Blocks.RED_CONCRETE);
        // simple transmutation star
        for (int i = -4; i <= 4; i++) {
            b.set(c + i, 0, c + i, Blocks.RED_CONCRETE);
            b.set(c + i, 0, c - i, Blocks.RED_CONCRETE);
        }
        b.set(c, 0, c, Blocks.GOLD_BLOCK);
        // surrounding walls/pillars
        for (int i = 0; i < 8; i++) {
            double a = i * Math.PI / 4;
            int x = c + (int) Math.round(Math.cos(a) * 8), z = c + (int) Math.round(Math.sin(a) * 8);
            b.column(x, 1, 6, z, Blocks.POLISHED_ANDESITE);
            b.set(x, 7, z, Blocks.CHISELED_STONE_BRICKS);
            b.set(x, 5, z, St.lantern(false).getBlock().defaultBlockState());
        }
        b.ring(c, 7, c, 8, Blocks.STONE_BRICK_SLAB);
        // book/work area in the corner
        b.set(c - 7, 1, c - 2, St.facing(Blocks.LECTERN, Direction.EAST));
        b.set(c - 7, 1, c + 2, Blocks.BOOKSHELF);
        b.set(c - 7, 2, c + 2, Blocks.BOOKSHELF);
        b.set(c + 7, 1, c - 2, Blocks.BREWING_STAND);
        b.set(c + 7, 1, c + 1, Blocks.CAULDRON);
        b.set(c + 7, 1, c + 2, St.facing(Blocks.CHEST, Direction.WEST));
        b.set(c + 7, 1, c - 3, St.facing(Blocks.SMITHING_TABLE, Direction.WEST));
        b.set(c, 1, c - 8, St.facing(Blocks.ANVIL, Direction.EAST));
        return b.build("Мастерская алхимика");
    }

    private static Structure swordForge() {
        B b = new B(CAT, "swordsmith_forge");
        b.fill(0, 0, 0, 12, 0, 9, Blocks.COBBLESTONE);
        b.fill(1, 1, 1, 11, 1, 8, Blocks.STONE_BRICKS);
        for (int[] c : new int[][]{{1, 1}, {11, 1}, {1, 8}, {11, 8}, {6, 1}, {6, 8}}) b.column(c[0], 2, 5, c[1], Blocks.DARK_OAK_LOG);
        b.fill(0, 6, 0, 12, 6, 9, Blocks.DARK_OAK_PLANKS);
        b.roofX(-1, 13, -1, 10, 7, Blocks.DARK_OAK_STAIRS, Blocks.DARK_OAK_SLAB);
        b.walls(1, 2, 8, 11, 5, 8, Blocks.BLACKSTONE);
        // forge area
        b.set(3, 2, 7, St.facing(Blocks.BLAST_FURNACE, Direction.SOUTH));
        b.set(4, 2, 7, St.facing(Blocks.BLAST_FURNACE, Direction.SOUTH));
        b.set(5, 2, 7, St.facing(Blocks.FURNACE, Direction.SOUTH));
        b.fill(4, 3, 7, 4, 5, 7, Blocks.BRICKS);
        b.set(8, 2, 7, Blocks.LAVA_CAULDRON);
        b.set(9, 2, 7, Blocks.CAULDRON);
        b.set(10, 2, 7, St.waterCauldron(3));
        b.set(3, 2, 3, St.facing(Blocks.ANVIL, Direction.EAST));
        b.set(9, 2, 3, St.facing(Blocks.GRINDSTONE, Direction.SOUTH));
        b.set(9, 2, 4, St.facing(Blocks.SMITHING_TABLE, Direction.SOUTH));
        b.set(6, 2, 4, St.facing(Blocks.CHEST, Direction.SOUTH));
        b.set(6, 2, 5, St.facing(Blocks.BARREL, Direction.SOUTH));
        b.fill(2, 2, 1, 10, 2, 1, Blocks.OAK_FENCE);
        for (int x : new int[]{3, 6, 9}) b.set(x, 5, 1, St.lantern(true));
        b.set(2, 3, 1, Blocks.RED_WOOL);
        b.set(10, 3, 1, Blocks.RED_WOOL);
        // chimney
        b.fill(4, 8, 7, 4, 11, 7, Blocks.BRICKS);
        b.set(4, 12, 7, Blocks.CAMPFIRE);
        return b.build("Кузница мастера мечей");
    }

    private static Structure scienceLab() {
        B b = new B(CAT, "stone_age_science_lab");
        b.fill(0, 0, 0, 12, 0, 10, Blocks.STONE);
        b.walls(0, 1, 0, 12, 4, 10, Blocks.COBBLESTONE);
        for (int x : new int[]{3, 6, 9}) { b.set(x, 3, 0, Blocks.GLASS); b.set(x, 3, 10, Blocks.GLASS); }
        b.fill(0, 5, 0, 12, 5, 10, Blocks.OXIDIZED_CUT_COPPER);
        b.roofX(-1, 13, -1, 11, 6, Blocks.OXIDIZED_CUT_COPPER_STAIRS, Blocks.OXIDIZED_CUT_COPPER_SLAB);
        b.door(6, 1, 0, Blocks.OAK_DOOR, Direction.NORTH);
        // copper tools & lab gear
        b.set(2, 1, 8, Blocks.COPPER_BLOCK);
        b.set(3, 1, 8, Blocks.COPPER_BLOCK);
        b.set(2, 2, 8, Blocks.BREWING_STAND);
        b.set(3, 2, 8, Blocks.CAULDRON);
        b.set(5, 1, 8, St.facing(Blocks.BLAST_FURNACE, Direction.SOUTH));
        b.set(6, 1, 8, St.facing(Blocks.FURNACE, Direction.SOUTH));
        b.set(7, 1, 8, St.facing(Blocks.SMOKER, Direction.SOUTH));
        b.set(10, 1, 8, St.facing(Blocks.CHEST, Direction.WEST));
        b.set(10, 1, 7, St.facing(Blocks.CHEST, Direction.WEST));
        b.fill(8, 1, 3, 11, 1, 4, Blocks.OAK_PLANKS);
        b.set(9, 2, 3, Blocks.GLASS);
        b.set(10, 2, 4, Blocks.POTTED_RED_MUSHROOM);
        b.set(1, 1, 2, Blocks.BOOKSHELF);
        b.set(1, 2, 2, Blocks.BOOKSHELF);
        b.set(1, 1, 3, Blocks.BOOKSHELF);
        b.set(1, 2, 3, Blocks.BOOKSHELF);
        b.set(6, 4, 5, St.lantern(true));
        b.set(6, 5, 5, Blocks.CHAIN);
        return b.build("Лаборатория науки из камня");
    }

    private static Structure hangar() {
        B b = new B(CAT, "mecha_hangar");
        b.fill(0, 0, 0, 24, 0, 18, Blocks.GRAY_CONCRETE);
        b.walls(0, 1, 0, 24, 12, 18, Blocks.WHITE_CONCRETE);
        for (int y = 1; y <= 12; y += 4) b.fill(0, y, 0, 24, y, 0, Blocks.LIGHT_GRAY_CONCRETE);
        for (int x = 0; x <= 24; x += 6) b.column(x, 1, 12, 0, Blocks.IRON_BLOCK);
        b.fill(0, 13, 0, 24, 13, 18, Blocks.GRAY_CONCRETE);
        b.fill(6, 1, 0, 18, 9, 0, Blocks.AIR);
        b.fill(6, 10, 0, 18, 10, 0, Blocks.IRON_BLOCK);
        b.fill(7, 1, 0, 17, 9, 0, Blocks.IRON_BARS);
        // crane rail
        b.fill(2, 11, 2, 22, 11, 2, Blocks.IRON_BARS);
        b.fill(2, 11, 16, 22, 11, 16, Blocks.IRON_BARS);
        // control room
        b.walls(1, 1, 12, 8, 4, 17, Blocks.GRAY_CONCRETE);
        b.fill(1, 5, 12, 8, 5, 17, Blocks.GRAY_CONCRETE);
        b.set(4, 3, 12, Blocks.LIGHT_BLUE_STAINED_GLASS_PANE);
        b.set(5, 3, 12, Blocks.LIGHT_BLUE_STAINED_GLASS_PANE);
        b.set(2, 1, 16, Blocks.REDSTONE_LAMP);
        b.set(3, 1, 16, Blocks.OBSERVER);
        b.set(4, 1, 16, Blocks.DISPENSER);
        // simple mecha statue silhouette
        b.fill(12, 1, 8, 13, 3, 9, Blocks.WHITE_CONCRETE); // legs
        b.fill(11, 4, 8, 14, 7, 10, Blocks.WHITE_CONCRETE); // torso
        b.fill(14, 5, 9, 15, 8, 9, Blocks.BLUE_CONCRETE);
        b.fill(10, 5, 9, 10, 8, 9, Blocks.BLUE_CONCRETE);
        b.fill(12, 8, 9, 13, 9, 9, Blocks.RED_CONCRETE); // head
        b.set(12, 9, 8, Blocks.SEA_LANTERN);
        b.set(13, 9, 8, Blocks.SEA_LANTERN);
        for (int x : new int[]{4, 12, 20}) { b.set(x, 12, 6, Blocks.SEA_LANTERN); b.set(x, 12, 12, Blocks.SEA_LANTERN); }
        return b.build("Ангар боевых мехов");
    }
}
