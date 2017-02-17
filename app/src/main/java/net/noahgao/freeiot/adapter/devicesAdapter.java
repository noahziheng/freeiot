package net.noahgao.freeiot.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;

import net.noahgao.freeiot.R;
import net.noahgao.freeiot.api.ApiClient;
import net.noahgao.freeiot.model.DeviceModel;
import net.noahgao.freeiot.model.ProductModel;
import net.noahgao.freeiot.model.ProductSimpleModel;
import net.noahgao.freeiot.model.UserModel;
import net.noahgao.freeiot.util.Auth;
import net.noahgao.freeiot.util.Badge;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Noah Gao on 17-2-12.
 * By Android Studio
 */

public class devicesAdapter extends XRecyclerView.Adapter<devicesAdapter.ViewHolder> implements View.OnClickListener {

    public List<DeviceModel.DeviceMeta.DeviceMetaModel<ProductSimpleModel<String>>> datas = null;
    public devicesAdapter(List<DeviceModel.DeviceMeta.DeviceMetaModel<ProductSimpleModel<String>>> datas) {
        this.datas = datas;
    }

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;
    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view , DeviceModel.DeviceMeta.DeviceMetaModel<ProductSimpleModel<String>> data);
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
        DeviceModel.DeviceMeta.DeviceMetaModel<ProductSimpleModel<String>> mDevice = datas.get(position);
        viewHolder.mTitleView.setText(mDevice.getName());
        viewHolder.mStatusView.setText(Badge.buildStatus(mDevice.getStatus()));
        viewHolder.mDescView.setText(mDevice.getProduct().getName() + " (" + mDevice.getProduct().getOwner() + ")");
        if(!mDevice.getProduct().getOwner().contains("@")) {
            Call<UserModel> call = ApiClient.API.getUser(mDevice.getProduct().getOwner(), Auth.getToken());
            call.enqueue(new TempCallback<UserModel>(position, mDevice));
        }
        viewHolder.itemView.setTag(datas.get(position));
    }

    @Override
    @SuppressWarnings(value = {"unchecked"})
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemClickListener.onItemClick(v, (DeviceModel.DeviceMeta.DeviceMetaModel<ProductSimpleModel<String>>) v.getTag());
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

    private class TempCallback<T> implements Callback<T> {

        final int index;
        private final DeviceModel.DeviceMeta.DeviceMetaModel<ProductSimpleModel<String>> nDevice;

        TempCallback (int index, DeviceModel.DeviceMeta.DeviceMetaModel<ProductSimpleModel<String>> nDevice) {
            this.index = index;
            this.nDevice = nDevice;
        }

        @Override
        public void onResponse(Call<T> call, Response<T> response) {
            if(response.isSuccessful() && response.body() instanceof UserModel) {
                nDevice.getProduct().setOwner(((UserModel) response.body()).getEmail());
                datas.set(index,nDevice);
                devicesAdapter.this.notifyDataSetChanged();
            }
        }

        @Override
        public void onFailure(Call<T> call, Throwable t) {
            t.printStackTrace();
        }
    }
}
