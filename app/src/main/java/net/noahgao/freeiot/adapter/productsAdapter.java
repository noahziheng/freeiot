package net.noahgao.freeiot.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;

import net.noahgao.freeiot.R;
import net.noahgao.freeiot.model.ProductSimpleModel;

import java.util.List;

/**
 * Created by Noah Gao on 17-2-12.
 * By Android Studio
 */

public class productsAdapter extends XRecyclerView.Adapter<productsAdapter.ViewHolder> implements View.OnClickListener {

    public List<ProductSimpleModel> datas = null;
    public productsAdapter(List<ProductSimpleModel> datas) {
        this.datas = datas;
    }

    private productsAdapter.OnRecyclerViewItemClickListener mOnItemClickListener = null;
    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view , ProductSimpleModel data);
    }

    //创建新View，被LayoutManager所调用
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item,viewGroup,false);
        ViewHolder vh = new ViewHolder(view);
        view.setOnClickListener(this);
        return vh;
    }
    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.mTitleView.setText(datas.get(position).getName());
        viewHolder.mDescView.setText(datas.get(position).getCommit());
        viewHolder.mStatusView.setText("");
        viewHolder.itemView.setTag(datas.get(position));
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemClickListener.onItemClick(v, (ProductSimpleModel) v.getTag());
        }
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    //获取数据的数量
    @Override
    public int getItemCount() {
        return datas.size();
    }
    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends XRecyclerView.ViewHolder {
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
