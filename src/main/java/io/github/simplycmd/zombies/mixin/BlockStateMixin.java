package io.github.simplycmd.zombies.mixin;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.MapCodec;
import io.github.simplycmd.zombies.BlockProgress;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Property;
import org.spongepowered.asm.mixin.Mixin;

//@Mixin(BlockState.class)
public class BlockStateMixin extends BlockState {
    //BlockProgress progress = new BlockProgress(this, );

    public BlockStateMixin(Block block, ImmutableMap<Property<?>, Comparable<?>> immutableMap, MapCodec<BlockState> mapCodec) {
        super(block, immutableMap, mapCodec);
    }

    //@Override
    //public BlockProgress getProgress() {
        //return progress;
    //}
}
