package net.ali.rhein.mvpbase.feature.customer_record_card.form_outlet;


import net.ali.rhein.mvpbase.models.FormOutletOrderResponse;
import net.ali.rhein.mvpbase.models.FormOutletRecordCardResponse;
import net.ali.rhein.mvpbase.models.ProductResponse;

/**
 * Created by rhein on 4/3/18.
 */

interface FormOutletView {
    void showLoading(int status);

    void hideLoading(int status);

    void showError(String message);

    void showSuccessCheck(FormOutletRecordCardResponse model);

    void showSuccessOrder(FormOutletOrderResponse model);

    void showProductResponse(ProductResponse model);
}
