package vip.xioix.crabbase.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

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
        setUpStatusBarColor();

        mActivity = this;
        mEventBus = new EventBus(TAG);
        mEventBus.register(this);
    }

    private void setUpStatusBarColor() {
        int color = requireStatuBarColor();
        if(color == -1) return;
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //设置状态栏颜色
        window.setStatusBarColor(color);
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

    protected int requireStatuBarColor(){
        return -1;
    }

    private ProgressDialog mLoading =  null;
    protected void showProgress(String content){
        showProgress(null,content);
    }

    protected void showProgress(String title,String content){
        if(mLoading != null && mLoading.isShowing()){
            mLoading.dismiss();
        }
        mLoading = ProgressDialog.show(this,title,content);
    }
    protected void dissmissProgress(){
        if(mLoading != null && mLoading.isShowing()){
            mLoading.dismiss();
        }
    }

}
