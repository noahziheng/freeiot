package net.noahgao.freeiot.api;

import net.noahgao.freeiot.model.DeviceModel;
import net.noahgao.freeiot.model.ProductModel;
import net.noahgao.freeiot.model.ProductSimpleModel;
import net.noahgao.freeiot.model.UserModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.DELETE;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Noah Gao on 17-2-8.
 * By Android Studio
 */

public interface Api {

    @FormUrlEncoded
    @POST("/user/auth")
    Call<UserModel> auth(@Field("email") String email, @Field("password") String password);

    @FormUrlEncoded
    @POST("/user")
    Call<UserModel> reg(@Field("email") String email, @Field("password") String password);

    @FormUrlEncoded
    @PUT("/user/{id}")
    Call<Object> modifyPassword(@Path("id") String id, @Field("password") String password,@Query("token") String token);

    @GET("/user/{id}")
    Call<UserModel> getUser(@Path("id") String id, @Query("token") String token);

    @GET("/product")
    Call<List<ProductSimpleModel>> getProducts(@Query("token") String token);

    @GET("/product/{id}")
    Call<ProductModel> getProduct(@Path("id") String id, @Query("token") String token);

    @GET("/device")
    Call<List<DeviceModel.DeviceMeta.DeviceMetaModel>> getDevices(@Query("owner") String owner, @Query("token") String token);

    @GET("/device/{id}")
    Call<DeviceModel> getDevice(@Path("id") String id, @Query("token") String token);

}
