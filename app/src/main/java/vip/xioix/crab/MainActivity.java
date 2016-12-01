package vip.xioix.crab;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;

import com.avos.avoscloud.feedback.FeedbackAgent;

import cn.leancloud.chatkit.activity.LCIMContactFragment;
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

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(final MenuItem item) {
        Log.d(TAG, "onNavigationItemSelected: ");

        if (!item.isChecked()) {
            switch (item.getItemId()) {
                case R.id.nav_conversation:
                case R.id.nav_contacts:
                    showSelectFragment(getClickFragment(item.getItemId()));
                    break;
                case R.id.nav_timeline:
                    break;
                case R.id.nav_setting:
                    break;
                case R.id.nav_feedback:
                    FeedbackAgent agent = new FeedbackAgent(mActivity);
                    agent.startDefaultThreadActivity();
                    break;
                case R.id.nav_logout:
                    App.appContext.logout();
                    break;
            }
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private Fragment getClickFragment(int itemId){
        Fragment fg = fgList.get(itemId);
        if(fg == null){
            fg = createSelectFragment(itemId);
        }
        return fg;
    }

    private void showSelectFragment(Fragment fg) {
        Log.d(TAG, "showSelectFragment: ");
        if(fg == null)return;

        boolean isFragmentShowing = getSupportFragmentManager().findFragmentByTag(fg.getClass().getSimpleName()) == fg;
        if(isFragmentShowing) return;


        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_main, fg, fg.getClass().getSimpleName());
        transaction.addToBackStack(null);

        transaction.commit();
    }

    private Fragment createSelectFragment(int id) {
        Fragment fg = null;

        if (id == R.id.nav_conversation) {
            fg = new LCIMConversationListFragment();
        } else if (id == R.id.nav_contacts) {
            fg = new LCIMContactFragment();
        }
        Log.d(TAG, "createSelectFragment: id:" + id + " fg:" + (fg == null ? null : fg.getClass().getSimpleName()));
        return fg;
    }
}
