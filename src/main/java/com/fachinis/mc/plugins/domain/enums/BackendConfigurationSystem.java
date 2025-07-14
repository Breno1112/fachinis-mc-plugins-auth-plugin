package com.fachinis.mc.plugins.domain.enums;

public enum BackendConfigurationSystem {
    API("API"),
    REMOTE_DATABASE("REMOTE_DATABASE"),
    IN_MEMORY_DATABASE("IN_MEMORY_DATABASE"),
    FILE("FILE"),
    UNKNOWN("UNKOWN");

    private final String propertyValue;

    BackendConfigurationSystem(String value) {
        this.propertyValue = value;
    }

    public static BackendConfigurationSystem parse(String value) {
        BackendConfigurationSystem parsed = BackendConfigurationSystem.UNKNOWN;
        for (BackendConfigurationSystem item: values()) {
            if (item.propertyValue.equals(value)) {
                parsed = item;
                break;
            }
        }
        return parsed;
    }
}
