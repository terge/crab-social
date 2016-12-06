package vip.xioix.crab;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;

import org.apache.commons.lang.StringUtils;

import vip.xioix.crabbase.base.AbsActivity;
import vip.xioix.crabbase.util.Check;
import vip.xioix.crabbase.util.UIHandler;

public class InputSmsCaptchaActivity extends AbsActivity {
    public static final String KEY_MOBILE = "mobile";
    private String mMobile;
    private EditText etCode;
    private TextView tvCountDown;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_sms_captcha);
        mMobile = getIntent().getStringExtra(KEY_MOBILE);
        Check.d(mMobile!=null,"lost mobile argument");

        String confirStr = String.format(getString(R.string.registration_activity_confirm_code_details),mMobile);
        ((TextView)findViewById(R.id.confirm_code_details)).setText(confirStr);
        etCode = (EditText) findViewById(R.id.pincode_edittext);
        tvCountDown = (TextView) findViewById(R.id.get_new_code_or_wait);
        requestSmsCode();
    }


    volatile int countDownSecond = 60;
    Runnable countDown = new Runnable() {
        @Override
        public void run() {
            if(countDownSecond != 0){
                Log.d(TAG, "run: 1");
                String str = "请稍候  "+"<b><font color=\"#00acc1\">"+(countDownSecond --)+"</font></b>";
                tvCountDown.setText(Html.fromHtml(str));
                UIHandler.postDelayed(this,1000);
                tvCountDown.setOnClickListener(null);
            }

            else {
                Log.d(TAG, "run: 2");
                tvCountDown.setText(Html.fromHtml("<b><font color=\"#6200ea\">获取新的验证码</font></b>"));
                tvCountDown.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        requestSmsCode();
                    }
                });
            }
        }
    };

    private void requestSmsCode(){
        Log.d(TAG, "requestSmsCode: ");
        countDownSecond = 60;
        AVUser.requestLoginSmsCodeInBackground(mMobile,null);
        UIHandler.post(countDown);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        countDownSecond = 0;
    }

    public void onNext(View view) {
        String smsCode = etCode.getText().toString();
        if(checkInput(smsCode)){
            signUpOrLogin(mMobile,smsCode);
        }
    }

    private void signUpOrLogin(String mMobile, String smsCode) {
        AVUser.signUpOrLoginByMobilePhoneInBackground(mMobile, smsCode, new LogInCallback<AVUser>() {
            @Override
            public void done(AVUser avUser, AVException e) {
                if(e != null){
                    onLoginFail(e);
                    return;
                }

                onLoginSuccess(avUser);
            }
        });

    }

    private void onLoginSuccess(AVUser avUser){
        startActivity(new Intent(this,MyProfileActivity.class));
        finish();
    }

    private void onLoginFail(AVException e){
        showError(e.getLocalizedMessage());
    }



    private boolean checkInput(String smsCode) {
        String errorHint = null;
        if(StringUtils.isEmpty(smsCode)){
            errorHint = getString(R.string.registration_activity_confirm_code);
        }
        else{
            int length = smsCode.length();
            if(length <4 || length > 8){
                errorHint = getString(R.string.registration_activity_confirm_code);
            }
        }

        boolean result = errorHint == null;
        if(!result){
            etCode.requestFocus();
            etCode.setError(errorHint);
        }
        return result;

    }
}
