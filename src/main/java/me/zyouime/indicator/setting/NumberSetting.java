package me.zyouime.indicator.setting;

import net.minecraft.client.util.math.MatrixStack;

public class NumberSetting extends Setting<Float> {

    public NumberSetting(String configKey) {
        super(configKey, Float.class);
    }

}
