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

package net.noahgao.freeiot.ui.pages;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import net.noahgao.freeiot.R;
import net.noahgao.freeiot.adapter.wifiResultsAdapter;
import net.noahgao.freeiot.model.WifiResultModel;
import net.noahgao.freeiot.ui.InitDeviceActivity;
import net.noahgao.freeiot.util.DialogUtil;
import net.noahgao.freeiot.util.WifiAdmin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link InitDeviceFindFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link InitDeviceFindFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InitDeviceFindFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private WifiAdmin mWifi;
    private List<ScanResult> listWifi = new ArrayList<>();
    private List<WifiResultModel> listResults = new ArrayList<>();
    private ProgressDialog mProgressDialog;

    private View mEmptyView;
    private Button mRefreshBtn;
    private refreshListener mRefreshListener;
    private RecyclerView mRecyclerView;
    private wifiResultsAdapter mAdapter;

    public InitDeviceFindFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment InitDeviceFindFragment.
     */
    public static InitDeviceFindFragment newInstance() {
        return new InitDeviceFindFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWifi = WifiAdmin.getInstance(getActivity());
        mRefreshListener = new refreshListener();
    }

    public class refreshListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            initwifi();
        }
    }

    public void initwifi() {
        if (mWifi != null) {
            mWifi.OpenWifi();
            mProgressDialog = ProgressDialog.show(getActivity(),"提示","正在寻找设备");
            if(mWifi.startScan()){
                boolean t = mWifi.getWifiState();
                listWifi = mWifi.getWifiList();
                if(listWifi.size() == 0)
                    DialogUtil.showSingleDialog(getActivity(), "警告", "设备寻找出错，是否存在以下问题：\n1.您的周围没有任何Wifi热点\n2.Android M（6.0）以上用户需打开位置开关并给予相应权限", "确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            getActivity().finish();
                        }
                    });
                else {
                    final Handler mHandler = new deviceFindHandler(InitDeviceFindFragment.this);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if(mProgressDialog!=null) mProgressDialog.dismiss();
                            listResults.clear();
                            for(ScanResult o: listWifi) {
                                if(o.SSID.startsWith("FIOT_")) {
                                    try {
                                        listResults.add(new WifiResultModel(o.SSID.replaceAll("FIOT_","")));
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                            mHandler.sendEmptyMessage(0);
                        }
                    }).start();
                }
            }
        } else {
            DialogUtil.showSingleDialog(getActivity(), "系统警告", "Wifi状态异常\n入网向导错误101", "确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    getActivity().finish();
                }
            });
        }
    }
    public static class deviceFindHandler extends Handler {
        private InitDeviceFindFragment mContext;

        public deviceFindHandler (InitDeviceFindFragment context) {
            mContext = context;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if(mContext.listResults.size() == 0) {
                        mContext.mEmptyView.setVisibility(View.VISIBLE);
                        mContext.mRecyclerView.setVisibility(View.GONE);
                        mContext.mRefreshBtn.setOnClickListener(mContext.mRefreshListener);
                    } else {
                        mContext.mEmptyView.setVisibility(View.GONE);
                        mContext.mRecyclerView.setVisibility(View.VISIBLE);
                    }
                    mContext.mAdapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        initwifi();
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_init_device_find, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mEmptyView = view.findViewById(R.id.device_find_empty_view);
        mRefreshBtn = (Button) view.findViewById(R.id.device_find_empty_refresh_btn);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.device_find_view);
        mAdapter = new wifiResultsAdapter(listResults);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter.setOnItemClickListener(new wifiResultsAdapter.OnRecyclerViewItemClickListener(){
            @Override
            public void onItemClick(View view,WifiResultModel data) {
                ((InitDeviceActivity) getActivity()).setmResult(data);
                ((InitDeviceActivity) getActivity()).setmWifi(mWifi);
                mListener.onReadyForNext();
            }
        });
        mRecyclerView.setAdapter(mAdapter);
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
        void onReadyForNext();
    }
}
