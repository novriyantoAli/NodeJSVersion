package net.ali.rhein.mvpbase.feature.homes;

import android.content.Intent;

/**
 * Created by rhein on 3/30/18.
 */

public interface HomesView {

    void moveToActivity(Intent intent);

    void showMessage(String message);

    void setData();
}
