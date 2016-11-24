package vip.xioix.crab.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import vip.xioix.crab.R;

/**
 * Created by terge on 16-11-23.
 */

public class ConversationFg extends AbsFg {

    private RecyclerView rvConversation;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fg_conversation,null);
        rvConversation = (RecyclerView) view.findViewById(R.id.rv_conversation);
        return view;
    }

}
