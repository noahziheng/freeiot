package net.noahgao.freeiot.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;

import net.noahgao.freeiot.R;
import net.noahgao.freeiot.model.RealtimeDataModel;

import java.util.List;

/**
 * Created by Noah Gao on 17-2-15.
 * By Android Studio
 */

public class realtimeDataAdapter extends XRecyclerView.Adapter<realtimeDataAdapter.BodyViewHolder> {

    private List<RealtimeDataModel> listData;
    public realtimeDataAdapter(List<RealtimeDataModel> datas) {
        this.listData = datas;
    }

    @Override
    public BodyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.realtime_item,viewGroup,false);
        return new BodyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BodyViewHolder holder, int position) {
        holder.nameView.setText(listData.get(position).getName());
        holder.labelView.setText("(" + listData.get(position).getLabel() + ")");
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }


    public class BodyViewHolder extends RecyclerView.ViewHolder {
        private TextView nameView;
        private TextView labelView;
        public BodyViewHolder(View itemView) {
            super(itemView);
            nameView = (TextView) itemView.findViewById(R.id.tv_realtime_name);
            labelView = (TextView) itemView.findViewById(R.id.tv_realtime_label);
        }
    }
}
