package io.github.simplycmd.zombies.mixin;

import io.github.simplycmd.zombies.Main;
import io.github.simplycmd.zombies.ZombieType;
import io.github.simplycmd.zombies.access.ZombieVillagerEntityAccess;
import net.minecraft.block.Block;
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

import java.util.ArrayList;
import java.util.List;
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

        Objects.requireNonNull(this.getAttributeInstance(EntityAttributes.GENERIC_FOLLOW_RANGE)).addTemporaryModifier(new EntityAttributeModifier("follow", type.getFollowRange(), EntityAttributeModifier.Operation.ADDITION));
        Objects.requireNonNull(this.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED)).addTemporaryModifier(new EntityAttributeModifier("speed", type.getMovementSpeed(), EntityAttributeModifier.Operation.ADDITION));
        Objects.requireNonNull(this.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE)).addTemporaryModifier(new EntityAttributeModifier("damage", type.getAttackDamage(), EntityAttributeModifier.Operation.ADDITION));
        Objects.requireNonNull(this.getAttributeInstance(EntityAttributes.GENERIC_ARMOR)).addTemporaryModifier(new EntityAttributeModifier("armor", type.getArmor(), EntityAttributeModifier.Operation.ADDITION));
    }

    @Inject(method = "tick", at = @At("HEAD"))
    public void tick(CallbackInfo ci) {
        if (tick_counter == 80) {
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
                this.updatePosition(this.getBlockPos().getX(), this.getBlockPos().getY() + 1, this.getBlockPos().getZ());
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

                        List<Block> blacklisted_blocks = new ArrayList();
                        blacklisted_blocks.add(Blocks.AIR);
                        blacklisted_blocks.add(Blocks.OBSIDIAN);
                        blacklisted_blocks.add(Blocks.BEDROCK);
                        blacklisted_blocks.add(Blocks.WATER);
                        blacklisted_blocks.add(Blocks.LAVA);

                        if (!blacklisted_blocks.contains(world.getBlockState(pos).getBlock())) {
                            this.playSound(SoundEvents.BLOCK_STONE_BREAK, 1F, 1.0F);
                            world.setBlockState(pos, Blocks.AIR.getDefaultState());
                        }
                        BlockPos position = new BlockPos(this.getBlockPos().getX(), this.getBlockPos().getY() - 1, this.getBlockPos().getZ());
                        if (target_pos.getY() > zombie_pos.getY() && world.getBlockState(position).getBlock() == Blocks.AIR) {
                            world.setBlockState(position, Blocks.STONE.getDefaultState());
                        }

                        /*if (!blacklisted_blocks.contains(world.getBlockState(pos).getBlock())) {
                            int hardness = Math.round(world.getBlockState(pos).getHardness(world, pos) / 2);
                            Main.universal_block_damage.putIfAbsent(pos, hardness);
                            if ((world.getBlockState(pos).getBlock() == Blocks.DEAD_BUBBLE_CORAL_BLOCK || world.getBlockState(pos).getBlock() == Blocks.DEAD_TUBE_CORAL_BLOCK) && Main.universal_block_damage.get(pos) == null) {
                                Main.universal_block_damage.replace(pos, 3);
                            }
                            System.out.println(Main.universal_block_damage.get(pos));
                            Main.universal_block_damage.replace(pos, Main.universal_block_damage.get(pos) - 1);
                            this.playSound(SoundEvents.ENTITY_TURTLE_EGG_BREAK, 1F, 1.0F);
                            if (Main.universal_block_damage.get(pos) <= 0) {
                                this.playSound(world.getBlockState(pos).getSoundGroup().getBreakSound(), 1F, 1.0F);
                                world.setBlockState(pos, Blocks.AIR.getDefaultState());
                                Main.universal_block_damage.remove(pos);
                            }
                        }*/
                    }
        }
    }

    @Override
    public ZombieType getZombType() {
        return type;
    }
}
