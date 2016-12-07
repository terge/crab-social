package vip.xioix.crab;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseArray;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.feedback.FeedbackAgent;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.squareup.picasso.Picasso;

import cn.leancloud.chatkit.LCCustomKey;
import cn.leancloud.chatkit.activity.LCIMConversationListFragment;
import vip.xioix.crabbase.base.AbsActivity;

public class MainActivity extends AbsActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    SparseArray<Fragment> fgList = new SparseArray<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        toolbar.setNavigationIcon(R.drawable.ic_launcher);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        initHeadervew(navigationView.getHeaderView(0));

        openIMClient();
    }

    private void openIMClient() {
        Log.d(TAG, "openIMClient: ");
        AVIMClient client = AVIMClient.getInstance(curUser.getObjectId());
        client.open(new AVIMClientCallback() {
            @Override
            public void done(AVIMClient avimClient, AVIMException e) {
                showConversationList();
            }
        });
    }

    private void showConversationList() {
        Log.d(TAG, "showConversationList: ");
        Fragment fg = new LCIMConversationListFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_main, fg, fg.getClass().getSimpleName());
        transaction.addToBackStack(null);

        transaction.commit();
    }

    private void initHeadervew(View headerView) {

        Object url = curUser.get(LCCustomKey.AVATAR_URL);
        if(url != null){
            ImageView ivAvatar = (ImageView) headerView.findViewById(R.id.iv_header_avatar);
            Picasso.with(this)
                    .load((String) curUser.get(LCCustomKey.AVATAR_URL))
                    .resize(60,60)
                    .centerCrop()
                    .into(ivAvatar);
        }

        TextView tvName = (TextView) headerView.findViewById(R.id.tv_header_name);
        tvName.setText(curUser.getUsername());


        TextView tvMobile = (TextView) headerView.findViewById(R.id.tv_header_mobile);
        tvMobile.setText(curUser.getMobilePhoneNumber());
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    @Override
    public boolean onNavigationItemSelected(final MenuItem item) {
        Log.d(TAG, "onNavigationItemSelected: ");

        if (!item.isChecked()) {
            switch (item.getItemId()) {
                case R.id.nav_profile:
                    onProfileItemClick();
                    break;
                case R.id.nav_timeline:
                    onTimeLineItemClick();
                    break;
                case R.id.nav_setting:
                    onSettingItemClick();
                    break;
                case R.id.nav_feedback:
                    onFeedbackItemClick();
                    break;
                case R.id.nav_logout:
                    onLogoutItemClick();
                    break;
            }
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        item.setChecked(false);
        return true;
    }

    private void onLogoutItemClick() {
        App.appContext.logout();
    }

    private void onFeedbackItemClick() {
        FeedbackAgent agent = new FeedbackAgent(mActivity);
        agent.startDefaultThreadActivity();
    }

    private void onSettingItemClick() {
        startActivity(new Intent(this,SettingsActivity.class));
    }

    private void onTimeLineItemClick() {
        startActivity(new Intent(this,TimeLineActivity.class));
    }

    private void onProfileItemClick() {
        startActivity(new Intent(this,MyProfileActivity.class));
    }
}
