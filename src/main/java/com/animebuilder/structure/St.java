package com.animebuilder.structure;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.*;

/** Factories for directional block states. */
public final class St {
    private St() {}

    public static BlockState stair(Block b, Direction facing, boolean top) {
        return b.defaultBlockState().setValue(StairBlock.FACING, facing).setValue(StairBlock.HALF, top ? Half.TOP : Half.BOTTOM);
    }
    public static BlockState slab(Block b, boolean top) {
        return b.defaultBlockState().setValue(SlabBlock.TYPE, top ? SlabType.TOP : SlabType.BOTTOM);
    }
    public static BlockState log(Block b, Direction.Axis axis) {
        return b.defaultBlockState().setValue(RotatedPillarBlock.AXIS, axis);
    }
    public static BlockState lantern(boolean hanging) {
        return Blocks.LANTERN.defaultBlockState().setValue(LanternBlock.HANGING, hanging);
    }
    public static BlockState leaves(Block b) {
        return b.defaultBlockState().setValue(LeavesBlock.PERSISTENT, true);
    }
    public static BlockState waterCauldron(int level) {
        return Blocks.WATER_CAULDRON.defaultBlockState().setValue(LayeredCauldronBlock.LEVEL, level);
    }
    public static BlockState trapdoor(Block b, Direction facing, boolean open, boolean top) {
        return b.defaultBlockState().setValue(TrapDoorBlock.FACING, facing)
                .setValue(TrapDoorBlock.OPEN, open).setValue(TrapDoorBlock.HALF, top ? Half.TOP : Half.BOTTOM);
    }
    public static BlockState door(Block b, Direction facing, boolean upper) {
        return b.defaultBlockState().setValue(DoorBlock.FACING, facing)
                .setValue(DoorBlock.HALF, upper ? DoubleBlockHalf.UPPER : DoubleBlockHalf.LOWER);
    }
    public static BlockState bed(Block b, Direction facing, boolean head) {
        return b.defaultBlockState().setValue(BedBlock.FACING, facing).setValue(BedBlock.PART, head ? BedPart.HEAD : BedPart.FOOT);
    }
    /** Generic facing for furnaces, chests, anvils, ladders, barrels, torches etc. */
    public static BlockState facing(Block b, Direction d) {
        BlockState s = b.defaultBlockState();
        if (s.hasProperty(BlockStateProperties.HORIZONTAL_FACING) && d.getAxis().isHorizontal())
            return s.setValue(BlockStateProperties.HORIZONTAL_FACING, d);
        if (s.hasProperty(BlockStateProperties.FACING)) return s.setValue(BlockStateProperties.FACING, d);
        return s;
    }
}
