package com.example.myclient.modules;

import com.example.myclient.settings.BoolSetting;
import com.example.myclient.settings.NumberSetting;
import net.minecraft.entity.Entity;
import org.lwjgl.glfw.GLFW;

/** Подсветка сущностей через клиентский флаг glowing. */
public class Esp extends Module {
    private final BoolSetting players = add(new BoolSetting("Players", true));
    private final BoolSetting mobs = add(new BoolSetting("Mobs", true));
    private final NumberSetting range = add(new NumberSetting("Range", 64, 8, 128, 4));

    public Esp() { super("ESP", GLFW.GLFW_KEY_R); }

    @Override
    public void onTick() {
        if (mc.world == null || mc.player == null) return;
        for (Entity e : mc.world.getEntities()) {
            boolean ok = Targets.valid(e, players.value, mobs.value) && e.distanceTo(mc.player) <= range.value;
            if (ok) e.setGlowing(true);
            else if (e.isGlowing()) e.setGlowing(false);
        }
    }

    @Override
    public void onDisable() {
        if (mc.world == null) return;
        for (Entity e : mc.world.getEntities()) e.setGlowing(false);
    }
}
