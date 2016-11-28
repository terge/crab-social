package vip.xioix.crab;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by terge on 16-11-28.
 */

public class RemoteConfig {
    private static final String FILE_NAME = "remote.config";
    public static class Key{
        public static final String AVOS_APP_ID = "avosAppId";
        public static final String AVOS_APP_KEY = "avosAppKey";
    }

    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    public RemoteConfig(){
        sp = App.appContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        editor = sp.edit();
    }

    public void set(String key,String value){
        editor.putString(key,value).commit();
    }

    public String get(String key){
        return sp.getString(key,null);
    }

    public String get(String key,String defaultVaule){
        return sp.getString(key,defaultVaule);
    }

}
