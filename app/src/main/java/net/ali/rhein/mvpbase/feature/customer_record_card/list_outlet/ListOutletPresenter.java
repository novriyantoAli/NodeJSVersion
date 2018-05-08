package net.ali.rhein.mvpbase.feature.customer_record_card.list_outlet;

import android.content.SharedPreferences;
import android.os.Bundle;

import net.ali.rhein.mvpbase.base.ui.BasePresenter;
import net.ali.rhein.mvpbase.feature.customer_record_card.form_outlet.FormOutletFragment;
import net.ali.rhein.mvpbase.models.ListOutletData;
import net.ali.rhein.mvpbase.models.ListOutletResponse;
import net.ali.rhein.mvpbase.network.NetworkCallback;

import java.util.HashMap;

/**
 * Created by rhein on 4/3/18.
 */

public class ListOutletPresenter extends BasePresenter<ListOutletView> {

    ListOutletPresenter(ListOutletView listOutletView){
        super.attachView(listOutletView);
    }

    void loadData(SharedPreferences sharedPreferences, String idkabupaten){
        view.showLoading();

        String token         = sharedPreferences.getString("api_token", "");
        String clientService = "frontend-client";
        String authKey       = "simplerestapi";
        String userID        = sharedPreferences.getString("id_user", "");
        String salesId       = sharedPreferences.getString("id_salesman", "");

        HashMap<String, String> params = new HashMap<>();
        params.put("sales_id", salesId);
        params.put("id_kabupaten", idkabupaten);

        addSubscribe(apiStores.getOutletResponse(authKey, token, clientService, userID, params), new NetworkCallback<ListOutletResponse>() {

            @Override
            public void onSuccess(ListOutletResponse model) {
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

    void prepareToFragment(String id_kabupaten, String nama_provinsi, String nama_kabupaten, ListOutletData data){
        Bundle bundle = new Bundle();
        bundle.putString("id_kabupaten", id_kabupaten);
        bundle.putString("nama_provinsi", nama_provinsi);
        bundle.putString("nama_kabupaten", nama_kabupaten);
        bundle.putString("latitude", data.getLatitude());
        bundle.putString("longtitude", data.getLongtitude());
        bundle.putString("nama_outlet", data.getNamaOutlet());
        bundle.putString("tanggal_kunjungan", data.getTglPlan());
        bundle.putString("id_outlet", data.getIdOutlet());
        bundle.putString("nama_pemilik", data.getNamaPemilik());
        bundle.putString("no_telp_outlet", data.getNoTelpOutlet());
        bundle.putString("jenis_outlet", data.getJenisOutlet());
        bundle.putString("alamat_outlet", data.getAlamatOutlet());
        FormOutletFragment formOutletFragment = new FormOutletFragment();
        formOutletFragment.setArguments(bundle);
        view.moveToFragment(formOutletFragment);
    }

}
