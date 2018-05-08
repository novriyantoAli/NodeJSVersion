package net.ali.rhein.mvpbase.feature.customer_record_card.form_outlet;

import android.content.SharedPreferences;

import net.ali.rhein.mvpbase.base.ui.BasePresenter;
import net.ali.rhein.mvpbase.models.FormOutletOrderResponse;
import net.ali.rhein.mvpbase.models.FormOutletRecordCardResponse;
import net.ali.rhein.mvpbase.models.ProductResponse;
import net.ali.rhein.mvpbase.network.NetworkCallback;

import java.util.HashMap;

/**
 * Created by rhein on 4/3/18.
 */

public class FormOutletPresenter extends BasePresenter<FormOutletView>{
    FormOutletPresenter(FormOutletView formOutletView){
        super.attachView(formOutletView);
    }

    void sendData(SharedPreferences sharedPreferences, String id_outlet, String id_produk,  String stok,
                  String pembelian, String latitude, String longitude){
        view.showLoading(0);
        String token         = sharedPreferences.getString("api_token", "");
        String clientService = "frontend-client";
        String authKey       = "simplerestapi";
        String userID        = sharedPreferences.getString("id_user", "");
        String salesId       = sharedPreferences.getString("id_salesman", "");

        HashMap<String, String> params = new HashMap<>();
        params.put("id_outlet", id_outlet);
        params.put("id_salesman", salesId);
        params.put("id_produk", id_produk);
        params.put("stock", stok);
        params.put("pembelian", pembelian);
        params.put("latitude", latitude);
        params.put("longitude", longitude);

        addSubscribe(apiStores.getCheckOutletResponse(authKey, token, clientService, userID, params), new NetworkCallback<FormOutletRecordCardResponse>() {

            @Override
            public void onSuccess(FormOutletRecordCardResponse model) {
                view.showSuccessCheck(model);
            }

            @Override
            public void onFailure(String message) {
                view.showError(message);
            }

            @Override
            public void onFinish() {
                view.hideLoading(0);
            }
        });
    }

    void order(SharedPreferences sharedPreferences, String id_outlet, String id_produk,
               String batas_kredit, String jumlahOrder, String jenisPembayaran, String lamaPembayaran){
        view.showLoading(1);
        String token         = sharedPreferences.getString("api_token", "");
        String clientService = "frontend-client";
        String authKey       = "simplerestapi";
        String userID        = sharedPreferences.getString("id_user", "");
        String salesId       = sharedPreferences.getString("id_salesman", "");

        HashMap<String, String> params = new HashMap<>();
        params.put("id_outlet", id_outlet);
        params.put("id_salesman", salesId);
        params.put("jml_order", jumlahOrder);
        params.put("id_produk", id_produk);
        params.put("jenis_pembayaran", jenisPembayaran);
        params.put("batas_kredit", batas_kredit);
        params.put("lama_pembayaran", lamaPembayaran);

        addSubscribe(apiStores.getOrderOutletResponse(authKey, token, clientService, userID, params), new NetworkCallback<FormOutletOrderResponse>() {

            @Override
            public void onSuccess(FormOutletOrderResponse model) {
                view.showSuccessOrder(model);
            }

            @Override
            public void onFailure(String message) {
                view.showError(message);
            }

            @Override
            public void onFinish() {
                view.hideLoading(1);
            }
        });
    }

    void getFormInformation(SharedPreferences sharedPreferences){
        String token         = sharedPreferences.getString("api_token", "");
        String clientService = "frontend-client";
        String authKey       = "simplerestapi";
        String userID        = sharedPreferences.getString("id_user", "");
        String salesId       = sharedPreferences.getString("id_salesman", "");

        HashMap<String, String> params = new HashMap<>();
        params.put("sales_id", salesId);

        addSubscribe(apiStores.getProductResponse(authKey, token, clientService, userID, params), new NetworkCallback<ProductResponse>() {

            @Override
            public void onSuccess(ProductResponse model) {
                view.showProductResponse(model);
            }

            @Override
            public void onFailure(String message) {
                view.showError(message);
            }

            @Override
            public void onFinish() {

            }
        });

    }
}
