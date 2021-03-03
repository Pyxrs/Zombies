package io.github.simplycmd.zombies;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.server.ServerTickCallback;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.mob.ZombieVillagerEntity;
import net.minecraft.network.packet.s2c.play.TitleS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.village.VillagerProfession;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.function.Predicate;

public class Main implements ModInitializer {
    public static Logger LOGGER = LogManager.getLogger();

    public static final String MOD_ID = "zombies";
    public static final String MOD_NAME = "Zombies!";

    int tick_counter = 0;

    long old_day = 0;
    public static boolean blood_moon = false;
    public static boolean blood_moon_night = false;

    public static final EntityType<Zombie> ZOMBIE = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(MOD_ID, "zombie"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, Zombie::new).dimensions(EntityDimensions.fixed(0.75f, 0.75f)).build());

    @Override
    public void onInitialize() {
        ServerTickCallback.EVENT.register(this::onServerTick);
        log(Level.INFO, "Initializing");
        FabricDefaultAttributeRegistry.register(ZOMBIE, Zombie.createZombieAttributes());
    }

    public static void log(Level level, String message){
        LOGGER.log(level, "["+MOD_NAME+"] " + message);
    }

    private void onServerTick(MinecraftServer minecraftServer) {
        if (tick_counter == 40) { //400
            tick_counter = 0;
            update(minecraftServer);
        } else {
            tick_counter++;
        }
    }

    private void update(MinecraftServer server) {
        ServerWorld world = server.getOverworld();
        server.getCommandManager().execute(server.getCommandSource(), "/execute as @e[type=zombies:zombie] run disguise @s as minecraft:zombie_villager {VillagerData:{profession:\"minecraft:farmer\"}}");
        /*Iterator<Entity> entities = world.getEntitiesByType(EntityType.ZOMBIE_VILLAGER, (entity) -> true).iterator();
        while (entities.hasNext()) {
            ZombieVillagerEntity entity = (ZombieVillagerEntity) entities.next();
            if (entity.getVillagerData().getProfession() == VillagerProfession.FARMER) {
                System.out.println(entity.getPos() + "   " + entity.getVillagerData().getProfession());
            }
        }*/
        long day = world.getTimeOfDay() / 24000L;
        if (day == 0) {
            day = 1;
        }
        long time = (world.getTimeOfDay() / day) / 2;

        if (day != old_day) {
            old_day = day;
            newDay(server, day);
        }

        if (blood_moon) {
            if (time > 12000 && time < 12055) {
                server.getPlayerManager().sendToAll(new TitleS2CPacket(TitleS2CPacket.Action.ACTIONBAR, Text.of("§4§lA BLOOD MOON IS RISING...")));
            }
            if (time > 12000) {
                blood_moon_night = true;
            }
        }
        System.out.println(blood_moon + "         " + time);
    }

    private void newDay(MinecraftServer server, long day) {
        blood_moon = isNightBloodMoon(day);
    }

    private Boolean isNightBloodMoon(long day) {
        double chance = 0.8D * (day / 250D);

        if (chance > 0.8) {
            chance = 0.8;
        } else if (chance < 0.2) {
            chance = 0.2;
        }

        if (Math.random() < chance) {
            System.out.println("TRUEO");
            return true;
        } else {
            System.out.println("FALSO");
            return false;
        }
    }
}
