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
public class NotificationMetaModel extends Model {
    private Count count;

    public class Count {
        private int total;
        private int unread;

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getUnread() {
            return unread;
        }

        public void setUnread(int unread) {
            this.unread = unread;
        }
    }

    public Count getCount() {
        return count;
    }

    public void setCount(Count count) {
        this.count = count;
    }

    public int getTotal() {
        return count.getTotal();
    }

    public int getUnread() {
        return count.getUnread();
    }
}
