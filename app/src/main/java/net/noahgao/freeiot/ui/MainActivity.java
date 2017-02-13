package net.noahgao.freeiot.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import net.noahgao.freeiot.R;
import net.noahgao.freeiot.model.UserModel;
import net.noahgao.freeiot.pages.IndexFragment;
import net.noahgao.freeiot.pages.ProductsFragment;
import net.noahgao.freeiot.util.Auth;
import net.noahgao.freeiot.util.Badge;
import net.noahgao.freeiot.util.UpdateManager;

/**
 * Created by Noah Gao on 17-2-6.
 * By Android Studio
 */

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
            IndexFragment.OnFragmentInteractionListener,
            ProductsFragment.OnFragmentInteractionListener {

    public UserModel mUser;
    FragmentManager fragmentManager = getSupportFragmentManager();
    private Fragment[] fragments={
            new IndexFragment(),
            new ProductsFragment()
    };
    private Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(!Auth.check()) {
            Intent intentLogin = new Intent(MainActivity.this, LoginActivity.class);
            intentLogin.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);//LoginActivity不添加到后退栈
            startActivity(intentLogin);
        }
        UpdateManager.doUpdate(this, false);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if(mUser == null) mUser = Auth.getUser();
        TextView emailView = (TextView) ((NavigationView) findViewById(R.id.nav_view)).getHeaderView(0).findViewById(R.id.nav_header_email);
        TextView roleView = (TextView) ((NavigationView) findViewById(R.id.nav_view)).getHeaderView(0).findViewById(R.id.nav_header_role);
        emailView.setText(mUser.getEmail());
        roleView.setText(Badge.buildRole(mUser.getRole()));

        changePage(0);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Log.i("MAIN","OnBackPressed");
            // super.onBackPressed(); 	// 不要调用父类的方法
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
        }
    }

    public void onFragmentInteraction() {}

    public void changePage(int tag) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (currentFragment != null) fragmentTransaction.remove(currentFragment);
        fragmentTransaction.add(R.id.content_main,fragments[tag]);
        fragmentTransaction.commit();
        currentFragment = fragments[tag];
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(0).setChecked(false);
        navigationView.getMenu().getItem(1).setChecked(false);
        switch (tag) {
            case 0:
                navigationView.getMenu().getItem(0).setChecked(true);
                break;
            case 1:
                navigationView.getMenu().getItem(1).setChecked(true);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_toolbar,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                Toast.makeText(getApplicationContext(), "搜索功能尚未开放", Toast.LENGTH_SHORT).show();
                break;
            case R.id.notification:
                Toast.makeText(getApplicationContext(), "暂时没有未读的通知", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            changePage(0);
        } else if (id == R.id.nav_products) {
            changePage(1);
        } else if (id == R.id.nav_settings) {
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
