package net.ali.rhein.mvpbase.feature.customer_record_card.form_outlet;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidadvance.topsnackbar.TSnackbar;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import net.ali.rhein.mvpbase.R;
import net.ali.rhein.mvpbase.base.mvp.MvpFragment;
import net.ali.rhein.mvpbase.models.Actual;
import net.ali.rhein.mvpbase.models.FormOutletOrderResponse;
import net.ali.rhein.mvpbase.models.FormOutletRecordCardResponse;
import net.ali.rhein.mvpbase.models.ProductData;
import net.ali.rhein.mvpbase.models.ProductResponse;

import java.util.ArrayList;
import java.util.List;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Context.MODE_PRIVATE;

public class FormOutletFragment extends MvpFragment<FormOutletPresenter> implements
        OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, FormOutletView {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private static final int REQUEST_LOCATION = 1;

    private boolean locationGranted = false;

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;

    @BindView(R.id.linear_layout_content)
    LinearLayout linearLayoutContent;

    // include layout

    @BindView(R.id.text_view_outlet_name)
    TextView textViewOutletName;

    @BindView(R.id.text_view_owner_name)
    TextView textViewOwnerName;

    @BindView(R.id.text_view_outlet_type)
    TextView textViewOutletType;

    @BindView(R.id.text_view_address)
    TextView textViewAddress;

    @BindView(R.id.text_view_phone_number)
    TextView textViewPhoneNumber;

    @BindView(R.id.text_view_area)
    TextView textViewArea;

    @BindView(R.id.text_view_sales_target)
    TextView textViewSalesTarget;

    @BindView(R.id.text_view_sales_actual)
    TextView textViewSalesActual;

    @BindView(R.id.text_view_visit_date)
    TextView textViewVisitDate;

    @BindView(R.id.text_view_range_to_distance)
    TextView textViewRangeToDistance;

    @BindView(R.id.text_view_status)
    TextView textViewStatus;

    @BindView(R.id.spinner_record_card)
    Spinner spinnerRecordCard;

    @BindView(R.id.spinner_order)
    Spinner spinnerOrder;

    @BindView(R.id.spinner_payment)
    Spinner spinnerPayment;

    @BindView(R.id.text_input_selling)
    TextInputLayout textInputLayoutSelling;

    @BindView(R.id.text_input_stock)
    TextInputLayout textInputLayoutStock;

    @BindView(R.id.text_input_credit_limit)
    TextInputLayout textInputLayoutCreditLimit;

    @BindView(R.id.text_input_payment_period)
    TextInputLayout textInputLayoutPaymentPeriode;

    @BindView(R.id.text_input_order)
    TextInputLayout textInputLayoutOrder;

    @BindView(R.id.circular_progress_button_record_card)
    CircularProgressButton circularProgressButtonRecordCard;

    @BindView(R.id.circular_progress_button_order)
    CircularProgressButton circularProgressButtonOrder;

    @BindView(R.id.floating_action_button_get_location_manual)
    FloatingActionButton floatingActionButtonGetLocationManual;

    private String idProductRecordCard, idProductOrder, paymentMethod;

    private double manual = 0.0, auto = 0.0;

    private ArrayList<String> namaProduk, paymentType;
    private List<ProductData> productData;
    private List<Actual> actualData;

    private SharedPreferences sharedPreferences;
    private LocationManager locationManager;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private GoogleMap mGoogleMap;
    private OnFragmentInteractionListener mListener;

    private double autoLatitude, autoLongitude, manualLatitude, manualLongitude;

    public FormOutletFragment() {
    }

    public static FormOutletFragment newInstance(String param1, String param2) {
        FormOutletFragment fragment = new FormOutletFragment();
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

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(1 * 1000)
                .setFastestInterval(1 * 1000);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_form_outlet, container, false);

        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    protected FormOutletPresenter createPresenter() {
        return new FormOutletPresenter(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        namaProduk = new ArrayList<String>();
        paymentType = new ArrayList<String>();

        initViews(view);
        initListner();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        double latitudeOutlet = Double.valueOf(getArguments().getString("latitude"));
        double longtitudeOutlet = Double.valueOf(getArguments().getString("longtitude"));
        //buildGoogleApiClient();

        LatLng latLng = new LatLng(latitudeOutlet, longtitudeOutlet);

        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title(getArguments().getString("nama_outlet"));

        mGoogleMap.addMarker(options);

        mGoogleMap.setMyLocationEnabled(true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        locationGranted = false;
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            locationGranted = false;
                            return;
                        }
                    }
                    locationGranted = true;
                    //initMap();
                }
            }
        }
    }

    /*
    private void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(getContext()).addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        googleApiClient.connect();
    }
    */

    private void getLocationPermission() {
        String[] permission = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        if (ContextCompat.checkSelfPermission(this.getContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getContext(),
                    COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationGranted = true;
            } else {
                ActivityCompat.requestPermissions(getActivity(), permission, LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(getActivity(), permission, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (!hasPermission(Manifest.permission.ACCESS_FINE_LOCATION))
            return;

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        handleNewLocation(location);
    }

    private boolean hasPermission(String permission) {
        return ContextCompat.checkSelfPermission(getActivity(), permission) == PackageManager.PERMISSION_GRANTED;
    }
    private void handleNewLocation(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        /*
        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title("I am here!");
        */
        //mGoogleMap.addMarker(options);

        CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(latLng.latitude, latLng.longitude)).zoom(16).build();

        mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        //mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }
    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
        SupportMapFragment supportMapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);

    }

    @Override
    public void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected())
            mGoogleApiClient.disconnect();
    }

    @Override
    public void onLocationChanged(Location location) {

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(latLng.latitude, latLng.longitude)).zoom(16).build();

        mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        autoLatitude = location.getLatitude();
        autoLongitude = location.getLongitude();
        double latitude = Double.valueOf(getArguments().getString("latitude"));
        double longtitude = Double.valueOf(getArguments().getString("longtitude"));
        Location target = new Location("Target");
        target.setLatitude(latitude);
        target.setLongitude(longtitude);
        //double jarak = location.distanceTo(target)/1000;
        double distance = 6371 * Math.acos(Math.cos(Math.toRadians(target.getLatitude()) ) *
                Math.cos( Math.toRadians( location.getLatitude()) ) *
                Math.cos( Math.toRadians( location.getLongitude() ) - Math.toRadians(target.getLongitude()) ) +
                Math.sin( Math.toRadians(target.getLatitude()) ) * Math.sin( Math.toRadians( location.getLatitude() ) ) ) ;
        Log.e("Calculation Distance : ", String.valueOf(distance));

        auto = Math.round(distance*1000);
        if(auto > 20.0){
            textViewStatus.setText("Location not Granted!!");
            textViewStatus.setTextColor(Color.parseColor("#f44242"));
        }
        else{
            textViewStatus.setText("Location Granted..");
            textViewStatus.setTextColor(Color.parseColor("#00f95b"));
        }
        textViewRangeToDistance.setText(String.valueOf(auto) + " m");
    }

    private void checkLocationRange(){
        if (ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        Location location    = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        manualLatitude = location.getLatitude();
        manualLongitude = location.getLongitude();

        double latitudeOutlet = Double.valueOf(getArguments().getString("latitude"));
        double longtitudeOutlet = Double.valueOf(getArguments().getString("longtitude"));

        if(location != null){
            Log.e("Latitude User : ", String.valueOf(location.getLatitude()));
            Log.e("Longitude User : ", String.valueOf(location.getLongitude()));

            Location outlet = new Location("outlet");
            outlet.setLatitude(latitudeOutlet);
            outlet.setLongitude(longtitudeOutlet);

            manual = 6371 * Math.acos(Math.cos(Math.toRadians(outlet.getLatitude()) ) *
                    Math.cos( Math.toRadians( location.getLatitude()) ) *
                    Math.cos( Math.toRadians( location.getLongitude() ) - Math.toRadians(outlet.getLongitude()) ) +
                    Math.sin( Math.toRadians(outlet.getLatitude()) ) * Math.sin( Math.toRadians( location.getLatitude() ) ) ) ;
            Log.e("Calculation Distance : ", String.valueOf(manual));
            manual = manual * 1000;
            manual = Math.round(manual);
            textViewRangeToDistance.setText(String.valueOf(manual) + " m");
            if(manual > 20.0){
                textViewStatus.setText("Location not Granted!!");
                textViewStatus.setTextColor(Color.parseColor("#f44242"));
            }
            else{
                textViewStatus.setText("Location Granted...");
                textViewStatus.setTextColor(Color.parseColor("#00f95b"));
            }
        }
        else{
            showTopSnackBars("Please Wait..!!", R.drawable.ic_warning);
        }
    }

    private void initListner() {

        floatingActionButtonGetLocationManual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkLocationRange();
            }
        });

        circularProgressButtonRecordCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String selling = textInputLayoutSelling.getEditText().getText().toString().trim();
                String stock = textInputLayoutStock.getEditText().getText().toString().trim();
                if(selling.isEmpty() || stock.isEmpty()){
                    if(selling.isEmpty())
                        textInputLayoutSelling.setError("Field can't be empty");
                    else if(stock.isEmpty())
                        textInputLayoutStock.setError("Field can't be empty");
                }
                else{
                    textInputLayoutSelling.setError(null);
                    textInputLayoutStock.setError(null);
                    if(manual == 0.0 || auto == 0.0){
                        checkLocationRange();
                    }
                    sendData(String.valueOf(manualLatitude), String.valueOf(manualLongitude),
                            selling, stock);
                    Log.e("STATUS ", "MANUAL = " + manual + " AUTO = "+auto);
                    if(manual > 0.0 && manual < 20.0){
                        sendData(String.valueOf(manualLatitude), String.valueOf(manualLongitude),
                                selling, stock);
                        Log.e("STATUS ", "MANUAL LATITUDE = " + manualLatitude + " MANUAL LONGTITUDE = " + manualLongitude);
                    }
                    else if( auto > 0.0 && auto < 20.0){
                        sendData(String.valueOf(autoLatitude), String.valueOf(autoLongitude),
                                selling, stock);
                        Log.e("STATUS ", "AUTO LATITUDE = " + autoLatitude + " AUTO LONGTITUDE = " + autoLongitude);
                    }
                    else{
                        Toast.makeText(getContext(), "Not in Range", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        circularProgressButtonOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String creditLimit = textInputLayoutCreditLimit.getEditText().getText().toString().trim();
                String order = textInputLayoutOrder.getEditText().getText().toString().trim();
                String paymentPeriod = textInputLayoutPaymentPeriode.getEditText().getText().toString().trim();
                //Log.e("PAYMENT METHOD", paymentMethod);
                if(creditLimit.isEmpty() || order.isEmpty() || paymentPeriod.isEmpty()){
                    if(creditLimit.isEmpty())
                        textInputLayoutCreditLimit.setError("Field can't be empty!!");
                    else if(order.isEmpty())
                        textInputLayoutOrder.setError("Field can't be empty!!");
                    else if(paymentPeriod.isEmpty())
                        textInputLayoutPaymentPeriode.setError("Field can't be empty!!");
                    else{
                        textInputLayoutCreditLimit.setError("Field can't be empty!!");
                        textInputLayoutOrder.setError("Field can't be empty!!");
                        textInputLayoutPaymentPeriode.setError("Field can't be empty!!");
                    }
                }
                else{
                    textInputLayoutCreditLimit.setError(null);
                    textInputLayoutOrder.setError(null);
                    textInputLayoutPaymentPeriode.setError(null);
                    if(manual == 0.0 || auto == 0.0){
                        checkLocationRange();
                    }
                    Log.e("STATUS ", "MANUAL = " + manual + " AUTO = "+auto);
                    //orderData(creditLimit, order, paymentPeriod);
                    if(manual > 0.0 && manual < 20.0 ){
                        orderData(creditLimit, order, paymentPeriod);
                    }
                    else if(auto > 0.0 && auto < 20.0){
                        orderData(creditLimit, order, paymentPeriod);
                    }
                    else{
                        Toast.makeText(getContext(), "Anda Diluar Jangkauan!!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }




    private void orderData(String creditLimit, String order, String paymentPeriod){
        Log.e("FORM_OUTLET_FRAGMEN ", "prepare to order!!!!!!!!!!!");
        String id_outlet = getArguments().getString("id_outlet");
        //String jumlah_order = editTextJumlahOrder.getText().toString();
        presenter.order(sharedPreferences, id_outlet, String.valueOf(idProductOrder),creditLimit, order, paymentMethod, paymentPeriod);
    }

    private void sendData(String latitude, String longtitude, String selling, String stock){
        Log.e("FORM_OUTLET_FRAGMEN ", "prepare to kirim laporan!!!!!!!!!!!");
        String tgl_kunjungan = getArguments().getString("tanggal_kunjungan");
        String id_outlet = getArguments().getString("id_outlet");
        presenter.sendData(sharedPreferences, id_outlet, String.valueOf(idProductRecordCard),stock, selling, latitude, longtitude);
    }
    private void initViews(View view) {
        ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        //Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        getLocationPermission();
        toolbar.setNavigationIcon(R.drawable.ic_back);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonPressed();
            }
        });
        collapsingToolbar.setTitle("Customer Record Card");
        collapsingToolbar.setExpandedTitleColor(Color.parseColor("#44ffffff"));

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            getLocation();
        }
        sharedPreferences = getActivity().getSharedPreferences("data", MODE_PRIVATE);
        textViewOutletName.setText(getArguments().getString("nama_outlet"));
        textViewOwnerName.setText(getArguments().getString("nama_pemilik"));
        textViewOutletType.setText(getArguments().getString("jenis_outlet"));
        textViewAddress.setText(getArguments().getString("alamat_outlet"));
        textViewPhoneNumber.setText(getArguments().getString("no_telp_outlet"));
        textViewArea.setText(getArguments().getString("nama_provinsi") + ", " + getArguments().getString("nama_kabupaten"));
        textViewVisitDate.setText(getArguments().getString("tanggal_kunjungan"));

        presenter.getFormInformation(sharedPreferences);
    }

    public void onButtonPressed() {
        if (mListener != null) {
            mListener.onFragmentInteraction();
        }
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
    public void showLoading(int status) {
        if(status == 0)
            circularProgressButtonRecordCard.startAnimation();
        else
            circularProgressButtonOrder.startAnimation();
    }

    @Override
    public void hideLoading(int status) {
        if(status == 0)
            circularProgressButtonRecordCard.revertAnimation();
        else
            circularProgressButtonOrder.revertAnimation();
    }

    @Override
    public void showError(String message) {
        //Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();

        showTopSnackBars(message, R.drawable.ic_warning);
    }

    @Override
    public void showSuccessCheck(FormOutletRecordCardResponse model) {
        //Toast.makeText(getActivity(), model.getMessage(), Toast.LENGTH_LONG).show();
        if(model.getStatus() != 201)
            showTopSnackBars(model.getMessage(), R.drawable.ic_warning);
        else
            showTopSnackBars(model.getMessage(), R.drawable.ic_success);
        textInputLayoutSelling.getEditText().setText(null);
        textInputLayoutStock.getEditText().setText(null);
    }

    @Override
    public void showSuccessOrder(FormOutletOrderResponse model) {
        if(model.getStatus() != 200)
            showTopSnackBars(model.getMessage(), R.drawable.ic_warning);
        else
            showTopSnackBars(model.getMessage(), R.drawable.ic_success);
        //Toast.makeText(getActivity(), model.getMessage(), Toast.LENGTH_LONG).show();
        textInputLayoutCreditLimit.getEditText().setText(null);
        textInputLayoutOrder.getEditText().setText(null);
    }

    @Override
    public void showProductResponse(ProductResponse model) {
        circularProgressButtonRecordCard.setEnabled(true);
        circularProgressButtonOrder.setEnabled(true);

        productData = model.getData();
        actualData  = model.getActual();

        for(int i=0;i<productData.size();i++){
            namaProduk.add(productData.get(i).getNamaProduk());
        }

        spinnerRecordCard.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, namaProduk));
        spinnerRecordCard.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                idProductRecordCard = productData.get(i).getIdProduk();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        spinnerOrder.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, namaProduk));
        spinnerOrder.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                idProductOrder = productData.get(i).getIdProduk();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        paymentType.add("Cash");
        paymentType.add("Kredit");

        spinnerPayment.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, paymentType));

        spinnerPayment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                paymentMethod = paymentType.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        textViewSalesTarget.setText(model.getTarget().get(0).getTargetValue());
        int x=0;
        for(int i=0;i<actualData.size();i++){
            int m = Integer.parseInt(actualData.get(i).getTotalTerjual());
            x += m;
        }
        textViewSalesActual.setText(String.valueOf(x));
        if(x >= Integer.parseInt(model.getTarget().get(0).getTargetValue()))
            textViewSalesActual.setTextColor(Color.parseColor("#00f95b"));
        else

            textViewSalesActual.setTextColor(Color.parseColor("#f44242"));
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction();
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        }
    }

    private void showTopSnackBars(String message, int drawable){
        TSnackbar snackbar = TSnackbar.make(linearLayoutContent, message, TSnackbar.LENGTH_SHORT);
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


    protected void buildAlertMessageNoGps() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Please Turn ON your GPS Connection")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    /*
    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

    CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(latLng.latitude, latLng.longitude)).zoom(16).build();

    // googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

    //menghentikan pembaruan lokasi
        if (googleApiClient != null) {
        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
    }
    autoLatitude = location.getLatitude();
    autoLongitude = location.getLongitude();
    double latitude = Double.valueOf(getArguments().getString("latitude"));
    double longtitude = Double.valueOf(getArguments().getString("longtitude"));
    Location target = new Location("Target");
        target.setLatitude(latitude);
        target.setLongitude(longtitude);
    //double jarak = location.distanceTo(target)/1000;
    double distance = 6371 * Math.acos(Math.cos(Math.toRadians(target.getLatitude()) ) *
            Math.cos( Math.toRadians( location.getLatitude()) ) *
            Math.cos( Math.toRadians( location.getLongitude() ) - Math.toRadians(target.getLongitude()) ) +
            Math.sin( Math.toRadians(target.getLatitude()) ) * Math.sin( Math.toRadians( location.getLatitude() ) ) ) ;
        Log.e("Calculation Distance : ", String.valueOf(distance));

    auto = Math.round(distance*1000);
        if(auto > 20.0){
        textViewStatus.setText("Location not Granted!!");
        textViewStatus.setTextColor(Color.parseColor("#f44242"));
    }
        else{
        textViewStatus.setText("Location Granted..");
        textViewStatus.setTextColor(Color.parseColor("#00f95b"));
    }
        textViewRangeToDistance.setText(String.valueOf(auto) + " m");*/
    //menghentikan pembaruan lokasi
        /*
        if (googleApiClient != null) {
            //LocationServices.getFusedLocationProviderClient(this);
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
        */
}
