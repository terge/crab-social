package vip.xioix.crab;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import org.apache.commons.lang.StringUtils;

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
        if(checkMobile(mobile)){
            queryUserInfo(mobile);
        }
    }

    private void queryUserInfo(String mobile) {
    }

    private boolean checkMobile(String mobile) {
        boolean result = Validator.isPhone(mobile);

        if(!result){
            String hint = StringUtils.isEmpty(mobile)?getString(R.string.error_field_required):getString(R.string.error_invalid_mobile);
            etMobile.requestFocus();
            etMobile.setError(hint);
        }

        return result;
    }
}
