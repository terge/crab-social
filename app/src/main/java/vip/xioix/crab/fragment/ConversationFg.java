package vip.xioix.crab.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.callback.AVIMConversationQueryCallback;
import com.google.common.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import vip.xioix.crab.R;
import vip.xioix.crab.biz.ConversationBiz;

/**
 * Created by terge on 16-11-23.
 */

public class ConversationFg extends AbsFg {

    private RecyclerView rvConversation;
    private List<ConversationInfo> conversationList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fetConversationData();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fg_conversation, null);
        rvConversation = (RecyclerView) view.findViewById(R.id.rv_conversation);
        return view;
    }

    @Subscribe
    private void fillRecyclerView(List<ConversationInfo> conversationInfos){
        Log.d(TAG, "fillRecyclerView: ");
        Adapter mAdapter = new Adapter(conversationInfos);
        rvConversation.setAdapter(mAdapter);
    }

   

    private void fetConversationData() {
        Log.d(TAG, "getConversationData:");
        Observable.create(
                new Observable.OnSubscribe<List<AVIMConversation>>() {
                    @Override
                    public void call(final Subscriber<? super List<AVIMConversation>> subscriber) {
                        ConversationBiz.getInstance().getConversationQuery().findInBackground(new AVIMConversationQueryCallback() {
                            @Override
                            public void done(List<AVIMConversation> list, AVIMException e) {
                                subscriber.onNext(list);
                            }
                        });
                    }
                })
                .observeOn(Schedulers.computation())
                .subscribe(new Action1<List<AVIMConversation>>() {
                    @Override
                    public void call(List<AVIMConversation> converList) {
                        initConversationList(converList);
                    }
                });
    }

    private void initConversationList(List<AVIMConversation> avimConList){
        Log.d(TAG, "initConversationList: ");
        Observable.from(avimConList)
                //getLastMessage
                .map(new Func1<AVIMConversation, ConversationInfo>() {
                    @Override
                    public ConversationInfo call(AVIMConversation avimConversation) {
                        AVIMMessage avimMessage = avimConversation.getLastMessage();
                        ConversationInfo info = new ConversationInfo();
                        info.id = avimMessage.getConversationId();
                        info.memberIds = avimConversation.getMembers().toArray(info.memberIds);
                        info.title = avimMessage.getFrom();
                        info.content = avimMessage.getContent();
                        info.time = "" + avimMessage.getTimestamp();
                        return info;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ConversationInfo>() {
                    @Override
                    public void onCompleted() {
                        // TODO: 16-11-24 数据准备完毕，可以填充界面
                        mEventBus.post(conversationList);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ConversationInfo conversationInfo) {
                        conversationList.add(conversationInfo);
                    }
                });
    }

    
    

    class Adapter extends RecyclerView.Adapter {
        List<ConversationInfo> mData;
        LayoutInflater inflater = LayoutInflater.from(mContext);

        Adapter(List<ConversationInfo> data) {
            mData = data;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(inflater.inflate(R.layout.item_conversation, null));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ConversationInfo item = getItem(position);
            ViewHolder viewHolder = (ViewHolder) holder;

            viewHolder.avatar.setBackground(item.avatar);
            viewHolder.redDot.setVisibility(item.unReadMsgCount > 0 ? View.VISIBLE : View.GONE);
            viewHolder.tvTime.setText(item.time);
            viewHolder.tvTitle.setText(item.title);
            viewHolder.tvContent.setText(item.content);
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }

        private ConversationInfo getItem(int posiontion) {
            return mData == null ? null : mData.get(posiontion);
        }

    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView avatar;
        TextView tvTitle;
        TextView tvContent;
        TextView tvTime;
        View redDot;

        public ViewHolder(View itemView) {
            super(itemView);
            avatar = (ImageView) itemView.findViewById(R.id.conversation_item_avatar);
            tvTitle = (TextView) itemView.findViewById(R.id.conversation_item_title);
            tvContent = (TextView) itemView.findViewById(R.id.conversation_item_content);
            tvTime = (TextView) itemView.findViewById(R.id.conversation_item_time);
            redDot = itemView.findViewById(R.id.conversation_item_reddot);
        }
    }

    class ConversationInfo {
        String id;
        String title;
        String content;
        String time;
        int unReadMsgCount;
        String[] memberIds;
        Drawable avatar;
    }

}
