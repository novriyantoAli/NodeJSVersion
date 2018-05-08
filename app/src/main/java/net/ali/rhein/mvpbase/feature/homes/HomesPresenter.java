package net.ali.rhein.mvpbase.feature.homes;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;

import net.ali.rhein.mvpbase.base.ui.BasePresenter;
import net.ali.rhein.mvpbase.feature.approval_location.ApprovalLocationActivity;
import net.ali.rhein.mvpbase.models.LogoutResponse;
import net.ali.rhein.mvpbase.network.NetworkCallback;

/**
 * Created by rhein on 3/30/18.
 */

public class HomesPresenter extends BasePresenter<HomesView>{

    HomesPresenter(HomesView view){
        super.attachView(view);
    }

    void logout(SharedPreferences sharedPreferences){

        final String TAG = HomesPresenter.class.getSimpleName();
        String clientService = "frontend-client";
        String authKey       = "simplerestapi";
        String userID        = sharedPreferences.getString("", "id_user");
        String authorization = sharedPreferences.getString("", "api_token");

        addSubscribe(apiStores.getLogoutResponse(clientService, authKey, userID, authorization), new NetworkCallback<LogoutResponse>() {
            @Override
            public void onSuccess(LogoutResponse model) {
                view.showMessage(model.getMessage());
            }

            @Override
            public void onFailure(String message) {
                view.showMessage(message);
            }

            @Override
            public void onFinish() {
                view.setData();
            }
        });
    }

    void moveToActivity(HomesActivity activity){
        Intent i = new Intent(activity, ApprovalLocationActivity.class);
        i.putExtra("request", "apptovalLocation");
        view.moveToActivity(i);
    }
}
