package com.fachinis.mc.plugins.clients.factories.stsclient.concrete;

import com.fachinis.mc.plugins.clients.factories.stsclient.StsClient;

import okhttp3.Request.Builder;

public class NoAuthStsClient implements StsClient {

    @Override
    public Builder injectToken(Builder requestBuilder) {
        return requestBuilder;
    }
    
}
