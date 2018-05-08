package net.ali.rhein.mvpbase.feature.homes.sales_journey_plan;

import android.content.Intent;

import net.ali.rhein.mvpbase.models.SalesJourneyPlanResponse;

/**
 * Created by rhein on 4/1/18.
 */

public interface SalesJourneyPlanView {
    void showLoading();

    void hideLoading();

    void showError(String message);

    void showSuccess(SalesJourneyPlanResponse model);

    void moveToActivity(Intent intent);
}
