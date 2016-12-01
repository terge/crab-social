package vip.xioix.crab;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.avos.avoscloud.AVUser;

import vip.xioix.crabbase.base.AbsActivity;

public class SplashActivity extends AbsActivity implements Animation.AnimationListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        playLogoAnimation();
    }


    private void playLogoAnimation() {
        View logoLayout = findViewById(R.id.layout_logo);
        Animation anima = AnimationUtils.loadAnimation(this, R.anim.splash_logo_alpha);
        anima.setInterpolator(new AccelerateDecelerateInterpolator());
        anima.setAnimationListener(this);
        logoLayout.startAnimation(anima);
    }


    @Override
    public void onAnimationEnd(Animation animation) {
        AVUser user = AVUser.getCurrentUser();
        onAutoLoginResult(user != null);
    }


    private void onAutoLoginResult(boolean isSuccess){
        Class<? extends Activity> toActivity = isSuccess ? MainActivity.class : LoginActivity.class;
        startActivity(new Intent(SplashActivity.this, toActivity));
        finish();
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }
    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
