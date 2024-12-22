package com.karzansoft.fastvmi.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.karzansoft.fastvmi.R;
import com.karzansoft.fastvmi.Utils.Constant;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



       // Button checout = (Button) findViewById(R.id.am_action_checkout);
        //Button checkin = (Button) findViewById(R.id.am_action_checkin);

       /* checout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, VehicleSelectActivity.class);
                intent.putExtra("IsCheckout",true);
                intent.putExtra(Constant.CURRENT_MODE,Constant.RA_CHECKOUT);
                startActivityForResult(intent, 1);
            }
        });

        checkin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, VehicleSelectActivity.class);
                intent.putExtra("IsCheckout",false);
                intent.putExtra(Constant.CURRENT_MODE,Constant.RA_CHECKIN);
                startActivityForResult(intent, 1);
            }
        });*/

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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
    protected void onResume() {
        super.onResume();
       /* checkForCrashes();
        checkForUpdates();*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_ra_checkout) {
            Intent intent = new Intent(this, VehicleSelectActivity.class);
            intent.putExtra("IsCheckout",true);
            intent.putExtra(Constant.CURRENT_MODE,Constant.RA_CHECKOUT);
            startActivityForResult(intent, 1);
        }/*else if(id == R.id.nav_ra_checkin)
        {

            Intent intent = new Intent(this, VehicleSelectActivity.class);
            intent.putExtra("IsCheckout", false);
            intent.putExtra(Constant.CURRENT_MODE,Constant.RA_CHECKIN);
            startActivityForResult(intent, 1);
        }*/
        else if(id == R.id.nav_replacement)
        {
            Intent intent = new Intent(this, VehicleSelectActivity.class);
            intent.putExtra("IsCheckout", false);
            intent.putExtra(Constant.CURRENT_MODE,Constant.REPLACEMENT_CHECKIN);
            startActivityForResult(intent, 1);
        }
        else if(id == R.id.nav_nrm_checkout)
        {
            Intent intent = new Intent(this, VehicleSelectActivity.class);
            intent.putExtra("IsCheckout", true);
            intent.putExtra(Constant.CURRENT_MODE,Constant.NRM_CHECKOUT);
            startActivityForResult(intent, 1);
        }
       /* else if(id == R.id.nav_nrm_checkin)
        {
            Intent intent = new Intent(this, VehicleSelectActivity.class);
            intent.putExtra("IsCheckout", false);
            intent.putExtra(Constant.CURRENT_MODE,Constant.NRM_CHECKIN);
            startActivityForResult(intent, 1);
        }*/
       /* else if(id == R.id.nav_garage_checkout)
        {
            Intent intent = new Intent(this, VehicleSelectActivity.class);
            intent.putExtra("IsCheckout", true);
            intent.putExtra(Constant.CURRENT_MODE,Constant.GARAGE_CHECKOUT);
            startActivityForResult(intent, 1);
        }*/
      /*  else if(id == R.id.nav_garage_checkin)
        {
            Intent intent = new Intent(this, VehicleSelectActivity.class);
            intent.putExtra("IsCheckout", false);
            intent.putExtra(Constant.CURRENT_MODE,Constant.GARAGE_CHECKIN);
            startActivityForResult(intent, 1);
        }*/
       /* else if(id == R.id.nav_syncstatus)
        {
            AlertDialog.Builder builder =
                    new AlertDialog.Builder(this);
            builder.setTitle("Sync Status");
            builder.setMessage("No entries pending for synchronization");
            builder.setPositiveButton("Ok", null);

            builder.show();
        }

        else if(id == R.id.nav_offline)
        {
            final CharSequence[] items={"Check Out","Check In","Replacement","NRM Out","NRM In"};
            AlertDialog.Builder builder =
                    new AlertDialog.Builder(this,R.style.AppCompatAlertDialogStyle);
            builder.setTitle("Offline");
           *//* TextView title=new TextView(this);

            title.setText("Offline");
            title.setTextSize(R.dimen.title_text_size_medium);
            title.setTextColor(getResources().getColor(R.color.colorPrimary));
            builder.setCustomTitle(title);*//*
            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
           // builder.setMessage("No entries pending for synchronization");
            builder.setPositiveButton("Cancel", null);

            builder.show();
        }*/
        else if (id == R.id.nav_setting) {
           // Intent intent = new Intent(this, SettingsActivity.class);
            //startActivityForResult(intent, 1);
        }


       /* if (id == R.id.nav_checkout) {
            Intent intent = new Intent(this, VehicleSelectActivity.class);
            intent.putExtra("IsCheckout",true);
            intent.putExtra("title","Check Out");
            startActivityForResult(intent, 1);
        } else if (id == R.id.nav_checkin) {
            Intent intent = new Intent(this, VehicleSelectActivity.class);
            intent.putExtra("IsCheckout", false);
            intent.putExtra("title","Check In");
            startActivityForResult(intent, 1);
        } else if (id == R.id.nav_setting) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivityForResult(intent, 1);
        } else if (id == R.id.nav_signout) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();

        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                String result=data.getStringExtra("result");
                Toast.makeText(this,"Operation completed successfully.",Toast.LENGTH_LONG).show();
                //finish();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
                Toast.makeText(this,"Operation was canceled.",Toast.LENGTH_LONG).show();
            }
        }

    }//onActivityResult


    /*private void checkForCrashes() {
        CrashManager.register(this, "19b42aaaa1714585873ec3e518a8e65c");
    }

    private void checkForUpdates() {
        // Remove this for store builds!
        UpdateManager.register(this, "19b42aaaa1714585873ec3e518a8e65c");
    }*/
}
