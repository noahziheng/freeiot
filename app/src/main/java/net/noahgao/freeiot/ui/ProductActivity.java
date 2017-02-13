package net.noahgao.freeiot.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.noahgao.freeiot.R;
import net.noahgao.freeiot.api.ApiClient;
import net.noahgao.freeiot.model.ProductModel;
import net.noahgao.freeiot.util.Auth;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductActivity extends AppCompatActivity {

    private ProductModel product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
            getSupportActionBar().setTitle(getIntent().getStringExtra("name"));
        }

        Call<ProductModel> call = ApiClient.API.getProduct(getIntent().getStringExtra("id"), Auth.getToken());
        call.enqueue(new Callback<ProductModel>() {
            @Override
            public void onResponse(Call<ProductModel> call, Response<ProductModel> response) {
                if(response.isSuccessful()) {
                    product = response.body();
                    ((TextView) findViewById(R.id.basic_info_developer)).setText(product.getOwner().getDev().getFullName()+" ("+product.getOwner().getDev().getCompany()+")");
                    ((TextView) findViewById(R.id.basic_info_email)).setText(product.getOwner().getEmail());
                    ((TextView) findViewById(R.id.basic_info_commit)).setText(product.getCommit());
                    findViewById(R.id.readme_btn).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(ProductActivity.this,ReadmeActivity.class);
                            intent.putExtra("name",product.getName());
                            intent.putExtra("readme",product.getReadme());
                            startActivity(intent);
                        }
                    });
                    View view = LayoutInflater.from(findViewById(R.id.modsView).getContext()).inflate(R.layout.chip_item, (ViewGroup) findViewById(R.id.modsView),false);
                    ((ViewGroup) findViewById(R.id.modsView)).addView(view);
                }
            }

            @Override
            public void onFailure(Call<ProductModel> call, Throwable t) {
                t.printStackTrace();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //点击back键finish当前activity
        switch (item.getItemId()) {
            case android.R.id.home:
                //startActivity(new Intent(SettingsActivity.this,MainActivity.class));
                finish();
                break;
        }
        return true;
    }
}
