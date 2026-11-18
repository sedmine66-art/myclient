package com.example.myclient.modules;

import org.lwjgl.glfw.GLFW;

public class Sprint extends Module {
    public Sprint() { super("Sprint", GLFW.GLFW_KEY_G); }

    @Override
    public void onTick() {
        if (mc.player != null && mc.player.input.hasForwardMovement()) mc.player.setSprinting(true);
    }
}
