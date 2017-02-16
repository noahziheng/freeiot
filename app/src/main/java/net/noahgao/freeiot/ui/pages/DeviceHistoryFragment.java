package net.noahgao.freeiot.ui.pages;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import net.noahgao.freeiot.R;
import net.noahgao.freeiot.adapter.datasAdapter;
import net.noahgao.freeiot.api.ApiClient;
import net.noahgao.freeiot.model.DataModel;
import net.noahgao.freeiot.model.DeviceModel;
import net.noahgao.freeiot.util.Auth;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DeviceHistoryFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * create an instance of this fragment.
 */
public class DeviceHistoryFragment extends Fragment {

    private int mTime=1;
    View mView;
    Spinner mSpinner;
    private DeviceModel device;
    XRecyclerView mRecyclerView;
    datasAdapter mAdapter;
    List<DataModel> listData;

    private OnFragmentInteractionListener mListener;

    public DeviceHistoryFragment() {}

    public static DeviceHistoryFragment newInstance(DeviceModel arg) {
        DeviceHistoryFragment fragment = new DeviceHistoryFragment();
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
        return inflater.inflate(R.layout.fragment_device_history, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        // throw new RuntimeException();
        super.onViewCreated(view, savedInstanceState);
        mView = view;
        mSpinner = (Spinner) view.findViewById(R.id.device_get_data_spinner);
        if(mTime == 1) mSpinner.setSelection(0,true);
        else mSpinner.setSelection(mTime / 12,true);
        view.findViewById(R.id.device_get_data_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTime = (int) mSpinner.getSelectedItemId();
                if(mTime==0) mTime=1;
                else mTime=mTime*12;
                getDatas(false,true);
            }
        });
        mRecyclerView = (XRecyclerView) view.findViewById(R.id.device_history_view);
        listData = new ArrayList<>();
        mAdapter = new datasAdapter(listData,getActivity());
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        Drawable dividerDrawable = ContextCompat.getDrawable(getActivity(), R.drawable.divider_sample);
        mRecyclerView.addItemDecoration(mRecyclerView.new DividerItemDecoration(dividerDrawable));
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() { getDatas(true,false); }

            @Override
            public void onLoadMore() {
                if(mTime==1) mTime=0;
                mTime=mTime+12;
                if(mTime > 72) mTime=72;
                getDatas(false,true);
            }
        });
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
        getDatas(false,false);
    }

    //独立线程抓取、解析数据
    public void getDatas(final boolean refreshTag, final boolean moreTag) {
        final Handler mHandler = new nHandler(DeviceHistoryFragment.this,refreshTag, moreTag);
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(refreshTag || moreTag) {
                    try {
                        Response<DeviceModel> result = ApiClient.API.getDevice(device.getMeta().getDevice().get_id(),mTime,Auth.getToken()).execute();
                        if(result.isSuccessful()) {
                            device = result.body();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                listData.clear();
                listData.addAll(device.getData());
                Collections.sort(listData, new Comparator<DataModel>() {
                    @Override
                    public int compare(DataModel o1, DataModel o2) {
                        if(o1.getCreated_at().before(o2.getCreated_at())) return 1;
                        else return -1;
                    }
                });
                mHandler.sendEmptyMessage(0);
            }
        }).start();
    }
    public static class nHandler extends Handler {
        private final DeviceHistoryFragment mContext;
        private final boolean mRefreshTag;
        private final boolean mMoreTag;

        public nHandler (DeviceHistoryFragment context,boolean refreshTag,boolean moreTag) {
            mContext = context;
            mRefreshTag = refreshTag;
            mMoreTag = moreTag;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    mContext.mAdapter.notifyDataSetChanged();
                    if(mRefreshTag)
                        mContext.mRecyclerView.refreshComplete();
                    if(mMoreTag)
                        mContext.mRecyclerView.loadMoreComplete();
                    if(mContext.listData.size() > 0)
                        mContext.mView.findViewById(R.id.device_history_empty_view).setVisibility(View.GONE);
                    if(mContext.mTime == 1)
                        mContext.mSpinner.setSelection(0,true);
                    else
                        mContext.mSpinner.setSelection(mContext.mTime / 12,true);
                    break;
                default:
                    break;
            }
        }

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
