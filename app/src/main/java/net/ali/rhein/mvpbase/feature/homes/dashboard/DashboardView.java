package net.ali.rhein.mvpbase.feature.homes.dashboard;

import net.ali.rhein.mvpbase.models.SalesBarChartAllData;
import net.ali.rhein.mvpbase.models.SalesBarChartResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rhein on 4/21/18.
 */

public interface DashboardView {

    void showLoading();

    void hideLoading();

    void showError(String message);

    void showDrawGraph(List<SalesBarChartAllData> salesBarChartAllData);
}
