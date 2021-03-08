package io.github.simplycmd.zombies.mixin;

import io.github.simplycmd.zombies.Main;
import io.github.simplycmd.zombies.ZombieType;
import io.github.simplycmd.zombies.access.ZombieVillagerEntityAccess;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.mob.ZombieVillagerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(ZombieVillagerEntity.class)
public abstract class ZombieVillagerEntityMixin extends ZombieEntity implements ZombieVillagerEntityAccess {
    int tick_counter = 0;
    public ZombieType type;

    public ZombieVillagerEntityMixin(World world) {
        super(world);
    }

    @Override
    protected boolean burnsInDaylight() {
        return false;
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    public void init(EntityType<? extends ZombieVillagerEntity> entityType, World world, CallbackInfo ci) {
        type = ZombieType.determineType();

        // Add attributes
        Objects.requireNonNull(this.getAttributeInstance(EntityAttributes.GENERIC_FOLLOW_RANGE)).addPersistentModifier(new EntityAttributeModifier("follow", type.getFollowRange(), EntityAttributeModifier.Operation.ADDITION));
        Objects.requireNonNull(this.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED)).addPersistentModifier(new EntityAttributeModifier("speed", type.getMovementSpeed(), EntityAttributeModifier.Operation.ADDITION));
        Objects.requireNonNull(this.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE)).addPersistentModifier(new EntityAttributeModifier("damage", type.getAttackDamage(), EntityAttributeModifier.Operation.ADDITION));
        Objects.requireNonNull(this.getAttributeInstance(EntityAttributes.GENERIC_ARMOR)).addPersistentModifier(new EntityAttributeModifier("armor", type.getArmor(), EntityAttributeModifier.Operation.ADDITION));
    }

    @Inject(method = "tick", at = @At("HEAD"))
    public void tick(CallbackInfo ci) {
        if (tick_counter == 40) {
            tick_counter = 0;
            onTick();
        } else {
            tick_counter++;
        }
    }

    public void onTick() {
        type = ZombieType.calculateStats(type);
        if (Main.blood_moon_night) {
            BlockPos target_pos = this.getNavigation().getTargetPos();
            BlockPos zombie_pos = this.getBlockPos();
            int start_y;
            int final_y;
            if (target_pos == null) {
                target_pos = zombie_pos;
            }
            if (target_pos.getY() > zombie_pos.getY()) {
                start_y = 1;
                final_y = 1;
            } else if (target_pos.getY() < zombie_pos.getY()) {
                start_y = -1;
                final_y = 0;
            } else {
                start_y = 0;
                final_y = 0;
            }
            for (int dx=-1; dx<=1; dx++)
                for (int dy=start_y; dy<=final_y; dy++)
                    for (int dz=-1; dz<=1; dz++) {
                        BlockPos pos = new BlockPos(this.getBlockPos().getX() + dx, this.getBlockPos().getY() + dy, this.getBlockPos().getZ() + dz);
                        if (world.getBlockState(pos) != Blocks.AIR.getDefaultState()) {
                            this.playSound(SoundEvents.BLOCK_STONE_BREAK, 0.15F, 1.0F);
                            world.setBlockState(pos, Blocks.AIR.getDefaultState());
                        }
                    }
        }
    }

    @Override
    public ZombieType getZombType() {
        return type;
    }
}
