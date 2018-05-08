
package net.ali.rhein.mvpbase.models;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class SalesJourneyPlanResponse {

    @SerializedName("data")
    private List<SalesJourneyPlanData> mData;
    @SerializedName("status")
    private Long mStatus;
    @SerializedName("message")
    private String mMessage;

    public List<SalesJourneyPlanData> getData() {
        return mData;
    }

    public void setData(List<SalesJourneyPlanData> data) {
        mData = data;
    }

    public Long getStatus() {
        return mStatus;
    }

    public void setStatus(Long status) {
        mStatus = status;
    }

    public String getMessage(){ return mMessage; }

    public void setmMessage(String message){
        mMessage = message;
    }
}
