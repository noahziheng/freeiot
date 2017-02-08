package net.noahgao.freeiot;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class InitializeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_initialize);
        Intent intentLogin= new Intent(InitializeActivity.this,LoginActivity.class);
        intentLogin.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);//LoginActivity不添加到后退栈
        startActivity(intentLogin);
        InitializeActivity.this.finish();
    }
}
