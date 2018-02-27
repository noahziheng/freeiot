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
