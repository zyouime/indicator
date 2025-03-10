package me.zyouime.indicator.setting;

import me.zyouime.indicator.config.ConfigKey;
import me.zyouime.indicator.config.ModConfig;

public abstract class Setting<T> {

    private T value;
    private final ConfigKey configKey;

    public Setting(T value, ConfigKey configKey) {
        this.value = value;
        this.configKey = configKey;
    }

    public void save() {
        ModConfig.loadConfig();
        ModConfig.configData.setField(configKey.getKey(), value);
        ModConfig.saveConfig();
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

}
