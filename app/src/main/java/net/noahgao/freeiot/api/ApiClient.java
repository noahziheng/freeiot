/**
 * Copyright (C) 2015 JianyingLi <lijy91@foxmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.noahgao.freeiot.api;

import com.facebook.stetho.okhttp3.StethoInterceptor;

import net.noahgao.freeiot.Const;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.fastjson.FastJsonConverterFactory;

public class ApiClient {

    private static final int CONNECT_TIMEOUT_MILLIS = 20000; // 20 seconds
    private static final int READ_TIMEOUT_MILLIS    = 20000; // 20 seconds
    private static final int WRITE_TIMEOUT_MILLIS   = 20000; // 20 seconds

    public static Api API;

    public static synchronized void initialize() {
        if(API == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Const.API_URL)
                    .client(generateDefaultOkHttp())
                    .addConverterFactory(FastJsonConverterFactory.create())
                    .build();
            API = retrofit.create(Api.class);
        }
    }

    private static OkHttpClient generateDefaultOkHttp() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addNetworkInterceptor(new StethoInterceptor());
        builder.connectTimeout(CONNECT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
        builder.readTimeout(READ_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
        builder.writeTimeout(WRITE_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
        return builder.build();
    }
}
