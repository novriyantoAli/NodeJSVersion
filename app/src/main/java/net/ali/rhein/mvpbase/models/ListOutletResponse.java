
package net.ali.rhein.mvpbase.models;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class ListOutletResponse {

    @SerializedName("data")
    private List<ListOutletData> mData;
    @SerializedName("status")
    private Long mStatus;

    public List<ListOutletData> getData() {
        return mData;
    }

    public void setData(List<ListOutletData> data) {
        mData = data;
    }

    public Long getStatus() {
        return mStatus;
    }

    public void setStatus(Long status) {
        mStatus = status;
    }


}
