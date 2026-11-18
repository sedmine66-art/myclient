package com.example.myclient.settings;

public class NumberSetting extends Setting {
    public double value;
    public final double min, max, step;

    public NumberSetting(String name, double value, double min, double max, double step) {
        super(name);
        this.value = value; this.min = min; this.max = max; this.step = step;
    }

    public void setFromFraction(double f) {
        f = Math.max(0, Math.min(1, f));
        set(min + f * (max - min));
    }

    public void set(double v) {
        v = Math.round(v / step) * step;
        value = Math.max(min, Math.min(max, v));
    }

    public double fraction() { return (value - min) / (max - min); }
}
