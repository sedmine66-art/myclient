package com.example.myclient.modules;

import com.example.myclient.settings.NumberSetting;
import org.lwjgl.glfw.GLFW;

/** Для одиночной игры / своего сервера. */
public class Flight extends Module {
    private final NumberSetting speed = add(new NumberSetting("Speed", 0.05, 0.02, 0.5, 0.01));

    public Flight() { super("Flight", GLFW.GLFW_KEY_F); }

    @Override
    public void onTick() {
        if (mc.player == null) return;
        mc.player.getAbilities().allowFlying = true;
        mc.player.getAbilities().setFlySpeed((float) speed.value);
    }

    @Override
    public void onDisable() {
        if (mc.player == null) return;
        mc.player.getAbilities().setFlySpeed(0.05f);
        if (!mc.player.isCreative() && !mc.player.isSpectator()) {
            mc.player.getAbilities().flying = false;
            mc.player.getAbilities().allowFlying = false;
        }
    }
}
