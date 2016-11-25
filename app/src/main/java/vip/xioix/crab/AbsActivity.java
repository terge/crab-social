package vip.xioix.crab;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.google.common.eventbus.EventBus;

/**
 * Created by terge on 16-11-25.
 */

public class AbsActivity extends AppCompatActivity {
    protected final String TAG = getClass().getSimpleName();
    protected EventBus mEventBus;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mEventBus = new EventBus(TAG);
        mEventBus.register(this);
    }
}
