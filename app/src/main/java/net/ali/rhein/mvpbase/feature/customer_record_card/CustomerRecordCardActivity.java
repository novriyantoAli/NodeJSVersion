package net.ali.rhein.mvpbase.feature.customer_record_card;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import net.ali.rhein.mvpbase.R;
import net.ali.rhein.mvpbase.feature.customer_record_card.form_outlet.FormOutletFragment;
import net.ali.rhein.mvpbase.feature.customer_record_card.list_outlet.ListOutletFragment;

public class CustomerRecordCardActivity extends AppCompatActivity implements
        ListOutletFragment.OnFragmentInteractionListener,
        FormOutletFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_record_card);

        Bundle bundle = getIntent().getExtras();

        setUpFragment(bundle);
    }

    private void setUpFragment(Bundle bundle){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ListOutletFragment listOutletFragment = new ListOutletFragment();
        listOutletFragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.content, listOutletFragment, "listOutletFragment");

        fragmentTransaction.commit();
    }


    @Override
    public void onBackPressed() {
        if(getFragmentManager().getBackStackEntryCount() == 0) {
            super.onBackPressed();
        }
        else {
            getFragmentManager().popBackStack();
        }
    }


    @Override
    public void onFragmentInteraction() {
        onBackPressed();
    }
}
