package com.example.myclient;

import com.example.myclient.modules.Module;
import com.example.myclient.settings.*;
import com.google.gson.*;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Files;
import java.nio.file.Path;

public final class Config {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static Path path() { return FabricLoader.getInstance().getConfigDir().resolve("myclient.json"); }

    private Config() {}

    public static void save() {
        try {
            JsonObject root = new JsonObject();
            for (Module m : MyClient.modules) {
                JsonObject o = new JsonObject();
                o.addProperty("key", m.key);
                for (Setting s : m.settings) {
                    if (s instanceof BoolSetting b) o.addProperty(s.name, b.value);
                    else if (s instanceof NumberSetting n) o.addProperty(s.name, n.value);
                    else if (s instanceof ModeSetting md) o.addProperty(s.name, md.get());
                }
                root.add(m.name, o);
            }
            Files.writeString(path(), GSON.toJson(root));
        } catch (Exception e) { e.printStackTrace(); }
    }

    public static void load() {
        try {
            Path p = path();
            if (!Files.exists(p)) return;
            JsonObject root = JsonParser.parseString(Files.readString(p)).getAsJsonObject();
            for (Module m : MyClient.modules) {
                if (!root.has(m.name)) continue;
                JsonObject o = root.getAsJsonObject(m.name);
                if (o.has("key")) m.key = o.get("key").getAsInt();
                for (Setting s : m.settings) {
                    if (!o.has(s.name)) continue;
                    JsonElement el = o.get(s.name);
                    if (s instanceof BoolSetting b) b.value = el.getAsBoolean();
                    else if (s instanceof NumberSetting n) n.set(el.getAsDouble());
                    else if (s instanceof ModeSetting md) md.setByName(el.getAsString());
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
    }
}
