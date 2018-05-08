package net.ali.rhein.mvpbase.feature.order;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;

/**
 * Created by rhein on 4/5/18.
 */

public class LocationService extends Service {

    private static final long INTERVAL = 1000 * 2;
    private static final long FASTEST_INTERVAL = 1000;

    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mCurrentLocation, lStart, lEnd;

    private final IBinder mBinder = new LocalBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /*
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        //createLocationService();
        //mGoogleApiClient = new GoogleApiClient.Builder(this);
    return ;
    }
    */
    private class LocalBinder extends Binder {
        public LocationService getService(){
            return LocationService.this;
        }
    }
}
