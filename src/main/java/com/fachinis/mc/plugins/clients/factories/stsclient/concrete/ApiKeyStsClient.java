package com.fachinis.mc.plugins.clients.factories.stsclient.concrete;

import com.fachinis.mc.plugins.clients.factories.stsclient.StsClient;
import com.fachinis.mc.plugins.domain.constants.PluginConfigurationKeys;
import com.fachinis.mc.plugins.singletons.PluginConfigurationSingleton;

import okhttp3.Request;

public class ApiKeyStsClient implements StsClient {

    @Override
    public Request.Builder injectToken(Request.Builder request) {
        return request.header("Authorization", PluginConfigurationSingleton.getInstance().getPropertyString(PluginConfigurationKeys.BACKEND_API_STS_API_KEY));
    }
    
}
