package vip.xioix.crabbase.base;

import android.app.Fragment;
import android.content.Context;

import com.google.common.eventbus.EventBus;

import vip.xioix.crabbase.base.AbsActivity;

/**
 * Created by terge on 16-11-23.
 */

public class AbsFg extends Fragment{
    protected final String TAG = getClass().getSimpleName();
    protected  Context mContext;
    protected EventBus mEventBus;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        mEventBus = new EventBus(TAG);
        mEventBus.register(this);
    }

    protected AbsActivity getAbsActivity(){
        return (AbsActivity) mContext;
    }
}
