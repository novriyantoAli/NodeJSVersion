
package net.ali.rhein.mvpbase.models;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class JenisOutletData {

    @SerializedName("id_jenis_outlet")
    private String mIdJenisOutlet;
    @SerializedName("jenis_outlet")
    private String mJenisOutlet;

    public String getIdJenisOutlet() {
        return mIdJenisOutlet;
    }

    public void setIdJenisOutlet(String idJenisOutlet) {
        mIdJenisOutlet = idJenisOutlet;
    }

    public String getJenisOutlet() {
        return mJenisOutlet;
    }

    public void setJenisOutlet(String jenisOutlet) {
        mJenisOutlet = jenisOutlet;
    }

}
