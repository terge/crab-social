package cn.leancloud.chatkit;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by terge on 16-11-30.
 */

public class UserProvider implements LCChatProfileProvider {
    private static final List<LCChatKitUser> partUsers = new ArrayList<LCChatKitUser>();

    @Override
    public void fetchProfiles(List<String> userIdList, final LCChatProfilesCallBack profilesCallBack) {
        String curID = AVUser.getCurrentUser().getObjectId();
        AVQuery<AVUser> followerQuery = AVUser.followerQuery(curID,AVUser.class);
        followerQuery.findInBackground(new FindCallback<AVUser>() {
            @Override
            public void done(List<AVUser> avObjects, AVException avException) {
                saveUserList(avObjects);
                if(profilesCallBack!=null)profilesCallBack.done(partUsers, avException);
            }
        });

    }

    private void saveUserList(List<AVUser> userList){
        if(userList == null || userList.size() == 0)return;
        LCChatKitUser userModel;
        String id,name;
        Object avatarUrl;
        for(AVUser user:userList){
            id = user.getObjectId();
            name = user.getUsername();
            avatarUrl = user.get(LCCustomKey.AVATAR_URL);
            userModel = new LCChatKitUser(id,name, avatarUrl == null?null: (String) avatarUrl);
            partUsers.add(userModel);
        }
    }

    @Override
    public List<LCChatKitUser> getUserCache() {
        if(partUsers.size() == 0){
            LCChatKitUser master = new LCChatKitUser("582befb4128fe100694aff1e","David",null);
            partUsers.add(master);
        }
        return partUsers;
    }
}
