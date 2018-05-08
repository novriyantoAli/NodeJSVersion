
package net.ali.rhein.mvpbase.models;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class Target {

    @SerializedName("target_value")
    private String mTargetValue;

    public String getTargetValue() {
        return mTargetValue;
    }

    public void setTargetValue(String targetValue) {
        mTargetValue = targetValue;
    }

}
