package com.studio.smartbj.application;

import android.app.Application;
import android.graphics.Bitmap;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.picasso.LruCache;
import com.squareup.picasso.Picasso;
import com.studio.smartbj.utils.MyOkHttpDownloader;
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
        //配置Picasso
        Picasso picasso = new Picasso.Builder(this)
                //设置内存缓存大小,10MB
                .memoryCache(new LruCache(10 << 20))
                //设置下载图片的格式,这样可以节省一半的内存
                .defaultBitmapConfig(Bitmap.Config.RGB_565)
                //配置下载器,这里用的是OkHttp,必须单独加OkHttp,同时设置了餐盘存储的位置和大小
                //.downloader(new UrlConnectionDownloader()),这个是默认选择的类加载器,用的是HttpUrlConnection
                //.downloader(new OkHttpDownloader(this.getCacheDir(), 10 << 20))
                .downloader(new MyOkHttpDownloader(getCacheDir(), 20 << 20))//使用自己定义的MyOkHttpDownloader
                //设置图片左上角的三角标志,true表示显示
                //红色:代表从网络下载的图片
                //蓝色:代表从磁盘缓存加载的图片
                //绿色:代表从内存中加载的图片
                .indicatorsEnabled(true)
                //.loggingEnabled(true)
                .build();
        picasso.setSingletonInstance(picasso);//设置picasso的单例模式


    }
}
