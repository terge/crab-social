package vip.xioix.crabbase.util;

import android.content.Context;
import android.support.v4.content.ContextCompat;

/**
 * Created by terge on 16-12-5.
 */

public class PermisstionUtil {
    public static boolean checkRecordPermision(Context context,String permssion) {
        int permissionCheck = ContextCompat.checkSelfPermission(context,permssion);

        boolean result =  permissionCheck == android.content.pm.PackageManager.PERMISSION_GRANTED;
        return result;
    }
}
