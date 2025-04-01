package me.zyouime.indicator.config;

import com.google.gson.annotations.Expose;

import java.lang.reflect.Field;

public class ConfigData {

    boolean enabled = true;
    boolean animation = true;
    boolean inOneRow = false;
    float heartsSize = 25f;
    float heartsOffset = 0.5f;
    boolean showOnMobs = true;
    boolean showOnPlayers = true;
    boolean showYourHealth = false;
    boolean showYourDisplayName = false;
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
