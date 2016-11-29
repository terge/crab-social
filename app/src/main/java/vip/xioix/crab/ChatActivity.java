package vip.xioix.crab;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.avos.avoscloud.im.v2.callback.AVIMMessagesQueryCallback;
import com.google.common.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AbsActivity {
    public static final String CONVERSATION_ID = "CONVERSATION_ID";
    AVIMConversation mConversation;
    private AVIMClient mClient;
    private  List<AVIMMessage> msgList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_chat);
        initData();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        initData();
    }

    private void initData() {
        final String converId = getIntent().getStringExtra(CONVERSATION_ID);
        Check.d(converId !=null,"lost conversation id ");
        mClient = AVIMClient.getInstance(String.valueOf(converId));
        mClient.open(new AVIMClientCallback() {
            @Override
            public void done(AVIMClient avimClient, AVIMException e) {
                mClient = avimClient;
                mConversation = mClient.getConversation(converId);
                mEventBus.post(mConversation);
            }
        });
    }

    @Subscribe
    private void onConversationReady(AVIMConversation conversation){
        getMoreMsg(conversation);
    }

    @Subscribe
    private void onMsgListReady(List<AVIMMessage> msgList){
        mRecyclerView.setAdapter(new ChatAdatper(msgList));
    }


    /**
     * 获取更多的消息
     * 获取成功后，会通过消息将消息列表发送出来
     * @param conversation 会话
     */
    private void getMoreMsg(AVIMConversation conversation){
        Check.d(conversation!=null,"conversation is null");
        AVIMMessagesQueryCallback callback =  new AVIMMessagesQueryCallback() {
            @Override
            public void done(List<AVIMMessage> list, AVIMException e) {
                if(e != null){
                    Log.e(TAG, "done: ", e);
                    showError(e.getLocalizedMessage());
                    return;
                }
                /**
                 * 之所以没有直接使用msgList.addAll
                 * 是因为想将最新的放在list头，这样在下一次刷新时候
                 * index=0的元素，就是最老的元素
                 */
                if(list != null && list.size()!=0){
                    list.addAll(msgList);
                    msgList = list;
                    mEventBus.post(msgList);
                }
            }
        };

        AVIMMessage oldestMsg = msgList.get(0);
        if(oldestMsg == null){
            conversation.queryMessages(callback);
        }else {
            conversation.queryMessages(oldestMsg.getMessageId(),oldestMsg.getTimestamp(),20,callback);
        }

    }



    class ChatAdatper extends RecyclerView.Adapter<ViewHolder>{
        private List<AVIMMessage> mData;
        ChatAdatper(List<AVIMMessage> data){
            mData = data;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            //收到的消息
            View view = null;
            if(viewType == AVIMMessage.AVIMMessageIOType.AVIMMessageIOTypeIn.ordinal()){
                view = LayoutInflater.from(mActivity).inflate(R.layout.item_chat_in,parent,false);
            }
            //发出的消息
            else {
                view = LayoutInflater.from(mActivity).inflate(R.layout.item_chat_out,parent,false);
            }

            ViewHolder vh = new ViewHolder(view);
            return vh;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            AVIMTypedMessage msg = getItem(position);
//            holder.icon.setBackground(msg.getFrom());
            holder.tvTitle.setText(msg.getFrom());
//            holder.
        }

        @Override
        public int getItemCount() {
            return mData == null ? 0 : mData.size();
        }


        private AVIMTypedMessage getItem(int position){
            return (AVIMTypedMessage) mData.get(position);
        }

        @Override
        public int getItemViewType(int position) {
            return getItem(position).getMessageIOType().ordinal();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView icon;
        TextView tvTitle;
        View content;
        TextView tvTime;

        public ViewHolder(View itemView) {
            super(itemView);
            // TODO: 16-11-29 findviewById
        }
    }
}
