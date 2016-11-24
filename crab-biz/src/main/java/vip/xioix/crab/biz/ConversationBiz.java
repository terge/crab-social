package vip.xioix.crab.biz;

import java.util.List;

import vip.xioix.crab.entities.AbsCrabInfo;
import vip.xioix.crab.entities.Conversation;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

/**
 * Created by terge on 16-11-24.
 */

public class ConversationBiz {
    private static class Holder{
        public static ConversationBiz INSTANCE  = new ConversationBiz();
    }

    private ConversationBiz(){

    }

    public static ConversationBiz getInstance(){
        return Holder.INSTANCE;
    }


    public void getConversationList()
}
