package vip.xioix.crab;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.View;

import vip.xioix.crabbase.base.AbsActivity;


public class RequirePermissionActivity extends AbsActivity {
    private String[] permissions = new String[]{
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);
    }

    @Override
    protected int requireStatuBarColor() {
        return Color.WHITE;
    }


    private int REQUEST_PERMISSION = 10086;
    public void onNext(View view) {
        ActivityCompat.requestPermissions(
                this,
                permissions,
                REQUEST_PERMISSION
        );
    }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_PERMISSION){
            for(int result:grantResults){
                //只要有一个不同意接取消
                if(result != PackageManager.PERMISSION_GRANTED){
                    finish();
                    return;
                }
            }
            //全部允许了
            startActivity(new Intent(this,RegisterActivity.class));

        }

    }
}
