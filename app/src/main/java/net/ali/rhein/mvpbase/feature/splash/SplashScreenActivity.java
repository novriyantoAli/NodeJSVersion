package net.ali.rhein.mvpbase.feature.splash;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import net.ali.rhein.mvpbase.R;
import net.ali.rhein.mvpbase.feature.auth.AuthActivity;

public class SplashScreenActivity extends AppCompatActivity {

    LinearLayout l1,l2;
    Animation uptodown,downtoup;
    private static final String TAG = "SplashActivity";
    private static final int ERROR_DIALOG_REQUEST = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        l1 = (LinearLayout) findViewById(R.id.l1);
        l2 = (LinearLayout) findViewById(R.id.l2);
        uptodown = AnimationUtils.loadAnimation(this,R.anim.uptodown);
        downtoup = AnimationUtils.loadAnimation(this,R.anim.downtoup);
        l1.setAnimation(uptodown);
        l2.setAnimation(downtoup);

        if(isServiceOk()){
            init();
        }
    }

    @Override
    public void onBackPressed() {}

    private void init(){
        final Intent i = new Intent(this, AuthActivity.class);
        Thread timer = new Thread(){
            public void run(){
                try{
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                    startActivity(i);
                    finish();
                }
            }
        };
        timer.start();
    }
    public boolean isServiceOk(){
        Log.d(TAG, "isServiceOk : checking google services version");
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(SplashScreenActivity.this);
        if(available == ConnectionResult.SUCCESS){
            //everythings is fine and user can make up request
            Log.d(TAG, "isServiceOk : google play services is working!!");
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            Log.d(TAG, "isServiceOk : an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(SplashScreenActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }
        else{
            Toast.makeText(this, "We can make map request", Toast.LENGTH_LONG).show();
        }
        return false;

    }
}
