package com.karzansoft.fastvmi.Utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.karzansoft.fastvmi.Dialogs.AlertDialogFragment;
import com.karzansoft.fastvmi.MCCApplication;
import com.karzansoft.fastvmi.Models.AccessoryItem;
import com.karzansoft.fastvmi.Models.AppSettings;
import com.karzansoft.fastvmi.Models.Document;
import com.karzansoft.fastvmi.Models.DocumentImage;
import com.karzansoft.fastvmi.Models.LanguageItem;
import com.karzansoft.fastvmi.Models.LanguagesResult;
import com.karzansoft.fastvmi.Models.LocalizeText;
import com.karzansoft.fastvmi.Models.MarkDetail;
import com.karzansoft.fastvmi.Models.MovementInfo;
import com.karzansoft.fastvmi.Models.SymbolDetails;
import com.karzansoft.fastvmi.Models.SymbolImageDetail;
import com.karzansoft.fastvmi.Models.Vehicle;
import com.karzansoft.fastvmi.Models.VehicleLocation;
import com.karzansoft.fastvmi.Models.VehicleStatus;
import com.karzansoft.fastvmi.Network.Entities.Request.ValidateOperationRequest;
import com.karzansoft.fastvmi.Network.Entities.Request.VehicleSearchRequest;
import com.karzansoft.fastvmi.Network.Entities.Response.LoginResponse;
import com.karzansoft.fastvmi.Network.Entities.Response.ValidateOperationResponse;
import com.karzansoft.fastvmi.extended.CircleMarker;
import com.karzansoft.fastvmi.extended.LineMarker;
import com.karzansoft.fastvmi.extended.MarkerView;
import com.karzansoft.fastvmi.extended.MarkerViewInterior;
import com.karzansoft.fastvmi.extended.SymbolMarker;


import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;

/**
 * Created by Yasir on 4/12/2016.
 */
public class AppUtils {

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static void forceRTLIfSupported(Activity activity)
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
            activity.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
    }

    public static Vehicle parseQRCode(String content)
    {
        String[] parts = content.split("\n");
        Vehicle vehicle=new Vehicle();
        for (int i=0;i<parts.length;i++)
        {
            String part=parts[i];
            if(part.startsWith("FT"))
                vehicle.setFleetCode(part.substring(3).replaceAll("[\n\r]", ""));
            else  if(part.startsWith("RG"))
                vehicle.setPlateNo(part.substring(3).replaceAll("[\n\r]", ""));
            else if(part.startsWith("MK"))
                vehicle.setMake(part.substring(3).replaceAll("[\n\r]", ""));
            else if(part.startsWith("MD"))
                vehicle.setModel(part.substring(3).replaceAll("[\n\r]", ""));
            else if(part.startsWith("YR"))
                vehicle.setModelYear(part.substring(3).replaceAll("[\n\r]", ""));
        }

        return  vehicle;
    }

    public static HashMap<String,SymbolMarker> parseSymbol(String jsonString) {
        HashMap<String, SymbolMarker> symbolsHashMap = new HashMap<String, SymbolMarker>();
        try {

            JSONObject json=new JSONObject(jsonString);
            Iterator<String> iter = json.keys();
            while (iter.hasNext()) {
                String key = iter.next();

                symbolsHashMap.put(key,getSymbol(json.getJSONObject(key)));


            }
        }catch (Exception ex){ex.printStackTrace();}

        return symbolsHashMap;
}

    public  static SymbolMarker getSymbol(JSONObject jsonObject)
    {
        SymbolMarker symbolMarker=null;

        try {
            //JsonObject jsonObject=json.getAsJsonObject();
            Gson gson = new GsonBuilder().create();
            int type = jsonObject.getInt("markerType");
            if (type == Constant.SYMBOL_MARKER_LARGE_DENT || type == Constant.SYMBOL_MARKER_SMALL_DENT) {

                CircleMarker circleMarker = new  CircleMarker(jsonObject.getInt("startX"),jsonObject.getInt("startY"), jsonObject.getInt("markerType"), jsonObject.getInt("color"),Float.valueOf(""+jsonObject.get("scaleFactor")),jsonObject.getBoolean("isLarge"));
                circleMarker.anchor = Float.valueOf("" + jsonObject.getDouble("anchor"));
                symbolMarker = circleMarker;
            } else {

                LineMarker lineMarker = new LineMarker(jsonObject.getInt("startX"), jsonObject.getInt("startY"),jsonObject.getInt("endX"), jsonObject.getInt("endY"), jsonObject.getInt("markerType"),jsonObject.getInt("color"),Float.valueOf(""+jsonObject.get("scaleFactor")));
                lineMarker.selectedPoint = jsonObject.getInt("selectedPoint");
                symbolMarker = lineMarker;
            }
            symbolMarker.radious = jsonObject.getInt("radious");//isEditable
            symbolMarker.isEditable= jsonObject.getBoolean("isEditable");
            symbolMarker.isSelected = jsonObject.getBoolean("isSelected");
            symbolMarker.id = jsonObject.getString("id");


        }catch (Exception ex){ex.printStackTrace();}

        return symbolMarker;
    }

    public static ArrayList<String> getVehicesName(ArrayList<Vehicle> items,VehicleSearchRequest request)
    {
        ArrayList<String> names=new ArrayList<String>();
        for (int i=0;i<items.size();i++)
        {
            Vehicle item=items.get(i);
            if (request.getPlateNo()!=null && request.getPlateNo().length()>0)
                names.add(""+item.getPlateNo());
            else
                names.add(""+item.getFleetCode());
        }
        return names;
    }

    public static ArrayList<String> getCheckedListIds(ArrayList<AccessoryItem> list)
    {
        ArrayList<String> ids=new ArrayList<String>();

        if(list!=null)
        {
            for (int i=0;i<list.size();i++)
                ids.add(""+list.get(i).getId());
        }
        return ids;
    }

    public static SymbolDetails getSymbolDetailFromMark(MarkDetail mrk,int type)
    {
        SymbolDetails details = null;
        try {

            ArrayList<SymbolImageDetail> imges = new ArrayList<SymbolImageDetail>();
            for (int i = 0; i < mrk.getImages().size(); i++)
                imges.add(new SymbolImageDetail("", "" + mrk.getImages().get(i).getImageURL()));
            details = new SymbolDetails(type, "" + mrk.getDescription(), imges);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return details;
    }


    public static void loadCheckoutMrks(ValidateOperationResponse currentOperation,MarkerView markerView,Activity activity,ArrayList<String> fixedMarks)
    {
        // last movements mark loading
        if (currentOperation != null && currentOperation.getVehicle() != null) {
          //  MovementInfo lastMovement = currentOperation.getLastMovement();
            ArrayList<MarkDetail> marks = null;

          /*  if (lastMovement.isComplete() && lastMovement.getInDetail() != null)
                marks = lastMovement.getInMarks();

            else if (!lastMovement.isComplete() && lastMovement.getOutDetail() != null)
                marks = lastMovement.getOutMarks();
*/
            marks=currentOperation.getVehicle().getMarks();

            if (marks != null) {
                //marks=lastMovement.getOutMarks();
                for (int i = 0; i < marks.size(); i++) {
                    MarkDetail mark = marks.get(i);
                    if(fixedMarks.contains(""+mark.getId()))
                        continue;
                    JSONObject json = null;
                    try {
                        String js=mark.getVector();
                        json = new JSONObject(""+js);
                        if (json != null) {
                            switch (mark.getMarkTypeId()) {
                                case Constant.SYMBOL_MARKER_SMALL_DENT:
                                case Constant.SYMBOL_MARKER_LARGE_DENT:
                                case Constant.SYMBOL_MARKER_CRACK:
                                    markerView.addmark(json.getInt("X"), json.getInt("Y"), 0, 0, mark.getMarkTypeId(), "" + mark.getId(), false);
                                    ((MCCApplication) activity.getApplication()).addSymbol(""+mark.getId(), AppUtils.getSymbolDetailFromMark(mark, mark.getMarkTypeId()));
                                    break;
                                case Constant.SYMBOL_MARKER_SCRATCH:
                                case Constant.SYMBOL_MARKER_SCRATCH_THIN:
                                    markerView.addmark(json.getJSONObject("From").getInt("X"), json.getJSONObject("From").getInt("Y"), json.getJSONObject("To").getInt("X"), json.getJSONObject("To").getInt("Y"), mark.getMarkTypeId(), "" + mark.getId(), false);
                                    ((MCCApplication) activity.getApplication()).addSymbol(""+mark.getId(), AppUtils.getSymbolDetailFromMark(mark, mark.getMarkTypeId()));
                                    break;
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
    }

    public static void loadCheckoutInteriorMrks(ValidateOperationResponse currentOperation,MarkerViewInterior markerView,Activity activity,ArrayList<String> fixedMarks)
    {
        // last movements mark loading
        if (currentOperation != null && currentOperation.getVehicle() != null) {
           // MovementInfo lastMovement = currentOperation.getLastMovement();
            ArrayList<MarkDetail> marks = null;

         /*   if (lastMovement.isComplete() && lastMovement.getInDetail() != null)
                marks = lastMovement.getInMarks();

            else if (!lastMovement.isComplete() && lastMovement.getOutDetail() != null)
                marks = lastMovement.getOutMarks();*/

            marks=currentOperation.getVehicle().getMarks();

            if (marks != null) {

                for (int i = 0; i < marks.size(); i++) {
                    MarkDetail mark = marks.get(i);
                    if(fixedMarks.contains(""+mark.getId()))
                        continue;
                    JSONObject json = null;
                    try {
                        String js=""+mark.getVector();
                        json = new JSONObject(js);
                        if (json != null) {
                            switch (mark.getMarkTypeId()) {
                                case 5:
                                    markerView.addmark(json.getInt("X"), json.getInt("Y"), 0, 0, Constant.SYMBOL_MARKER_LARGE_DENT, "" + mark.getId());
                                    ((MCCApplication) activity.getApplication()).addSymbol(""+mark.getId(), AppUtils.getSymbolDetailFromMark(mark, Constant.SYMBOL_MARKER_LARGE_DENT));
                                    break;
                                case 7:
                                    markerView.addmark(json.getInt("X"), json.getInt("Y"), 0, 0, Constant.SYMBOL_MARKER_CRACK, "" + mark.getId());
                                    ((MCCApplication) activity.getApplication()).addSymbol(""+mark.getId(), AppUtils.getSymbolDetailFromMark(mark, Constant.SYMBOL_MARKER_CRACK));

                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
    }

    public static void loadVehicleExteriorMarks(Vehicle vehicle,MarkerView markerView,Activity activity,ArrayList<String> fixedMarks)
    {
        ArrayList<MarkDetail> marks = vehicle.getMarks();
        if (marks != null) {
            for (int i = 0; i < marks.size(); i++) {
                MarkDetail mark = marks.get(i);
                if(fixedMarks.contains(""+mark.getId()))
                    continue;
                JSONObject json = null;
                try {
                    String js=mark.getVector();
                    json = new JSONObject(""+js);
                    if (json != null) {
                        switch (mark.getMarkTypeId()) {
                            case Constant.SYMBOL_MARKER_SMALL_DENT:
                            case Constant.SYMBOL_MARKER_LARGE_DENT:
                            case Constant.SYMBOL_MARKER_CRACK:
                                markerView.addmark(json.getInt("X"), json.getInt("Y"), 0, 0, mark.getMarkTypeId(), "" + mark.getId(), false);
                                ((MCCApplication) activity.getApplication()).addSymbol(""+mark.getId(), AppUtils.getSymbolDetailFromMark(mark, mark.getMarkTypeId()));
                                break;
                            case Constant.SYMBOL_MARKER_SCRATCH:
                            case Constant.SYMBOL_MARKER_SCRATCH_THIN:
                                markerView.addmark(json.getJSONObject("From").getInt("X"), json.getJSONObject("From").getInt("Y"), json.getJSONObject("To").getInt("X"), json.getJSONObject("To").getInt("Y"), mark.getMarkTypeId(), "" + mark.getId(), false);
                                ((MCCApplication) activity.getApplication()).addSymbol(""+mark.getId(), AppUtils.getSymbolDetailFromMark(mark, mark.getMarkTypeId()));
                                break;
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public static void loadVehicleInteriorMarks(Vehicle vehicle,MarkerViewInterior markerView,Activity activity,ArrayList<String> fixedMarks)
    {
        ArrayList<MarkDetail> marks = vehicle.getMarks();
        if (marks != null) {
            for (int i = 0; i < marks.size(); i++) {
                MarkDetail mark = marks.get(i);
                if(fixedMarks.contains(""+mark.getId()))
                    continue;
                JSONObject json = null;
                try {
                    String js=""+mark.getVector();
                    json = new JSONObject(js);
                    if (json != null) {
                        switch (mark.getMarkTypeId()) {
                            case 5:
                                markerView.addmark(json.getInt("X"), json.getInt("Y"), 0, 0, Constant.SYMBOL_MARKER_LARGE_DENT, "" + mark.getId());
                                ((MCCApplication) activity.getApplication()).addSymbol(""+mark.getId(), AppUtils.getSymbolDetailFromMark(mark, Constant.SYMBOL_MARKER_LARGE_DENT));
                                break;
                            case 7:
                                markerView.addmark(json.getInt("X"), json.getInt("Y"), 0, 0, Constant.SYMBOL_MARKER_CRACK, "" + mark.getId());
                                ((MCCApplication) activity.getApplication()).addSymbol(""+mark.getId(), AppUtils.getSymbolDetailFromMark(mark, Constant.SYMBOL_MARKER_CRACK));
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public static String convertToSerializeDocs(ArrayList<DocumentImage> documentImages)
    {

        ArrayList<Document> documents=new ArrayList<>();
        if (documentImages!=null && documentImages.size()>0)
        {
            ArrayList<String> passImages=new ArrayList<>(),nIdImages=new ArrayList<>(),dlImages=new ArrayList<>();
            for (int i=0;i<documentImages.size();i++)
            {
                DocumentImage docimg=documentImages.get(i);
                switch (docimg.getDocumentType())
                {
                    case 2:
                        passImages.add(""+docimg.getUrl());
                        break;
                    case 3:
                        nIdImages.add("" + docimg.getUrl());
                        break;
                    case 4:
                        dlImages.add("" + docimg.getUrl());
                        break;
                }
            }
            if(passImages.size()>0)
                documents.add(new Document("Passport",passImages));
            if(nIdImages.size()>0)
                documents.add(new Document("National ID",nIdImages));
            if(dlImages.size()>0)
                documents.add(new Document("Driving License",dlImages));

        }

     return serializeDoc(documents);
    }

    public static String serializeDoc(List<Document> documents)
    {
        String jsonString="";
        try{
            Type typeOfHashMap = new TypeToken<List<Document>>() {}.getType();
            Gson gson = new GsonBuilder().create();
            jsonString = gson.toJson(documents, typeOfHashMap);
            Util.LogE("Json", "" + jsonString);
        }catch (Exception e){e.printStackTrace();}
        return jsonString;
    }

    public static ArrayList<Document> deSerializeDoc(String jsonString)
    {
        ArrayList<Document> documents=new ArrayList<>();

        try{
            Type typeOfHashMap = new TypeToken<ArrayList<Document>>() { }.getType();
            Gson gson = new GsonBuilder().create();
            if (jsonString.length()>0)
                documents=gson.fromJson(jsonString, typeOfHashMap);
            Util.LogE("Restored Json", "" + jsonString);
        }catch (Exception e){e.printStackTrace();
        }
        return documents;
    }

    public static VehicleStatus searchStatus(ArrayList<VehicleStatus> items, String text)
    {
        for (int i=0;i<items.size();i++)
        {
            if(items.get(i).getName().equalsIgnoreCase(text))
                return items.get(i);
        }

        return null;
    }

    public static VehicleLocation searchLocation(ArrayList<VehicleLocation> items, String text)
    {
        for (int i=0;i<items.size();i++)
        {
            if(items.get(i).getName().equalsIgnoreCase(text))
                return items.get(i);
        }

        return null;
    }

    public static int getIndexOf(String[] items,String keyword)
    {
        for (int i=0;i<items.length;i++)
        {
            if(items[i].equalsIgnoreCase(keyword))
                return i;
        }

        return -1;
    }

    public static void showVehicleStatusList(final EditText view,ArrayList<VehicleStatus> items,Activity activity)
    {
        if(items.size()<1)
            return;
        PopupMenu popup = new PopupMenu(activity, view);
        for (int i=0;i<items.size();i++)
        {
            popup.getMenu().add(items.get(i).getName());
        }
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                view.setText("" + item.getTitle());
                return true;
            }
        });
        popup.show();//showing popup menu
    }



    public static void showVehicleLocationList(final EditText view,ArrayList<VehicleLocation> items,Activity activity)
    {
        if(items.size()<1)
            return;
        PopupMenu popup = new PopupMenu(activity, view);
        for (int i=0;i<items.size();i++)
        {
            popup.getMenu().add(items.get(i).getName());
        }
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                view.setText("" + item.getTitle());
                return true;
            }
        });
        popup.show();//showing popup menu
    }


    public static void showOptionsList(final EditText view,String[] items,Activity activity)
    {
        PopupMenu popup = new PopupMenu(activity, view);
        for (int i=0;i<items.length;i++)
            popup.getMenu().add(items[i]);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                view.setText("" + item.getTitle());
                return true;
            }
        });

        popup.show();//showing popup menu
    }

    public static void showOptionsList(final View view,String[] items,Activity activity,PopupMenu.OnMenuItemClickListener itemClickListener)
    {
        PopupMenu popup = new PopupMenu(activity, view);
        for (int i=0;i<items.length;i++)
        {
            popup.getMenu().add(Menu.NONE, i, i,items[i]);

        }
        popup.setOnMenuItemClickListener(itemClickListener);

        popup.show();//showing popup menu
    }

    public static void saveDocType(Context context,String key,boolean value)
    {
        SharedPreferences sharedpreferences = context.getSharedPreferences(Constant.PREF_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }
    public static boolean getDocType(Context context,String key,boolean defaultV)
    {
        SharedPreferences sharedpreferences = context.getSharedPreferences(Constant.PREF_FILE, Context.MODE_PRIVATE);
        return sharedpreferences.getBoolean(key, defaultV);
    }

    public static  void saveAuth(Context context,String auth)
    {
        SharedPreferences sharedpreferences = context.getSharedPreferences(Constant.PREF_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        if (auth != null)
        {
            editor.putString(Constant.PREF_KEY_AUTH, "bearer " + auth);
           /* editor.putInt(Constant.PREF_KEY_USER_ID, auth.getStaffId());
            editor.putString(Constant.PREF_KEY_USER_NAME, "" + auth.getUserName());
//            editor.putString(Constant.PREF_KEY_TENANT_NAME, "" + auth.getTenant());
            editor.putBoolean(Constant.PREF_KEY_REMEMBER_ME,auth.isRememberMe());*/
        }
        else
        {
            editor.putString(Constant.PREF_KEY_AUTH, "");
            editor.putInt(Constant.PREF_KEY_USER_ID, -1);
           /* editor.putString(Constant.PREF_KEY_USER_NAME, "");
            editor.putString(Constant.PREF_KEY_TENANT_NAME, "");
            editor.putBoolean(Constant.PREF_KEY_REMEMBER_ME,false);*/
        }

        editor.commit();
    }

    public static String getAuthToken(Context context)
    {
        SharedPreferences sharedpreferences = context.getSharedPreferences(Constant.PREF_FILE, Context.MODE_PRIVATE);
        String auth=sharedpreferences.getString(Constant.PREF_KEY_AUTH, "");
        //Util.LogE("token",""+auth);
        return auth;

    }

    public static String getUserName(Context context) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(Constant.PREF_FILE, Context.MODE_PRIVATE);
        String auth = sharedpreferences.getString(Constant.PREF_KEY_USER_NAME, "");
        return auth;

    }

//    public static String getTenantName(Context context)
//    {
//        SharedPreferences sharedpreferences = context.getSharedPreferences(Constant.PREF_FILE, Context.MODE_PRIVATE);
//        String auth=sharedpreferences.getString(Constant.PREF_KEY_TENANT_NAME, "");
//        return auth;
//
//    }

    public static int getLoginStaffId(Context context) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(Constant.PREF_FILE, Context.MODE_PRIVATE);
        return sharedpreferences.getInt(Constant.PREF_KEY_USER_ID, -1);
    }

    public static boolean isRememberMe(Context context) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(Constant.PREF_FILE, Context.MODE_PRIVATE);
        boolean rem=sharedpreferences.getBoolean(Constant.PREF_KEY_REMEMBER_ME, false);
        return rem;

    }

    public static void saveLoginInfo(Context context,String loginInfo)
    {
        SharedPreferences sharedpreferences = context.getSharedPreferences(Constant.PREF_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        if (loginInfo != null) {
            Gson gson = new GsonBuilder().create();
//            String movString = gson.toJson(loginInfo, LoginResponse.class);
            editor.putString(Constant.PREF_KEY_lOGIN_INFO, loginInfo);
            editor.commit();
        }else {
            editor.putString(Constant.PREF_KEY_lOGIN_INFO, "");
            editor.commit();
        }
    }

    public static LoginResponse getLoginInfo(Context context)
    {
        LoginResponse loginInfo=null;
        SharedPreferences sharedpreferences = context.getSharedPreferences(Constant.PREF_FILE, Context.MODE_PRIVATE);
        String mov=sharedpreferences.getString(Constant.PREF_KEY_lOGIN_INFO, "");
        if (mov.length() > 0) {
            Gson gson = new GsonBuilder().create();
            loginInfo = gson.fromJson(mov, LoginResponse.class);
        } else
            loginInfo = new LoginResponse();

        return loginInfo;
    }

    public static void saveSettings(Context context, AppSettings appSettings)
    {

        SharedPreferences sharedpreferences = context.getSharedPreferences(Constant.PREF_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        if (appSettings != null) {
            Gson gson = new GsonBuilder().create();
            String movString = gson.toJson(appSettings, AppSettings.class);
            editor.putString(Constant.PREF_KEY_APP_SETTINGS, movString);
            editor.commit();
        }

    }

    public static AppSettings getSettings(Context context)
    {
        AppSettings appSettings =null;
        SharedPreferences sharedpreferences = context.getSharedPreferences(Constant.PREF_FILE, Context.MODE_PRIVATE);
        String set=sharedpreferences.getString(Constant.PREF_KEY_APP_SETTINGS, "");
        if (set.length() > 0) {
            Gson gson = new GsonBuilder().create();
            appSettings = gson.fromJson(set, AppSettings.class);
        } else
            appSettings = new AppSettings();

        return appSettings;
    }

    public static void setCurrentLanguage(Context context, LanguageItem languageItem)
    {
        SharedPreferences sharedpreferences = context.getSharedPreferences(Constant.PREF_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        if (languageItem != null) {
            Gson gson = new GsonBuilder().create();
            String jsonString = gson.toJson(languageItem, LanguageItem.class);
            editor.putString(Constant.PREF_KEY_APP_CURRENT_LANG, jsonString);
            editor.commit();
        }
    }

    public static LanguageItem getCurrentLanguage(Context context)
    {
        LanguageItem languageItem =null;
        SharedPreferences sharedpreferences = context.getSharedPreferences(Constant.PREF_FILE, Context.MODE_PRIVATE);
        String set=sharedpreferences.getString(Constant.PREF_KEY_APP_CURRENT_LANG, "");
        if (set.length() > 0) {
            Gson gson = new GsonBuilder().create();
            languageItem = gson.fromJson(set, LanguageItem.class);
        }

        return languageItem;
    }
    public static void saveLanguages_list(Context context, LanguagesResult languagesObject)
    {
        SharedPreferences sharedpreferences = context.getSharedPreferences(Constant.PREF_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        if (languagesObject != null) {
            Gson gson = new GsonBuilder().create();
            String movString = gson.toJson(languagesObject, LanguagesResult.class);
            editor.putString(Constant.PREF_KEY_APP_LANGUAGES_LIST, movString);
            editor.commit();
        }
    }

    public static LanguagesResult getLanguagesList(Context context)
    {

        LanguagesResult languagesResult =null;
        SharedPreferences sharedpreferences = context.getSharedPreferences(Constant.PREF_FILE, Context.MODE_PRIVATE);
        String set=sharedpreferences.getString(Constant.PREF_KEY_APP_LANGUAGES_LIST, "");
        if (set.length() > 0) {
            Gson gson = new GsonBuilder().create();
            languagesResult = gson.fromJson(set, LanguagesResult.class);
        }

        return languagesResult;
    }


    public static void saveLanguageStrings(Context context,HashMap<String,String> strings)
    {
        String val="";
        try{
            Type typeOfHashMap = new TypeToken<HashMap<String, String>>() { }.getType();
            Gson gson = new GsonBuilder().create();
            val=gson.toJson(strings, typeOfHashMap);
        }catch (Exception e){e.printStackTrace();}
        SharedPreferences sharedpreferences = context.getSharedPreferences(Constant.PREF_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(Constant.PREF_KEY_APP_LANG_STRINGS, val);
        editor.commit();
    }

    public static HashMap<String,String> getLanguageStrings(Context context)
    {
        HashMap<String,String> stringHashMap=null;
        SharedPreferences sharedpreferences = context.getSharedPreferences(Constant.PREF_FILE, Context.MODE_PRIVATE);
        String val=sharedpreferences.getString(Constant.PREF_KEY_APP_LANG_STRINGS, "");
        try{
            Type typeOfHashMap = new TypeToken<HashMap<String, String>>() { }.getType();
            Gson gson = new GsonBuilder().create();
            if(val.length()>1)
                stringHashMap=gson.fromJson(val, typeOfHashMap);
        }catch (Exception e){e.printStackTrace();}
        // Util.LogE("token",""+auth);
        return stringHashMap;

    }

    public static String getLocalizeString(Context context,String key,String defValue)
    {
        HashMap<String,String> languageTexts=getLanguageStrings(context);
        if(languageTexts!=null && languageTexts.get(key)!=null)
        {
            return languageTexts.get(key);

        }
        return defValue;
    }
    public static void setText(TextView tv,String text)
    {
        tv.setText(text);
    }
    public static void setTextHint(TextInputLayout textInputLayout, String hint)
    {
        textInputLayout.setHint(hint);
    }

    public static HashMap<String,String>  convertToMap(ArrayList<LocalizeText> texts)
    {
        HashMap<String,String> languageTexts=new HashMap<>(texts.size());
        for (int i=0;i<texts.size();i++)
        {
            LocalizeText localizeText=texts.get(i);
            languageTexts.put(localizeText.getKey(),localizeText.getTargetValue());
        }
        return languageTexts;
    }

    public static boolean isRTLLanguageSelected(Context context)
    {
        LanguageItem languageItem=getCurrentLanguage(context);
        if(languageItem!=null)
        {
            if(languageItem.getName().equalsIgnoreCase("he") ||languageItem.getName().equalsIgnoreCase("ar") )
                return true;
        }

        return false;
    }

    public static void saveMovementInfo(Context context,MovementInfo movementInfo)
    {

        SharedPreferences sharedpreferences = context.getSharedPreferences(Constant.PREF_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        if (movementInfo != null) {
            Gson gson = new GsonBuilder().create();
            String movString = gson.toJson(movementInfo, MovementInfo.class);
            editor.putString(Constant.PREF_KEY_MOV, movString);
            editor.commit();
        }

    }

    public static MovementInfo getMovementInfo(Context context)
    {
        MovementInfo movementInfo=null;
        SharedPreferences sharedpreferences = context.getSharedPreferences(Constant.PREF_FILE, Context.MODE_PRIVATE);
        String mov=sharedpreferences.getString(Constant.PREF_KEY_MOV, "");
        if (mov.length() > 0) {
            Gson gson = new GsonBuilder().create();
            movementInfo = gson.fromJson(mov, MovementInfo.class);
        } else
            movementInfo = new MovementInfo();

        return movementInfo;
    }


    public static void saveDocumetImages(Context context,String serilalizedJson)
    {
        SharedPreferences sharedpreferences = context.getSharedPreferences(Constant.PREF_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        if (serilalizedJson != null) {
            editor.putString(Constant.PREF_KEY_DOC_IMAGES, serilalizedJson);
            editor.commit();
        }
    }

    public static ArrayList<Document> getDocumentsImages(Context context)
    {
        ArrayList<Document> documents=new ArrayList<>();
        SharedPreferences sharedpreferences = context.getSharedPreferences(Constant.PREF_FILE, Context.MODE_PRIVATE);
        String jsonString=sharedpreferences.getString(Constant.PREF_KEY_DOC_IMAGES, "");
        if (jsonString.length() > 0) {
            try {
                Type typeOfHashMap = new TypeToken<ArrayList<Document>>() {
                }.getType();
                Gson gson = new GsonBuilder().create();
                documents = gson.fromJson(jsonString, typeOfHashMap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return documents;
    }

    public static ValidateOperationRequest getOperationRequest(String fleetcode,int current_mode)
    {
        ValidateOperationRequest request=new ValidateOperationRequest();
        request.setFleetCode(fleetcode);
        request.setMovementTypeId(Constant.RA_CHECKOUT);
        request.setOperationType(-1);
        request.setCheckType(-1);
        request.setMovementCategory(-1);
        if(current_mode==Constant.REPLACEMENT_CHECKIN)
        {
            request.setCheckType(2);
        }else if(current_mode==Constant.REPLACEMENT_CHECKOUT)
        {
            request.setCheckType(1);
        }else if(current_mode==Constant.NRM_CHECKOUT)
        {
            request.setMovementTypeId(-1);
            request.setMovementCategory(2);
        }

        return  request;
    }

    public static boolean isNetworkAvailable(Context context) {
        if(context==null)
            return false;
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void showMessage(String text,View parent,int duration)
    {
        Snackbar snack= Snackbar.make(parent, text, duration);
        View view = snack.getView();
        TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
        // tv.setTextColor(Color.RED);
        //tv.setGravity(Gravity.CENTER_HORIZONTAL);
        snack.setDuration(4000);
        snack.show();
    }
    public static void showToastMessage(String msg,Context context)
    {
        Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    public static void checkLocationNetwork(final Context context) {
        LocationManager mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
          boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}

        try {
            network_enabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {}

        if(!network_enabled && !gps_enabled){
            //notify user
            AlertDialog.Builder gpsAlert = new AlertDialog.Builder(context);
            gpsAlert.setTitle("Use Location");
            gpsAlert.setMessage("Location services are disabled in your device.Would you like to enable it? ");
            gpsAlert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent settings = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    context.startActivity(settings);
                }
            });
            gpsAlert.setNegativeButton("No",null);
            gpsAlert.setCancelable(false);

            gpsAlert.show();
        }

    }


    public static void showAlertWithEmailOption(String Title, final String Msg, final String otherInfo, final Fragment fragment)
    {
        AlertDialogFragment alert=AlertDialogFragment.newInstance(Title,Msg, "Report", "Cancel");
        alert.setButtonsClickListener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which== Dialog.BUTTON_POSITIVE) {
//                    String body="Tenant : "+getTenantName(fragment.getActivity())+"\n";
                    String body = "User Name : " + getUserName(fragment.getActivity()) + "\n";

                    try {
                        String versionName = fragment.getActivity().getPackageManager().getPackageInfo(fragment.getActivity().getPackageName(), 0).versionName;
                        body += "App Version : " + versionName + "\n";
                        body += "DateTime in Milli: " + System.currentTimeMillis() + "\n";
                    } catch (PackageManager.NameNotFoundException ex) {
                    }
                    body += "Error Message : \n" + Msg + "\n";

                    body += "Request Logs : \n" + otherInfo + "\n" + getDeviceInfo();

                    String uriText =
                            "mailto:support@speedautosystems.com" +
                                    "?subject=" + Uri.encode("VMI Error Report") +
                                    "&body=" + Uri.encode(body);

                    Uri uri = Uri.parse(uriText);

                    Intent sendIntent = new Intent(Intent.ACTION_SENDTO);
                    sendIntent.setData(uri);
                    fragment.getActivity().startActivity(Intent.createChooser(sendIntent, "Send email"));

                }
            }
        });
        alert.show(fragment.getFragmentManager(), "detailAlert");
    }

    public static String getDeviceInfo()
    {
        String  details="\nDevice Info \n";
              try {
                  details += "VERSION.RELEASE : " + Build.VERSION.RELEASE
                          + "\nVERSION.INCREMENTAL : " + Build.VERSION.INCREMENTAL
                          + "\nVERSION.SDK.NUMBER : " + Build.VERSION.SDK_INT
                          + "\nBOARD : " + Build.BOARD
                          + "\nBOOTLOADER : " + Build.BOOTLOADER
                          + "\nBRAND : " + Build.BRAND
                          + "\nDISPLAY : " + Build.DISPLAY
                          + "\nFINGERPRINT : " + Build.FINGERPRINT
                          + "\nHARDWARE : " + Build.HARDWARE
                          + "\nHOST : " + Build.HOST
                          + "\nID : " + Build.ID
                          + "\nMANUFACTURER : " + Build.MANUFACTURER
                          + "\nMODEL : " + Build.MODEL
                          + "\nPRODUCT : " + Build.PRODUCT
                          + "\nSERIAL : " + Build.SERIAL
                          + "\nTAGS : " + Build.TAGS
                          + "\nTIME : " + Build.TIME
                          + "\nTYPE : " + Build.TYPE
                          + "\nUNKNOWN : " + Build.UNKNOWN
                          + "\nUSER : " + Build.USER;
              }
              catch (Exception ex){}

        return details;
    }


    public static String getImageUrlFromName(String name)
    {
        String url=Constant.BASE_URL+"app/images/"+name;

        return url;
    }

    public static String generateRandomNumber() {
        Random r = new Random( System.currentTimeMillis() );
        int result = (1 + r.nextInt(2)) * 1000 + r.nextInt(1000);
        String resultString = String.valueOf(result);
        return resultString;
    }

    public static String getBase64String(String value) throws UnsupportedEncodingException {
        return Base64.encodeToString(value.getBytes("UTF-8"), Base64.NO_WRAP);
    }

    public static String getSelectedDateAndTime(final Calendar calendar)
    {
        Date date = calendar.getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        return formatter.format(date);
    }
}
