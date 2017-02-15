package net.noahgao.freeiot.model;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * Created by Noah Gao on 17-2-12.
 * By Android Studio
 */

public class ProductModel<T> extends Model {

    private String name;
    private String commit;
    private String readme;
    private String secret;
    private T owner;
    private List<ProductMod> mods;

    public class ProductMod {

        private String origin;
        private String remark;
        private JSONObject vars;
        private List<Integer> hidden;

        public String getOrigin() {
            return origin;
        }

        public void setOrigin(String origin) {
            this.origin = origin;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public JSONObject getVars() {
            return vars;
        }

        public void setVars(JSONObject vars) {
            this.vars = vars;
        }

        public List<Integer> getHidden() {
            return hidden;
        }

        public void setHidden(List<Integer> hidden) {
            this.hidden = hidden;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCommit() {
        return commit;
    }

    public void setCommit(String commit) {
        this.commit = commit;
    }

    public String getReadme() {
        return readme;
    }

    public void setReadme(String readme) {
        this.readme = readme;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public T getOwner() {
        return owner;
    }

    public void setOwner(T owner) {
        this.owner = owner;
    }

    public List<ProductMod> getMods() {
        return mods;
    }

    public void setMods(List<ProductMod> mods) {
        this.mods = mods;
    }
}
