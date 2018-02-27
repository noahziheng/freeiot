/*
 * Copyright (c) 2017. Noah Gao <noahgaocn@gmail.com>
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package net.noahgao.freeiot.model;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

@SuppressWarnings("unused")
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
