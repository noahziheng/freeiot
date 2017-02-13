package net.noahgao.freeiot.model;

import android.support.annotation.Nullable;
import android.util.ArrayMap;

/**
 * Created by Noah Gao on 17-2-7.
 * By Android Studio
 */

public class UserModel extends Model {

    private boolean loginF = false;

    private String id;

    private String email;

    private int role;

    private String token;

    private DeveloperBean dev;

    public class DeveloperBean {

        private class DeveloperName {
            private String last;
            private String first;

            public String getLast() {
                return last;
            }

            public void setLast(String last) {
                this.last = last;
            }

            public String getFirst() {
                return first;
            }

            public void setFirst(String first) {
                this.first = first;
            }
        }

        private DeveloperName name;

        public String getFullName() { return name.getLast() + name.getFirst(); }

        private String work;
        private String company;
        private String location;
        private String reason;

        public DeveloperName getName() {
            return name;
        }

        public void setName(DeveloperName name) {
            this.name = name;
        }

        public String getWork() {
            return work;
        }

        public void setWork(String work) {
            this.work = work;
        }

        public String getCompany() {
            return company;
        }

        public void setCompany(String company) {
            this.company = company;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }
    }

    public DeveloperBean getDev() {
        return dev;
    }

    public void setDev(DeveloperBean dev) {
        this.dev = dev;
    }

    public void initUser(@Nullable ArrayMap data) {
        if(data != null) {
            if(data.containsKey("_id")) _id = (String) data.get("_id");
            if(data.containsKey("email")) email = (String) data.get("email");
            if(data.containsKey("role")) role = (int) data.get("role");
            if(data.containsKey("token")) token = (String) data.get("token");
        }
        if(id!=null) {
            _id = id;
            id = null;
        }
        loginF = true;
    }

    public int getRole() {
        return role;
    }

    public String getEmail() {
        return email;
    }

    public String getToken() {
        return token;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setLoginF(boolean loginF) {
        this.loginF = loginF;
    }

    public boolean isLoginF() {
        return loginF;
    }
}
