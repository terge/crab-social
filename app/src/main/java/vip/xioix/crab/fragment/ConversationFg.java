package vip.xioix.crab.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import vip.xioix.crab.R;

/**
 * Created by terge on 16-11-23.
 */

public class ConversationFg extends AbsFg {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fg_conversation,null);
    }
}
