package com.fachinis.mc.plugins.clients;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import com.fachinis.mc.plugins.domain.dtos.auth.request.LoginRequestDTO;
import com.fachinis.mc.plugins.domain.entities.AuthenticatedUser;
import com.fachinis.mc.plugins.services.Component;
import com.fachinis.mc.plugins.singletons.PluginConfigurationSingleton;
import com.google.gson.Gson;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class AuthClient extends Component {

    private final OkHttpClient client = new OkHttpClient();
    private final Gson gson = new Gson();
    

    public CompletableFuture<AuthenticatedUser> doRegistration(String username, String password, String email) {
        CompletableFuture<AuthenticatedUser> future = new CompletableFuture<>();
        client.newCall(buildRegistrationRequest(username, password, email)).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException exception) {
                future.completeExceptionally(exception);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    if (!response.isSuccessful() || response.body() == null) {
                        future.completeExceptionally(new RuntimeException(String.format("Erro ao autenticar o usuário %s", username)));
                    } else {
                        final AuthenticatedUser authenticatedUser = gson.fromJson(response.body().charStream(), AuthenticatedUser.class);
                        future.complete(authenticatedUser);
                    }
                } catch(Exception e) {
                    future.completeExceptionally(e);
                }
            }
        });

        return future;
    }

    private Request buildRegistrationRequest(String username, String password, String email) {
        final LoginRequestDTO loginRequestDTO = new LoginRequestDTO();
        loginRequestDTO.setEmail(email);
        loginRequestDTO.setUsername(username);
        loginRequestDTO.setPassword(password);
        final String jsonBody = this.gson.toJson(loginRequestDTO);
        RequestBody body = RequestBody.create(jsonBody, MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
            .url(String.format(
                "%s%s", 
                PluginConfigurationSingleton.getInstance().getPropertyString("auth.api.url.basepath"),
                PluginConfigurationSingleton.getInstance().getPropertyString("auth.api.url.register.path")
                ))
            .post(body)
            .build();
        return request;
    }
}
