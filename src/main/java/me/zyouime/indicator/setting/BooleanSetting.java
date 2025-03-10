package me.zyouime.indicator.setting;

import me.zyouime.indicator.config.ConfigKey;
import net.minecraft.client.util.math.MatrixStack;

public class BooleanSetting extends Setting<Boolean> {

    public BooleanSetting(Boolean value, ConfigKey configKey) {
        super(value, configKey);
    }

}
