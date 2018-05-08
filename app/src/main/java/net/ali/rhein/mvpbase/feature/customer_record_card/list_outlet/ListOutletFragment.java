package net.ali.rhein.mvpbase.feature.customer_record_card.list_outlet;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidadvance.topsnackbar.TSnackbar;

import net.ali.rhein.mvpbase.R;
import net.ali.rhein.mvpbase.adapter.ListOutletAdapter;
import net.ali.rhein.mvpbase.base.mvp.MvpFragment;
import net.ali.rhein.mvpbase.feature.customer_record_card.form_outlet.FormOutletFragment;
import net.ali.rhein.mvpbase.models.ListOutletData;
import net.ali.rhein.mvpbase.models.ListOutletResponse;
import net.ali.rhein.mvpbase.utils.RecyclerItemClickListener;

import java.util.List;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Context.MODE_PRIVATE;


public class ListOutletFragment extends MvpFragment<ListOutletPresenter> implements ListOutletView,
        SwipeRefreshLayout.OnRefreshListener{

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    @BindView(R.id.linear_layout)
    LinearLayout linearLayout;


    @BindView(R.id.circular_progress_button)
    CircularProgressButton circularProgressButton;

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private SharedPreferences sharedPreferences;

    private LinearLayoutManager verticalLayoutmanager;

    private List<ListOutletData> list;

    private String id_kabupaten;

    public ListOutletFragment() {}

    public static ListOutletFragment newInstance(String param1, String param2) {
        ListOutletFragment fragment = new ListOutletFragment();
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
        View view = inflater.inflate(R.layout.fragment_list_outlet, container, false);

        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initPages(view);

        post();
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
    protected ListOutletPresenter createPresenter() {
        return new ListOutletPresenter(this);
    }

    @Override
    public void onRefresh() {
        presenter.loadData(sharedPreferences, id_kabupaten);
    }

    private RecyclerView.OnItemTouchListener selectItemOnRecycleView(){
        return new RecyclerItemClickListener(getContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position){
                presenter.prepareToFragment(getArguments().getString("id_kabupaten"),
                        getArguments().getString("nama_provinsi"), getArguments().getString("nama_kabupaten"), list.get(position)
                );
            }

            @Override
            public void onLongItemClick(View view, int position) {}
        });
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
        //Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
        showTopSnackBars(message, R.drawable.ic_warning);
        linearLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void showSuccess(ListOutletResponse model) {
        if(linearLayout.getVisibility() == View.VISIBLE)
            linearLayout.setVisibility(View.GONE);

        if(model.getStatus() == 200){
            this.list = model.getData();
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.setAdapter(new ListOutletAdapter(list, R.layout.item_list_outlet, getContext()));
        }
        else{
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void moveToFragment(FormOutletFragment formOutletFragment) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content, formOutletFragment, "FormOutletFragment");
        fragmentTransaction.addToBackStack("formoutletfragment");
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction();
    }

    public void onButtonPressed() {
        if (mListener != null) {
            mListener.onFragmentInteraction();
        }
    }

    private void initPages(View view){
        verticalLayoutmanager  = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        Toolbar toolbar = (Toolbar)view.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setTitle("Outlet List");
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonPressed();
            }
        });


        recyclerView.setLayoutManager(verticalLayoutmanager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addOnItemTouchListener(selectItemOnRecycleView());
        sharedPreferences = getActivity().getSharedPreferences("data", MODE_PRIVATE);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary),
                getResources().getColor(R.color.colorAccent));
        swipeRefreshLayout.setOnRefreshListener(this);
        id_kabupaten = getArguments().getString("id_kabupaten");
    }

    private void post(){
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                presenter.loadData(sharedPreferences, id_kabupaten);
            }
        });
        circularProgressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.loadData(sharedPreferences, id_kabupaten);
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
