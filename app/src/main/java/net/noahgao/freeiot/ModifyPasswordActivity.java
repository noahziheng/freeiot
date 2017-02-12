/**
 * Copyright (C) 2015 JianyingLi <lijy91@foxmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.noahgao.freeiot;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import net.noahgao.freeiot.api.ApiClient;
import net.noahgao.freeiot.model.UserModel;
import net.noahgao.freeiot.util.Auth;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ModifyPasswordActivity extends AppCompatActivity {

    private EditText mEdtOldPassword;
    private EditText mEdtNewPassword;
    private EditText mEdtReNewPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_password);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
        }

        mEdtOldPassword = (EditText) findViewById(R.id.edt_old_password);
        mEdtNewPassword = (EditText) findViewById(R.id.edt_new_password);
        mEdtReNewPassword = (EditText) findViewById(R.id.edt_re_new_password);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_save) {
            final String oldPassword = mEdtOldPassword.getText().toString();
            final String newPassword = mEdtNewPassword.getText().toString();
            final String reNewPassword = mEdtReNewPassword.getText().toString();
            if(oldPassword.equals("") || newPassword.equals("") || reNewPassword.equals("")) {
                Toast.makeText(getApplicationContext(), "有信息未填齐", Toast.LENGTH_SHORT).show();
                return false;
            }
            if(newPassword.equals(oldPassword)) {
                Toast.makeText(getApplicationContext(), "新老密码一致", Toast.LENGTH_SHORT).show();
                return false;
            }
            if(!newPassword.equals(reNewPassword)) {
                Toast.makeText(getApplicationContext(), "两次填写的密码不一致", Toast.LENGTH_SHORT).show();
                return false;
            }
            Call<UserModel> call = ApiClient.API.auth(Auth.getUser().getEmail(), oldPassword);
            call.enqueue(new Callback<UserModel>() {
                @Override
                public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                    if(!response.isSuccessful())
                        Toast.makeText(getApplicationContext(), "原密码有误", Toast.LENGTH_SHORT).show();
                    else {
                        Call<Object> modcall = ApiClient.API.modifyPassword(Auth.getUser().get_id(), newPassword, Auth.getToken());
                        modcall.enqueue(new Callback<Object>() {
                            @Override
                            public void onResponse(Call<Object> call, Response<Object> response) {
                                Auth.logout();
                                Toast.makeText(getApplicationContext(), "已完成,请自行登出重新登录", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<Object> call, Throwable t) {
                                t.printStackTrace();
                                Toast.makeText(getApplicationContext(), "请求出错", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }

                @Override
                public void onFailure(Call<UserModel> call, Throwable t) {
                    t.printStackTrace();
                    Toast.makeText(getApplicationContext(), "请求出错", Toast.LENGTH_SHORT).show();
                }
            });
            return true;
        } else if (item.getItemId() == android.R.id.home) {
            //startActivity(new Intent(ModifyPasswordActivity.this,AccountActivity.class));
            ModifyPasswordActivity.this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
