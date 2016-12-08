package vip.xioix.crab;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVMobilePhoneVerifyCallback;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.RequestMobileCodeCallback;

import vip.xioix.crabbase.base.AbsActivity;
import vip.xioix.crabbase.util.Check;
import vip.xioix.crabbase.util.UIHandler;
import vip.xioix.crabbase.util.validate.DigitLengthRangeValidator;
import vip.xioix.crabbase.util.validate.EmptyValidator;
import vip.xioix.crabbase.util.validate.ValidatorHelper;

public class InputSmsCaptchaActivity extends AbsActivity {
    public static final String KEY_MOBILE = "mobile";
    public static final String KEY_IS_FROM_REGISTER = "isFromRegister";
    private String mMobile;
    private boolean isFromRegister = false;
    private EditText etCode;
    private TextView tvCountDown;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_sms_captcha);
        mMobile = getIntent().getStringExtra(KEY_MOBILE);
        Check.d(mMobile!=null,"lost mobile argument");
        isFromRegister = getIntent().getBooleanExtra(KEY_IS_FROM_REGISTER,false);

        String confirStr = String.format(getString(R.string.registration_activity_confirm_code_details),mMobile);
        ((TextView)findViewById(R.id.confirm_code_details)).setText(confirStr);
        etCode = (EditText) findViewById(R.id.pincode_edittext);
        tvCountDown = (TextView) findViewById(R.id.get_new_code_or_wait);
        if(!isFromRegister)requestSmsCode();
    }


    volatile int countDownSecond = 60;
    Runnable countDown = new Runnable() {
        @Override
        public void run() {
            if(countDownSecond != 0){
                String str = "请稍候  "+"<b><font color=\"#00acc1\">"+(countDownSecond --)+"</font></b>";
                tvCountDown.setText(Html.fromHtml(str));
                UIHandler.postDelayed(this,1000);
                tvCountDown.setOnClickListener(null);
            }

            else {
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
        AVUser.requestMobilePhoneVerifyInBackground(mMobile, new RequestMobileCodeCallback() {
            @Override
            public void done(AVException e) {
                if (e != null) {
                    showError(e.getMessage());
                }
            }
        });

        UIHandler.post(countDown);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        countDownSecond = 0;
    }

    public void onNext(View view) {
        String smsCode = etCode.getText().toString();
        if(checkInput()){
            verifyPhone(mMobile,smsCode);
        }
    }

    private void verifyPhone(String mMobile, String smsCode) {
        AVUser.verifyMobilePhoneInBackground(smsCode, new AVMobilePhoneVerifyCallback() {
            @Override
            public void done(AVException e) {
                if(e != null){
                    onLoginFail(e);
                    return;
                }

                onVerifySuccess();
            }
        });
    }

    private void onVerifySuccess(){
        startActivity(new Intent(this,YourNameActivity.class));
        finish();
    }

    private void onLoginFail(AVException e){
        showError(e.getLocalizedMessage());
    }



    private boolean checkInput() {
        return ValidatorHelper.from(etCode)
                .set(new EmptyValidator(getString(R.string.registration_activity_confirm_code)))
                .and(new DigitLengthRangeValidator(getString(R.string.error_input_smscode_length),4,8))
                .check(true);
    }
}
