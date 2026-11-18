package com.example.myclient;

import com.example.myclient.gui.ClickGui;
import com.example.myclient.modules.*;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class MyClient implements ClientModInitializer {
    public static final List<Module> modules = new ArrayList<>();
    private boolean guiKeyWasDown;

    @Override
    public void onInitializeClient() {
        modules.add(new Sprint());
        modules.add(new Fullbright());
        modules.add(new Flight());
        modules.add(new Esp());
        modules.add(new Tracers());
        modules.add(new TriggerBot());
        modules.add(new AutoTool());
        modules.add(new AutoRespawn());
        Config.load();

        ClientTickEvents.END_CLIENT_TICK.register(mc -> {
            if (mc.player == null || mc.world == null) return;
            long window = mc.getWindow().getHandle();

            boolean guiDown = InputUtil.isKeyPressed(window, GLFW.GLFW_KEY_RIGHT_SHIFT);
            if (guiDown && !guiKeyWasDown && mc.currentScreen == null) mc.setScreen(new ClickGui());
            guiKeyWasDown = guiDown;

            for (Module m : modules) {
                boolean down = m.key > 0 && mc.currentScreen == null && InputUtil.isKeyPressed(window, m.key);
                if (down && !m.keyWasDown) m.toggle();
                m.keyWasDown = down;
                if (m.enabled) m.onTick();
            }
        });

        WorldRenderEvents.AFTER_ENTITIES.register(ctx -> {
            for (Module m : modules) if (m.enabled) m.onRender(ctx);
        });

        HudRenderCallback.EVENT.register((ctx, tickCounter) -> {
            MinecraftClient mc = MinecraftClient.getInstance();
            ctx.drawTextWithShadow(mc.textRenderer, "MyClient", 4, 4, 0xFF55FF55);
            int y = 16;
            for (Module m : modules) {
                if (!m.enabled) continue;
                ctx.drawTextWithShadow(mc.textRenderer, m.name, 4, y, 0xFFFFFFFF);
                y += 10;
            }
        });
    }
}
