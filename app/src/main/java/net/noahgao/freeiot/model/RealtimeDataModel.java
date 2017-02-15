package net.noahgao.freeiot.model;

/**
 * Created by Noah Gao on 17-2-15.
 * By Android Studio
 */

public class RealtimeDataModel<T> extends Model {
    private String name;
    private String label;
    private String unit;
    private String type;
    private T content;

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
