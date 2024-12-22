package com.karzansoft.fastvmi.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.karzansoft.fastvmi.DataBase.DBConstants;
import com.karzansoft.fastvmi.DataBase.DatabaseManager;
import com.karzansoft.fastvmi.Fragments.AgreementFragment;
import com.karzansoft.fastvmi.Fragments.BaseFragment;
import com.karzansoft.fastvmi.Fragments.GarageFragment;
import com.karzansoft.fastvmi.Fragments.HomeFragment;
import com.karzansoft.fastvmi.Fragments.InspectionFragment;
import com.karzansoft.fastvmi.Fragments.NRMFragment;
import com.karzansoft.fastvmi.Fragments.ReplacementFragment;
import com.karzansoft.fastvmi.Fragments.SettingsFragment;
import com.karzansoft.fastvmi.MCCApplication;
import com.karzansoft.fastvmi.Models.AccessoryItem;
import com.karzansoft.fastvmi.Models.AppSettings;
import com.karzansoft.fastvmi.Models.Contact;
import com.karzansoft.fastvmi.Models.LanguagesResult;
import com.karzansoft.fastvmi.Models.VehicleLocation;
import com.karzansoft.fastvmi.Models.VehicleStatus;
import com.karzansoft.fastvmi.Network.Entities.Request.SyncRequest;
import com.karzansoft.fastvmi.Network.Entities.Response.FirebaseAuthResponse;
import com.karzansoft.fastvmi.Network.Entities.Response.SyncResponse;
import com.karzansoft.fastvmi.Network.WebResponse;
import com.karzansoft.fastvmi.Network.WebServiceFactory;
import com.karzansoft.fastvmi.R;
import com.karzansoft.fastvmi.Services.ImageSyncService;
import com.karzansoft.fastvmi.Utils.AppUtils;
import com.karzansoft.fastvmi.Utils.Constant;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ActionBarDrawerToggle toggle;
    ProgressDialog progressDialog;
    DrawerLayout drawer;
    Handler mHandler;
    Toolbar toolbar;
    boolean isComingFromSettings;
    BaseFragment currentFragment;
    Menu navigationMenu;
    HashMap<String, String> languageTexts;
    TextView mTitle;
    //    View chat,unread;
    Context context;

    //boolean isMessageUnread;
//    Animation blinkAnimation;
    //AppSettings appSettings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (AppUtils.isRTLLanguageSelected(this))
            AppUtils.forceRTLIfSupported(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragetment_main);
        toolbar = findViewById(R.id.toolbar);
        context = this;
        setSupportActionBar(toolbar);
        mTitle = toolbar.findViewById(R.id.toolbar_title);
   /*       chat = toolbar.findViewById(R.id.imageView_chat);
        unread = toolbar.findViewById(R.id.imageView_unread);
        chat.setOnClickListener(this);*/
        if (AppUtils.isRTLLanguageSelected(this))
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_forward_white_24dp);
        loadLanguageText();
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
             public void onDrawerClosed(View drawerView) {
                 super.onDrawerClosed(drawerView);
             }
         };

        drawer.setDrawerListener(toggle);
        toggle.syncState();
        mHandler=new Handler(Looper.getMainLooper());

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationMenu=navigationView.getMenu();
        navigationView.setNavigationItemSelectedListener(this);
        View hView =  navigationView.getHeaderView(0);
        TextView userName=null;
        if(hView!=null)
            userName=(TextView)hView.findViewById(R.id.nav_username);
        if(userName!=null)
            userName.setText(""+AppUtils.getUserName(this));
        updateMenu();
        if(findViewById(R.id.mainContent) != null && savedInstanceState==null)
        {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.mainContent, new HomeFragment(),"HomeFrgament");
            ft.commit();
        }


        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(AppUtils.getLocalizeString(this, "Processing", "Processing..."));
        progressDialog.setCancelable(false);

        //Toast.makeText(this,""+ DatabaseManager.getInstance(getApplicationContext()).getAccessoryCount(),Toast.LENGTH_LONG).show();
        getMasterTables(false);


        //blinkAnimation = new ScaleAnimation(0, 1,0,1,Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//        blinkAnimation = new AlphaAnimation(1,0.2f);
//        blinkAnimation.setDuration(400);
//        blinkAnimation.setInterpolator(new LinearInterpolator());
//        blinkAnimation.setRepeatCount(Animation.INFINITE);
//        blinkAnimation.setRepeatMode(Animation.REVERSE);
        // unread.startAnimation(blinkAnimation);
//        unread.setVisibility(View.INVISIBLE);
        // appSettings = AppUtils.getSettings(this);
        //  hidemenu();
    }

    @Override
    public void setTitle(CharSequence title) {
        //super.setTitle(title);
        mTitle.setText(title);
    }
/*

    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.imageView_chat:

                IChatUser iChatUser = Utils.getUser(this);
                if(iChatUser!=null)
                {
                    if (ChatManager.getInstance() == null)
                    ChatManager.start(context, Constant.APP_ID, iChatUser);

                    ChatUI.getInstance().openConversationMessagesActivity(Constant.AGENT_ID,"Support Agent");
                }else {
                    getAccessTokenForFireBase(true);
                }
                //isMessageUnread = false;
//                showUnreadMessageIndicator(false);

                try{
                    Utils.setUnreadChat(false,this);
                    NotificationManager notificationManager =
                            (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.cancelAll();
                }catch (Exception ex){ex.printStackTrace();}

                break;
        }
    }

*/

    public void loadLanguageText()
    {
        languageTexts=AppUtils.getLanguageStrings(this);
    }

    public String getLocalizeString(String key,String defValue)
    {
        if(languageTexts!=null && languageTexts.get(key)!=null)
        {
            return languageTexts.get(key);
        }
        return defValue;
    }

    public void updateMenu() {
        MenuItem /*vehicleItem,*/agreementItem, staffItem, raAgreementOutItem, raAgreementInItem, replacementItem, nrmOutItem, nrmInItem, grpWorkshope, workshopeOutItem, workshopeInItem, settingItem, inspectionDamagesItem, inspectionWalkthrouhItem, logoutItem;

//        vehicleItem=navigationMenu.findItem(R.id.nav_add_vehicle);
        agreementItem = navigationMenu.findItem(R.id.grp_agreement);
        staffItem = navigationMenu.findItem(R.id.grp_staff_movement);
        raAgreementOutItem = navigationMenu.findItem(R.id.nav_ra_checkout);
        raAgreementInItem = navigationMenu.findItem(R.id.nav_ra_checkin);
        replacementItem = navigationMenu.findItem(R.id.nav_replacement);
        nrmOutItem = navigationMenu.findItem(R.id.nav_nrm_checkout);
        nrmInItem = navigationMenu.findItem(R.id.nav_nrm_checkin);
        grpWorkshope = navigationMenu.findItem(R.id.grp_garage);
        workshopeOutItem = navigationMenu.findItem(R.id.garage_out);
        workshopeInItem = navigationMenu.findItem(R.id.garage_in);
        settingItem = navigationMenu.findItem(R.id.nav_setting);
//        inspectionDamagesItem=navigationMenu.findItem(R.id.nav_inspect);
//        inspectionWalkthrouhItem = navigationMenu.findItem(R.id.nav_walkthroughinspect);
        logoutItem = navigationMenu.findItem(R.id.nav_logout);

        // directoryItem.setTitle(getLocalizeString("Directory",directoryItem.getTitle().toString()));
//        vehicleItem.setTitle(getLocalizeString("AddVehicle",vehicleItem.getTitle().toString()));
        agreementItem.setTitle(getLocalizeString("RentalAgreement", agreementItem.getTitle().toString()));
        raAgreementOutItem.setTitle(getLocalizeString("Check-Out", raAgreementOutItem.getTitle().toString()));
        raAgreementInItem.setTitle(getLocalizeString("Check-In", raAgreementInItem.getTitle().toString()));
        replacementItem.setTitle(getLocalizeString("Replacement", replacementItem.getTitle().toString()));
        staffItem.setTitle(getLocalizeString("StaffMovement", staffItem.getTitle().toString()));
        nrmOutItem.setTitle(getLocalizeString("Check-Out", nrmOutItem.getTitle().toString()));
        nrmInItem.setTitle(getLocalizeString("Check-In", nrmInItem.getTitle().toString()));
        grpWorkshope.setTitle(getLocalizeString("WorkshopMovements", grpWorkshope.getTitle().toString()));
        workshopeOutItem.setTitle(getLocalizeString("Check-Out", workshopeOutItem.getTitle().toString()));
        workshopeInItem.setTitle(getLocalizeString("Check-In", workshopeInItem.getTitle().toString()));
//        inspectionDamagesItem.setTitle(getLocalizeString("InspectionDamages",inspectionDamagesItem.getTitle().toString()));
//        inspectionWalkthrouhItem.setTitle(getLocalizeString("InspectionWalkthrough",inspectionWalkthrouhItem.getTitle().toString()));
        settingItem.setTitle(getLocalizeString("Settings", settingItem.getTitle().toString()));
        logoutItem.setTitle(getLocalizeString("Logout", logoutItem.getTitle().toString()));
    }

    public void hidemenu() {
        // if(!appSettings.issCSEnabled()){
//            MenuItem navigationView = navigationMenu.findItem(R.id.nav_walkthroughinspect);
//            navigationView.setVisible(false);
        //  }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }
    public void setRunningFragment(BaseFragment baseFragment)
    {
        this.currentFragment=baseFragment;
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            int stackCount=getSupportFragmentManager().getBackStackEntryCount();
            if ( stackCount< 2){

                if(stackCount==1 && !isComingFromSettings)
                {
                    //showOperationStatus(false);
                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(this);
                    builder.setTitle(getLocalizeString("CancelTransaction","Cancel Transaction"));
                    builder.setMessage(getLocalizeString("AreYouSureYouWantToCancelThisTransaction","Are you sure you want to cancel this transaction?"));
                    builder.setPositiveButton(getLocalizeString("Yes","Yes"), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                            enableDrawerToggle(true);
                            isComingFromSettings=false;
                            FragmentMainActivity.super.onBackPressed();
                        }
                    });
                    builder.setNegativeButton(getLocalizeString("No","No"), null);


                    builder.show();
                    return;

                }

                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                enableDrawerToggle(true);
                isComingFromSettings=false;
            }


            if(currentFragment!=null )
            {
                if(!currentFragment.onBackPressed())
                    super.onBackPressed();
            }
            else
            super.onBackPressed();

        }
    }
/*

    private void showUnreadMessageIndicator(boolean start)
    {

        if (start)
        {
            unread.setVisibility(View.VISIBLE);
            unread.startAnimation(blinkAnimation);
        }else {
            unread.clearAnimation();
            unread.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        try {
            if(ChatManager.getInstance() !=null)
            {
               ChatManager.getInstance().getConversationsHandler().upsetUnreadConversationsListener(this);
            }
        }catch (Exception ex){}

        if(Utils.isUnreadChat(this))
        {
            showUnreadMessageIndicator(true);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("Onpause","Called");
        try {
            if(ChatManager.getInstance() !=null)
            {
                ChatManager.getInstance().getConversationsHandler().removeUnreadConversationsListener(this);
            }
        }catch (Exception ex){ex.printStackTrace();}
        showUnreadMessageIndicator(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposeChatManger();
    }

    private void disposeChatManger()
    {
        try {
            if(ChatManager.getInstance() !=null)
            {
                ChatManager.getInstance().getMyPresenceHandler().dispose();
                ChatManager.getInstance().getConversationsHandler().removeUnreadConversationsListener(this);
                ChatManager.getInstance().dispose();
            }
        }catch (Exception ex){ex.printStackTrace();}
    }
*/

    public void parentBackPress()
    {
        super.onBackPressed();
    }

    public void enableDrawerToggle(boolean tog)
    {

        toggle.setDrawerIndicatorEnabled(tog);
        if(!tog)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }else
        {
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }

    }

    public void addFragment(Fragment fragment,String tag,boolean drawerToggle)
    {   enableDrawerToggle(drawerToggle);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (AppUtils.isRTLLanguageSelected(this))
            ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right, R.anim.slide_in_right, R.anim.slide_out_left);
        else
            ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
        ft.replace(R.id.mainContent, fragment, tag);
        ft.addToBackStack("New_State")
                .commit();
    }

    public void popAllAddedFragment()
    {
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            enableDrawerToggle(true);
            getSupportFragmentManager().popBackStackImmediate("New_State", FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }catch (Exception e){e.printStackTrace();}
    }
    public void popFragment()
    {
        try {
            getSupportFragmentManager().popBackStack();
        }catch (Exception e){e.printStackTrace();}
    }


    public void showProgress()
    {
        try {
            if (progressDialog == null)
                return;
            progressDialog.setMessage(getLocalizeString("Processing", "Processing..."));
            progressDialog.show();
        }catch (Exception ex){}
    }

    public void  hideProgress()
    {
        try {
            if (progressDialog == null)
                return;
            progressDialog.setMessage(getLocalizeString("Processing", "Processing..."));
            progressDialog.dismiss();
        }catch (Exception ex){}
    }
    public void isSettings(boolean isSetting)
    {

        this.isComingFromSettings=isSetting;
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.main, menu);
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
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_ra_checkout) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    BaseFragment fragment = AgreementFragment.newInstance(Constant.RA_CHECKOUT);
                    addFragment(fragment, "" + fragment.getClass().getName(), false);
                }
            }, 350);
            ((MCCApplication)getApplication()).removeAllSymbols();// clear previous data before starting new operation
            ((MCCApplication)getApplication()).clearDocuments();
        }
        else  if (id == R.id.nav_ra_checkin) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    BaseFragment fragment = AgreementFragment.newInstance(Constant.RA_CHECKIN);
                    addFragment(fragment, "" + fragment.getClass().getName(), false);
                }
            }, 350);
            ((MCCApplication)getApplication()).removeAllSymbols();// clear previous data before starting new operation
            ((MCCApplication)getApplication()).clearDocuments();
        }
        else if(id == R.id.nav_replacement) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ReplacementFragment fragment = ReplacementFragment.newInstance(Constant.REPLACEMENT_CHECKIN);
                    addFragment(fragment, "" + fragment.getClass().getName(), false);
                }
            }, 350);
            ((MCCApplication) getApplication()).removeAllSymbols();
            ((MCCApplication) getApplication()).clearDocuments();
        } else if (id == R.id.nav_syncstatus) {
            int pendingMovementCount = 0;
//            int pendingMovementCount=DatabaseManager.getInstance(getApplicationContext()).getRecordCount(DBConstants.TABLE_MOVEMENT_JSON,0);
            int pendingImageCount = DatabaseManager.getInstance(getApplicationContext()).getRecordCount(DBConstants.TABLE_MARK_IMAGE, 0);
            String msg = "";
            if (pendingMovementCount > 0 || pendingImageCount > 0) {
//                msg+="Movements pending: "+pendingMovementCount;
                msg += "\nImages pending: " + pendingImageCount;
            } else
                msg = "No entries pending for synchronization";

            AlertDialog.Builder builder =
                    new AlertDialog.Builder(this, R.style.MyDialogAlertTheme);
            builder.setTitle("Sync Status");
            builder.setMessage(msg);
            if (pendingImageCount > 0) {
                builder.setPositiveButton("Sync Images", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent serviceIntent = new Intent(FragmentMainActivity.this, ImageSyncService.class);
                        ContextCompat.startForegroundService(FragmentMainActivity.this, serviceIntent);
                    }
                });

                builder.setNegativeButton("Cancel", null);
            } else {
                builder.setPositiveButton("Ok", null);
            }

            builder.show();
        } else if (id == R.id.nav_nrm_checkout) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    NRMFragment fragment = NRMFragment.newInstance(Constant.NRM_CHECKOUT);
                    addFragment(fragment, "" + fragment.getClass().getName(), false);
                }
            }, 350);
            ((MCCApplication)getApplication()).removeAllSymbols();
            ((MCCApplication)getApplication()).clearDocuments();
        }
        else if(id == R.id.nav_nrm_checkin)
        {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    NRMFragment fragment = NRMFragment.newInstance(Constant.NRM_CHECKIN);
                    addFragment(fragment, "" + fragment.getClass().getName(), false);
                }
            }, 350);
            ((MCCApplication)getApplication()).removeAllSymbols();
            ((MCCApplication)getApplication()).clearDocuments();
        }
        else if(id == R.id.garage_out)
        {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    GarageFragment fragment = GarageFragment.newInstance(Constant.GARAGE_CHECKOUT);
                    addFragment(fragment, "" + fragment.getClass().getName(), false);
                }
            }, 350);
            ((MCCApplication)getApplication()).removeAllSymbols();
            ((MCCApplication)getApplication()).clearDocuments();
        }
        else if(id == R.id.garage_in)
        {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    GarageFragment fragment = GarageFragment.newInstance(Constant.GARAGE_CHECKIN);
                    addFragment(fragment, "" + fragment.getClass().getName(), false);
                }
            }, 350);
            ((MCCApplication)getApplication()).removeAllSymbols();
            ((MCCApplication)getApplication()).clearDocuments();
        }
     /*   else if(id == R.id.nav_add_vehicle)
        {
            isComingFromSettings=true;
            AddVehicleFragment fragment= AddVehicleFragment.newInstance();
            addFragment(fragment, "" + fragment.getClass().getName(), false);
        }*/

       /* else if(id==R.id.nav_report)
        {
            DialogHelper.showReportsDialog(FragmentMainActivity.this);
        }*/

        else if (id == R.id.nav_setting) {
            isComingFromSettings=true;
            SettingsFragment settingsFragment=SettingsFragment.newInstance();
            addFragment(settingsFragment, "" + settingsFragment.getClass().getName(), false);
        }
/*        else if(id==R.id.nav_inspect)
        {
            Intent i=new Intent(this,InspectVehicleActivity.class);
            startActivity(i);
        }
        else if(id == R.id.nav_walkthroughinspect)
        {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    BaseFragment fragment = InspectionFragment.newInstance(Constant.VehicleInspection);
                    addFragment(fragment, "" + fragment.getClass().getName(), false);
                }
            }, 350);
            ((MCCApplication)getApplication()).removeAllSymbols();// clear previous data before starting new operation
            ((MCCApplication)getApplication()).clearDocuments();
        }*/
        else if(id==R.id.nav_logout)
        {

            AlertDialog.Builder builder =
                    new AlertDialog.Builder(this);
            builder.setTitle(getLocalizeString("Logout","Logout"));
            builder.setMessage(getLocalizeString("LogOutMsg","Are you sure you want to logout?"));
            builder.setPositiveButton(getLocalizeString("Logout","Logout"), new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

//                    disposeChatManger();
                    AppUtils.saveAuth(FragmentMainActivity.this, null);
                    FirebaseAuth.getInstance().signOut();
//                    Utils.setUser(FragmentMainActivity.this,null);

                    Intent intent = new Intent(FragmentMainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    if (AppUtils.isRTLLanguageSelected(FragmentMainActivity.this))
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    else
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    finish();
                }
            });
            builder.setNegativeButton(getLocalizeString("No","No"), null);

            builder.show();

            return true;

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void showOperationStatus(boolean isSuccess)
    {
        if (isSuccess)
            Toast.makeText(this,getLocalizeString("OperationSuccessMsg","Operation completed successfully."),Toast.LENGTH_LONG).show();
        else
            Toast.makeText(this,getLocalizeString("OperationCancelMsg","Operation was canceled."),Toast.LENGTH_LONG).show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
    }

    public void getMasterTables(boolean showProgress)
    {
        if(showProgress){
            progressDialog.setMessage(getLocalizeString("Syncing","Syncing..."));
        progressDialog.show();}

        Call<WebResponse<SyncResponse>> syncResponse= WebServiceFactory.getInstance().getSyncItems(new SyncRequest(),AppUtils.getAuthToken(this));
        syncResponse.enqueue(new Callback<WebResponse<SyncResponse>>() {
            @Override
            public void onResponse(Call<WebResponse<SyncResponse>> call, final Response<WebResponse<SyncResponse>> response) {


                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().isSuccess()) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {

                                final AppSettings appSettings = response.body().getResult().getSettings();
                                if (!appSettings.issCSEnabled()) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            // change UI elements here
                                            hidemenu();
                                        }
                                    });
                                }
                                AppUtils.saveSettings(getApplicationContext(), appSettings);

                                if (response.body().getResult().getLanguages() != null) {
                                    LanguagesResult languagesObject = new LanguagesResult();
                                    languagesObject.setItems(response.body().getResult().getLanguages());
                                    AppUtils.saveLanguages_list(getApplicationContext(), languagesObject);
                                }

                                DatabaseManager.getInstance(getApplicationContext()).deleteAllAccessory();
                                DatabaseManager.getInstance(getApplicationContext()).deleteAllLocations();
                                DatabaseManager.getInstance(getApplicationContext()).deleteAllStaff();
                                DatabaseManager.getInstance(getApplicationContext()).deleteAllStatuses();

                                ArrayList<AccessoryItem> items = response.body().getResult().getChecklistItems();
                                if (items != null && items.size() > 0) {
                                    DatabaseManager.getInstance(getApplicationContext()).addAccessories(items);
                                }

                                ArrayList<VehicleLocation> locations = response.body().getResult().getLocations();
                                if (locations != null && locations.size() > 0) {
                                    DatabaseManager.getInstance(getApplicationContext()).addVehicleLocation(locations);
                                }

                                ArrayList<Contact> staff = response.body().getResult().getStaffMembers();
                                if (staff != null && staff.size() > 0) {
                                    DatabaseManager.getInstance(getApplicationContext()).addStaffMember(staff);
                                }

                                ArrayList<VehicleStatus> vehiclestatus = response.body().getResult().getVehicleStatus();
                                if (vehiclestatus != null && vehiclestatus.size() > 0) {
                                    DatabaseManager.getInstance(getApplicationContext()).addVehicleStatus(vehiclestatus);
                                }

                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressDialog.dismiss();
                                        Intent serviceIntent = new Intent(FragmentMainActivity.this, ImageSyncService.class);
                                        ContextCompat.startForegroundService(FragmentMainActivity.this,serviceIntent);
/*
                                        IChatUser iChatUser = Utils.getUser(FragmentMainActivity.this);
                                        if (iChatUser == null )
                                        {
                                            getAccessTokenForFireBase(false);
                                        }else {
                                            if (ChatManager.getInstance() == null)
                                            ChatManager.start(FragmentMainActivity.this,Constant.APP_ID,iChatUser);

                                            ChatManager.getInstance().getMyPresenceHandler().connect();
                                            ChatManager.getInstance().getConversationsHandler().connect();
                                            ChatManager.getInstance().getConversationsHandler().upsetUnreadConversationsListener(FragmentMainActivity.this);
                                            SaveFirebaseInstanceIdService.updateTokenOnServer(FragmentMainActivity.this);
                                        }*/
                                    }
                                });

                            }
                        }).start();


                    } else {
                        progressDialog.dismiss();
                        AppUtils.showMessage("" + response.body().getError().getMessage(), toolbar, Snackbar.LENGTH_SHORT);
                    }

                } else {
                    progressDialog.dismiss();

                    if (response.code() == 401)
                        relogin();
                    else
                        AppUtils.showMessage(getLocalizeString("SyncingError","Error in Syncing."), toolbar, Snackbar.LENGTH_SHORT);

                }


            }

            @Override
            public void onFailure(Call<WebResponse<SyncResponse>> call, Throwable t) {

               // AppUtils.showMessage("Connection time out!", toolbar, Snackbar.LENGTH_SHORT);
                progressDialog.dismiss();
            }
        });
    }
/*

    @Override
    public void onUnreadConversationCounted(int count, ChatRuntimeException e) {

        Log.e("Unread Count",""+count);
        if (count>0){
            Utils.setUnreadChat(true,this);
            //if(Utils.isUnreadChat(this))
            showUnreadMessageIndicator(true);
        }
    }
*/

    private void getAccessTokenForFireBase(final boolean openChatWindow)
    {
        showProgress();
        WebServiceFactory.getInstance().getAccessToken(AppUtils.getAuthToken(this)).enqueue(new Callback<WebResponse<FirebaseAuthResponse>>() {
            @Override
            public void onResponse(Call<WebResponse<FirebaseAuthResponse>> call, Response<WebResponse<FirebaseAuthResponse>> response) {

                try {

                    if (response.body() != null && response.body().isSuccess() && response.body().getResult() != null && response.body().getResult().getToken() != null && response.body().getResult().getToken().length() > 0) {

                        fireBaseAuthenticate(response.body().getResult().getToken(),openChatWindow);

                    } else {
                        hideProgress();
                    }
                }catch (Exception ex){  hideProgress();}
            }

            @Override
            public void onFailure(Call<WebResponse<FirebaseAuthResponse>> call, Throwable t) {
               try {
                   Toast.makeText(context, "Error in getting Access token", Toast.LENGTH_LONG).show();
                   hideProgress();
               }catch (Exception ex){ex.printStackTrace();}
            }
        });
    }

    public void fireBaseAuthenticate(String token,final boolean openChatWindow)
    {
        FirebaseAuth.getInstance().signInWithCustomToken(token).addOnCompleteListener(FragmentMainActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                try {
                    if (task.isSuccessful()) {
                        String userName = AppUtils.getUserName(context);
/*                        String tennantName = AppUtils.getTenantName(context);
                        IChatUser iChatUser = new ChatUser(tennantName.toLowerCase() + "_" + userName.toLowerCase(), userName);
                        if (ChatManager.getInstance() == null)
                        ChatManager.start(context, Constant.APP_ID, iChatUser);
                        Utils.setUser(context,iChatUser);
                        ChatManager.getInstance().getMyPresenceHandler().connect();
                        ChatManager.getInstance().getConversationsHandler().connect();
                        ChatManager.getInstance().getConversationsHandler().upsetUnreadConversationsListener(FragmentMainActivity.this);

                        Map<String, Object> user = new HashMap<>();
                        user.put("uid", iChatUser.getId());
                        user.put("firstname", iChatUser.getFullName());
                        user.put("userName", iChatUser.getFullName());
                        user.put("tenantName",tennantName);
                        user.put("lastname", "");
                        user.put("appName","vmi");
                        user.put("platform","android");

                        ChatManager.getInstance().createContactFor(iChatUser.getId(), user, new OnContactCreatedCallback() {
                            @Override
                            public void onContactCreatedSuccess(ChatRuntimeException exception) {

                                if (exception == null && openChatWindow){
                                    ChatUI.getInstance().openConversationMessagesActivity(Constant.AGENT_ID,"Support Agent");
                                Util.LogE("Contact Created","Contact Creadted successfully");
                                }
                            }
                        });*/

//                        SaveFirebaseInstanceIdService.updateTokenOnServer(FragmentMainActivity.this);
                    }else {
                        task.getException().printStackTrace();
                    }

                    hideProgress();

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

    }




    public void relogin()
    {
        Toast.makeText(this,getLocalizeString("SessionErrorMsg","Session expired Please login again."),Toast.LENGTH_LONG).show();
        AppUtils.saveAuth(FragmentMainActivity.this, null);
        Intent intent = new Intent(FragmentMainActivity.this, LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }

    public void emailCheckCard(long movementId)
    {
       /* Call<ResponseBody> call=WebServiceFactory.getInstance().emailCheckCard(""+movementId,AppUtils.getAuthToken(this));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {}
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {}
        });*/
    }
}
