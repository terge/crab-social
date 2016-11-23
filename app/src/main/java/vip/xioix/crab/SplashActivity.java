package vip.xioix.crab;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class SplashActivity extends AppCompatActivity implements Animation.AnimationListener {

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
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
