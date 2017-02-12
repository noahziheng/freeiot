package net.noahgao.freeiot.model;

import java.util.List;

/**
 * Created by Noah Gao on 17-2-12.
 * By Android Studio
 */

public class DeviceModel extends Model {

    private DeviceMeta meta;
    private List<DataModel> data;

    public class DeviceMeta {

        private DeviceMetaModel device;
        private int datalimit;

        public class DeviceMetaModel {
            private String name;
            private String secret;
            private UserModel owner;
            private ProductSimpleModel<String> product;
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

            public ProductSimpleModel<String> getProduct() { return product; }

            public void setProduct(ProductSimpleModel<String> product) { this.product = product; }
        }

        public DeviceMetaModel getDevice() {
            return device;
        }

        public void setDevice(DeviceMetaModel device) {
            this.device = device;
        }

        public int getDatalimit() {
            return datalimit;
        }

        public void setDatalimit(int datalimit) {
            this.datalimit = datalimit;
        }
    }

    public int getStatus() {
        return meta.getDevice().getStatus();
    }

    public void setStatus(int status) {
        meta.getDevice().setStatus(status);
    }

    public String getName() {
        return meta.getDevice().getName();
    }

    public void setName(String name) {
        meta.getDevice().setName(name);
    }

    public String getSecret() {
        return meta.getDevice().getSecret();
    }

    public void setSecret(String secret) {
        meta.getDevice().setSecret(secret);
    }

    public UserModel getOwner() {
        return meta.getDevice().getOwner();
    }

    public void setOwner(UserModel owner) {
        meta.getDevice().setOwner(owner);
    }

    public int getDatalimit() {
        return meta.getDatalimit();
    }

    public void setDatalimit(int datalimit) {
        meta.setDatalimit(datalimit);
    }

    public List<DataModel> getDatas() {
        return data;
    }

    public void setDatas(List<DataModel> datas) {
        this.data = datas;
    }
}
