package net.noahgao.freeiot.ui.pages;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;

import net.noahgao.freeiot.R;
import net.noahgao.freeiot.api.ApiClient;
import net.noahgao.freeiot.model.WifiResultModel;
import net.noahgao.freeiot.ui.InitDeviceActivity;
import net.noahgao.freeiot.util.Auth;
import net.noahgao.freeiot.util.DialogUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link InitDeviceCreateFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link InitDeviceCreateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InitDeviceCreateFragment extends Fragment {

    WifiResultModel mResult;
    private OnFragmentInteractionListener mListener;

    public InitDeviceCreateFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment InitDeviceCreateFragment.
     */
    public static InitDeviceCreateFragment newInstance() {
        return new InitDeviceCreateFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_init_device_create, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mResult=((InitDeviceActivity) getActivity()).getmResult();
        if(mResult.getName() == null) handleWarning("该设备不是已支持的产品！");
        ((EditText) view.findViewById(R.id.device_create_input)).setText(mResult.getName());
        view.findViewById(R.id.device_create_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.findViewById(R.id.device_create_progress).setVisibility(View.VISIBLE);
                Call<JSONObject> call = ApiClient.API.putDevice(((EditText) view.findViewById(R.id.device_create_input)).getText().toString(),Auth.getUser().get_id(),mResult.getProduct(),Auth.getToken());
                call.enqueue(new Callback<JSONObject>() {
                    @Override
                    public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                        if(response.isSuccessful()) {
                            mResult.setDevice(response.body().getString("_id"));
                            mResult.setDeviceName(response.body().getString("name"));
                            mResult.setDeviceSecret(response.body().getString("secret"));
                            view.findViewById(R.id.device_create_done).setVisibility(View.VISIBLE);
                            view.findViewById(R.id.device_create_progressBar).setVisibility(View.GONE);
                            ((TextView) view.findViewById(R.id.device_creating_text)).setText("设备创建完成！");
                            mListener.onReadyForNext();
                        } else {
                            handleWarning("设备创建失败！\n"+response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<JSONObject> call, Throwable t) {
                        t.printStackTrace();
                        handleWarning("设备创建失败！\n"+t.getMessage());
                    }
                });
            }
        });
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
        void onReadyForFinish();
    }
}
