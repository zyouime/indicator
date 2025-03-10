package me.zyouime.indicator.util;

import me.zyouime.indicator.HealthIndicator;

public interface Wrapper {

    HealthIndicator HEALTH_INDICATOR = HealthIndicator.getInstance();
    HealthIndicator.Settings SETTINGS = HEALTH_INDICATOR.settings;
}
