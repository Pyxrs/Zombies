package io.github.simplycmd.zombies.mixin;

import io.github.simplycmd.zombies.Main;
import io.github.simplycmd.zombies.access.ZombieVillagerEntityAccess;
import net.minecraft.entity.*;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.ZombieVillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.collection.TypeFilterableList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.village.VillagerData;
import net.minecraft.village.VillagerType;
import net.minecraft.world.SpawnHelper;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.WorldChunk;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;

@Mixin(SpawnHelper.class)
public class SpawnHelperMixin {
    /**
     * @author SimplyCmd
     */
    @Overwrite
    @Nullable
    private static SpawnSettings.SpawnEntry pickRandomSpawnEntry(ServerWorld serverWorld, StructureAccessor structureAccessor, ChunkGenerator chunkGenerator, SpawnGroup spawnGroup, Random random, BlockPos blockPos) {
        ZombieVillagerEntity entity = new ZombieVillagerEntity(EntityType.ZOMBIE_VILLAGER, serverWorld);
        entity.updatePosition(blockPos.getX(), blockPos.getY(), blockPos.getZ());
        entity.setVillagerData(new VillagerData(VillagerType.PLAINS, ((ZombieVillagerEntityAccess)entity).getZombType().getProfession(), 0));
        float blood = 0;
        if (Main.blood_moon_night) {
            blood = 0.1F;
        }
        if (isGudSpawn(entity, serverWorld, blockPos) && Math.random() < (Main.increaseByDay(0.1, 0.8, 500D) + blood)) {
            serverWorld.spawnEntityAndPassengers(entity);
        }
        return null;
    }

    private static Boolean isGudSpawn(Entity entity, ServerWorld world, BlockPos pos) {
        double x = pos.getX();
        double y = pos.getY();
        double z = pos.getZ();
        PlayerEntity playerEntity = world.getClosestPlayer(x, y, z, -1.0D, false);
        if (playerEntity != null) {
            double squaredDistance = playerEntity.squaredDistanceTo(x, y, z);

            // Entity limit
            WorldChunk chunk = (WorldChunk) world.getChunk(pos);
            Iterator<TypeFilterableList<Entity>> entities = Arrays.stream(chunk.getEntitySectionArray()).iterator();
            int total_entities = 0;
            while (entities.hasNext()) {
                TypeFilterableList<Entity> list = entities.next();
                total_entities += list.size();
            }

            if (SpawnHelper.canSpawn(SpawnRestriction.Location.ON_GROUND, world, pos, entity.getType()) && total_entities <= Main.increaseByDay(1, 5, 250)) {
                return squaredDistance < (double) (entity.getType().getSpawnGroup().getImmediateDespawnRange() * entity.getType().getSpawnGroup().getImmediateDespawnRange());
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
