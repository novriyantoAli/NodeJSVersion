
package net.ali.rhein.mvpbase.models;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class SalesJourneyPlanData {

    @SerializedName("add_outlet")
    private Object mAddOutlet;
    @SerializedName("call_cycle")
    private String mCallCycle;
    @SerializedName("estimasi")
    private String mEstimasi;
    @SerializedName("id_kabupaten")
    private String mIdKabupaten;
    @SerializedName("id_provinsi")
    private String mIdProvinsi;
    @SerializedName("id_salesman")
    private String mIdSalesman;
    @SerializedName("nama_kabupaten")
    private String mNamaKabupaten;
    @SerializedName("nama_provinsi")
    private String mNamaProvinsi;
    @SerializedName("tgl_plan")
    private String mTglPlan;

    public Object getAddOutlet() {
        return mAddOutlet;
    }

    public void setAddOutlet(Object addOutlet) {
        mAddOutlet = addOutlet;
    }

    public String getCallCycle() {
        return mCallCycle;
    }

    public void setCallCycle(String callCycle) {
        mCallCycle = callCycle;
    }

    public String getEstimasi() {
        return mEstimasi;
    }

    public void setEstimasi(String estimasi) {
        mEstimasi = estimasi;
    }

    public String getIdKabupaten() {
        return mIdKabupaten;
    }

    public void setIdKabupaten(String idKabupaten) {
        mIdKabupaten = idKabupaten;
    }

    public String getIdProvinsi() {
        return mIdProvinsi;
    }

    public void setIdProvinsi(String idProvinsi) {
        mIdProvinsi = idProvinsi;
    }

    public String getIdSalesman() {
        return mIdSalesman;
    }

    public void setIdSalesman(String idSalesman) {
        mIdSalesman = idSalesman;
    }

    public String getNamaKabupaten() {
        return mNamaKabupaten;
    }

    public void setNamaKabupaten(String namaKabupaten) {
        mNamaKabupaten = namaKabupaten;
    }

    public String getNamaProvinsi() {
        return mNamaProvinsi;
    }

    public void setNamaProvinsi(String namaProvinsi) {
        mNamaProvinsi = namaProvinsi;
    }

    public String getTglPlan() {
        return mTglPlan;
    }

    public void setTglPlan(String tglPlan) {
        mTglPlan = tglPlan;
    }

}
