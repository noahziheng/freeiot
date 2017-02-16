package net.noahgao.freeiot.ui.pages;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import net.noahgao.freeiot.R;
import net.noahgao.freeiot.adapter.realtimeDataAdapter;
import net.noahgao.freeiot.api.ApiClient;
import net.noahgao.freeiot.model.DataModel;
import net.noahgao.freeiot.model.DeviceModel;
import net.noahgao.freeiot.model.ModModel;
import net.noahgao.freeiot.model.ProductModel;
import net.noahgao.freeiot.model.RealtimeDataModel;
import net.noahgao.freeiot.util.Auth;
import net.noahgao.freeiot.util.Badge;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DeviceRealtimeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class DeviceRealtimeFragment extends Fragment {

    private DeviceModel device;
    XRecyclerView mRecyclerView;
    realtimeDataAdapter mAdapter;
    List<RealtimeDataModel> listData;

    private OnFragmentInteractionListener mListener;

    public DeviceRealtimeFragment() {}

    public static DeviceRealtimeFragment newInstance(DeviceModel arg) {
        DeviceRealtimeFragment fragment = new DeviceRealtimeFragment();
        Bundle bundle = new Bundle();
        bundle.putString("DEVICE", JSON.toJSONString(arg));
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            device = JSON.parseObject(getArguments().getString("DEVICE"),new TypeReference<DeviceModel>() {});
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_device_realtime, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        // throw new RuntimeException();
        super.onViewCreated(view, savedInstanceState);
        if(device.getMeta().getDevice().getStatus() > 2) {
            view.findViewById(R.id.device_lost_view).setVisibility(View.GONE);
            mRecyclerView = (XRecyclerView) view.findViewById(R.id.device_realtime_view);
            listData = new ArrayList<>();
            mAdapter = new realtimeDataAdapter(listData);
            final GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
            mRecyclerView.setLoadingMoreEnabled(false);
            mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
                @Override
                public void onRefresh() { getDatas(true); }

                @Override
                public void onLoadMore() {}
            });
            mRecyclerView.setLayoutManager(layoutManager);
            mAdapter.setOnItemClickListener(new realtimeDataAdapter.OnRecyclerViewItemClickListener(){
                @Override
                public void onItemClick(final View view, final RealtimeDataModel data, final int position) {
                    if(data.isControll()) {
                        final ArrayMap<String, Object> query = new ArrayMap<>();
                        if(Objects.equals(data.getType(), "boolean")) {
                            final Boolean r;
                            if(data.getContent() instanceof Integer)
                                r = ((Integer) data.getContent()) == 1;
                            else if (data.getContent() instanceof Boolean)
                                r = (Boolean) data.getContent();
                            else
                                r = true;
                            query.put(data.getLabel(),!r);
                            Call<JSONArray> call = ApiClient.API.putData(device.getMeta().getDevice().get_id(), query, Auth.getToken());
                            call.enqueue(new Callback<JSONArray>() {
                                @Override
                                public void onResponse(Call<JSONArray> call, Response<JSONArray> response) {
                                    if (response.isSuccessful()) {
                                        Toast.makeText(getActivity(), "控制指令已下发", Toast.LENGTH_SHORT).show();
                                        RealtimeDataModel<Boolean> newData = new RealtimeDataModel<>();
                                        newData.setControll(data.isControll());
                                        newData.setLabel(data.getLabel());
                                        newData.setName(data.getName());
                                        newData.setType(data.getType());
                                        newData.setUnit(data.getUnit());
                                        newData.setContent(!r);
                                        listData.set(position, newData);
                                        mAdapter.notifyDataSetChanged();
                                    }

                                }

                                @Override
                                public void onFailure(Call<JSONArray> call, Throwable t) {
                                    t.printStackTrace();
                                }
                            });
                        } else {
                            LayoutInflater inflater = getActivity().getLayoutInflater();
                            View dialog = inflater.inflate(R.layout.item_dialog_editdata,(ViewGroup) getActivity().findViewById(R.id.edit_data_view));
                            final EditText editText = (EditText) dialog.findViewById(R.id.edit_data_value);
                            AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());  //先得到构造器
                            builder.setTitle("编辑控制点数据");
                            builder.setView(dialog);
                            builder.setPositiveButton("确定",new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(final DialogInterface dialog, int which) {
                                    query.put(data.getLabel(), editText.getText().toString());
                                    Call<JSONArray> call = ApiClient.API.putData(device.getMeta().getDevice().get_id(), query, Auth.getToken());
                                    call.enqueue(new Callback<JSONArray>() {
                                        @Override
                                        public void onResponse(Call<JSONArray> call, Response<JSONArray> response) {
                                            if (response.isSuccessful()) {
                                                Toast.makeText(getActivity(), "控制指令已下发", Toast.LENGTH_SHORT).show();
                                                dialog.dismiss();
                                                RealtimeDataModel<String> newData = new RealtimeDataModel<>();
                                                newData.setControll(data.isControll());
                                                newData.setLabel(data.getLabel());
                                                newData.setName(data.getName());
                                                newData.setType(data.getType());
                                                newData.setUnit(data.getUnit());
                                                newData.setContent(editText.getText().toString());
                                                listData.set(position, newData);
                                                mAdapter.notifyDataSetChanged();
                                            }

                                        }

                                        @Override
                                        public void onFailure(Call<JSONArray> call, Throwable t) {
                                            t.printStackTrace();
                                        }
                                    });
                                }
                            });
                            builder.setNegativeButton("取消",new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            builder.create().show();

                        }
                    } else {
                        Snackbar.make(view, "该功能暂未上线", Snackbar.LENGTH_SHORT).show();
                    }
                }
            });
            mRecyclerView.setAdapter(mAdapter);
            getDatas(false);
        } else {
            view.findViewById(R.id.device_realtime_view).setVisibility(View.GONE);
            ((TextView) view.findViewById(R.id.device_status_badge)).append(Badge.buildStatus(device.getMeta().getDevice().getStatus(), 50));
        }
    }

    //独立线程抓取、解析数据
    public void getDatas(final boolean refreshTag) {
        final Handler mHandler = new nHandler(DeviceRealtimeFragment.this,refreshTag);
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(refreshTag) {
                    try {
                        Response<DeviceModel> result = ApiClient.API.getDevice(device.getMeta().getDevice().get_id(),Auth.getToken()).execute();
                        if(result.isSuccessful()) device = result.body();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                listData.clear();
                listData.addAll(parseRealtimeData(device));
                mHandler.sendEmptyMessage(0);
            }
        }).start();
    }
    public static class nHandler extends Handler {
        private final DeviceRealtimeFragment mContext;
        private final boolean mRefreshTag;

        public nHandler (DeviceRealtimeFragment context,boolean refreshTag) {
            mContext = context;
            mRefreshTag = refreshTag;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    mContext.mAdapter.notifyDataSetChanged();
                    if(mRefreshTag) mContext.mRecyclerView.refreshComplete();
                    break;
                default:
                    break;
            }
        }

    }

    // 将API数据集/模块信息解析为实时数据模型
    // NoahGao 2017
    public List<RealtimeDataModel> parseRealtimeData (DeviceModel device) {
        List<ProductModel<String>.ProductMod> listProductMods = device.getMeta().getDevice().getProduct().getMods();
        List<DataModel> listDatas = device.getData();
        ArrayMap<String, DataModel> listLatestDatas = new ArrayMap<>();
        List<JSONObject> listPoints = new ArrayList<>();
        List<RealtimeDataModel> listResults = new ArrayList<>();
        List<RealtimeDataModel> listControlls = new ArrayList<>();
        //联网解析模块信息
        for (ProductModel<String>.ProductMod o: listProductMods) {
            try {
                JSONObject vt = o.getVars();
                vt.put("hidden",o.getHidden());
                Response<ModModel> result = ApiClient.API.getMod(o.getOrigin(),vt, Auth.getToken()).execute();
                if(result.isSuccessful()) {
                    if(result.body().getUplink()!=null) {
                        for (JSONObject j : result.body().getUplink()) {
                            j.put("controll",false);
                            listPoints.add(j);
                        }
                    }
                    if(result.body().getDownlink()!=null) {
                        for (JSONObject j : result.body().getDownlink()) {
                            j.put("controll",true);
                            listPoints.add(j);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //遍历数据集获取最新数据
        for (DataModel p: listDatas) {
            if((!listLatestDatas.containsKey(p.getLabel()) || listLatestDatas.get(p.getLabel()).getCreated_at().before(p.getCreated_at())) && !Objects.equals(p.getLabel(), "SYS")) listLatestDatas.put(p.getLabel(),p);
        }
        //转换为实时数据格式
        for(JSONObject q: listPoints) {
            RealtimeDataModel<Object> t = new RealtimeDataModel<>();
            t.setName(q.getString("name"));
            t.setLabel(q.getString("label"));
            t.setType(q.getJSONObject("format").getString("type"));
            t.setUnit(q.getJSONObject("format").getString("unit"));
            t.setControll(q.getBoolean("controll"));
            if(listLatestDatas.containsKey(q.getString("label"))) t.setContent(listLatestDatas.get(q.getString("label")).getContent());
            else t.setContent("N/A");
            if(q.getBoolean("controll")) listControlls.add(t);
            else listResults.add(t);
        }
        listResults.addAll(listControlls);//保证设备上行数据在前
        return listResults;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
