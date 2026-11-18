package com.example.myclient.modules;

import net.minecraft.block.BlockState;
import net.minecraft.hit.BlockHitResult;
import net.minecraft.hit.HitResult;
import org.lwjgl.glfw.GLFW;

public class AutoTool extends Module {
    public AutoTool() { super("AutoTool", GLFW.GLFW_KEY_UNKNOWN); }

    @Override
    public void onTick() {
        if (mc.player == null || mc.world == null || !mc.options.attackKey.isPressed()) return;
        if (mc.crosshairTarget == null || mc.crosshairTarget.getType() != HitResult.Type.BLOCK) return;

        BlockState state = mc.world.getBlockState(((BlockHitResult) mc.crosshairTarget).getBlockPos());
        int best = mc.player.getInventory().selectedSlot;
        float bestSpeed = mc.player.getInventory().getStack(best).getMiningSpeedMultiplier(state);
        for (int i = 0; i < 9; i++) {
            float s = mc.player.getInventory().getStack(i).getMiningSpeedMultiplier(state);
            if (s > bestSpeed) { bestSpeed = s; best = i; }
        }
        mc.player.getInventory().selectedSlot = best;
    }
}
