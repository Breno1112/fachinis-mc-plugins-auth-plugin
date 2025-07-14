package com.fachinis.mc.plugins.clients.factories.stsclient.concrete;

import com.fachinis.mc.plugins.clients.factories.stsclient.StsClient;

import okhttp3.Request;

public class NoAuthStsClient implements StsClient {

    @Override
    public void injectToken(Request request) {}
    
}
