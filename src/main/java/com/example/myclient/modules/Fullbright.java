package com.example.myclient.modules;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import org.lwjgl.glfw.GLFW;

public class Fullbright extends Module {
    public Fullbright() { super("Fullbright", GLFW.GLFW_KEY_B); }

    @Override
    public void onTick() {
        if (mc.player != null)
            mc.player.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 400, 0, false, false));
    }

    @Override
    public void onDisable() {
        if (mc.player != null) mc.player.removeStatusEffect(StatusEffects.NIGHT_VISION);
    }
}
