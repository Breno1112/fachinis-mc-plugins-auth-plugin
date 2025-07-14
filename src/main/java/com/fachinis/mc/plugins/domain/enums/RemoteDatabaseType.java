package com.fachinis.mc.plugins.domain.enums;

public enum RemoteDatabaseType {
    MYSQL("MYSQL"),
    MONGO_DB("MONGO_DB"),
    SQL_SERVER("SQL_SERVER"),
    POSTGRES_SQL("POSTGRES_SQL"),
    UNKNOWN("UNKNOWN");

    private String propertyValue;

    RemoteDatabaseType(String value) {
        this.propertyValue = value;
    }

    public RemoteDatabaseType parse(String value) {
        RemoteDatabaseType parsed = UNKNOWN;
        for (RemoteDatabaseType item: values()) {
            if (item.propertyValue.equals(value)) {
                parsed = item;
                break;
            }
        }
        return parsed;
    }
}
