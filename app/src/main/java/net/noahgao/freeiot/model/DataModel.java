package net.noahgao.freeiot.model;

import com.alibaba.fastjson.TypeReference;

/**
 * Created by Noah Gao on 17-2-12.
 * By Android Studio
 */

public class DataModel extends Model {

    private int type;
    private String device;
    private String label;
    private String content;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
