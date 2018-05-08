package net.ali.rhein.mvpbase.feature.homes.sales_journey_plan;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidadvance.topsnackbar.TSnackbar;

import net.ali.rhein.mvpbase.R;
import net.ali.rhein.mvpbase.adapter.SalesJourneyPlanAdapter;
import net.ali.rhein.mvpbase.base.mvp.MvpFragment;
import net.ali.rhein.mvpbase.models.SalesJourneyPlanData;
import net.ali.rhein.mvpbase.models.SalesJourneyPlanResponse;
import net.ali.rhein.mvpbase.utils.GridSpacingItemDecoration;
import net.ali.rhein.mvpbase.utils.RecyclerItemClickListener;

import java.util.List;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Context.MODE_PRIVATE;

public class SalesJourneyPlanFragment extends MvpFragment<SalesJourneyPlanPresenter> implements
        SalesJourneyPlanView, SwipeRefreshLayout.OnRefreshListener{

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final String TAG = SalesJourneyPlanData.class.getSimpleName();

    private String mParam1;
    private String mParam2;

    List<SalesJourneyPlanData> list;

    @BindView(R.id.linear_layout)
    LinearLayout linearLayout;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.circular_progress_button)
    CircularProgressButton circularProgressButton;

    private SharedPreferences sharedPreferences;

    private OnFragmentInteractionListener mListener;

    public SalesJourneyPlanFragment() {}

    public static SalesJourneyPlanFragment newInstance(String param1, String param2) {
        SalesJourneyPlanFragment fragment = new SalesJourneyPlanFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (mListener != null) {
            mListener.onFragmentInteraction("Sales Journey Plan");
        }
        View view = inflater.inflate(R.layout.fragment_sales_journey_plan, container, false);

        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initPages();

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.addOnItemTouchListener(selectItemOnRecycleView());
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(getContext(), 2, 10, true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        post();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    protected SalesJourneyPlanPresenter createPresenter() {
        return new SalesJourneyPlanPresenter(this);
    }

    @Override
    public void onRefresh() {
        presenter.loadData(sharedPreferences);
    }

    @Override
    public void showLoading() {
        if(linearLayout.getVisibility() == View.VISIBLE)
            circularProgressButton.startAnimation();
        else
            swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        circularProgressButton.revertAnimation();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showError(String message) {
        showTopSnackBars(message, R.drawable.ic_warning);
        swipeRefreshLayout.setVisibility(View.GONE);
        linearLayout.setVisibility(View.VISIBLE);

    }

    @Override
    public void showSuccess(SalesJourneyPlanResponse model) {
        if(linearLayout.getVisibility() == View.VISIBLE)
            linearLayout.setVisibility(View.GONE);

        swipeRefreshLayout.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.VISIBLE);

        if(model.getStatus() == 200){
            list = model.getData();
            recyclerView.setAdapter(new SalesJourneyPlanAdapter(list, R.layout.item_sales_journey_plan, getContext()));
        }
    }

    @Override
    public void moveToActivity(Intent intent) {
        startActivity(intent);
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(String title);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden)
            mListener.onFragmentInteraction("Sales Journey Plan");
    }

    private RecyclerView.OnItemTouchListener selectItemOnRecycleView(){
        return new RecyclerItemClickListener(getContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {
                presenter.prepareToActivity(list.get(position), getActivity());
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        });
    }

    private void initPages(){
        sharedPreferences = getActivity().getSharedPreferences("data", MODE_PRIVATE);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary), getResources().getColor(R.color.colorAccent));
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    private void post(){
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                presenter.loadData(sharedPreferences);
            }
        });
        circularProgressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.loadData(sharedPreferences);
            }
        });
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
