package net.ali.rhein.mvpbase.feature.auth;

import android.content.Intent;

import net.ali.rhein.mvpbase.models.LoginResponse;

/**
 * Created by rhein on 3/27/18.
 */

interface AuthView {

    void showLoading();

    void hideLoading();

    void showLoginError(String messasge);

    void showLoginSuccess(LoginResponse model);

    void showValidationUsernameError(String message);

    void showValidationPasswordError(String message);

    void showValidationSuccess(String username, String password);

    void moveToActivity(Intent intent);

}
