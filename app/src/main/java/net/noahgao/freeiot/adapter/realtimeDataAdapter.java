package net.noahgao.freeiot.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;

import net.noahgao.freeiot.R;
import net.noahgao.freeiot.model.RealtimeDataModel;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Objects;

/**
 * Created by Noah Gao on 17-2-15.
 * By Android Studio
 */

public class realtimeDataAdapter extends XRecyclerView.Adapter<realtimeDataAdapter.BodyViewHolder> implements View.OnClickListener {

    private List<RealtimeDataModel> listData;
    public realtimeDataAdapter(List<RealtimeDataModel> datas) {
        this.listData = datas;
    }

    private realtimeDataAdapter.OnRecyclerViewItemClickListener mOnItemClickListener = null;
    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view , RealtimeDataModel data, int position);
    }

    @Override
    public BodyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_realtime,viewGroup,false);
        BodyViewHolder vh = new BodyViewHolder(view);
        view.setOnClickListener(this);
        return vh;
    }

    @Override
    public void onBindViewHolder(final BodyViewHolder holder, int position) {
        RealtimeDataModel mData = listData.get(position);
        ArrayMap<String,Object> tagData = new ArrayMap<>();
        tagData.put("data",mData);
        tagData.put("index",position);
        holder.itemView.setTag(tagData);
        holder.nameView.setText(mData.getName());
        holder.labelView.setText("(" + mData.getLabel() + ")");
        holder.switchView.setVisibility(View.GONE);
        if(Objects.equals(mData.getType(), "boolean")) {
            if (mData.isControll()) {
                if(!mData.getContent().equals("N/A")) {
                    Boolean r;
                    if(mData.getContent() instanceof Integer)
                        r = ((Integer) mData.getContent()) == 1;
                    else if (mData.getContent() instanceof Boolean)
                        r = (Boolean) mData.getContent();
                    else
                        r = true;
                    holder.dataView.setText(r ? "ON" : "OFF");
                    holder.switchView.setChecked(r);
                    holder.switchView.setVisibility(View.VISIBLE);
                    final realtimeDataAdapter tContext = realtimeDataAdapter.this;
                    holder.switchView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            tContext.onClick(holder.itemView);
                        }
                    });
                } else holder.dataView.setText(mData.getContent().toString());
            } else {
                if(!mData.getContent().equals("N/A")) holder.dataView.setText((Integer) mData.getContent() == 1 ? "YES" : "NO");
                else holder.dataView.setText(mData.getContent().toString());
            }
        } else {
            holder.dataView.setText(mData.getContent().toString());
            if ((mData.getContent().toString().length() + mData.getUnit().length()) > 5)
                holder.unitView.setText(mData.getUnit());
            else {
                holder.unitView.setText("");
                holder.dataView.append(mData.getUnit());
            }
        }
    }

    @Override
    @SuppressWarnings(value = {"unchecked"})
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            ArrayMap tagData = (ArrayMap) v.getTag();
            mOnItemClickListener.onItemClick(v, (RealtimeDataModel) tagData.get("data"), (Integer) tagData.get("index"));
        }
    }

    public void setOnItemClickListener(realtimeDataAdapter.OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }


    public class BodyViewHolder extends RecyclerView.ViewHolder {
        private TextView nameView;
        private TextView labelView;
        private TextView dataView;
        private TextView unitView;
        private Switch switchView;
        public BodyViewHolder(View itemView) {
            super(itemView);
            nameView = (TextView) itemView.findViewById(R.id.tv_realtime_name);
            labelView = (TextView) itemView.findViewById(R.id.tv_realtime_label);
            dataView = (TextView) itemView.findViewById(R.id.tv_realtime_data);
            unitView = (TextView) itemView.findViewById(R.id.tv_realtime_unit);
            switchView = (Switch) itemView.findViewById(R.id.tv_realtime_switch);
        }
    }
}
