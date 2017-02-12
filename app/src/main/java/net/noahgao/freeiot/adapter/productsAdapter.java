package net.noahgao.freeiot.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.noahgao.freeiot.R;
import net.noahgao.freeiot.model.ProductSimpleModel;

import java.util.List;

/**
 * Created by Noah Gao on 17-2-12.
 * By Android Studio
 */

public class productsAdapter extends RecyclerView.Adapter<productsAdapter.ViewHolder> {

    public List<ProductSimpleModel> datas = null;
    public productsAdapter(List<ProductSimpleModel> datas) {
        this.datas = datas;
    }
    //创建新View，被LayoutManager所调用
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item,viewGroup,false);
        return new ViewHolder(view);
    }
    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.mTitleView.setText(datas.get(position).getName());
        viewHolder.mDescView.setText(datas.get(position).getCommit());
        viewHolder.mStatusView.setText("");
    }
    //获取数据的数量
    @Override
    public int getItemCount() {
        return datas.size();
    }
    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mStatusView;
        public TextView mTitleView;
        public TextView mDescView;
        public ViewHolder(View view){
            super(view);
            mTitleView = (TextView) view.findViewById(R.id.tv_name);
            mDescView = (TextView) view.findViewById(R.id.tv_description);
            mStatusView = (TextView) view.findViewById(R.id.tv_status);

        }
    }
}
