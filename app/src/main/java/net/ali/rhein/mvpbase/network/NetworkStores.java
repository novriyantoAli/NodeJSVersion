package net.ali.rhein.mvpbase.network;

import net.ali.rhein.mvpbase.models.FormOutletOrderResponse;
import net.ali.rhein.mvpbase.models.FormOutletRecordCardResponse;
import net.ali.rhein.mvpbase.models.JenisOutletResponse;
import net.ali.rhein.mvpbase.models.ListOutletResponse;
import net.ali.rhein.mvpbase.models.LoginResponse;
import net.ali.rhein.mvpbase.models.LogoutResponse;
import net.ali.rhein.mvpbase.models.ProductResponse;
import net.ali.rhein.mvpbase.models.SalesBarChartResponse;
import net.ali.rhein.mvpbase.models.SalesJourneyPlanResponse;
import net.ali.rhein.mvpbase.models.SaveOutletResponse;

import java.util.HashMap;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by rhein on 3/27/18.
 */

public interface NetworkStores {

    // coba kawan
    @FormUrlEncoded
    @POST("auth/login")
    Observable<LoginResponse> getLoginResponse(
            @Header("Client-Service") String clientService,
            @Header("Auth-Key") String authKey,
            @FieldMap HashMap<String, String> params);

    @POST("auth/logout")
    Observable<LogoutResponse> getLogoutResponse(
            @Header("Client-Service") String clientService,
            @Header("Auth-Key") String authKey,
            @Header("User-ID")String id,
            @Header("Auth") String token);

    @FormUrlEncoded
    @POST("salesjourneyplan")
    Observable<SalesJourneyPlanResponse> getSalesPlan(
            @Header("Auth-Key") String authKey,
            @Header("Auth") String token,
            @Header("Client-Service") String clientService,
            @Header("User-ID") String id, @FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST("salesjourneyplan/detail")
    Observable<ListOutletResponse> getOutletResponse(
            @Header("Auth-Key") String authKey,
            @Header("Auth") String token,
            @Header("Client-Service") String clientService,
            @Header("User-ID") String id, @FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST("customerrecordcard/create")
    Observable<FormOutletRecordCardResponse> getCheckOutletResponse(
            @Header("Auth-Key") String authKey,
            @Header("Auth") String token,
            @Header("Client-Service") String clientService,
            @Header("User-ID") String id, @FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST("customerrecordcard/order")
    Observable<FormOutletOrderResponse> getOrderOutletResponse(
            @Header("Auth-Key") String authKey,
            @Header("Auth") String token,
            @Header("Client-Service") String clientService,
            @Header("User-ID") String id, @FieldMap HashMap<String, String> params);

    @GET("outlets")
    Observable<JenisOutletResponse> getJenisOutletResponse(
            @Header("Auth-Key") String authKey,
            @Header("Auth") String token,
            @Header("Client-Service") String clientService,
            @Header("User-ID") String id);

    @FormUrlEncoded
    @POST("outlets/saveOutlet")
    Observable<SaveOutletResponse> getSaveOutletResponse(
            @Header("Auth-Key") String authKey,
            @Header("Auth") String token,
            @Header("Client-Service") String clientService,
            @Header("User-ID") String id, @FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST("product")
    Observable<ProductResponse> getProductResponse(
            @Header("Auth-Key") String authKey,
            @Header("Auth") String token,
            @Header("Client-Service") String clientService,
            @Header("User-ID") String id, @FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST("customerrecordcard/getSalesBarChart")
    Observable<SalesBarChartResponse> getSalesBarChartResponse(
            @Header("Auth-Key") String authKey,
            @Header("Auth") String token,
            @Header("Client-Service") String clientService,
            @Header("User-ID") String id, @FieldMap HashMap<String, String> params);
}
