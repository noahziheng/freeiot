package net.noahgao.freeiot.ui.pages;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import net.noahgao.freeiot.R;
import net.noahgao.freeiot.adapter.wifiResultsAdapter;
import net.noahgao.freeiot.model.WifiResultModel;
import net.noahgao.freeiot.ui.InitDeviceActivity;
import net.noahgao.freeiot.util.DialogUtil;
import net.noahgao.freeiot.util.WifiAdmin;

import java.io.IOException;
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

    private TextView mSSIDView;

    private OnFragmentInteractionListener mListener;
    private RecyclerView mRecyclerView;
    private wifiResultsAdapter mAdapter;

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
        void onReadyForFinish();
    }
}
