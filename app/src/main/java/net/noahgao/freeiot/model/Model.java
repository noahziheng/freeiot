package net.noahgao.freeiot.model;

import java.util.Date;

/**
 * Created by Noah Gao on 17-2-7.
 * By Android Studio
 */

public class Model {

    public String _id;
    public Date created_at;
    public Date updated_at;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }
}
