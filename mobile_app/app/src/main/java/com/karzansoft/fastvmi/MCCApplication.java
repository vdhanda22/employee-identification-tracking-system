package com.karzansoft.fastvmi;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.multidex.MultiDex;

import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.karzansoft.fastvmi.DataBase.DatabaseManager;
import com.karzansoft.fastvmi.Models.Document;
import com.karzansoft.fastvmi.Models.SymbolDetails;
import com.karzansoft.fastvmi.Network.ScureImageDownloader;
import com.karzansoft.fastvmi.Utils.AppUtils;
import com.karzansoft.fastvmi.Utils.Util;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Yasir on 2/11/2016.
 */
public class MCCApplication extends Application {

    List<Document> mDocuments;
    HashMap<String,SymbolDetails> symbolDetailsHashMap;

    @Override
    public void onCreate() {
        super.onCreate();
        //TooLargeTool.startLogging(this);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .memoryCacheExtraOptions(480,800).threadPoolSize(10)
                .memoryCacheSize(10)
                .defaultDisplayImageOptions((new DisplayImageOptions.Builder())
                        .bitmapConfig(Bitmap.Config.RGB_565)
                        .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                        .cacheInMemory(true).cacheOnDisk(true)
                        .showImageOnFail(R.drawable.empty_photo)
                        .considerExifParams(true)
                        .build())
                .imageDownloader(new ScureImageDownloader(this,1000*5,1000*20))
                .build();
        ImageLoader.getInstance().init(config);

        DatabaseManager.getInstance(this).deleteImageByStatusID(1);// Delete All previously Synced Images
        /*DatabaseManager.getInstance(this).openDatabase();
        DatabaseManager.getInstance(this).closeDatabase();*/
        //EmojiManager.install(new IosEmojiProvider());
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
       // Stetho.initializeWithDefaults(this);
    }

    public List<Document> getDocuments()
    {
        if(this.mDocuments==null)
        {
            this.mDocuments= new ArrayList<Document>();
            Document doc0=new Document(AppUtils.getLocalizeString(this,"Passport","Passport"), new ArrayList<String>());
            Document doc1=new Document(AppUtils.getLocalizeString(this,"NationalId","National ID"), new ArrayList<String>());
            Document doc2=new Document(AppUtils.getLocalizeString(this,"DrivingLicense","Driving License"), new ArrayList<String>());
            mDocuments.add(doc0);
            mDocuments.add(doc1);
            mDocuments.add(doc2);
        }
        return  this.mDocuments;
    }

    public void clearDocuments()
    {
        if(this.mDocuments!=null)
        {
            this.mDocuments.clear();
            this.mDocuments=null;
        }
    }



    public String addSymbol(int type,String keyId)
    {
        if(this.symbolDetailsHashMap==null)
            this.symbolDetailsHashMap=new HashMap<String,SymbolDetails>();
        String id=System.currentTimeMillis()+"";
        this.symbolDetailsHashMap.put(keyId,new SymbolDetails(type));
        return id;
    }

    public void addSymbol(String keyId,SymbolDetails obj)
    {
        if(this.symbolDetailsHashMap==null)
            this.symbolDetailsHashMap=new HashMap<String,SymbolDetails>();

       if(obj!=null)
           this.symbolDetailsHashMap.put(keyId,obj);

    }

    public SymbolDetails getSymbolById(String keyId)
    {
        SymbolDetails item=null;
        if(this.symbolDetailsHashMap!=null)
        item=this.symbolDetailsHashMap.get(keyId);
        return item;
    }

    public void removeSymbol(String keyId)
    {
        if(this.symbolDetailsHashMap!=null && this.symbolDetailsHashMap.containsKey(keyId))
            this.symbolDetailsHashMap.remove(keyId);
    }

    public void removeAllSymbols()
    {
        if(this.symbolDetailsHashMap!=null)
            this.symbolDetailsHashMap.clear();
    }

    public String getSerializedMap()
    {
        String jsonString="";
        try{
        Type typeOfHashMap = new TypeToken<HashMap<String, SymbolDetails>>() { }.getType();
        Gson gson = new GsonBuilder().create();
            if(symbolDetailsHashMap!=null)
        jsonString = gson.toJson(symbolDetailsHashMap,typeOfHashMap);
            Util.LogE("Json", "" + jsonString);
        }catch (Exception e){e.printStackTrace();}
        return jsonString;

    }

    public void loadSerializeMap(String jsonString)
    {
        try{
            Type typeOfHashMap = new TypeToken<HashMap<String, SymbolDetails>>() { }.getType();
            Gson gson = new GsonBuilder().create();
            if (jsonString.length()>0)
            symbolDetailsHashMap=gson.fromJson(jsonString, typeOfHashMap);
        }catch (Exception e){e.printStackTrace();
        }
    }

    public String getSerializedDoc()
    {
        String jsonString="";
        try{
            Type typeOfHashMap = new TypeToken<ArrayList<Document>>() { }.getType();
            Gson gson = new GsonBuilder().create();
            if(mDocuments!=null)
                jsonString = gson.toJson(mDocuments, typeOfHashMap);
            Util.LogE("Json", "" + jsonString);
        }catch (Exception e){e.printStackTrace();}
        return jsonString;

    }

    public void loadSerializeDoc(String jsonString)
    {
        try{
            Type typeOfHashMap = new TypeToken<ArrayList<Document>>() { }.getType();
            Gson gson = new GsonBuilder().create();
            if (jsonString.length()>0)
                mDocuments=gson.fromJson(jsonString, typeOfHashMap);
            Util.LogE("Restored Json", "" + jsonString);
        }catch (Exception e){e.printStackTrace();
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this); // add this
    }
}
