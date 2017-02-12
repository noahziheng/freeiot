package net.noahgao.freeiot.util;

import android.content.SharedPreferences;
import android.util.ArrayMap;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import net.noahgao.freeiot.api.ApiClient;
import net.noahgao.freeiot.model.UserModel;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Created by Noah Gao on 17-2-6.
 * By Android Studio
 */

public class Auth {

    private static String json;
    private static UserModel mUser;
    private static boolean sIsInitialized = false;
    private static SharedPreferences ssharedPref;

    public static synchronized void initialize(SharedPreferences sharedPref) {
        if (sIsInitialized) {
            return;
        }
        sIsInitialized = true;
        ssharedPref = sharedPref;
    }

    public static boolean check() {
        if(mUser == null) mUser = new UserModel();
        if (mUser.isLoginF()) return true;
        else {
            if(ssharedPref != null) {
                String authstr = ssharedPref.getString("AUTH",null);
                if(authstr != null) {
                    ArrayMap authobj = JSON.parseObject(authstr,new TypeReference<ArrayMap>() {});
                    mUser.initUser(authobj);
                    return true;
                }
            }
            return false;
        }
    }

    public static boolean login(String email,String password) {
        try {
            Response<UserModel> result = ApiClient.API.auth(email, password).execute();
            if(!result.isSuccessful()) {
                if(result.code() == 404) {
                    result = reg(email,password);
                    if(result == null) return false;
                } else return false;
            }
            mUser = result.body();
            mUser.initUser(null);
            //保存登录状态
            if(ssharedPref != null) {
                SharedPreferences.Editor editor = ssharedPref.edit();
                editor.putString("AUTH", JSON.toJSONString(mUser));
                editor.apply();
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void logout() {
        if(ssharedPref != null) {
            SharedPreferences.Editor editor = ssharedPref.edit();
            editor.putString("AUTH", null);
            editor.apply();
        }
        mUser = null;
    }

    private static Response<UserModel> reg(String email, String password) {
        Response<UserModel> result = null;
        try {
            result = ApiClient.API.reg(email, password).execute();
            if(!result.isSuccessful()) result=null;
            else result = ApiClient.API.auth(email, password).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(result!=null && result.isSuccessful()) return result;
        else return null;
    }

    static public UserModel getUser() {
        return mUser;
    }

    static public String getToken() {
        return mUser.getToken();
    }

}
