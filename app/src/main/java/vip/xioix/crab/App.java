package vip.xioix.crab;

import android.app.Application;
import android.content.Intent;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVOnlineConfigureListener;

import org.json.JSONObject;

import java.util.Iterator;

import cn.leancloud.chatkit.LCChatKit;
import cn.leancloud.chatkit.UserProvider;
import vip.xioix.crabbase.base.RemoteConfig;

/**
 * Created by terge on 16-11-23.
 */

public class App extends Application{

    public static final boolean DEBUG  = true;
    public static App appContext;
    private static final String AVOS_APP_ID = "tgIKa9aNaLs975LMgMIOvt2A-gzGzoHsz";
    private static final String AVOS_APP_KEY = "dsYRn3WhJdxFaDiV0HbHYHn3";

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = this;
        RemoteConfig remoteConfig = new RemoteConfig(this);
        String appId = remoteConfig.get(RemoteConfig.Key.AVOS_APP_ID,AVOS_APP_ID);
        String appKey = remoteConfig.get(RemoteConfig.Key.AVOS_APP_KEY,AVOS_APP_KEY);

        LCChatKit.getInstance().setProfileProvider(new UserProvider());
        LCChatKit.getInstance().init(appContext,appId,appKey);
//        AVOSCloud.initialize(this,appId,appKey);
        AVOSCloud.setDebugLogEnabled(true);
        refreshRemoteConfig();
    }

    private void refreshRemoteConfig() {
        AVAnalytics.setOnlineConfigureListener(new AVOnlineConfigureListener() {
            @Override
            public void onDataReceived(JSONObject jsonObject) {
                if(jsonObject == null) return;
                Iterator<String> iterator = jsonObject.keys();
                String key ;
                RemoteConfig remoteConfig = new RemoteConfig(App.appContext);
                while(iterator.hasNext()){
                    key = iterator.next();
                    remoteConfig.set(key,jsonObject.optString(key));
                }
            }
        });
    }

    public void logout(){
        Intent intent = new Intent(this,LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


}
