package net.noahgao.freeiot.pages;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jcodecraeer.xrecyclerview.XRecyclerView;

import net.noahgao.freeiot.ui.ProductActivity;
import net.noahgao.freeiot.R;
import net.noahgao.freeiot.adapter.productsAdapter;
import net.noahgao.freeiot.api.ApiClient;
import net.noahgao.freeiot.model.ProductSimpleModel;
import net.noahgao.freeiot.util.Auth;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProductsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProductsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductsFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private XRecyclerView mRecyclerView;
    private productsAdapter mAdapter;
    private List<ProductSimpleModel> listData;

    public ProductsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ProductsFragment.
     */
    public static ProductsFragment newInstance() {
        return new ProductsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_products, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = (XRecyclerView) view.findViewById(R.id.products_view);
        listData = new ArrayList<>();
        mAdapter = new productsAdapter(listData);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        Drawable dividerDrawable = ContextCompat.getDrawable(getActivity(), R.drawable.divider_sample);
        mRecyclerView.addItemDecoration(mRecyclerView.new DividerItemDecoration(dividerDrawable));
        mRecyclerView.setLoadingMoreEnabled(false);
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() { getProducts(true); }

            @Override
            public void onLoadMore() {}
        });
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter.setOnItemClickListener(new productsAdapter.OnRecyclerViewItemClickListener(){
            @Override
            public void onItemClick(View view, ProductSimpleModel data) {
                Intent intent = new Intent(getActivity(), ProductActivity.class);
                intent.putExtra("id",data.get_id());
                intent.putExtra("name",data.getName());
                startActivity(intent);
                Toast.makeText(getActivity(), data.getName(), Toast.LENGTH_SHORT).show();

            }
        });
        mRecyclerView.setAdapter(mAdapter);
        getProducts(false);
    }

    public void getProducts (final boolean refreshTag) {
        Call<List<ProductSimpleModel>> call = ApiClient.API.getProducts(Auth.getToken());
        call.enqueue(new Callback<List<ProductSimpleModel>>() {
            @Override
            public void onResponse(Call<List<ProductSimpleModel>> call, Response<List<ProductSimpleModel>> response) {
                listData.addAll(response.body());
                if (mAdapter!=null) mAdapter.notifyDataSetChanged();
                if (mRecyclerView!=null&&refreshTag) mRecyclerView.refreshComplete();
            }

            @Override
            public void onFailure(Call<List<ProductSimpleModel>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
    
    public void onButtonPressed() {
        if (mListener != null) {
            mListener.onFragmentInteraction();
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
        // TODO: Update argument type and name
        void onFragmentInteraction();
    }
}
