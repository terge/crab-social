package vip.xioix.crab.biz;

/**
 * Created by terge on 16-11-23.
 */

public class Account {
    private static class Holder{
        public static Account INSTANCE  = new Account();
    }

    private Account(){

    }

    public static Account getInstance(){
        return Holder.INSTANCE;
    }



    public boolean autoLogin(){
        return true;
    }
}
