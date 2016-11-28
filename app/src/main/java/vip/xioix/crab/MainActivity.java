package vip.xioix.crab;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import vip.xioix.crab.fragment.AbsFg;
import vip.xioix.crab.fragment.ConversationFg;

import static android.R.attr.id;

public class MainActivity extends AbsActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    SparseArray<AbsFg> fgList = new SparseArray<>();

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

        Observable.just(item)
                //当前item已经是选中状态，不做处理
                .filter(new Func1<MenuItem, Boolean>() {
                    @Override
                    public Boolean call(MenuItem menuItem) {
                        Log.d(TAG, "onNavigationItemSelected: 1");
                        return !item.isChecked();
                    }
                })
                //取出缓存中的Fragment
                .map(new Func1<MenuItem, AbsFg>() {
                    @Override
                    public AbsFg call(MenuItem menuItem) {
                        Log.d(TAG, "onNavigationItemSelected: 2");
                        int id = item.getItemId();
                        return fgList.get(id);
                    }
                })
                //过滤掉当前fg与要显示的fg是同一个的情况
                .filter(new Func1<AbsFg, Boolean>() {
                    @Override
                    public Boolean call(AbsFg absFg) {
                        Log.d(TAG, "onNavigationItemSelected: 3");
                        return absFg == null || getFragmentManager().findFragmentByTag(absFg.getClass().getSimpleName()) != absFg;
                    }
                })
                //创建新的fg
                .map(new Func1<AbsFg, AbsFg>() {
                    @Override
                    public AbsFg call(AbsFg absFg) {
                        Log.d(TAG, "onNavigationItemSelected: 4");
                        if (absFg == null) {
                            absFg = createSelectFragment(item.getItemId());
                        }
                        return absFg;
                    }
                })
                //过滤掉null
                .filter(new Func1<AbsFg, Boolean>() {
                    @Override
                    public Boolean call(AbsFg absFg) {
                        Log.d(TAG, "onNavigationItemSelected: 5");
                        return absFg != null;
                    }
                })
                //展示fg
                .subscribe(new Action1<AbsFg>() {
                    @Override
                    public void call(AbsFg absFg) {
                        Log.d(TAG, "onNavigationItemSelected: 6");
                        fgList.append(id, absFg);
                        showSelectFragment(absFg);
                    }
                });
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showSelectFragment(AbsFg fg) {
        Log.d(TAG, "showSelectFragment: ");
        // Create fragment and give it an argument specifying the article it should show
        Bundle args = new Bundle();
        //        args.putInt(ArticleFragment.ARG_POSITION, position);
        fg.setArguments(args);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.content_main, fg, fg.getClass().getSimpleName());
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }

    private AbsFg createSelectFragment(int id) {
        AbsFg fg = null;

        if (id == R.id.nav_conversation) {
            fg = new ConversationFg();
        } else if (id == R.id.nav_friends) {

        } else if (id == R.id.nav_timeline) {

        } else if (id == R.id.nav_setting) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }
        Log.d(TAG, "createSelectFragment: id:"+id +" fg:"+(fg==null?null:fg.getClass().getSimpleName()));
        return fg;
    }
}
