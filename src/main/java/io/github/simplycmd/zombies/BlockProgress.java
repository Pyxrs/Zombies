package io.github.simplycmd.zombies;

import com.sun.org.apache.xpath.internal.operations.Bool;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.village.VillagerProfession;

public class BlockProgress {
    BlockState blockstate;
    BlockPos pos;
    ServerWorld world;

    Double damage;

    public BlockProgress(BlockState blockstate, BlockPos pos, ServerWorld world) {
        this.blockstate = blockstate;
        this.pos = pos;
        this.world = world;
    }

    public double getDamage() {
        if (stillThere(pos, blockstate, world)) return damage;
        else return 0;
    }

    public void damage() {
        if (stillThere(pos, blockstate, world)) damage--;
        if (damage <= 0) {
            world.playSound(pos.getX(), pos.getY(), pos.getZ(), blockstate.getSoundGroup().getBreakSound(), SoundCategory.BLOCKS, 1F, 1.0F, false);
            world.setBlockState(pos, Blocks.AIR.getDefaultState());
        }
    }

    public BlockState getBlockState() {
        return blockstate;
    }

    public BlockPos getPos() {
        return pos;
    }

    public static Boolean stillThere(BlockPos pos, BlockState state, ServerWorld world) {
        return world.getBlockState(pos) == state;
    }
}
