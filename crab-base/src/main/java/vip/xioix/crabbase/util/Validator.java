package vip.xioix.crabbase.util;

import android.util.Patterns;

/**
 * Created by terge on 16-11-23.
 */

public class Validator {
    public static boolean isPhone(String phone){
        return Patterns.PHONE.matcher(phone).matches();
    }
}
