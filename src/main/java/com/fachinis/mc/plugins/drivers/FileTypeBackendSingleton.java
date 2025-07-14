package com.fachinis.mc.plugins.drivers;

public class FileTypeBackendSingleton {
    

    private FileTypeBackendSingleton() {}

    private static class FileTypeBackendSingletonInstanceHolder {
        private static final FileTypeBackendSingleton INSTANCE = new FileTypeBackendSingleton();
    }

    public FileTypeBackendSingleton getInstance() {
        return FileTypeBackendSingletonInstanceHolder.INSTANCE;
    }
}
