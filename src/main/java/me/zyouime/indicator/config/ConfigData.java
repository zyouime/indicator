package me.zyouime.indicator.config;

import com.google.gson.annotations.Expose;

import java.lang.reflect.Field;

public class ConfigData {

    @Expose
    boolean enabled = true;
    @Expose
    boolean animation = true;
    @Expose
    boolean inOneRow = false;
    @Expose
    float heartsSize = 25f;
    @Expose
    float heartsOffset = 0.5f;
    @Expose
    boolean showOnMobs = true;
    @Expose
    boolean showOnPlayers = true;
    @Expose
    boolean showHealthInInvisibleEntities = true;
    @Expose
    boolean showYourHealth = false;
    @Expose
    boolean showDisplayNameForInvisibleEntities = true;
    @Expose
    boolean showYourDisplayName = false;
    @Expose
    boolean scoreboardMode = false;

    public void setField(String fieldName, Object value) {
        try {
            Field field = this.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(this, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public Object getField(String fieldName) {
        try {
            Field field = this.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(this);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }
}
