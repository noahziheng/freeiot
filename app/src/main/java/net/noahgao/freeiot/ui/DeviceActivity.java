package net.noahgao.freeiot.ui;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import net.noahgao.freeiot.R;
import net.noahgao.freeiot.adapter.devicePageAdapter;
import net.noahgao.freeiot.api.ApiClient;
import net.noahgao.freeiot.model.DeviceModel;
import net.noahgao.freeiot.ui.pages.DeviceHistoryFragment;
import net.noahgao.freeiot.ui.pages.DeviceOptionsFragment;
import net.noahgao.freeiot.ui.pages.DeviceRealtimeFragment;
import net.noahgao.freeiot.util.Auth;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeviceActivity extends AppCompatActivity implements DeviceRealtimeFragment.OnFragmentInteractionListener,DeviceHistoryFragment.OnFragmentInteractionListener,DeviceOptionsFragment.OnFragmentInteractionListener {

    public DeviceModel device;
    private devicePageAdapter pagerAdapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
            getSupportActionBar().setTitle(getIntent().getStringExtra("name"));
        }

        final ProgressDialog dialog = ProgressDialog.show(DeviceActivity.this,"","正在加载中");

        Call<DeviceModel> call = ApiClient.API.getDevice(getIntent().getStringExtra("id"), Auth.getToken());
        call.enqueue(new Callback<DeviceModel>() {
            @Override
            public void onResponse(Call<DeviceModel> call, Response<DeviceModel> response) {
                dialog.dismiss();
                if(response.isSuccessful()) {
                    device = response.body();
                    List<Fragment> fragmentContainter = new ArrayList<>();
                    fragmentContainter.add(DeviceRealtimeFragment.newInstance(device));
                    fragmentContainter.add(DeviceHistoryFragment.newInstance(device));
                    fragmentContainter.add(DeviceOptionsFragment.newInstance(device));
                    String tabTitles[] =  new String[]{"实时数据","历史数据","设备配置"};
                    pagerAdapter = new devicePageAdapter(getSupportFragmentManager(), fragmentContainter, tabTitles);
                    viewPager = (ViewPager) findViewById(R.id.device_viewpager);
                    viewPager.setAdapter(pagerAdapter);
                    tabLayout = (TabLayout) findViewById(R.id.device_tabs);
                    tabLayout.setupWithViewPager(viewPager);
                } else {
                    Toast.makeText(DeviceActivity.this, "HTTP"+response.code()+ " 错误！\n"+response.message(),Toast.LENGTH_LONG).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<DeviceModel> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(DeviceActivity.this,t.getMessage(),Toast.LENGTH_LONG);
                dialog.dismiss();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //点击back键finish当前activity
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {}
}
