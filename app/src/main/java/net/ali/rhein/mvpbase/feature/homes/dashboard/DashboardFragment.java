package net.ali.rhein.mvpbase.feature.homes.dashboard;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidadvance.topsnackbar.TSnackbar;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.udevel.widgetlab.TypingIndicatorView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import net.ali.rhein.mvpbase.R;
import net.ali.rhein.mvpbase.base.mvp.MvpFragment;
import net.ali.rhein.mvpbase.feature.homes.HomesActivity;
import net.ali.rhein.mvpbase.models.SalesBarChartAllData;
import net.ali.rhein.mvpbase.models.SalesBarChartData;
import net.ali.rhein.mvpbase.models.SalesBarChartResponse;

import java.util.ArrayList;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Context.MODE_PRIVATE;

public class DashboardFragment extends MvpFragment<DashboardPresenter> implements DashboardView,
        OnChartValueSelectedListener, SwipeRefreshLayout.OnRefreshListener{

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final String TAG = DashboardFragment.class.getSimpleName();

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private SharedPreferences sharedPreferences;

    @BindView(R.id.linear_layout)
    LinearLayout linearLayout;

    @BindView(R.id.line_chart)
    LineChart lineChart;

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    private float[] yData = {25.3f, 10.6f, 66.76f, 44.32f, 46.01f, 16.89f, 23.9f};
    private String[] xData = {"Mitch", "Jessica" , "Mohammad" , "Kelsey", "Sam", "Robert", "Ashley"};


    public DashboardFragment() {}

    public static DashboardFragment newInstance(String param1, String param2) {
        DashboardFragment fragment = new DashboardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden)
            mListener.onFragmentInteraction("Dashboard");
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(String title);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        if (mListener != null)
            mListener.onFragmentInteraction("Dashboard");

        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialize();
    }

    @Override
    protected DashboardPresenter createPresenter() {
        return new DashboardPresenter(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onNothingSelected() {}


    @Override
    public void onValueSelected(Entry e, Highlight h) {
        int pos1 = e.toString().indexOf("y: ");
        String sales = e.toString().substring(pos1 + 7);
        //Log.e(TAG, String.valueOf(pos1));
        Log.e(TAG, e.toString());
        Log.e(TAG, h.toString());
        /*
        for(int i = 0; i < yData.length; i++){

            if(yData[i] == Float.parseFloat(sales)){
                pos1 = i;
                break;
            }
        }
        */
        //String employee = xData[pos1 + 1];
        //Toast.makeText(getContext(), "Employee " + employee + "\n" + "Sales: $" + sales + "K", Toast.LENGTH_LONG).show();
    }

    @Override
    public void showLoading() {
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showError(String message) {
        showTopSnackBars(message, R.drawable.ic_warning);
    }

    @Override
    public void showDrawGraph(List<SalesBarChartAllData> salesBarChartAllData) {

        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(false);

        ArrayList<Entry> yValues = new ArrayList<>();

        for (int i=0;i<salesBarChartAllData.size();i++){
            Log.e("DASHBOARD ::", String.valueOf(salesBarChartAllData.get(i).getProductSelling()));
        }

        for (int i=0;i<salesBarChartAllData.size();i++){
            yValues.add(new Entry(i, salesBarChartAllData.get(i).getProductSelling()));
        }

        LimitLine upperLimit = new LimitLine(20f, "Target Day");
        upperLimit.setLineWidth(4f);
        upperLimit.enableDashedLine(10f,10f,0f);
        upperLimit.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        upperLimit.setTextSize(15f);

        LimitLine lowerLimit = new LimitLine(5f, "Danger");
        lowerLimit.setLineWidth(4f);
        lowerLimit.enableDashedLine(10f,10f,10f);
        lowerLimit.setLabelPosition(LimitLine.LimitLabelPosition.LEFT_BOTTOM);
        lowerLimit.setTextSize(15f);

        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.removeAllLimitLines();
        leftAxis.addLimitLine(upperLimit);
        leftAxis.addLimitLine(lowerLimit);
        leftAxis.setAxisMaximum(100f);
        leftAxis.setAxisMinimum(0f);
        leftAxis.enableGridDashedLine(10f,10f,0);
        leftAxis.setDrawLimitLinesBehindData(true);

        LineDataSet set1 = new LineDataSet(yValues, "All Product Trafic");

        set1.setFillAlpha(110);
        set1.setColor(ColorTemplate.rgb("#ad01ba"));
        set1.setLineWidth(3f);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);
        LineData data = new LineData(dataSets);

        if(salesBarChartAllData.size() > 0){
            lineChart.setData(data);
            lineChart.setOnChartValueSelectedListener(this);
        }
    }

    @Override
    public void onRefresh() {
        presenter.loadData(sharedPreferences);
    }



    private void initialize(){
        sharedPreferences = getActivity().getSharedPreferences("data", MODE_PRIVATE);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary), getResources().getColor(R.color.colorAccent));
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                presenter.loadData(sharedPreferences);
            }
        });
        //presenter.loadData(sharedPreferences);
        //createRandomGraph();
    }
    private void showTopSnackBars(String message, int drawable){
        TSnackbar snackbar = TSnackbar.make(linearLayout, message, TSnackbar.LENGTH_SHORT);
        snackbar.setActionTextColor(Color.WHITE);
        snackbar.setIconLeft(drawable, 24);
        snackbar.setIconPadding(8);
        View snackbarView = snackbar.getView();
        switch(drawable){
            case R.drawable.ic_success:
                snackbarView.setBackgroundColor(Color.parseColor("#5cb85c"));
                break;
            case R.drawable.ic_warning:
                snackbarView.setBackgroundColor(Color.parseColor("#d10202"));
                break;
        }
        TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        snackbar.show();
    }
}
