package io.github.simplycmd.zombies;

import com.google.common.collect.ImmutableSet;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.sound.SoundEvent;
import net.minecraft.village.VillagerProfession;
import net.minecraft.world.poi.PointOfInterestType;
import org.jetbrains.annotations.Nullable;

public class ZombieType {
    public static final ZombieType NORMAL = new ZombieType(35.0, 0.23000000417232513, 3.0,2.0D, VillagerProfession.NONE);
    public static final ZombieType SPEEDY = new ZombieType(40.0, 0.38000000417232513, 2.0,2.0D, VillagerProfession.FLETCHER);
    public static final ZombieType SIGHTY = new ZombieType(60.0, 0.23000000417232513, 3.0,2.0D, VillagerProfession.CARTOGRAPHER);
    public static final ZombieType PUNCHY = new ZombieType(35.0, 0.20000000417232513, 6.0,1.5D, VillagerProfession.WEAPONSMITH);
    public static final ZombieType TANKY = new ZombieType(35.0, 0.23000000417232513, 3.0,6.0D, VillagerProfession.ARMORER);
    double follow_range;
    double movement_speed;
    double attack_damage;
    double armor;
    VillagerProfession profession;

    private ZombieType(double follow_range, double movement_speed, double attack_damage, double armor, VillagerProfession profession) {
        this.follow_range = follow_range;
        this.movement_speed = movement_speed;
        this.attack_damage = attack_damage;
        this.armor = armor;
        this.profession = profession;
    }

    public double getFollowRange() {
        return follow_range;
    }

    public double getMovementSpeed() {
        return movement_speed;
    }
}
