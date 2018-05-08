
package net.ali.rhein.mvpbase.models;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class JenisOutletResponse {

    @SerializedName("data")
    private List<JenisOutletData> mData;
    @SerializedName("id")
    private String mId;
    @SerializedName("message")
    private String mMessage;
    @SerializedName("status")
    private Long mStatus;

    public List<JenisOutletData> getData() {
        return mData;
    }

    public void setData(List<JenisOutletData> data) {
        mData = data;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public Long getStatus() {
        return mStatus;
    }

    public void setStatus(Long status) {
        mStatus = status;
    }

}
