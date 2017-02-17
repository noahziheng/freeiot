package net.noahgao.freeiot.ui.pages;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import net.noahgao.freeiot.R;
import net.noahgao.freeiot.api.ApiClient;
import net.noahgao.freeiot.model.DeviceModel;
import net.noahgao.freeiot.util.Auth;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DeviceOptionsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * create an instance of this fragment.
 */
public class DeviceOptionsFragment extends Fragment {

    private DeviceModel device;

    private OnFragmentInteractionListener mListener;

    public DeviceOptionsFragment() {}

    public static DeviceOptionsFragment newInstance(DeviceModel arg) {
        DeviceOptionsFragment fragment = new DeviceOptionsFragment();
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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);
        view.findViewById(R.id.device_empty_btn).setOnClickListener(new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("提示")
                        .setMessage("真的要这么做么？")
                        .setPositiveButton( "确定",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Call<Object> call = ApiClient.API.emptyData(device.getMeta().getDevice().get_id(), Auth.getToken());
                                        call.enqueue(new TempCallback());
                                    }
                                })
                        .setNegativeButton( "取消", null).show();
            }
        });
        view.findViewById(R.id.device_delete_btn).setOnClickListener(new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("提示")
                        .setMessage("真的要这么做么？")
                        .setPositiveButton( "确定",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Call<Object> call = ApiClient.API.deleteDevice(device.getMeta().getDevice().get_id(), Auth.getToken());
                                    call.enqueue(new TempCallback());
                                }
                            })
                        .setNegativeButton( "取消", null).show();
            }
        });
        view.findViewById(R.id.device_disconnect_btn).setOnClickListener(new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("提示")
                        .setMessage("强制下线只适用于数据库中设备状态与实际情况不符时用于校正，不能真的用于将设备从MQTT队列中移除！如有需要请从硬件层级操作！\n")
                        .setPositiveButton( "确定",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Call<Object> call = ApiClient.API.deleteDevice(device.getMeta().getDevice().get_id(), Auth.getToken());
                                        call.enqueue(new TempCallback());
                                    }
                                })
                        .setNegativeButton( "取消", null).show();
            }
        });
        view.findViewById(R.id.device_rename_btn).setOnClickListener(new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = getActivity().getLayoutInflater();
                View dialog = inflater.inflate(R.layout.item_dialog_rename,(ViewGroup) getActivity().findViewById(R.id.device_rename_view));
                final EditText editText = (EditText) dialog.findViewById(R.id.device_rename_value);
                editText.setText(device.getMeta().getDevice().getName());
                AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());  //先得到构造器
                builder.setTitle("设备改名");
                builder.setView(dialog);
                builder.setPositiveButton("确定",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        Call<Object> call = ApiClient.API.renameDevice(device.getMeta().getDevice().get_id(), editText.getText().toString(), Auth.getToken());
                        call.enqueue(new TempCallback());
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
        });

    }

    private class TempOnClickListener implements View.OnClickListener {

        private Call<Object> mCall;

        public TempOnClickListener (Call<Object> call) {
            mCall = call;
        }

        @Override
        public void onClick(View v) {
            Call<Object> call = mCall;
            call.enqueue(new TempCallback());
        }

    }

    private class TempCallback implements Callback<Object> {
        @Override
        public void onResponse(Call<Object> call, Response<Object> response) {
            if(response.isSuccessful()) {
                Toast.makeText(getActivity(),"操作成功！",Toast.LENGTH_SHORT).show();
                getActivity().finish();
            } else Toast.makeText(getActivity(),"请求失败！请重试！",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onFailure(Call<Object> call, Throwable t) {
            t.printStackTrace();
            Toast.makeText(getActivity(),"请求失败！请重试！",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_device_options, container, false);
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
