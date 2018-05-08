
package net.ali.rhein.mvpbase.models;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class ProductResponse {

    @SerializedName("data")
    private List<ProductData> mData;
    @SerializedName("actual")
    private List<Actual> mActual;
    @SerializedName("target")
    private List<Target> mTarget;
    @SerializedName("id")
    private String mId;
    @SerializedName("message")
    private String mMessage;
    @SerializedName("status")
    private Long mStatus;

    public List<ProductData> getData() {
        return mData;
    }

    public void setData(List<ProductData> data) {
        mData = data;
    }

    public List<Actual> getActual() {
        return mActual;
    }

    public void setActual(List<Actual> actual) {
        mActual = actual;
    }

    public List<Target> getTarget() {
        return mTarget;
    }

    public void setTarget(List<Target> target) {
        mTarget = target;
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
