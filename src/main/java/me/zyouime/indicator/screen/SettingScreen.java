package me.zyouime.indicator.screen;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.zyouime.indicator.setting.Setting;
import me.zyouime.indicator.util.Wrapper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class SettingScreen implements Wrapper {

    public static Screen getScreen(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Text.translatable("indicator.screen.title"));

        builder.setSavingRunnable(() -> SETTINGS.settingsList.forEach(Setting::save));

        ConfigCategory general = builder.getOrCreateCategory(Text.translatable("indicator.screen.category"));

        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        fillSettings(general, entryBuilder);

        return builder.build();
    }

    private static void fillSettings(ConfigCategory general, ConfigEntryBuilder entryBuilder) {
        general.addEntry(entryBuilder.startBooleanToggle(Text.translatable("indicator.screen.option1"), SETTINGS.scoreboardMode.getValue())
                .setDefaultValue(false)
                .setSaveConsumer(bl -> SETTINGS.scoreboardMode.setValue(bl))
                .setTooltip(Text.translatable("indicator.screen.option1.tooltip"))
                .build());

        general.addEntry(entryBuilder.startBooleanToggle(Text.translatable("indicator.screen.option2"), SETTINGS.animation.getValue())
                .setDefaultValue(true)
                .setSaveConsumer(bl -> SETTINGS.animation.setValue(bl))
                .setTooltip(Text.translatable("indicator.screen.option2.tooltip"))
                .build());

        general.addEntry(entryBuilder.startFloatField(Text.translatable("indicator.screen.option3"), SETTINGS.heartsSize.getValue())
                .setDefaultValue(25f)
                .setSaveConsumer(size -> SETTINGS.heartsSize.setValue(size))
                .setMin(10.0f)
                .setMax(50.0f)
                .build());

        general.addEntry(entryBuilder.startFloatField(Text.translatable("indicator.screen.option4"), SETTINGS.heartsOffset.getValue())
                .setDefaultValue(0.5f)
                .setSaveConsumer(offset -> SETTINGS.heartsOffset.setValue(offset))
                .setMin(0.1f)
                .setMax(1.2f)
                .setTooltip(Text.translatable("indicator.screen.option4.tooltip"))
                .build());

        general.addEntry(entryBuilder.startBooleanToggle(Text.translatable("indicator.screen.option5"), SETTINGS.inOneRow.getValue())
                .setDefaultValue(false)
                .setSaveConsumer(bl -> SETTINGS.inOneRow.setValue(bl))
                .setTooltip(Text.translatable("indicator.screen.option5.tooltip"))
                .build());

        general.addEntry(entryBuilder.startBooleanToggle(Text.translatable("indicator.screen.option7"), SETTINGS.showOnMobs.getValue())
                .setDefaultValue(true)
                .setSaveConsumer(bl -> SETTINGS.showOnMobs.setValue(bl))
                .build());

        general.addEntry(entryBuilder.startBooleanToggle(Text.translatable("indicator.screen.option8"), SETTINGS.showOnPlayers.getValue())
                .setDefaultValue(true)
                .setSaveConsumer(bl -> SETTINGS.showOnPlayers.setValue(bl))
                .build());

        general.addEntry(entryBuilder.startBooleanToggle(Text.translatable("indicator.screen.option9"), SETTINGS.showHealthInInvisibleEntities.getValue())
                .setDefaultValue(true)
                .setSaveConsumer(bl -> SETTINGS.showHealthInInvisibleEntities.setValue(bl))
                .build());

        general.addEntry(entryBuilder.startBooleanToggle(Text.translatable("indicator.screen.option10"), SETTINGS.showYourHealth.getValue())
                .setDefaultValue(false)
                .setSaveConsumer(bl -> SETTINGS.showYourHealth.setValue(bl))
                .build());

        general.addEntry(entryBuilder.startBooleanToggle(Text.translatable("indicator.screen.option11"), SETTINGS.showDisplayNameForInvisibleEntities.getValue())
                .setDefaultValue(true)
                .setSaveConsumer(bl -> SETTINGS.showDisplayNameForInvisibleEntities.setValue(bl))
                .build());

        general.addEntry(entryBuilder.startBooleanToggle(Text.translatable("indicator.screen.option12"), SETTINGS.showYourDisplayName.getValue())
                .setDefaultValue(false)
                .setSaveConsumer(bl -> SETTINGS.showYourDisplayName.setValue(bl))
                .build());
    }

}
