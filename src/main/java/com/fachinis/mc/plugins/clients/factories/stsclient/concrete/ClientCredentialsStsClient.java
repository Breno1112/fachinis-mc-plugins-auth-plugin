package com.fachinis.mc.plugins.clients.factories.stsclient.concrete;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;

import com.fachinis.mc.plugins.clients.factories.stsclient.StsClient;
import com.fachinis.mc.plugins.domain.constants.PluginConfigurationKeys;
import com.fachinis.mc.plugins.domain.dtos.sts.response.StsAccessTokenResponse;
import com.fachinis.mc.plugins.singletons.PluginConfigurationSingleton;
import com.google.gson.Gson;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ClientCredentialsStsClient implements StsClient {

    private final OkHttpClient client = new OkHttpClient();
    private final Gson gson = new Gson();
    private StsAccessTokenResponse cachedAccessToken = null;
    private LocalDateTime lastTokenRefresh = null;

    @Override
    public Builder injectToken(Builder requestBuilder) {
        if (!this.isTokenInvalid()) {
            return requestBuilder.addHeader("Bearer", this.cachedAccessToken.getAccess_token());
        }
        this.refreshAccessTokenAndWait();
        if (this.isTokenInvalid()) {
            return requestBuilder;
        }
        return requestBuilder.addHeader("Bearer", this.cachedAccessToken.getAccess_token());
    }

    private void refreshAccessTokenAndWait() {
        try {
            this.refreshLastTokenRequestDate();
            final Response response = client.newCall(this.buildRequest()).execute();
            if (response.isSuccessful()) {
                final StsAccessTokenResponse accessTokenResponse = this.gson.fromJson(response.body().string(), StsAccessTokenResponse.class);
                this.cachedAccessToken = accessTokenResponse;
            } else {
                this.cachedAccessToken = null;
            }
        } catch (IOException e) {
            this.cachedAccessToken = null;
        }
    }

    private Request buildRequest() {
        return new Request.Builder()
            .url(
                URI.
                    create(
                        String.format(
                            "%s%s",PluginConfigurationSingleton.getInstance().getPropertyString(PluginConfigurationKeys.BACKEND_API_STS_CLIENT_CREDENTIALS_CLIENT_ID
                        )
                    )
                ).toString()
            )
            .addHeader("Content-Type", "application/x-www-form-urlencoded")
            .post(this.buildRequestBody())
            .build();
    }

    private RequestBody buildRequestBody() {
        return new FormBody.Builder()
                .add("grant_type", "client_credentials")
                .add("client_id", PluginConfigurationSingleton.getInstance().getPropertyString(PluginConfigurationKeys.BACKEND_API_STS_CLIENT_CREDENTIALS_CLIENT_ID))
                .add("client_secret", PluginConfigurationSingleton.getInstance().getPropertyString(PluginConfigurationKeys.BACKEND_API_STS_CLIENT_CREDENTIALS_CLIENT_ID))
                .build();
    }

    private void refreshLastTokenRequestDate() {
        this.lastTokenRefresh = LocalDateTime.now();
    }

    private boolean isTokenInvalid() {
        if (this.lastTokenRefresh == null || this.cachedAccessToken == null) {
            return true;
        }

        LocalDateTime invalidDate = this.lastTokenRefresh.plusSeconds(this.cachedAccessToken.getExpires_in());
        final LocalDateTime now = LocalDateTime.now();
        return now.isAfter(invalidDate);
    }
    
}
