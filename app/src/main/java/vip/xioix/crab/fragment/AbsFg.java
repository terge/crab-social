package vip.xioix.crab.fragment;

import android.app.Fragment;
import android.content.Context;

/**
 * Created by terge on 16-11-23.
 */

public class AbsFg extends Fragment{
    protected  Context mContext;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }
}
