package vip.xioix.crab;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.SaveCallback;

import org.apache.commons.lang.StringUtils;

import vip.xioix.crabbase.base.AbsActivity;

public class YourNameActivity extends AbsActivity {
    EditText etName;
    Button btnNext;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_name);
        etName = (EditText) findViewById(R.id.et_profile_name);
        btnNext = (Button) findViewById(R.id.btn_profile_next);
        fab = (FloatingActionButton) findViewById(R.id.fab_profile_capture);

        String userName = curUser == null?null:curUser.getUsername();
        etName.setText(userName);

    }


    public void onCaptureCLick(View view) {

    }

    public void onNext(View view) {
        String userName = etName.getText().toString();
        if(checkInput(userName)){
            curUser.setUsername(userName);
//            curUser.put();
            curUser.saveInBackground(new SaveCallback() {
                @Override
                public void done(AVException e) {
                    onSaveResule(e);
                }
            });
        }
    }

    private boolean checkInput(String userName) {
        boolean result = !StringUtils.isEmpty(userName);
        if(!result){
            etName.requestFocus();
            etName.setError(getString(R.string.your_name_text_hint));
        }
        return result;
    }

    private void onSaveResule(AVException e) {
        if(e != null){
            showError(e.getLocalizedMessage());
            return;
        }
        startActivity(new Intent(this,MainActivity.class));
    }
}
