package net.noahgao.freeiot.ui.pages;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import net.noahgao.freeiot.R;
import net.noahgao.freeiot.adapter.wifiResultsAdapter;
import net.noahgao.freeiot.model.WifiResultModel;
import net.noahgao.freeiot.ui.InitDeviceActivity;
import net.noahgao.freeiot.util.DialogUtil;
import net.noahgao.freeiot.util.WifiAdmin;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link InitDeviceWifiFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link InitDeviceWifiFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InitDeviceWifiFragment extends Fragment {

    private WifiResultModel mResult;
    private WifiAdmin mWifi;
    private String mSSID;
    private String mPassword;
    private WifiConfiguration mSavedConfig;

    private TextView mSSIDView;
    private TextView mTextView;
    private ProgressBar mProgressBar;
    private ImageView mDoneView;

    private OnFragmentInteractionListener mListener;
    private RecyclerView mRecyclerView;
    private wifiResultsAdapter mAdapter;
    private boolean mT=false;

    public InitDeviceWifiFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment InitDeviceWifiFragment.
     */
    public static InitDeviceWifiFragment newInstance() {
        return new InitDeviceWifiFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_init_device_wifi, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mResult=((InitDeviceActivity) getActivity()).getmResult();
        mWifi=((InitDeviceActivity) getActivity()).getmWifi();
        mSSID = mWifi.getWifiInfo().getSSID().replaceAll("\"","");
        mPassword = ((EditText) view.findViewById(R.id.device_wifi_password_input)).getText().toString();
        if(mResult.getName() == null) DialogUtil.showSingleDialog(getActivity(), "警告", "该设备不是已支持的产品！", "确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                getActivity().finish();
            }
        });
        mSSIDView = ((TextView) view.findViewById(R.id.device_init_current_wifi_tv));
        mTextView = ((TextView) view.findViewById(R.id.device_init_text));
        mProgressBar = (ProgressBar) view.findViewById(R.id.device_init_progressBar);
        mDoneView = (ImageView) view.findViewById(R.id.device_init_done);
        ((TextView) view.findViewById(R.id.device_init_current_product_tv)).setText(mResult.getName());
        ((TextView) view.findViewById(R.id.device_init_current_device_tv)).setText(mResult.getDeviceName() + " (FIOT_" + mResult.getProduct() + ")");
        mSSIDView.setText(mSSID);
        view.findViewById(R.id.device_change_wifi_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = getActivity().getLayoutInflater();
                View dialog = inflater.inflate(R.layout.item_dialog_choosewifi,(ViewGroup) getActivity().findViewById(R.id.choosewifi_dialog));
                mRecyclerView = (RecyclerView) dialog.findViewById(R.id.choosewifi_dialog);
                List<WifiResultModel> listResults = new ArrayList<>();
                for (ScanResult o : mWifi.getWifiList()) {
                    try {
                        WifiResultModel t = new WifiResultModel(null);
                        t.setProduct(o.SSID);
                        if(Objects.equals(o.SSID, mSSID)) t.setSelected(true);
                        listResults.add(t);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                mAdapter = new wifiResultsAdapter(listResults);
                final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                mRecyclerView.setLayoutManager(layoutManager);
                mAdapter.setOnItemClickListener(new wifiResultsAdapter.OnRecyclerViewItemClickListener(){
                    @Override
                    public void onItemClick(View view,WifiResultModel data) {
                        mSSID = data.getProduct();
                        mSSIDView.setText(mSSID);
                    }
                });
                mRecyclerView.setAdapter(mAdapter);
                new AlertDialog.Builder(getActivity())
                        .setTitle("请选择")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setView(dialog)
                        .create().show();
            }
        });
        view.findViewById(R.id.device_connect_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.findViewById(R.id.device_init_progress).setVisibility(View.VISIBLE);
                mPassword = ((EditText) view.findViewById(R.id.device_wifi_password_input)).getText().toString();
                mSavedConfig = mWifi.isExsits(mSSID);
                view.findViewById(R.id.device_connect_btn).setEnabled(false);
                mWifi.addNetwork(mWifi.createWifiCfg("FIOT_"+mResult.getProduct(),mResult.getSecret(),3));
                initDevice();
            }
        });
    }

    public void initDevice() {
        final Handler mHandler = new nHandler(InitDeviceWifiFragment.this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean connected = false;
                Socket mSocket = null;
                BufferedReader mReader = null;
                BufferedWriter mWriter = null;
                while (!connected) {
                    try {
                        mSocket = new Socket("192.168.4.1", 3000);
                        mReader = new BufferedReader(new InputStreamReader(mSocket.getInputStream(), "utf-8"));
                        mWriter = new BufferedWriter(new OutputStreamWriter(mSocket.getOutputStream(), "utf-8"));
                        connected=true;
                    } catch (IOException ignored) {
                    }
                }
                mHandler.sendEmptyMessage(0);
                try {
                    mWriter.write("+D:"+mResult.getDevice()+","+mResult.getDeviceSecret()+";+W:"+mSSID+","+mPassword+";\n");
                    mWriter.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                boolean isStartRecieveMsg = true;
                while(isStartRecieveMsg) {
                    try {
                        if(mReader.ready()) {
                                /*读取一行字符串，读取的内容来自于客户机
                                reader.readLine()方法是一个阻塞方法，
                                从调用这个方法开始，该线程会一直处于阻塞状态，
                                直到接收到新的消息，代码才会往下走*/
                            String data = mReader.readLine();
                            isStartRecieveMsg=false;
                            String a[] = data.split(";");
                            if(a.length<4){
                                mHandler.sendEmptyMessage(3);
                                break;
                            }
                            if((!Objects.equals(a[2], "+C") || !Objects.equals(a[3], "+C"))) {
                                mHandler.sendEmptyMessage(3);
                                break;
                            } else mHandler.sendEmptyMessage(1);
                            String b[] = a[0].split(",");
                            mResult.setVersion(b[0].replaceAll("V:","")+b[1]+b[2]);
                            if(Objects.equals(b[3], "1")) mResult.setOtaSupport(true);
                            else mResult.setOtaSupport(false);
                            mHandler.sendEmptyMessage(2);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    mReader.close();
                    mWriter.close();
                    mSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }public static class nHandler extends Handler {
        private InitDeviceWifiFragment mContext;

        public nHandler (InitDeviceWifiFragment context) {
            mContext=context;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    mContext.mTextView.setText("已连接到设备，正在写入...");
                    break;
                case 1:
                    mContext.mTextView.setText("已完成，正在收尾...");
                    break;
                case 2:
                    mContext.mProgressBar.setVisibility(View.GONE);
                    mContext.mDoneView.setVisibility(View.VISIBLE);
                    mContext.mTextView.setText("已完成设备初始化");
                    //断开当前的连接
                    mContext.mWifi.disconnectCurrentNetwork();
                    if(mContext.mSavedConfig!=null) mContext.mWifi.addNetwork(mContext.mSavedConfig);
                    else mContext.mWifi.addNetwork(mContext.mWifi.createWifiCfg(mContext.mSSID,mContext.mPassword,3));
                    mContext.mListener.onReadyForNext();
                    break;
                case 3:
                    //断开当前的连接
                    mContext.mWifi.disconnectCurrentNetwork();
                    if(mContext.mSavedConfig!=null) mContext.mWifi.addNetwork(mContext.mSavedConfig);
                    else mContext.mWifi.addNetwork(mContext.mWifi.createWifiCfg(mContext.mSSID,mContext.mPassword,3));
                    mContext.handleWarning("初始化失败！设备返回了错误的回应信息！");
                    break;
                default:
                    break;
            }
        }

    }

    public void handleWarning (String msg) {
        DialogUtil.showSingleDialog(getActivity(), "警告", msg, "确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                getActivity().finish();
            }
        });
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
