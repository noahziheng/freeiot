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

import java.util.List;
import java.util.Objects;

import si.virag.fuzzydateformatter.FuzzyDateTimeFormatter;

/**
 * Created by Noah Gao on 17-2-15.
 * By Android Studio
 */

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
        holder.timeView.setText(FuzzyDateTimeFormatter.getTimeAgo(mContext, mData.getCreated_at()));
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
