package com.example.myclient.gui;

import com.example.myclient.Config;
import com.example.myclient.MyClient;
import com.example.myclient.modules.Module;
import com.example.myclient.settings.*;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * ЛКМ по модулю — вкл/выкл, ПКМ — открыть настройки, СКМ — сменить бинд
 * (Backspace/Delete — убрать бинд, Esc — отмена).
 */
public class ClickGui extends Screen {
    private static final int X = 20, Y = 20, W = 160, H = 14;
    private static final Set<Module> open = new HashSet<>();

    private record Row(int y, Module m, Setting s) {}

    private Module binding;
    private NumberSetting dragging;

    public ClickGui() { super(Text.literal("ClickGUI")); }

    private List<Row> rows() {
        List<Row> list = new ArrayList<>();
        int y = Y + H;
        for (Module m : MyClient.modules) {
            list.add(new Row(y, m, null));
            y += H;
            if (open.contains(m)) {
                for (Setting s : m.settings) { list.add(new Row(y, m, s)); y += H; }
            }
        }
        return list;
    }

    private String keyName(int key) {
        if (key <= 0) return "NONE";
        return InputUtil.fromKeyCode(key, 0).getLocalizedText().getString();
    }

    @Override
    public void render(DrawContext ctx, int mx, int my, float delta) {
        ctx.fill(X, Y, X + W, Y + H, 0xFF101010);
        ctx.drawTextWithShadow(textRenderer, "MyClient", X + 4, Y + 3, 0xFFFFFFFF);

        for (Row r : rows()) {
            boolean hover = mx >= X && mx < X + W && my >= r.y() && my < r.y() + H;
            if (r.s() == null) {
                int bg = r.m().enabled ? 0xFF2E7D32 : (hover ? 0xFF404040 : 0xFF2A2A2A);
                ctx.fill(X, r.y(), X + W, r.y() + H, bg);
                String bind = binding == r.m() ? "..." : keyName(r.m().key);
                String arrow = r.m().settings.isEmpty() ? "" : (open.contains(r.m()) ? " v" : " >");
                ctx.drawTextWithShadow(textRenderer, r.m().name + arrow, X + 4, r.y() + 3, 0xFFFFFFFF);
                ctx.drawTextWithShadow(textRenderer, "[" + bind + "]",
                        X + W - 6 - textRenderer.getWidth("[" + bind + "]"), r.y() + 3, 0xFFAAAAAA);
            } else {
                ctx.fill(X, r.y(), X + W, r.y() + H, hover ? 0xFF383838 : 0xFF1E1E1E);
                Setting s = r.s();
                String text;
                if (s instanceof BoolSetting b) text = s.name + ": " + (b.value ? "ON" : "OFF");
                else if (s instanceof NumberSetting n) {
                    ctx.fill(X, r.y(), X + (int) (W * n.fraction()), r.y() + H, 0xFF1565C0);
                    text = s.name + ": " + String.format("%.2f", n.value);
                } else text = s.name + ": " + ((ModeSetting) s).get();
                ctx.drawTextWithShadow(textRenderer, text, X + 10, r.y() + 3, 0xFFFFFFFF);
            }
        }
        super.render(ctx, mx, my, delta);
    }

    @Override
    public boolean mouseClicked(double mx, double my, int button) {
        for (Row r : rows()) {
            if (mx < X || mx >= X + W || my < r.y() || my >= r.y() + H) continue;
            if (r.s() == null) {
                if (button == 0) r.m().toggle();
                else if (button == 1 && !r.m().settings.isEmpty()) {
                    if (!open.remove(r.m())) open.add(r.m());
                } else if (button == 2) binding = r.m();
            } else if (button == 0) {
                Setting s = r.s();
                if (s instanceof BoolSetting b) b.value = !b.value;
                else if (s instanceof ModeSetting md) md.cycle();
                else if (s instanceof NumberSetting n) {
                    dragging = n;
                    n.setFromFraction((mx - X) / W);
                }
            }
            return true;
        }
        return super.mouseClicked(mx, my, button);
    }

    @Override
    public boolean mouseDragged(double mx, double my, int button, double dx, double dy) {
        if (dragging != null) {
            dragging.setFromFraction((mx - X) / W);
            return true;
        }
        return super.mouseDragged(mx, my, button, dx, dy);
    }

    @Override
    public boolean mouseReleased(double mx, double my, int button) {
        dragging = null;
        return super.mouseReleased(mx, my, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (binding != null) {
            if (keyCode == GLFW.GLFW_KEY_BACKSPACE || keyCode == GLFW.GLFW_KEY_DELETE) binding.key = -1;
            else if (keyCode != GLFW.GLFW_KEY_ESCAPE) binding.key = keyCode;
            binding = null;
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void removed() { Config.save(); }

    @Override
    public boolean shouldPause() { return false; }
}
