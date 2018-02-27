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

import android.support.annotation.Nullable;

import net.noahgao.freeiot.api.ApiClient;
import net.noahgao.freeiot.util.Auth;

import java.io.IOException;

import retrofit2.Response;

@SuppressWarnings("unused")
public class WifiResultModel extends Model {
    private String product;
    private String name;
    private String secret;
    private String device;
    private String deviceName;
    private String deviceSecret;
    private String version;
    private boolean otaSupport;
    private boolean isSelected = false;

    public WifiResultModel (@Nullable String product) throws IOException {
        if(product!=null) {
            setProduct(product);
            Response<ProductModel<UserModel>> result = ApiClient.API.getProduct(product, Auth.getToken()).execute();
            if (result.isSuccessful()) {
                setName(result.body().getName());
                setSecret(result.body().getSecret());
            }
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceSecret() {
        return deviceSecret;
    }

    public void setDeviceSecret(String deviceSecret) {
        this.deviceSecret = deviceSecret;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isOtaSupport() {
        return otaSupport;
    }

    public void setOtaSupport(boolean otaSupport) {
        this.otaSupport = otaSupport;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
