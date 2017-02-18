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

package net.noahgao.freeiot.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;

import net.noahgao.freeiot.R;
import net.noahgao.freeiot.model.DataModel;
import net.noahgao.freeiot.util.Badge;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import si.virag.fuzzydateformatter.FuzzyDateTimeFormatter;

public class datasAdapter extends XRecyclerView.Adapter<datasAdapter.BodyViewHolder> {

    private List<DataModel> listData;
    private Context mContext;
    public datasAdapter(List<DataModel> datas, Context context) {
        this.listData = datas;
        mContext = context;
    }

    @Override
    public BodyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_data,viewGroup,false);
        return new BodyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final BodyViewHolder holder, int position) {
        DataModel mData = listData.get(position);
        holder.typeView.setText(Badge.buildMsgType(mData.getType()));
        holder.labelView.setText(mData.getLabel());
        if(mData.getCreated_at().before(new Date())) holder.timeView.setText(FuzzyDateTimeFormatter.getTimeAgo(mContext, mData.getCreated_at()));
        else holder.timeView.setText("刚刚");
        holder.contentView.setText(getContent(mData.getContent().toString()));
    }

    public static String getContent (String val) {
        if (Objects.equals(val, "online")) return "设备上线";
        else if (Objects.equals(val, "offline")) return "设备离线";
        else if (Objects.equals(val, "empty")) return "数据清空";
        else if (Objects.equals(val, "create")) return "设备创建";
        else if (Objects.equals(val, "activate")) return "设备激活";
        return val;
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }


    public class BodyViewHolder extends RecyclerView.ViewHolder {
        private TextView typeView;
        private TextView labelView;
        private TextView contentView;
        private TextView timeView;
        public BodyViewHolder(View itemView) {
            super(itemView);
            typeView = (TextView) itemView.findViewById(R.id.data_tv_type);
            labelView = (TextView) itemView.findViewById(R.id.data_tv_label);
            contentView = (TextView) itemView.findViewById(R.id.data_tv_content);
            timeView = (TextView) itemView.findViewById(R.id.data_tv_time);
        }
    }
}
