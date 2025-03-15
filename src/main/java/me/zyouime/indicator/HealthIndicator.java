package me.zyouime.indicator;

import me.zyouime.indicator.config.ConfigData;
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

        public BooleanSetting enabled = register(new BooleanSetting("enabled"));
        public NumberSetting heartsSize = register(new NumberSetting("heartsSize"));
        public NumberSetting heartsOffset = register(new NumberSetting("heartsOffset"));
        public BooleanSetting animation = register(new BooleanSetting("animation"));
        public BooleanSetting inOneRow = register(new BooleanSetting("inOneRow"));
        public BooleanSetting showOnMobs = register(new BooleanSetting("showOnMobs"));
        public BooleanSetting showOnPlayers = register(new BooleanSetting("showOnPlayers"));
        public BooleanSetting showHealthInInvisibleEntities = register(new BooleanSetting("showHealthInInvisibleEntities"));
        public BooleanSetting showDisplayNameForInvisibleEntities = register(new BooleanSetting("showDisplayNameForInvisibleEntities"));
        public BooleanSetting showYourHealth = register(new BooleanSetting("showYourHealth"));
        public BooleanSetting showYourDisplayName = register(new BooleanSetting("showYourDisplayName"));
        public BooleanSetting scoreboardMode = register(new BooleanSetting("scoreboardMode"));

        private <T extends Setting<?>> T register(T s) {
            this.settingsList.add(s);
            return s;
        }
    }

    public static HealthIndicator getInstance() {
        return instance;
    }
}
