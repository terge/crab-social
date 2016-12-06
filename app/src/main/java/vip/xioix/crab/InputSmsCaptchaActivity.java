package vip.xioix.crab;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import vip.xioix.crabbase.util.Check;

public class InputSmsCaptchaActivity extends AppCompatActivity {
    public static final String KEY_MOBILE = "mobile";
    private String mMobile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_sms_captcha);
        mMobile = getIntent().getStringExtra(KEY_MOBILE);
        Check.d(mMobile!=null,"lost mobile argument");

        String confirStr = String.format(getString(R.string.registration_activity_confirm_code_details),mMobile);
        ((TextView)findViewById(R.id.confirm_code_details)).setText(confirStr);

    }
}
