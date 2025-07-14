package com.fachinis.mc.plugins.clients.factories.stsclient;

import okhttp3.Request;

public interface StsClient {
    

    public Request.Builder injectToken(Request.Builder requestBuilder);
}
