package vip.xioix.crabbase.base;

import android.app.Activity;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.avos.avoscloud.AVUser;
import com.google.common.eventbus.EventBus;

import vip.xioix.crabbase.util.UIHandler;

/**
 * Created by terge on 16-11-25.
 */

public class AbsActivity extends AppCompatActivity {
    protected final String TAG = getClass().getSimpleName();
    protected EventBus mEventBus;
    protected Activity mActivity;
    protected AVUser curUser = AVUser.getCurrentUser();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        mEventBus = new EventBus(TAG);
        mEventBus.register(this);
    }

    public void showError(final String msg){
        boolean isInMainThread = Looper.myLooper() == getMainLooper();
        if(isInMainThread){
            View view = findViewById(android.R.id.content);
            Snackbar.make(view,msg,Snackbar.LENGTH_SHORT).show();
        }else {
            UIHandler.post(new Runnable() {
                @Override
                public void run() {
                    Snackbar.make(getWindow().getDecorView(),msg,Snackbar.LENGTH_SHORT).show();
                }
            });
        }

    }
}
