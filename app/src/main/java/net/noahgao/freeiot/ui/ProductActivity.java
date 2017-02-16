package net.noahgao.freeiot.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.xiaofeng.flowlayoutmanager.FlowLayoutManager;

import net.noahgao.freeiot.R;
import net.noahgao.freeiot.api.ApiClient;
import net.noahgao.freeiot.model.ProductModel;
import net.noahgao.freeiot.model.UserModel;
import net.noahgao.freeiot.util.Auth;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductActivity extends AppCompatActivity {

    private ProductModel<UserModel> product;

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

        Call<ProductModel<UserModel>> call = ApiClient.API.getProduct(getIntent().getStringExtra("id"), Auth.getToken());
        call.enqueue(new Callback<ProductModel<UserModel>>() {
            @Override
            public void onResponse(Call<ProductModel<UserModel>> call, Response<ProductModel<UserModel>> response) {
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
                }
            }

            @Override
            public void onFailure(Call<ProductModel<UserModel>> call, Throwable t) { t.printStackTrace(); }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductActivity.this, InitDeviceActivity.class);
                intent.putExtra("id",product.get_id());
                intent.putExtra("name",product.getName());
                startActivity(intent);
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
}
