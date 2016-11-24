package vip.xioix.crab.biz;

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



}
