package net.ali.rhein.mvpbase.feature.approval_location;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidadvance.topsnackbar.TSnackbar;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import net.ali.rhein.mvpbase.R;
import net.ali.rhein.mvpbase.base.mvp.MvpActivity;
import net.ali.rhein.mvpbase.models.JenisOutletData;
import net.ali.rhein.mvpbase.models.JenisOutletResponse;
import net.ali.rhein.mvpbase.models.SaveOutletResponse;

import java.util.ArrayList;
import java.util.List;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import br.com.simplepass.loading_button_lib.interfaces.OnAnimationEndListener;
import butterknife.BindView;

public class ApprovalLocationActivity extends MvpActivity<ApprovalLocationPresenter> implements
        ApprovalLocationView, OnMapReadyCallback,GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnMarkerDragListener,
        GoogleMap.OnMapLongClickListener, LocationListener {

    private static final String TAG = ApprovalLocationActivity.class.getSimpleName();
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;

    private GoogleMap googleMap;
    GoogleApiClient googleApiClient;
    private LocationManager locationManager;
    private Location currentLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LatLng latLng;
    private String request;

    private double pointingLatitude, pointingLongitude;

    private MarkerOptions markerOptions = new MarkerOptions();
    private boolean locationGranted = false;

    private SharedPreferences sharedPreferences;

    private ArrayList<String> jenis_apotik;
    private List<JenisOutletData> jenisOutletData;

    @BindView(R.id.spinner_jenis_outlet)
    Spinner spinner;

    @BindView(R.id.edit_text_outlet_name)
    EditText editTextOutletName;

    @BindView(R.id.edit_text_owner)
    EditText editTextOwnerName;

    @BindView(R.id.edit_text_outlet_address)
    EditText editTextAddressName;

    @BindView(R.id.edit_text_phone_number)
    EditText editTextPhoneNumber;

    @BindView(R.id.button_lock)
    CircularProgressButton circularProgressButton;

    @BindView(R.id.linear_layout_content)
    LinearLayout linearLayoutContent;

    private String id_jenis_outlet;

    /**
     * Override Method From Presenter
     **/

    @Override
    protected ApprovalLocationPresenter createPresenter() {
        return new ApprovalLocationPresenter(this);
    }

    @Override
    public void showSuccessSaveData(SaveOutletResponse model) {
        if(model.getStatus() != 200)
            showTopSnackBars(model.getMessage(), R.drawable.ic_warning);
        else
            showTopSnackBars(model.getMessage(), R.drawable.ic_success);
    }

    @Override
    public void showSuccess(JenisOutletResponse model) {
        circularProgressButton.setEnabled(true);
        jenisOutletData = model.getData();

        for(int i=0;i<jenisOutletData.size();i++){
            jenis_apotik.add(jenisOutletData.get(i).getJenisOutlet());
        }

        spinner.setAdapter(new ArrayAdapter<String>(ApprovalLocationActivity.this, android.R.layout.simple_spinner_dropdown_item, jenis_apotik));

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                id_jenis_outlet = jenisOutletData.get(i).getIdJenisOutlet();
                //Toast.makeText(ApprovalLocationActivity.this, jenisOutletData.get(i).getIdJenisOutlet(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
    }

    @Override
    public void showError(String message) {
        TSnackbar.make(linearLayoutContent, message, TSnackbar.LENGTH_SHORT).show();
    }

    @Override
    public void hideLoading() {
        circularProgressButton.doneLoadingAnimation(Color.parseColor("#333639"), BitmapFactory.decodeResource(getResources(), R.drawable.ic_error));
        circularProgressButton.revertAnimation();
    }

    @Override
    public void showLoading() {circularProgressButton.startAnimation();}

    /*
     * Maps Function
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.e(TAG, "onMapReady : Map Is Ready");
        this.googleMap = googleMap;
        this.googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        if(request.equalsIgnoreCase("mapsDirection")){
            latLng = new LatLng(Double.parseDouble(getIntent().getStringExtra("latitude")), Double.parseDouble(getIntent().getStringExtra("longitude")));
            addMarker(latLng, getIntent().getStringExtra("outlet"));
        }
        if (locationGranted) {
            this.googleMap.setOnMarkerDragListener(this);
            //Adding a long click listener to the map
            this.googleMap.setOnMapLongClickListener(this);
            getLocationDevice();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            //LatLng latLng = new LatLng()
            //this.googleMap.addMarker(new MarkerOptions().position(latLng).draggable(true));
            this.googleMap.setMyLocationEnabled(true);

        }
    }


    @Override
    public void onLocationChanged(Location location) {
        currentLocation = location;
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(latLng.latitude, latLng.longitude)).zoom(16).build();

        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        //menghentikan pembaruan lokasi
        if (googleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        @SuppressLint("RestrictedApi") LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);//LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {}

    private void addMarker(LatLng latLng, String title) {
        markerOptions.position(latLng);
        markerOptions.title(title);
        googleMap.addMarker(markerOptions);
    }

    /*
    Generate Auto
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approval_location);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_back);

        request         = getIntent().getStringExtra("request");
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        jenis_apotik = new ArrayList<String>();
        sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        presenter.loadData(sharedPreferences);
        getLocationPermission();
        initListner();
    }


    private void initListner() {

        circularProgressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(ApprovalLocationActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(ApprovalLocationActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                String name_outlet = editTextOutletName.getText().toString();
                String name_owner = editTextOwnerName.getText().toString();
                String name_address = editTextAddressName.getText().toString();
                String phone_number = editTextPhoneNumber.getText().toString();
                if(name_outlet.equalsIgnoreCase(""))
                    editTextOutletName.setError("Outlet Name Cannot Be Empty!!");
                else if(name_owner.equalsIgnoreCase(""))
                    editTextOwnerName.setError("Owner Name Cannot Be Empty!!");
                else if(name_address.equalsIgnoreCase(""))
                    editTextAddressName.setError("Address Name Cannot Be Empty!!");
                else if(phone_number.equalsIgnoreCase(""))
                    editTextPhoneNumber.setError("Phone Number Cannot Be Empty!!");
                else if(pointingLatitude <= 0 || pointingLongitude <= 0)
                    showTopSnackBars("Please Click Location!!", R.drawable.ic_warning);
                else{
                    if(currentLocation != null){
                        presenter.saveData(sharedPreferences, name_outlet, name_owner,
                                name_address, phone_number, id_jenis_outlet,
                                pointingLongitude, pointingLatitude,
                                currentLocation.getLongitude(), currentLocation.getLatitude());
                    }
                    else
                        Log.e("", "");
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.e(TAG, "onRequestPermissionResult : Called");
        locationGranted = false;
        switch (requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:{
                if(grantResults.length > 0){
                    for(int i=0;i<grantResults.length;i++){
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            Log.e(TAG, "onRequestPermissionResult : permission failed");
                            locationGranted = false;
                            return;
                        }
                    }
                    Log.e(TAG, "onRequestPermissionResult : permission granted");
                    locationGranted = true;
                    initMap();
                }
            }
        }
    }

    private void initMap(){
        Log.e(TAG, "initMap : initializing Map");
        SupportMapFragment supportMapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(ApprovalLocationActivity.this);
    }


    private void getLocationDevice(){
        buildGoogleApiClient();
        Log.e(TAG, "getLocationDevice : getting device current location");
    }

    private void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        googleApiClient.connect();
    }

    private void getLocationPermission(){
        Log.e(TAG, "getLocationPermission : getting location permission");
        String[]permission = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                locationGranted = true;
                initMap();
            }
            else{
                ActivityCompat.requestPermissions(this, permission, LOCATION_PERMISSION_REQUEST_CODE);
            }
        }
        else{
            ActivityCompat.requestPermissions(this, permission, LOCATION_PERMISSION_REQUEST_CODE);
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

    @Override
    public void onMapLongClick(LatLng latLng) {
        googleMap.clear();
        pointingLatitude = latLng.latitude;
        pointingLongitude = latLng.longitude;
        String msg = pointingLatitude + ", " + pointingLongitude;
        googleMap.addMarker(new MarkerOptions().position(latLng).draggable(true));
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        googleMap.clear();
        pointingLatitude  = marker.getPosition().latitude;
        pointingLongitude = marker.getPosition().longitude;
        String msg = pointingLatitude + ", " + pointingLongitude;
        LatLng latLng = new LatLng(pointingLatitude, pointingLongitude);
        googleMap.addMarker(new MarkerOptions().position(latLng).draggable(true).title("Current Location"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
