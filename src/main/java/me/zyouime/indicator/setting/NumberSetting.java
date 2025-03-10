package me.zyouime.indicator.setting;

import me.zyouime.indicator.config.ConfigKey;
import net.minecraft.client.util.math.MatrixStack;

public class NumberSetting extends Setting<Float> {

    public NumberSetting(Float value, ConfigKey configKey) {
        super(value, configKey);
    }

}
