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

import java.util.List;

public class DeviceModel extends Model {

    private DeviceMeta meta;
    private List<DataModel> data;

    public static class DeviceMeta {

        private DeviceMetaModel<ProductModel<String>> device;
        private int datalimit;

        public static class DeviceMetaModel<T> extends Model {
            private String name;
            private String secret;
            private UserModel owner;
            private T product;
            private int status;

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
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

            public T getProduct() { return product; }

            public void setProduct(T product) { this.product = product; }
        }

        public DeviceMetaModel<ProductModel<String>> getDevice() {
            return device;
        }

        public void setDevice(DeviceMetaModel<ProductModel<String>> device) {
            this.device = device;
        }

        public int getDatalimit() {
            return datalimit;
        }

        public void setDatalimit(int datalimit) {
            this.datalimit = datalimit;
        }
    }

    public List<DataModel> getData() {
        return data;
    }

    public void setData(List<DataModel> data) {
        this.data = data;
    }

    public DeviceMeta getMeta() {
        return meta;
    }

    public void setMeta(DeviceMeta meta) {
        this.meta = meta;
    }
}
