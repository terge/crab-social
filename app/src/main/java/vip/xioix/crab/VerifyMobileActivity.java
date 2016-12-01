package vip.xioix.crab;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVMobilePhoneVerifyCallback;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.RequestMobileCodeCallback;

import vip.xioix.crabbase.base.AbsActivity;
import vip.xioix.crabbase.util.UIHandler;


public class VerifyMobileActivity extends AbsActivity {

    public static final String KEY_MOBILE = "mobile";
    String mMobile = null;
    EditText etCaptcha;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone);
        mMobile = getIntent().getStringExtra(KEY_MOBILE);
        if(mMobile == null){
//            toast("未输入手机号");
            finish();
        }
        ((TextView)findViewById(R.id.tv_smscaptcha_mobile)).setText(mMobile);
        etCaptcha = (EditText) findViewById(R.id.et_verify_smscode);
        requestSmsCaptcha(mMobile);
    }


    public void onConfirmClick(View view){
        String captchaStr = etCaptcha.getText().toString().trim();
        if(checkInput(captchaStr)){
            verifyCaptcha(captchaStr);
        }
    }

    private boolean checkInput(String captcha) {
        boolean result =  captcha.length()>3;
        if(!result){
            etCaptcha.requestFocus();
            etCaptcha.setError("请输入验证码");
        }
        return result;
    }

    private void verifyCaptcha(String captcha) {
        AVUser.verifyMobilePhoneInBackground(captcha,new AVMobilePhoneVerifyCallback(){
            @Override
            public void done(AVException e) {
                if(e==null){
                    setResult(RESULT_OK);
                }else {
                    showError(e.getMessage());
                }
            }
        });
    }


    private void requestSmsCaptcha(String mobile){
        if(App.DEBUG){
            Log.e(TAG, "requestSmsCaptcha: mock");
            UIHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    setResult(RESULT_OK);
                }
            },2000);
            return;
        }
        AVOSCloud.requestSMSCodeInBackground(mobile, "螃蟹社交", "手机号验证", 10, new RequestMobileCodeCallback() {
            @Override
            public void done(AVException e) {
                if (e != null) {
                    showError(e.getMessage());
                }
            }
        });
    }
}

