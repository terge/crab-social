package vip.xioix.crab;

import android.Manifest;
import android.content.Context;

import vip.xioix.crabbase.util.PermisstionUtil;

/**
 * Created by terge on 16-12-5.
 */

public class PermissionChecker {
    private Context mContext;
    public PermissionChecker(Context context){
        mContext = context;
    }

    public boolean hasContactPer(){
        return PermisstionUtil.checkRecordPermision(mContext, Manifest.permission.READ_CONTACTS);
    }

    public boolean hasRecordPer(){
        return PermisstionUtil.checkRecordPermision(mContext,Manifest.permission.RECORD_AUDIO);
    }

    public boolean hasRWStoragyPer(){
        return PermisstionUtil.checkRecordPermision(mContext,Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

//    public boolean hasMediaPer(){
//        return PermisstionUtil.checkRecordPermision(mContext,Manifest.permission.CAMERA);
//    }

    
}
