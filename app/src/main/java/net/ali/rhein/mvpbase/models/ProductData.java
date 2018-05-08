
package net.ali.rhein.mvpbase.models;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class ProductData {

    @SerializedName("date_created")
    private String mDateCreated;
    @SerializedName("date_edit")
    private String mDateEdit;
    @SerializedName("id_produk")
    private String mIdProduk;
    @SerializedName("nama_produk")
    private String mNamaProduk;
    @SerializedName("no_regis")
    private String mNoRegis;
    @SerializedName("status")
    private String mStatus;
    @SerializedName("tgl_regis")
    private String mTglRegis;
    @SerializedName("user_created")
    private String mUserCreated;
    @SerializedName("user_edit")
    private String mUserEdit;

    public String getDateCreated() {
        return mDateCreated;
    }

    public void setDateCreated(String dateCreated) {
        mDateCreated = dateCreated;
    }

    public String getDateEdit() {
        return mDateEdit;
    }

    public void setDateEdit(String dateEdit) {
        mDateEdit = dateEdit;
    }

    public String getIdProduk() {
        return mIdProduk;
    }

    public void setIdProduk(String idProduk) {
        mIdProduk = idProduk;
    }

    public String getNamaProduk() {
        return mNamaProduk;
    }

    public void setNamaProduk(String namaProduk) {
        mNamaProduk = namaProduk;
    }

    public String getNoRegis() {
        return mNoRegis;
    }

    public void setNoRegis(String noRegis) {
        mNoRegis = noRegis;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

    public String getTglRegis() {
        return mTglRegis;
    }

    public void setTglRegis(String tglRegis) {
        mTglRegis = tglRegis;
    }

    public String getUserCreated() {
        return mUserCreated;
    }

    public void setUserCreated(String userCreated) {
        mUserCreated = userCreated;
    }

    public String getUserEdit() {
        return mUserEdit;
    }

    public void setUserEdit(String userEdit) {
        mUserEdit = userEdit;
    }

}
