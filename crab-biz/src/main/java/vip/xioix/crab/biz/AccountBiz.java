package vip.xioix.crab.biz;

/**
 * Created by terge on 16-11-23.
 */

public class AccountBiz {
    private static class Holder{
        public static AccountBiz INSTANCE  = new AccountBiz();
    }

    private AccountBiz(){

    }

    public static AccountBiz getInstance(){
        return Holder.INSTANCE;
    }



    public boolean autoLogin(){
        return true;
    }
}
