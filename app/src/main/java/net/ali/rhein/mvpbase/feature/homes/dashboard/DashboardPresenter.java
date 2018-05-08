package net.ali.rhein.mvpbase.feature.homes.dashboard;

import android.content.SharedPreferences;
import android.util.Log;

import net.ali.rhein.mvpbase.base.ui.BasePresenter;
import net.ali.rhein.mvpbase.models.SalesBarChartAllData;
import net.ali.rhein.mvpbase.models.SalesBarChartData;
import net.ali.rhein.mvpbase.models.SalesBarChartResponse;
import net.ali.rhein.mvpbase.network.NetworkCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by rhein on 4/21/18.
 */

public class DashboardPresenter extends BasePresenter<DashboardView> {

    List<SalesBarChartData> barChartData;
    List<SalesBarChartAllData> barChartAllData = new ArrayList<>();

    DashboardPresenter(DashboardView view){
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
        params.put("id_salesman", salesId);

        addSubscribe(apiStores.getSalesBarChartResponse(authKey, token, clientService, userID, params), new NetworkCallback<SalesBarChartResponse>() {
            @Override
            public void onSuccess(SalesBarChartResponse model) {
                if(model.getStatus() == 200) {
                    barChartData = model.getData();
                    buildData();
                }
                else
                    view.showError(model.getMessage());
            }

            @Override
            public void onFailure(String message) {
                //buildData();
                view.showError(message);
            }

            @Override
            public void onFinish() {
                view.hideLoading();
            }
        });
    }

    private void buildData(){
        barChartAllData = new ArrayList<>();
        for(int i=0;i<barChartData.size();i++){
            SalesBarChartAllData salesData = new SalesBarChartAllData();
            salesData.setDateName(barChartData.get(i).getTglKunjungan());
            salesData.setProductSelling(Integer.parseInt(barChartData.get(i).getPembelian()));
            barChartAllData.add(i, salesData);
        }
        Log.e("DASHBOARD :: ", "BEFORE REMOVE " + " SIZE :: " + barChartAllData.size());

        for (int i=0;i<barChartAllData.size();i++){
            for(int j=0;j<barChartAllData.size();j++){
                if(barChartAllData.get(i).getDateName().equalsIgnoreCase(barChartAllData.get(j).getDateName()) && i != j){
                    barChartAllData.get(i).setProductSelling(barChartAllData.get(i).getProductSelling() + barChartAllData.get(j).getProductSelling());
                    barChartAllData.remove(j);
                }
            }
        }
        view.showDrawGraph(barChartAllData);
        Log.e("DASHBOARD :: ", "AFTER REMOVE " + " SIZE :: " + barChartAllData.size());


    }
}
