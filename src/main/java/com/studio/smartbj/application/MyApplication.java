package com.studio.smartbj.application;

import android.app.Application;

import com.squareup.okhttp.OkHttpClient;
import com.studio.smartbj.utils.OkHttpClientUtils;

/**
 * Created by Administrator on 2017/3/22.
 */
public class MyApplication extends Application {
    private static MyApplication app;

    public static MyApplication getInstance() {
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        initOkHttpUtils();
        initPicasso();
    }
    //初始化单例OkHttpClient对象
    private void initOkHttpUtils() {
        OkHttpClient okHttpClient = OkHttpClientUtils.getOkHttpSingletonInstance();
    }

    private void initPicasso() {

    }
}
