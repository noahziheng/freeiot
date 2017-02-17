package net.noahgao.freeiot.ui;

import android.net.Uri;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.baoyachi.stepview.HorizontalStepView;
import com.baoyachi.stepview.bean.StepBean;

import net.noahgao.freeiot.R;
import net.noahgao.freeiot.adapter.initDevicePageAdapter;
import net.noahgao.freeiot.model.WifiResultModel;
import net.noahgao.freeiot.ui.pages.InitDeviceCreateFragment;
import net.noahgao.freeiot.ui.pages.InitDeviceFindFragment;
import net.noahgao.freeiot.ui.pages.InitDeviceFinishFragment;
import net.noahgao.freeiot.ui.pages.InitDeviceWifiFragment;
import net.noahgao.freeiot.util.WifiAdmin;

import java.util.ArrayList;
import java.util.List;

public class InitDeviceActivity extends AppCompatActivity implements
        InitDeviceFindFragment.OnFragmentInteractionListener,
        InitDeviceWifiFragment.OnFragmentInteractionListener,
        InitDeviceCreateFragment.OnFragmentInteractionListener,
        InitDeviceFinishFragment.OnFragmentInteractionListener {

    private HorizontalStepView mStepView;
    private List<String> stepsList;
    List<Fragment> fragmentContainter = new ArrayList<>();
    FragmentManager fragmentManager = getSupportFragmentManager();
    private Fragment currentFragment;
    private int curPageIndex = -1;
    private Button nextStepBtn;

    private WifiResultModel mResult;
    private WifiAdmin mWifi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init_device);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
            getSupportActionBar().setTitle("设备入网向导");// + getIntent().getStringExtra("name")
        }

        mStepView = (HorizontalStepView) findViewById(R.id.step_view);
        stepsList = new ArrayList<>();
        stepsList.add("扫描");
        stepsList.add("创建");
        stepsList.add("连网");
        stepsList.add("完成");
        fragmentContainter.add(InitDeviceFindFragment.newInstance());
        fragmentContainter.add(InitDeviceCreateFragment.newInstance());
        fragmentContainter.add(InitDeviceWifiFragment.newInstance());
        fragmentContainter.add(InitDeviceFinishFragment.newInstance());
        nextStepBtn = (Button) findViewById(R.id.next_step_btn);
        setNextBtn(0);
        handleStep(0);
        nextStepBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(curPageIndex == -1 || curPageIndex == 3) InitDeviceActivity.this.finish();
                else handleStep(curPageIndex+1);
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

    public void handleStep(int tag) {
        List<StepBean> stepsBeanList = new ArrayList<>();
        for(int i = 0; i<stepsList.size(); i++) {
            int t;
            if(tag < i ) t = -1;
            else if(tag > i) t = 1;
            else t = 0;
            stepsBeanList.add(new StepBean(stepsList.get(i),t));
        }

        mStepView.setStepViewTexts(stepsBeanList)//总步骤
                .setTextSize(12)//set textSize
                .setStepsViewIndicatorCompletedLineColor(ContextCompat.getColor(InitDeviceActivity.this, android.R.color.white))//设置StepsViewIndicator完成线的颜色
                .setStepsViewIndicatorUnCompletedLineColor(ContextCompat.getColor(InitDeviceActivity.this, R.color.uncompleted_text_color))//设置StepsViewIndicator未完成线的颜色
                .setStepViewComplectedTextColor(ContextCompat.getColor(InitDeviceActivity.this, android.R.color.white))//设置StepsView text完成线的颜色
                .setStepViewUnComplectedTextColor(ContextCompat.getColor(InitDeviceActivity.this, R.color.uncompleted_text_color))//设置StepsView text未完成线的颜色
                .setStepsViewIndicatorCompleteIcon(ContextCompat.getDrawable(InitDeviceActivity.this, R.drawable.complted))//设置StepsViewIndicator CompleteIcon
                .setStepsViewIndicatorDefaultIcon(ContextCompat.getDrawable(InitDeviceActivity.this, R.drawable.default_icon))//设置StepsViewIndicator DefaultIcon
                .setStepsViewIndicatorAttentionIcon(ContextCompat.getDrawable(InitDeviceActivity.this, R.drawable.attention));//设置StepsViewIndicator AttentionIcon}

        if(curPageIndex != tag) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            if (currentFragment != null) fragmentTransaction.remove(currentFragment);
            fragmentTransaction.add(R.id.init_device_main_view, fragmentContainter.get(tag));
            fragmentTransaction.commit();
            currentFragment = fragmentContainter.get(tag);
            curPageIndex = tag;
            setNextBtn(0);
        }
    }

    public void setNextBtn (int tag) {
        switch (tag) {
            case 0:
                nextStepBtn.setEnabled(false);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    nextStepBtn.setTextColor(getResources().getColor(R.color.gray,getTheme()));
                }
                break;
            case 1:
                nextStepBtn.setEnabled(true);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    nextStepBtn.setTextColor(getResources().getColor(R.color.black_all,getTheme()));
                }
                break;
        }
    }

    public WifiResultModel getmResult() {
        return mResult;
    }

    public void setmResult(WifiResultModel mResult) {
        this.mResult = mResult;
    }

    public WifiAdmin getmWifi() {
        return mWifi;
    }

    public void setmWifi(WifiAdmin mWifi) {
        this.mWifi = mWifi;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onReadyForNext() {
        setNextBtn(1);
    }

    @Override
    public void onReadyForFinish() {
        onReadyForNext();
        nextStepBtn.setText("完成");
    }
}
