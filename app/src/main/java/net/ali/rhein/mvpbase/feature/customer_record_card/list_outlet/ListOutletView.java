package net.ali.rhein.mvpbase.feature.customer_record_card.list_outlet;

import net.ali.rhein.mvpbase.feature.customer_record_card.form_outlet.FormOutletFragment;
import net.ali.rhein.mvpbase.models.ListOutletResponse;


/**
 * Created by rhein on 4/3/18.
 */

public interface ListOutletView {

    void showLoading();

    void hideLoading();

    void showError(String message);

    void showSuccess(ListOutletResponse model);

    void moveToFragment(FormOutletFragment formOutletFragment);
}
