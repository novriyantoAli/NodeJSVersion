
package net.ali.rhein.mvpbase.models;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class Actual {

    @SerializedName("id_produk")
    private String mIdProduk;
    @SerializedName("total_terjual")
    private String mTotalTerjual;

    public String getIdProduk() {
        return mIdProduk;
    }

    public void setIdProduk(String idProduk) {
        mIdProduk = idProduk;
    }

    public String getTotalTerjual() {
        return mTotalTerjual;
    }

    public void setTotalTerjual(String totalTerjual) {
        mTotalTerjual = totalTerjual;
    }
}
