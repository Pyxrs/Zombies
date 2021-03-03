package io.github.simplycmd.zombies;

import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Zombie extends ZombieEntity {
    int tick_counter = 0;
    World world;

    protected Zombie(EntityType<? extends ZombieEntity> entityType, World world) {
        super(entityType, world);
        this.world = world;
        this.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).addPersistentModifier(new EntityAttributeModifier("speed", 0.2D, EntityAttributeModifier.Operation.ADDITION));
        System.out.println(this.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).getValue());
    }

    @Override
    protected boolean burnsInDaylight() {
        return false;
    }

    public void tick() {
        if (tick_counter == 40) {
            tick_counter = 0;
            onTick();
        } else {
            tick_counter++;
        }
        super.tick();
    }

    public static DefaultAttributeContainer.Builder createZombieAttributes() {
        return HostileEntity.createHostileAttributes().add(EntityAttributes.GENERIC_FOLLOW_RANGE, 35.0).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.23000000417232513).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 3.0).add(EntityAttributes.GENERIC_ARMOR, 2.0D).add(EntityAttributes.ZOMBIE_SPAWN_REINFORCEMENTS);
    }

    public void onTick() {
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
}