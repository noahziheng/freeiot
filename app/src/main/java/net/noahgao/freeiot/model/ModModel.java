package net.noahgao.freeiot.model;



import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * Created by Noah Gao on 17-2-14.
 * By Android Studio
 */

public class ModModel extends Model {
    private String driver;
    private String name;
    private List<JSONObject> uplink;
    private List<JSONObject> downlink;

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<JSONObject> getUplink() {
        return uplink;
    }

    public void setUplink(List<JSONObject> uplink) {
        this.uplink = uplink;
    }

    public List<JSONObject> getDownlink() {
        return downlink;
    }

    public void setDownlink(List<JSONObject> downlink) {
        this.downlink = downlink;
    }
}
