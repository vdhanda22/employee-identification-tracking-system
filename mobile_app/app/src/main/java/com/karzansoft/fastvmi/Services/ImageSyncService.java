package com.karzansoft.fastvmi.Services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.google.gson.JsonObject;
import com.karzansoft.fastvmi.Activities.FragmentMainActivity;
import com.karzansoft.fastvmi.DataBase.DatabaseManager;
import com.karzansoft.fastvmi.Models.AccessToken;
import com.karzansoft.fastvmi.Models.MarkImage;
import com.karzansoft.fastvmi.Network.WebResponse;
import com.karzansoft.fastvmi.Network.WebServiceFactory;
import com.karzansoft.fastvmi.R;
import com.karzansoft.fastvmi.Utils.AppUtils;
import com.karzansoft.fastvmi.Utils.Constant;
import com.karzansoft.fastvmi.Utils.Util;
import com.koushikdutta.ion.Ion;

import java.io.File;
import java.util.ArrayList;

import retrofit2.Call;

/**
 * Created by Yasir on 5/15/2016.
 */
public class ImageSyncService extends IntentService {



    public ImageSyncService() {
        super(ImageSyncService.class.getName());
        setIntentRedelivery(true);
    }

    public ImageSyncService(String name) {
        super(ImageSyncService.class.getName());
        setIntentRedelivery(true);
    }
    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        startForeGround();
        return super.onStartCommand(intent, flags, startId);
    }
    @Override
    protected void onHandleIntent(Intent intent) {

        Util.LogE("Service", "Started");
        ArrayList<MarkImage> images=DatabaseManager.getInstance(getApplicationContext()).getImagesBySyncStatus(0);
        if(images!=null && images.size()>0)
        {
            syncImages(images);
        }

        Util.LogE("Service", "End");
    }


    private void syncImages(ArrayList<MarkImage> images) {
        for (int i = 0; i < images.size(); i++) {
            if (getApplicationContext() != null)// if token expired or invalid try to upload again with new token
            {
                uploadImage(images.get(i), AppUtils.getAuthToken(getApplicationContext()));
            }
        }
    }

    private boolean uploadImage(MarkImage img, String token) {
        if (token == null)
            return false;


        try {
            Util.LogE("Img Path", "" + img.getImagePath());
            if (img.getImagePath() != null && img.getImagePath().length() > 1) {
                File f = new File(img.getImagePath());
                if (f.exists()) {
               /* JSONObject jsonObject = new JSONObject();
                jsonObject.put("guid", img.getGuid());
                jsonObject.put("fileName", img.getGuid() + ".jpg");
                String input = "" + jsonObject.toString();*/

                    String SERVER_URL = Constant.BASE_URL;
//                    int urlIndex=AppUtils.getUrlIndex(getApplicationContext(),-1);
                /*    if(urlIndex>-1 && Constant.IS_LIVE_BUILD)
                    {
                        //  companyContainer.getEditText().setText(getResources().getStringArray(R.array.company_names)[urlIndex]);
                        SERVER_URL=getResources().getStringArray(R.array.company_urls)[urlIndex];
                    }*/

                    //Ion.getDefault(getApplicationContext()).configure().setLogging("UploadLogs", Log.DEBUG);
                    JsonObject jsonResponse = Ion.with(getApplicationContext()).load(SERVER_URL + "api/MarkImage/Upload")
                            // .setHeader("Content-Type","multipart/form-data")

                            .setHeader("Authorization", token)
                            .setMultipartParameter("guid", img.getGuid())
                            .setMultipartFile("file", "image/jpeg", f)

                            .asJsonObject().get();
                    //{"success":false,"result":null,"error":{"code":0,"message":"An internal error occurred during your request!","details":null,"validationErrors":null},"unAuthorizedRequest":false}
                    if (jsonResponse.has("success") && jsonResponse.get("success").getAsBoolean()) {
                        DatabaseManager.getInstance(getApplicationContext()).updateImageStatusByID(img.getId(), 1);
                        Util.LogE("Success", "" + img.getId());

                    }

                    Util.LogE("Response", "" + jsonResponse.toString());
                }

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;





/*

        URI blobUri = URI.create(token.getBaseUrl()+"/"+img.getGuid()+".jpg");
        StorageCredentials storageCredentials=new StorageCredentialsSharedAccessSignature(token.getToken());

        try {
            CloudBlockBlob blob = new CloudBlockBlob(blobUri, storageCredentials);
            if(img.getImagePath()!=null && img.getImagePath().length()>1)
             blob.uploadFromFile(img.getImagePath());

            Util.LogE("Success", "" + blobUri.getPath() + "" + token.getToken());
            DatabaseManager.getInstance(getApplicationContext()).updateImageStatusByID(img.getId(),1);
        }catch (StorageException ex)
        {
            ex.printStackTrace();
            return false;
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
         return true;*/
    }

    private AccessToken authenticateStorageAccess(String authKey)
    {
        AccessToken token=null;
       try {
           Call<WebResponse<AccessToken>> request = WebServiceFactory.getInstance().getStorageAccessToken(authKey);
           token=request.execute().body().getResult();
       }catch (Exception ex){ex.printStackTrace();}

        return token;
    }
    private void startForeGround()
    {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, Constant.Notification_Channel_ID);

        mBuilder.setSmallIcon(R.drawable.ic_stat_ac_unit);
        //  mBuilder.setContentTitle("Data Syncing...");
        mBuilder.setContentText("Syncing Images...");
        mBuilder.setColor( ContextCompat.getColor(this, R.color.colorPrimary));

        mBuilder.setPriority(NotificationCompat.PRIORITY_LOW);


        Intent pintent = new Intent(this, FragmentMainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, pintent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pendingIntent);
        Notification notification = mBuilder.build();

        startForeground(1101,notification);
    }
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = Constant.Notification_Channel_Name;
            String description = Constant.Notification_Channel_Desc;
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel channel = new NotificationChannel(Constant.Notification_Channel_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}
