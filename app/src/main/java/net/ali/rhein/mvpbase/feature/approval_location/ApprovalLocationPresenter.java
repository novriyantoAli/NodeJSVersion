package net.ali.rhein.mvpbase.feature.approval_location;

import android.content.SharedPreferences;

import net.ali.rhein.mvpbase.base.ui.BasePresenter;
import net.ali.rhein.mvpbase.models.JenisOutletResponse;
import net.ali.rhein.mvpbase.models.SaveOutletResponse;
import net.ali.rhein.mvpbase.network.NetworkCallback;

import java.util.HashMap;

/**
 * Created by rhein on 3/30/18.
 */

public class ApprovalLocationPresenter extends BasePresenter<ApprovalLocationView> {

    ApprovalLocationPresenter(ApprovalLocationView view){ super.attachView(view);}

    void loadData(SharedPreferences sharedPreferences){
        String token         = sharedPreferences.getString("api_token", "");
        String clientService = "frontend-client";
        String authKey       = "simplerestapi";
        String userID        = sharedPreferences.getString("id_user", "");
        String salesId       = sharedPreferences.getString("id_salesman", "");


        addSubscribe(apiStores.getJenisOutletResponse(authKey, token, clientService, userID), new NetworkCallback<JenisOutletResponse>() {

            @Override
            public void onSuccess(JenisOutletResponse model) {
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

    void saveData(SharedPreferences sharedPreferences, String nama_outlet, String nama_pemilik,
                  String alamat_outlet, String no_telp_outlet, String id_jenis_outlet,
                  double longtitude, double latitude, double xlongtitude, double xlatitude){

        view.showLoading();

        String token         = sharedPreferences.getString("api_token", "");
        String clientService = "frontend-client";
        String authKey       = "simplerestapi";
        String userID        = sharedPreferences.getString("id_user", "");
        String salesId       = sharedPreferences.getString("id_salesman", "");


        HashMap<String, String> params = new HashMap<>();
        params.put("nama_outlet", nama_outlet);
        params.put("nama_pemilik", nama_pemilik);
        params.put("alamat_outlet", alamat_outlet);
        params.put("no_telp_outlet", no_telp_outlet);
        params.put("id_salesman", salesId);
        params.put("latitude", String.valueOf(latitude));
        params.put("longtitude", String.valueOf(longtitude));
        params.put("id_jenis_outlet", id_jenis_outlet);
        params.put("xlatitude", String.valueOf(xlatitude));
        params.put("xlongtitude", String.valueOf(xlongtitude));

        addSubscribe(apiStores.getSaveOutletResponse(authKey, token, clientService, userID, params), new NetworkCallback<SaveOutletResponse>() {

            @Override
            public void onSuccess(SaveOutletResponse model) {
                view.showSuccessSaveData(model);
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

}
