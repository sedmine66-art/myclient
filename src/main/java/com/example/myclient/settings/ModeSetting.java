package com.example.myclient.settings;

public class ModeSetting extends Setting {
    public final String[] modes;
    public int index;

    public ModeSetting(String name, int index, String... modes) {
        super(name);
        this.modes = modes; this.index = index;
    }

    public String get() { return modes[index]; }
    public void cycle() { index = (index + 1) % modes.length; }
    public void setByName(String n) {
        for (int i = 0; i < modes.length; i++) if (modes[i].equals(n)) index = i;
    }
}
