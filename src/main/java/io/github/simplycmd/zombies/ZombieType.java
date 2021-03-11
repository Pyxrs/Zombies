package io.github.simplycmd.zombies;

import net.minecraft.village.VillagerProfession;

public class ZombieType {
    public static final ZombieType NORMAL = new ZombieType(0, 0, 0,0, VillagerProfession.NONE);
    public static final ZombieType SPEEDY = new ZombieType(5.0, 0.05, -1.0,0, VillagerProfession.FLETCHER);
    public static final ZombieType SIGHTY = new ZombieType(25.0, 0, 0,0, VillagerProfession.CARTOGRAPHER);
    public static final ZombieType PUNCHY = new ZombieType(0, -0.01, 6.0,-0.5D, VillagerProfession.WEAPONSMITH);
    public static final ZombieType TANKY = new ZombieType(0, -0.03, 1.0D,4.0D, VillagerProfession.ARMORER);
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

    public double getAttackDamage() {
        return attack_damage;
    }

    public double getArmor() {
        return armor;
    }

    public void setFollowRange(double follow_range) {
        this.follow_range = follow_range;
    }

    public void setMovementSpeed(double movement_speed) {
        this.movement_speed = movement_speed;
    }

    public void setAttackDamage(double attack_damage) {
        this.attack_damage = attack_damage;
    }

    public void setArmor(double armor) {
        this.armor = armor;
    }

    public VillagerProfession getProfession() {
        return profession;
    }

    public static ZombieType determineType() {
        double random = Math.random();
        if (random <= 0.125) {
            return SPEEDY;
        } else if (random > 0.125 && random <= 0.25) {
            return SIGHTY;
        } else if (random > 0.25 && random <= 0.375) {
            return PUNCHY;
        } else if (random > 0.375 && random <= 0.5) {
            return TANKY;
        } else {
            return NORMAL;
        }
    }

    public static ZombieType calculateStats(ZombieType type) {
        type.setFollowRange(doCalculations(type.getFollowRange()));
        type.setMovementSpeed(doCalculations(type.getMovementSpeed()));
        type.setAttackDamage(doCalculations(type.getAttackDamage()));
        type.setArmor(doCalculations(type.getArmor()));

        return type;
    }

    private static double doCalculations(double stat) {
        return stat;// + Math.abs(Main.increaseByDay(0, stat, 250D));
    }
}
