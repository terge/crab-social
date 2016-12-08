package vip.xioix.crab;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import cn.leancloud.chatkit.activity.LCIMContactFragment;

public class ContactActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        showContactFragment();
    }

    private void showContactFragment() {
        Fragment fg = new LCIMContactFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.activity_contact, fg, fg.getClass().getSimpleName())
                .commit();
    }
}
