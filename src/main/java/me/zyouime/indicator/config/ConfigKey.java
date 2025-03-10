package me.zyouime.indicator.config;

public enum ConfigKey {
    HEARTS("enabled"),
    HEARTS_SIZE("heartsSize"),
    HEARTS_OFFSET("heartsOffset"),
    IN_ONE_ROW("inOneRow"),
    ANIMATION("animation"),
    SHOW_ON_MOBS("showOnMobs"),
    SHOW_ON_PLAYERS("showOnPlayers"),
    SHOW_HEALTH_IN_INVISIBLE_ENTITIES("showHealthInInvisibleEntities"),
    SHOW_YOUR_HEALTH("showYourHealth"),
    SHOW_DISPLAY_NAME_FOR_INVISIBLE_ENTITIES("showDisplayNameForInvisibleEntities"),
    SHOW_YOUR_DISPLAY_NAME("showYourDisplayName"),
    SCOREBOARD_MODE("scoreboardMode");

    private final String key;

    ConfigKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
