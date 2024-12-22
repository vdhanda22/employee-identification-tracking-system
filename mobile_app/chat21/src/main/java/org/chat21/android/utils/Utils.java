package org.chat21.android.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.chat21.android.BuildConfig;
import org.chat21.android.core.users.models.ChatUser;
import org.chat21.android.core.users.models.IChatUser;

/**
 * Created by Yasir on 8/5/2018.
 */

public class Utils {

    public static final String PREF_KEY_ID = "key_id";
    public static final String PREF_KEY_NAME = "key_name";
    public static final String PREF_FILE ="main_file";
    public static final String PREF_KEY_UNREAD_CHAT ="key_unread";
    public static final String PREF_KEY_CHAT_BUILD ="key_chat_build";
    public static final String PREF_KEY_LANGUAGE_CODE ="key_lang_code";
    public static final String PREF_KEY_TRANSLATION ="key_is_translation";
    public static final String PREF_KEY_TRANSLATION_SETTING ="key_is_translation_setting";
    public static final String PREF_KEY_TRANSLATION_LANGUAGE_SETTING ="key_is_language_setting";
    static final boolean IS_LOG_ENABLED = false;

    public static void setUser(Context context, IChatUser iChatUser)
    {
        SharedPreferences sharedpreferences = context.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        if (iChatUser != null && iChatUser.getId() != null && iChatUser.getFullName()!=null) {
            editor.putString(PREF_KEY_ID, iChatUser.getId());
            editor.putString(PREF_KEY_NAME,iChatUser.getFullName());
            editor.commit();
        }else {
            editor.putString(PREF_KEY_ID, "");
            editor.putString(PREF_KEY_NAME,"");
            editor.commit();
        }
    }

    public static IChatUser getUser(Context context)
    {
        SharedPreferences sharedpreferences = context.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
        String id=sharedpreferences.getString(PREF_KEY_ID, "");
        String name=sharedpreferences.getString(PREF_KEY_NAME, "");
       IChatUser iChatUser = null;
        if (id.length() > 1) {
          iChatUser = new ChatUser(id,name);
        }

        return iChatUser;
    }

    public static void setUnreadChat(boolean isUnreadChat,Context context)
    {
        SharedPreferences sharedpreferences = context.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean(PREF_KEY_UNREAD_CHAT,isUnreadChat);
        editor.commit();

    }

    public static boolean isLiveChatBuild(Context context)
    {
        SharedPreferences sharedpreferences = context.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
        return sharedpreferences.getBoolean(PREF_KEY_CHAT_BUILD,false);
    }


    public static void setLiveChatBuild(boolean isLiveChatBuild,Context context)
    {
        SharedPreferences sharedpreferences = context.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean(PREF_KEY_CHAT_BUILD,isLiveChatBuild);
        editor.commit();

    }

    public static boolean isUnreadChat(Context context)
    {
        SharedPreferences sharedpreferences = context.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
        return sharedpreferences.getBoolean(PREF_KEY_UNREAD_CHAT,false);
    }

    public static void setTranslationEnable(boolean isEnabled,Context context)
    {
        SharedPreferences sharedpreferences = context.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean(PREF_KEY_TRANSLATION,isEnabled);
        editor.commit();

    }

    public static boolean isTranslationEnabled(Context context)
    {
        if (context == null)
            return false;
        SharedPreferences sharedpreferences = context.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
        return sharedpreferences.getBoolean(PREF_KEY_TRANSLATION,true);
    }

    public static boolean isTranslationSetting(Context context)
    {
        SharedPreferences sharedpreferences = context.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
        return sharedpreferences.getBoolean(PREF_KEY_TRANSLATION_SETTING,true);
    }

    public static void setTranslationSetting(boolean isEnabled,Context context)
    {
        SharedPreferences sharedpreferences = context.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean(PREF_KEY_TRANSLATION_SETTING,isEnabled);
        editor.commit();

    }

    public static boolean isLanguageSelected(Context context)
    {
        SharedPreferences sharedpreferences = context.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
        return sharedpreferences.getBoolean(PREF_KEY_TRANSLATION_LANGUAGE_SETTING,false);
    }

    public static void setLanguageSelected(boolean isSelected,Context context)
    {
        SharedPreferences sharedpreferences = context.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean(PREF_KEY_TRANSLATION_LANGUAGE_SETTING,isSelected);
        editor.commit();

    }


    public static void setLanguageCode(String code,Context context)
    {
        if (code !=null && code.length()>0){
        SharedPreferences sharedpreferences = context.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(PREF_KEY_LANGUAGE_CODE,code);
        editor.commit();
        }

    }

    public static String getLanguageCode(Context context,String def)
    {
        SharedPreferences sharedpreferences = context.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
        return sharedpreferences.getString(PREF_KEY_LANGUAGE_CODE,def);
    }

    public static void e(String tag,String message)
    {
        if (IS_LOG_ENABLED)
        {
            Log.e(tag,message);
        }
    }

    public static void d(String tag,String message)
    {
        if (IS_LOG_ENABLED)
        {
            Log.d(tag,message);
        }
    }
    public static void i(String tag,String message)
    {
        if (IS_LOG_ENABLED)
        {
            Log.i(tag,message);
        }
    }
    public static void v(String tag,String message)
    {
        if (IS_LOG_ENABLED)
        {
            Log.v(tag,message);
        }
    }
    public static void w(String tag,String message)
    {
        if (IS_LOG_ENABLED)
        {
            Log.w(tag,message);
        }
    }

    public static void w(String tag,String message, Throwable tr)
    {
        if (IS_LOG_ENABLED)
        {
            Log.w(tag,message,tr);
        }
    }

    public static void e(String tag, String msg, Throwable tr)
    {
        if(IS_LOG_ENABLED)
        {
            Log.e(tag,msg,tr);
        }
    }
}
