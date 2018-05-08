package net.ali.rhein.mvpbase.feature.auth;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;

import net.ali.rhein.mvpbase.base.ui.BasePresenter;
import net.ali.rhein.mvpbase.feature.homes.HomesActivity;
import net.ali.rhein.mvpbase.models.LoginData;
import net.ali.rhein.mvpbase.models.LoginResponse;
import net.ali.rhein.mvpbase.network.NetworkCallback;

import java.util.HashMap;

/**
 * Created by rhein on 3/27/18.
 */

public class AuthPresenter extends BasePresenter<AuthView> {

    AuthPresenter(AuthView view) {
        super.attachView(view);
    }

    void login(String username, String password) {
        view.showLoading();

        HashMap<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);

        String clientService = "frontend-client";
        String authKey       = "simplerestapi";

        addSubscribe(apiStores.getLoginResponse(clientService, authKey, params), new NetworkCallback<LoginResponse>() {
            @Override
            public void onSuccess(LoginResponse model) {
                view.showLoginSuccess(model);
            }

            @Override
            public void onFailure(String message) {
                view.showLoginError(message);
            }

            @Override
            public void onFinish() {
                view.hideLoading();
            }
        });
    }

    void validate(String username, String password){
        if (username.contains("@") && username.contains(".")) {
            if (username.length() == 0 || username.equals("")){
                view.showValidationUsernameError("Email/Username tidak boleh kosong.");
            }
            else{
                if (password.length() < 5)
                    view.showValidationPasswordError("Password harus 5 karakter");

                else
                    view.showValidationSuccess(username, password);

            }
        }
        else if (username.matches("[a-zA-Z0-9]*")){
            if (username.length() == 0 || username.equals("")){
                view.showValidationUsernameError("Email/Username tidak boleh kosong");
            }
            else{
                if (password.length() < 5)
                    view.showValidationPasswordError("Password harus 5 karakter");

                else
                    view.showValidationSuccess(username, password);

            }
        }
        else{
            view.showValidationUsernameError("Masukkan Email/Username valid");
        }
    }


    void bindData(LoginResponse response, SharedPreferences sharedPreferences, Activity activity){

        SharedPreferences.Editor editor = sharedPreferences.edit();

        LoginData loginData = response.getData();

        String api_token    = response.getToken();
        String id_user      = loginData.getIdDistroUser();
        String id_salesman  = loginData.getIdSalesman();
        String email_user   = loginData.getEmail();
        String nama_user    = loginData.getNamaUser();

        editor.putString("api_token", api_token);
        editor.putString("id_user", id_user);
        editor.putString("id_salesman", id_salesman);
        editor.putString("email_user", email_user);
        editor.putString("nama_user", nama_user);

        editor.putInt("login", 1);

        editor.commit();

        Intent intent = new Intent(activity, HomesActivity.class);
        view.moveToActivity(intent);

    }
}
