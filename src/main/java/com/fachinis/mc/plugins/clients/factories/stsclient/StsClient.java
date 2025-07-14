package com.fachinis.mc.plugins.clients.factories.stsclient;

import okhttp3.Request;

public interface StsClient {
    

    public void injectToken(Request request);
}
