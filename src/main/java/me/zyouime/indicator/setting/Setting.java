package me.zyouime.indicator.setting;

import me.zyouime.indicator.config.ModConfig;

public class Setting<T> {

    private T value;
    private final String configKey;

    public Setting(String configKey, Class<T> type) {
        this.configKey = configKey;
        Object obj = ModConfig.configData.getField(configKey);
        this.value = type.isInstance(obj) ? type.cast(obj) : null;
    }

    public void save() {
        ModConfig.loadConfig();
        ModConfig.configData.setField(configKey, value);
        ModConfig.saveConfig();
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
