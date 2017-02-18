package net.noahgao.freeiot.ui.pages;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.noahgao.freeiot.R;
import net.noahgao.freeiot.model.WifiResultModel;
import net.noahgao.freeiot.ui.InitDeviceActivity;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link InitDeviceFinishFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link InitDeviceFinishFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InitDeviceFinishFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public InitDeviceFinishFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment InitDeviceFinishFragment.
     */
    public static InitDeviceFinishFragment newInstance() {
        return new InitDeviceFinishFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_init_device_finish, container, false);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        WifiResultModel mResult = ((InitDeviceActivity) getActivity()).getmResult();
        ((TextView) view.findViewById(R.id.finish_tv_ota)).setText("设备固件OTA支持：");
        ((TextView) view.findViewById(R.id.finish_tv_version)).setText("FreeIOT固件版本：v"+ mResult.getVersion());
        if(!mResult.isOtaSupport()) ((TextView) view.findViewById(R.id.finish_tv_ota)).append("不支持");
        else ((TextView) view.findViewById(R.id.finish_tv_ota)).append("支持");
        mListener.onReadyForFinish();
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
        void onReadyForFinish();
    }
}
