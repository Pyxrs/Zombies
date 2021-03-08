package io.github.simplycmd.zombies;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.server.ServerTickCallback;
import net.minecraft.network.packet.s2c.play.TitleS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main implements ModInitializer {
    public static Logger LOGGER = LogManager.getLogger();

    public static final String MOD_ID = "zombies";
    public static final String MOD_NAME = "Zombies!";

    int tick_counter = 0;

    long old_day = 0;
    public static long day;
    public static boolean blood_moon = false;
    public static boolean blood_moon_night = false;

    @Override
    public void onInitialize() {
        ServerTickCallback.EVENT.register(this::onServerTick);
        log(Level.INFO, "Initializing");
    }

    public static void log(Level level, String message){
        LOGGER.log(level, "["+MOD_NAME+"] " + message);
    }

    private void onServerTick(MinecraftServer minecraftServer) {
        if (tick_counter == 40) {
            tick_counter = 0;
            update(minecraftServer);
        } else {
            tick_counter++;
        }
    }

    private void update(MinecraftServer server) {
        ServerWorld world = server.getOverworld();
        day = world.getTimeOfDay() / 24000L;
        if (day == 0) {
            day = 1;
        }
        long time = (world.getTimeOfDay() / day) / 2;

        if (day != old_day) {
            old_day = day;
            blood_moon = isNightBloodMoon();
        }

        if (blood_moon) {
            if (time > 12000 && time < 12055) {
                server.getPlayerManager().sendToAll(new TitleS2CPacket(TitleS2CPacket.Action.ACTIONBAR, Text.of("§4§lA BLOOD MOON IS RISING...")));
            }
            if (time > 12000) {
                blood_moon_night = true;
            }
        }
    }

    private Boolean isNightBloodMoon() {
        double chance = increaseByDay(0.2, 0.8, 250);

        return Math.random() < chance;
    }

    public static double increaseByDay(double min, double max, double max_day) {
        return clamped(min, max, max * (day / max_day));
    }

    public static double clamped(double min, double max, double value) {
        if (value > max) {
            value = max;
        } else if (value < min) {
            value = min;
        }
        return value;
    }
}
