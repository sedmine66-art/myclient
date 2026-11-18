package com.example.myclient.modules;

import com.example.myclient.settings.BoolSetting;
import com.example.myclient.settings.NumberSetting;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

public class Tracers extends Module {
    private final BoolSetting players = add(new BoolSetting("Players", true));
    private final BoolSetting mobs = add(new BoolSetting("Mobs", false));
    private final NumberSetting range = add(new NumberSetting("Range", 64, 8, 128, 4));

    public Tracers() { super("Tracers", GLFW.GLFW_KEY_V); }

    @Override
    public void onRender(WorldRenderContext ctx) {
        if (mc.world == null || mc.player == null) return;
        MatrixStack ms = ctx.matrixStack();
        VertexConsumerProvider vcp = ctx.consumers();
        if (ms == null || vcp == null) return;

        VertexConsumer vc = vcp.getBuffer(RenderLayer.getLines());
        MatrixStack.Entry entry = ms.peek();
        Camera cam = ctx.camera();
        Vec3d camPos = cam.getPos();
        float td = ctx.tickCounter().getTickDelta(false);
        Vec3d start = Vec3d.fromPolar(cam.getPitch(), cam.getYaw());

        for (Entity e : mc.world.getEntities()) {
            if (!Targets.valid(e, players.value, mobs.value)) continue;
            if (e.distanceTo(mc.player) > range.value) continue;

            Vec3d end = e.getLerpedPos(td).add(0, e.getHeight() / 2.0, 0).subtract(camPos);
            Vec3d dir = end.subtract(start).normalize();
            int r = 255, g = e instanceof PlayerEntity ? 60 : 220, b = e instanceof PlayerEntity ? 60 : 0;

            vc.vertex(entry, (float) start.x, (float) start.y, (float) start.z)
                    .color(r, g, b, 255).normal(entry, (float) dir.x, (float) dir.y, (float) dir.z);
            vc.vertex(entry, (float) end.x, (float) end.y, (float) end.z)
                    .color(r, g, b, 255).normal(entry, (float) dir.x, (float) dir.y, (float) dir.z);
        }
    }
}
