package me.zyouime.indicator;

import me.zyouime.indicator.config.ConfigData;
import me.zyouime.indicator.config.ConfigKey;
import me.zyouime.indicator.config.ModConfig;
import me.zyouime.indicator.screen.SettingScreen;
import me.zyouime.indicator.setting.BooleanSetting;
import me.zyouime.indicator.setting.NumberSetting;
import me.zyouime.indicator.setting.Setting;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class HealthIndicator implements ModInitializer {

    private final KeyBinding TOGGLE_KEY = KeyBindingHelper.registerKeyBinding(new KeyBinding("indicator.key.toggle", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_F12, "indicator.key.category"));
    private final KeyBinding SETTINGS_KEY = KeyBindingHelper.registerKeyBinding(new KeyBinding("indicator.key.settings", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, "indicator.key.category"));
    public Settings settings;
    private static HealthIndicator instance;

    public HealthIndicator() {
        instance = this;
    }

    @Override
    public void onInitialize() {
        ModConfig.register();
        settings = new Settings();
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (TOGGLE_KEY.wasPressed()) {
                settings.enabled.setValue(!settings.enabled.getValue());
                settings.enabled.save();
            }
            if (SETTINGS_KEY.wasPressed()) {
                client.setScreen(SettingScreen.getScreen(client.currentScreen));
            }
        });
    }


    public static class Settings {
        public List<Setting<?>> settingsList = new ArrayList<>();
        ConfigData configData = ModConfig.configData;

        public BooleanSetting enabled = register(new BooleanSetting((Boolean) configData.getField("enabled"), ConfigKey.HEARTS));
        public NumberSetting heartsSize = register(new NumberSetting((Float) configData.getField("heartsSize"), ConfigKey.HEARTS_SIZE));
        public NumberSetting heartsOffset = register(new NumberSetting((Float) configData.getField("heartsOffset"), ConfigKey.HEARTS_OFFSET));
        public BooleanSetting animation = register(new BooleanSetting((Boolean) configData.getField("animation"), ConfigKey.ANIMATION));
        public BooleanSetting inOneRow = register(new BooleanSetting((Boolean) configData.getField("inOneRow"), ConfigKey.IN_ONE_ROW));
        public BooleanSetting showOnMobs = register(new BooleanSetting((Boolean) configData.getField("showOnMobs"), ConfigKey.SHOW_ON_MOBS));
        public BooleanSetting showOnPlayers = register(new BooleanSetting((Boolean) configData.getField("showOnPlayers"), ConfigKey.SHOW_ON_PLAYERS));
        public BooleanSetting showHealthInInvisibleEntities = register(new BooleanSetting((Boolean) configData.getField("showHealthInInvisibleEntities"), ConfigKey.SHOW_HEALTH_IN_INVISIBLE_ENTITIES));
        public BooleanSetting showDisplayNameForInvisibleEntities = register(new BooleanSetting((Boolean) configData.getField("showDisplayNameForInvisibleEntities"), ConfigKey.SHOW_DISPLAY_NAME_FOR_INVISIBLE_ENTITIES));
        public BooleanSetting showYourHealth = register(new BooleanSetting((Boolean) configData.getField("showYourHealth"), ConfigKey.SHOW_YOUR_HEALTH));
        public BooleanSetting showYourDisplayName = register(new BooleanSetting((Boolean) configData.getField("showYourDisplayName"), ConfigKey.SHOW_YOUR_DISPLAY_NAME));
        public BooleanSetting scoreboardMode = register(new BooleanSetting((Boolean) configData.getField("scoreboardMode"), ConfigKey.SCOREBOARD_MODE));

        private <T extends Setting<?>> T register(T s) {
            this.settingsList.add(s);
            return s;
        }
    }

    public static HealthIndicator getInstance() {
        return instance;
    }
}
