package net.noahgao.freeiot.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import net.noahgao.freeiot.R;
import net.noahgao.freeiot.model.WifiResultModel;

import java.util.List;

/**
 * Created by Noah Gao on 17-2-17.
 * By Android Studio
 */

public class wifiResultsAdapter extends RecyclerView.Adapter<wifiResultsAdapter.ViewHolder> implements View.OnClickListener {

    public List<WifiResultModel> datas = null;
    public wifiResultsAdapter(List<WifiResultModel> datas) {
        this.datas = datas;
        //实现单选方法二： 设置数据集时，找到默认选中的pos
        for (int i = 0; i < datas.size(); i++) {
            if (datas.get(i).isSelected()) {
                mSelectedPos = i;
            }
        }
    }

    private int mSelectedPos = -1;//实现单选  方法二，变量保存当前选中的position

    private wifiResultsAdapter.OnRecyclerViewItemClickListener mOnItemClickListener = null;
    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view , WifiResultModel data);
    }

    //创建新View，被LayoutManager所调用
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_find_device_btn,viewGroup,false);
        ViewHolder vh = new ViewHolder(view);
        view.setOnClickListener(this);
        return vh;
    }
    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {
        viewHolder.mBtn.setChecked(datas.get(position).isSelected());
        String t;
        if(datas.get(position).getName()!=null) t = datas.get(position).getName() + " (FIOT_" + datas.get(position).getProduct() + ")";
        else t = datas.get(position).getProduct();
        viewHolder.mBtn.setText(t);
        viewHolder.mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSelectedPos!=viewHolder.getAdapterPosition()){
                    //先取消上个item的勾选状态
                    if(mSelectedPos!=-1) {
                        datas.get(mSelectedPos).setSelected(false);
                        notifyItemChanged(mSelectedPos);
                    }
                    //设置新Item的勾选状态
                    mSelectedPos = viewHolder.getAdapterPosition();
                    datas.get(mSelectedPos).setSelected(true);
                    notifyItemChanged(mSelectedPos);
                    if (mOnItemClickListener != null) {
                        //注意这里使用getTag方法获取数据
                        mOnItemClickListener.onItemClick(view, datas.get(viewHolder.getAdapterPosition()));
                    }
                }
            }
        });
        viewHolder.itemView.setTag(datas.get(position));
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemClickListener.onItemClick(v, (WifiResultModel) v.getTag());
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
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public RadioButton mBtn;
        public ViewHolder(View view){
            super(view);
            mBtn = (RadioButton) view.findViewById(R.id.device_find_radio);
        }
    }
}
