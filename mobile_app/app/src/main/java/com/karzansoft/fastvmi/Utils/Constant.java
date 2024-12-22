package com.karzansoft.fastvmi.Utils;

/**
 * Created by Yasir on 2/17/2016.
 */
public class Constant {

    public static final int CHECKOUT = 1;
    public static final int CHECKIN = 2;

    public static final int RA_CHECKOUT = 1;
    public static final int RA_CHECKIN = 2;
    public static final int REPLACEMENT_CHECKIN = 3;
    public static final int REPLACEMENT_CHECKOUT = 4;
    public static final int NRM_CHECKOUT = 5;
    public static final int NRM_CHECKIN = 6;
    public static final int GARAGE_CHECKOUT = 7;
    public static final int GARAGE_CHECKIN = 8;
    public static final int VehicleInspection = 9; //1

    public static final int MARKER_SYMBOL_TICK = 1;
    public static final int MARKER_SYMBOL_CROSS = 2;

    public static final int SYMBOL_MARKER_SMALL_DENT = 1;
    public static final int SYMBOL_MARKER_LARGE_DENT = 2;
    public static final int SYMBOL_MARKER_SCRATCH = 4;
    public static final int SYMBOL_MARKER_SCRATCH_THIN = 3;
    public static final int SYMBOL_MARKER_CRACK = 6;
    public static final int ACCESSORIES_OUT = 1;
    public static final int ACCESSORIES_IN = 2;

    public static final String CURRENT_MODE = "current_mode";
    public static final String APP_STATE = "app_state";
    //public static final int VERIFICATION_CODE_MESSAGE_COUNTS = 3;

//    public static final String BASE_URL = "http://192.168.2.22:8095/";// Local
    public static final String BASE_URL = "http://2d74dbb43e0f.ngrok.io/";// Live

    //public static final String BASE_URL="http://202.166.161.157:8090/"; //temporary url
    //public static final  String BASE_URL="https://speedcloudapptest.azurewebsites.net/";
    public static final String PREF_FILE = "mCC_pref";
    public static final String PREF_KEY_AUTH = "auth";
    public static final String PREF_KEY_USER_ID = "login_user";
    public static final String PREF_KEY_USER_NAME = "user_name";
    //    public static final String PREF_KEY_TENANT_NAME = "tenant_name";
    public static final String PREF_KEY_lOGIN_INFO = "login_info";
    public static final String PREF_KEY_MOV = "movementinfo";
    public static final String PREF_KEY_DOC_TYPE = "document_type";
    public static final String PREF_KEY_DOC_PASS = "document_passport";
    public static final String PREF_KEY_DOC_NID = "document_nationalId";
    public static final String PREF_KEY_DOC_IMAGES = "document_images";
    public static final String PREF_KEY_REMEMBER_ME = "remember_me";
    public static final String PREF_KEY_APP_SETTINGS = "app_settings";
    public static final String PREF_KEY_APP_LANGUAGES_LIST = "app_language_list";
    public static final String PREF_KEY_APP_CURRENT_LANG = "app_Current_language";
    public static final String PREF_KEY_APP_LANG_STRINGS = "app_language_strings";
    public static final String GOOGLE_MAP_API_KEY = "";//AIzaSyD9rydTteeN8WCQvcJbZC4B-o4HnkjWJFw
    /////////LIVE//////////
    public static final String APP_ID = "1:34719083064:android:d3022c5fc6d04a0e";// Production
    public static final String AGENT_ID = "Dt6BGtSj0tVd1AIOZWOa7aft6Du2";

    //public static final String AGENT_ID="OzcoNxU21VOqDPX9oNXNBnziHBi2";
    /////////TEST//////////
   /* public static final String APP_ID = "1:34719083064:android:5e31c493014d4257";//Stage
    public static final String AGENT_ID ="FKs7O8igsFVkRAXDRA0GzsGCnI82";*/

    public static final String Notification_Channel_Name = "DataSyncing";
    public static final String Notification_Channel_Desc = "Image syncing";
    public static final String Notification_Channel_ID = "channel_001";
}
