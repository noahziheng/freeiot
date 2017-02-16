package net.noahgao.freeiot.ui;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.baoyachi.stepview.HorizontalStepView;
import com.baoyachi.stepview.bean.StepBean;

import net.noahgao.freeiot.R;

import java.util.ArrayList;
import java.util.List;

public class InitDeviceActivity extends AppCompatActivity {

    private HorizontalStepView mStepView;
    private List<String> stepsList;

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
        stepsList.add("连网");
        stepsList.add("创建");
        stepsList.add("完成");
        handleStep(0);

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
    }
}
