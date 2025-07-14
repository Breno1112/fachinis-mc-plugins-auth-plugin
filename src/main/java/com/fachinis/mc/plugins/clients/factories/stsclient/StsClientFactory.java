package com.fachinis.mc.plugins.clients.factories.stsclient;

import com.fachinis.mc.plugins.clients.factories.stsclient.concrete.ApiKeyStsClient;
import com.fachinis.mc.plugins.clients.factories.stsclient.concrete.ClientCredentialsStsClient;
import com.fachinis.mc.plugins.clients.factories.stsclient.concrete.NoAuthStsClient;
import com.fachinis.mc.plugins.singletons.PluginConfigurationSingleton;

public class StsClientFactory {
    
    private StsClientFactory() {}

    private StsClient client;

    private static class StsClientFactoryInstanceHolder {
        private static final StsClientFactory INSTANCE = new StsClientFactory();
    }

    public static StsClientFactory getInstance() {
        return StsClientFactoryInstanceHolder.INSTANCE;
    }

    public void inicialize() {
        switch (PluginConfigurationSingleton.getInstance().getApiStsConfigurationType()) {
            case CLIENT_CREDENTIALS:
                this.client = new ClientCredentialsStsClient();
                break;
            case API_KEY:
                this.client = new ApiKeyStsClient();
            case NONE:
                this.client = new NoAuthStsClient();
            default:
                break;
        }
    }

    public StsClient getClient() {
        return this.client;
    }
}
