package net.noahgao.freeiot.model;

import android.support.annotation.Nullable;
import android.util.ArrayMap;

/**
 * Created by Noah Gao on 17-2-7.
 * By Android Studio
 */

public class UserModel extends Model {

    private boolean loginF = false;

    private String id;

    private String email;

    private int role;

    private String token;

    public void initUser(@Nullable ArrayMap data) {
        if(data != null) {
            if(data.containsKey("_id")) _id = (String) data.get("_id");
            if(data.containsKey("email")) email = (String) data.get("email");
            if(data.containsKey("role")) role = (int) data.get("role");
            if(data.containsKey("token")) token = (String) data.get("token");
        }
        if(id!=null) {
            _id = id;
            id = null;
        }
        loginF = true;
    }

    public int getRole() {
        return role;
    }

    public String getEmail() {
        return email;
    }

    public String getToken() {
        return token;
    }

    public boolean isLoginF() {
        return loginF;
    }
}
