package com.example.myclient.modules;

import org.lwjgl.glfw.GLFW;

public class AutoRespawn extends Module {
    public AutoRespawn() { super("AutoRespawn", GLFW.GLFW_KEY_UNKNOWN); }

    @Override
    public void onTick() {
        if (mc.player != null && mc.player.isDead()) mc.player.requestRespawn();
    }
}
