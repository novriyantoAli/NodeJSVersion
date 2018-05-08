package net.ali.rhein.mvpbase.feature.homes.sales_journey_plan;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import net.ali.rhein.mvpbase.base.ui.BasePresenter;
import net.ali.rhein.mvpbase.feature.customer_record_card.CustomerRecordCardActivity;
import net.ali.rhein.mvpbase.models.SalesJourneyPlanData;
import net.ali.rhein.mvpbase.models.SalesJourneyPlanResponse;
import net.ali.rhein.mvpbase.network.NetworkCallback;

import java.util.HashMap;

/**
 * Created by rhein on 4/1/18.
 */

public class SalesJourneyPlanPresenter extends BasePresenter<SalesJourneyPlanView> {

    SalesJourneyPlanPresenter(SalesJourneyPlanView view){
        super.attachView(view);
    }

    void loadData(SharedPreferences sharedPreferences){
        view.showLoading();

        String token         = sharedPreferences.getString("api_token", "");
        String clientService = "frontend-client";
        String authKey       = "simplerestapi";
        String userID        = sharedPreferences.getString("id_user", "");
        String salesId       = sharedPreferences.getString("id_salesman", "");

        HashMap<String, String> params = new HashMap<>();
        params.put("sales_id", salesId);

        addSubscribe(apiStores.getSalesPlan(authKey, token, clientService, userID, params), new NetworkCallback<SalesJourneyPlanResponse>() {

            @Override
            public void onSuccess(SalesJourneyPlanResponse model) {
                view.showSuccess(model);
            }

            @Override
            public void onFailure(String message) {
                view.showError(message);
            }

            @Override
            public void onFinish() {
                view.hideLoading();
            }
        });
    }

    void prepareToActivity(SalesJourneyPlanData data, Activity activity){
        Intent intent = new Intent(activity, CustomerRecordCardActivity.class);

        Bundle bundle = new Bundle();

        //Menyisipkan tipe data String ke dalam obyek bundle
        bundle.putString("id_kabupaten", data.getIdKabupaten());
        bundle.putString("nama_provinsi", data.getNamaProvinsi());
        bundle.putString("nama_kabupaten", data.getNamaKabupaten());
        intent.putExtras(bundle);

        view.moveToActivity(intent);

    }
}
