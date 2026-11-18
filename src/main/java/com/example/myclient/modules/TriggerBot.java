package com.example.myclient.modules;

import com.example.myclient.settings.BoolSetting;
import com.example.myclient.settings.NumberSetting;
import net.minecraft.entity.Entity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.Hand;
import org.lwjgl.glfw.GLFW;

/** Бьёт сущность под прицелом, когда кулдаун оружия восстановлен. Для одиночной игры / своего сервера. */
public class TriggerBot extends Module {
    private final BoolSetting players = add(new BoolSetting("Players", true));
    private final BoolSetting mobs = add(new BoolSetting("Mobs", true));
    private final NumberSetting cooldown = add(new NumberSetting("Cooldown", 1.0, 0.5, 1.0, 0.05));

    public TriggerBot() { super("TriggerBot", GLFW.GLFW_KEY_T); }

    @Override
    public void onTick() {
        if (mc.player == null || mc.interactionManager == null || mc.currentScreen != null) return;
        if (mc.crosshairTarget == null || mc.crosshairTarget.getType() != HitResult.Type.ENTITY) return;

        Entity target = ((EntityHitResult) mc.crosshairTarget).getEntity();
        if (!Targets.valid(target, players.value, mobs.value)) return;
        if (mc.player.getAttackCooldownProgress(0.5f) < cooldown.value) return;

        mc.interactionManager.attackEntity(mc.player, target);
        mc.player.swingHand(Hand.MAIN_HAND);
    }
}
