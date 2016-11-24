package vip.xioix.crab;

import android.app.Application;

import com.avos.avoscloud.AVOSCloud;

/**
 * Created by terge on 16-11-23.
 */

public class App extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        AVOSCloud.initialize(this,"tgIKa9aNaLs975LMgMIOvt2A-gzGzoHsz","dsYRn3WhJdxFaDiV0HbHYHn3");
    }
}
