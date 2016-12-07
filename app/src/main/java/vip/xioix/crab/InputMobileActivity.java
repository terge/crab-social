package vip.xioix.crab;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.SignUpCallback;
import com.google.common.eventbus.Subscribe;

import vip.xioix.crabbase.base.AbsActivity;
import vip.xioix.crabbase.util.validate.DigitLengthRangeValidator;
import vip.xioix.crabbase.util.validate.EmptyValidator;
import vip.xioix.crabbase.util.validate.PhoneValidator;
import vip.xioix.crabbase.util.validate.ValidatorHelper;

public class InputMobileActivity extends AbsActivity {
    private EditText etMobile,etPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_mobile);
        etMobile = (EditText) findViewById(R.id.phone_number);
        etPassword = (EditText) findViewById(R.id.et_login_password);
    }

    public void onNext(View view) {
        if (checkMobile()) {
            String mobile = etMobile.getText().toString();
            String password = etPassword.getText().toString();
            tryLogin(mobile,password);
//            queryUserInfo(mobile);
            Intent intent = new Intent(this,InputSmsCaptchaActivity.class);
            intent.putExtra(InputSmsCaptchaActivity.KEY_MOBILE,mobile);
            startActivity(intent);
        }
    }

    private void tryLogin(final String mobile, final String password) {
        AVUser.loginByMobilePhoneNumberInBackground(mobile, password, new LogInCallback<AVUser>() {
            @Override
            public void done(AVUser avUser, AVException e) {
                if(e == null){
                    //onLoginSuccess
                    mEventBus.post(avUser);
                }else {
                    if (e.getCode() == AVException.USER_DOESNOT_EXIST) {
                        register(mobile, password);
                    }else if(e.getCode() == AVException.USER_MOBILEPHONE_NOT_VERIFIED){
                        toVerifyMobile(mobile);
                    }else{
                        showError(e.getMessage());
                    }
                }
            }
        });
    }

    private void toVerifyMobile(String mobile){
        Log.d(TAG, "toVerifyMobile: ");
        Intent intent = new Intent(this, InputSmsCaptchaActivity.class);
        intent.putExtra(VerifyMobileActivity.KEY_MOBILE, mobile);
        startActivity(intent);
        finish();
    }


    @Subscribe
    private void onLoginSuccess(AVUser user){
        Log.d(TAG, "onLoginSuccess: ");
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }

    @Subscribe
    private void onRegisterSuccess(String mobile) {
        Log.d(TAG, "onRegisterSuccess: ");
        toVerifyMobile(mobile);
    }


    private void register(final String mMobile, String mPassword) {
        Log.d(TAG, "register: ");
        AVUser user = new AVUser();
        user.setUsername(mMobile);
        user.setMobilePhoneNumber(mMobile);
        user.setPassword(mPassword);
        user.put("channel","582c3513c4c9710054368976");
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(AVException e) {
                if(e == null){
                    //onRegisterSuccess
                    mEventBus.post(mMobile);
                }
            }
        });
    }



    private boolean checkMobile() {
        boolean isMobileInputOK = ValidatorHelper.from(etMobile)
                .set(new EmptyValidator(getString(R.string.error_field_required)))
                .and(new PhoneValidator(getString(R.string.error_invalid_mobile)))
                .check(true);
        if(!isMobileInputOK)return false;

        return ValidatorHelper.from(etPassword)
                .set(new EmptyValidator(getString(R.string.error_field_required)))
                .and(new DigitLengthRangeValidator(getString(R.string.error_password_length),6,20))
                .check(true);



    }

}
