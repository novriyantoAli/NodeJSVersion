package net.ali.rhein.mvpbase.feature.homes;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.ali.rhein.mvpbase.R;
import net.ali.rhein.mvpbase.base.mvp.MvpActivity;
import net.ali.rhein.mvpbase.feature.approval_location.ApprovalLocationActivity;
import net.ali.rhein.mvpbase.feature.auth.AuthActivity;
import net.ali.rhein.mvpbase.feature.homes.dashboard.DashboardFragment;
import net.ali.rhein.mvpbase.feature.homes.sales_journey_plan.SalesJourneyPlanFragment;

import butterknife.BindView;

public class HomesActivity extends MvpActivity<HomesPresenter> implements HomesView,
        NavigationView.OnNavigationItemSelectedListener,
        SalesJourneyPlanFragment.OnFragmentInteractionListener,
        DashboardFragment.OnFragmentInteractionListener{

    private DashboardFragment dashboardFragment;
    private SalesJourneyPlanFragment salesJourneyPlanFragment;

    private SharedPreferences sharedPreferences;

    @Override
    protected HomesPresenter createPresenter() {
        return new HomesPresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homes);

        if(savedInstanceState == null){
            dashboardFragment        = DashboardFragment.newInstance("A", "foo");
            salesJourneyPlanFragment = SalesJourneyPlanFragment.newInstance("B", "foo");
        }
        this.sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.moveToActivity(HomesActivity.this);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        TextView textViewUsername = headerView.findViewById(R.id.text_view_username);
        TextView textViewEmail = headerView.findViewById(R.id.text_view_email);
        textViewUsername.setText(sharedPreferences.getString("nama_user", ""));
        textViewEmail.setText(sharedPreferences.getString("email_user", ""));
        defaultFragment(navigationView);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.homes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        switch (item.getItemId()){
            case R.id.nav_dashboard:
                if(dashboardFragment.isAdded()){
                    fragmentTransaction.show(dashboardFragment);
                }
                else{
                    fragmentTransaction.add(R.id.contentHomes, dashboardFragment, "A");
                }
                if(salesJourneyPlanFragment.isAdded()){
                    fragmentTransaction.hide(salesJourneyPlanFragment);
                }
                break;
            case R.id.nav_sales_journey_plan:
                if (salesJourneyPlanFragment.isAdded()) {
                    fragmentTransaction.show(salesJourneyPlanFragment);
                } else {
                    fragmentTransaction.add(R.id.contentHomes, salesJourneyPlanFragment, "B");
                }
                if (dashboardFragment.isAdded()) { fragmentTransaction.hide(dashboardFragment); }
                //if (otherFragments.isAdded()) { fragmentTransaction.hide(otherFragments); }
                break;
            case R.id.nav_profile:
                break;
            case R.id.nav_logout:
                showExitDialog();
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();
        return true;
    }

    @Override
    public void onFragmentInteraction(String title) {
        try{
            getSupportActionBar().setTitle(title);
        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    public void defaultFragment(NavigationView navigationView) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (dashboardFragment.isAdded()) {
            fragmentTransaction.show(dashboardFragment);
        } else {
            fragmentTransaction.add(R.id.contentHomes, dashboardFragment, "A");
        }
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();
        navigationView.setCheckedItem(R.id.nav_dashboard);
    }

    @Override
    public void moveToActivity(Intent intent) {
        startActivity(intent);
    }

    @Override
    public void showMessage(String message) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        Snackbar.make(drawer, message, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    @Override
    public void setData() {

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("api_token", "");
        editor.putString("id","");
        editor.putInt("login", 0);

        editor.commit();

        //presenter.moveToActivity(HomesActivity.this);
        Intent i = new Intent(HomesActivity.this, AuthActivity.class);
        startActivity(i);
        finish();
    }

    private void showExitDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        // set title dialog
        alertDialogBuilder.setTitle("Exit from Application?");

        alertDialogBuilder
                .setMessage("Click yes if you Want.!!")
                .setIcon(R.drawable.ic_power_settings_new_black_24dp)
                .setCancelable(false)
                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        presenter.logout(sharedPreferences);
                        //HomesActivity.this.finish();
                    }
                })
                .setNegativeButton("No",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();
    }
}
