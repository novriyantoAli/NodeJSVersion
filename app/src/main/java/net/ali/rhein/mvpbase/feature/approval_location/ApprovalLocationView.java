package net.ali.rhein.mvpbase.feature.approval_location;

import net.ali.rhein.mvpbase.models.JenisOutletResponse;
import net.ali.rhein.mvpbase.models.SaveOutletResponse;

/**
 * Created by rhein on 3/30/18.
 */

public interface ApprovalLocationView {

    void showSuccess(JenisOutletResponse model);

    void showSuccessSaveData(SaveOutletResponse model);

    void showError(String message);

    void hideLoading();

    void showLoading();
}
