package com.example.myclient.modules;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;

public final class Targets {
    private Targets() {}

    public static boolean valid(Entity e, boolean players, boolean mobs) {
        if (e == null || e == MinecraftClient.getInstance().player) return false;
        if (!(e instanceof LivingEntity) || !e.isAlive()) return false;
        return (players && e instanceof PlayerEntity) || (mobs && e instanceof HostileEntity);
    }
}
