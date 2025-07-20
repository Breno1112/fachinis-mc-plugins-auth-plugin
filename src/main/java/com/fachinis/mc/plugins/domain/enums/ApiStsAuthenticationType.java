package com.fachinis.mc.plugins.domain.enums;

public enum ApiStsAuthenticationType {
    CLIENT_CREDENTIALS("CLIENT_CREDENTIALS"),
    API_KEY("API_KEY"),
    NONE("NONE"),
    UNKNOWN("UNKNOWN");
    
    private String propertyValue;

    ApiStsAuthenticationType(String value) {
        this.propertyValue = value;
    }

    public static ApiStsAuthenticationType parse(String value) {
        ApiStsAuthenticationType parsed = UNKNOWN;
        for (ApiStsAuthenticationType item: values()) {
            if (item.propertyValue.equals(value)) {
                parsed = item;
                break;
            }
        }
        return parsed;
    }

    public String getPropertyValue() {
        return this.propertyValue;
    }
}
