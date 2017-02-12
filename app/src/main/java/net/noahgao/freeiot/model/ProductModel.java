package net.noahgao.freeiot.model;

import android.util.ArrayMap;

import java.util.List;

/**
 * Created by Noah Gao on 17-2-12.
 * By Android Studio
 */

public class ProductModel extends Model {

    private String name;
    private String commit;
    private String readme;
    private String secret;
    private UserModel owner;
    private List<ProductMod> mods;

    public class ProductMod {

        private String origin;
        private String remark;
        private ArrayMap<String, String> vars;
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

        public ArrayMap<String, String> getVars() {
            return vars;
        }

        public void setVars(ArrayMap<String, String> vars) {
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

    public UserModel getOwner() {
        return owner;
    }

    public void setOwner(UserModel owner) {
        this.owner = owner;
    }

    public List<ProductMod> getMods() {
        return mods;
    }

    public void setMods(List<ProductMod> mods) {
        this.mods = mods;
    }
}
