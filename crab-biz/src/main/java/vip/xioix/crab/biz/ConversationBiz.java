package vip.xioix.crab.biz;

import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversationQuery;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationQueryCallback;

import java.util.Arrays;

/**
 * Created by terge on 16-11-24.
 */

public class ConversationBiz {
    private static class Holder{
        public static ConversationBiz INSTANCE  = new ConversationBiz();
    }

    private ConversationBiz(){
        mClient.open(new AVIMClientCallback() {
            @Override
            public void done(AVIMClient avimClient, AVIMException e) {
                // TODO: 16-11-24 Only mClient opened,can do other conversation staff
                mClient = avimClient;
            }
        });
    }

    public static ConversationBiz getInstance(){
        return Holder.INSTANCE;
    }


    private AVUser curUser = AccountBiz.getInstance().getCurrentUser();
    private  AVIMClient mClient = AVIMClient.getInstance(curUser.getObjectId());

    /**
     * 获取当前用户所有的回话列表
     * @param callback
     */
    public void getMyConversationList(AVIMConversationQueryCallback callback){
        AVIMConversationQuery query = mClient.getQuery();
        query.containsMembers(Arrays.asList(curUser.getObjectId()));
        query.findInBackground(callback);
    }

    public AVIMConversationQuery getConversationQuery(){
        return mClient.getQuery();
    }
}
