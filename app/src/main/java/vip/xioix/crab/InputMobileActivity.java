package vip.xioix.crab;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;

import org.apache.commons.lang.StringUtils;

import java.util.List;

import vip.xioix.crabbase.base.AbsActivity;
import vip.xioix.crabbase.util.Validator;

public class InputMobileActivity extends AbsActivity {
    private EditText etMobile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_mobile);
        etMobile = (EditText) findViewById(R.id.phone_number);
    }

    public void onNext(View view) {
        String mobile = etMobile.getText().toString();
        if (checkMobile(mobile)) {
//            queryUserInfo(mobile);
            Intent intent = new Intent(this,InputSmsCaptchaActivity.class);
            intent.putExtra(InputSmsCaptchaActivity.KEY_MOBILE,mobile);
            startActivity(intent);
        }
    }

    private void queryUserInfo(final String mobile) {
        AVQuery<AVUser> query = AVUser.getQuery();
        query.whereEqualTo("mobilePhoneNumber", mobile);
        showProgress(getString(R.string.wait_when_connecting));

        query.findInBackground(new FindCallback<AVUser>() {
            @Override
            public void done(List<AVUser> list, AVException e) {
                dissmissProgress();
                if (e != null) {
                    showError(e.getLocalizedMessage());
                    return;
                }

                boolean isEmpty = list == null || list.size() == 0;
                AVUser user = isEmpty ? null : list.get(0);
                //新用户
                if (user == null) {
                    onNewUserLogin(mobile);
                }
                //老用户
                else {
                    onOldUserLogin(user);
                }
            }
        });
    }

    private void onOldUserLogin(AVUser user) {
        startActivity(new Intent(this,MyProfileActivity.class));
    }

    private void onNewUserLogin(String mobile) {
        startActivity(new Intent(this,InputSmsCaptchaActivity.class));
    }

    private boolean checkMobile(String mobile) {
        boolean result = Validator.isPhone(mobile);

        if (!result) {
            String hint = StringUtils.isEmpty(mobile) ? getString(R.string.error_field_required) : getString(R.string.error_invalid_mobile);
            etMobile.requestFocus();
            etMobile.setError(hint);
        }

        return result;
    }

}
