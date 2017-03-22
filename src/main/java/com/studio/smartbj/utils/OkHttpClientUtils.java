package com.studio.smartbj.utils;

import android.content.Context;

import com.squareup.okhttp.Cache;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

import java.io.IOException;
import java.io.InputStream;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

/**
 * Created by Administrator on 2017/3/22.
 */
public class OkHttpClientUtils {
    private static final String TAG = "OKHttpUtils";
    private static OkHttpClient okHttpClient = null;
    private static OkHttpClientUtils okHttpUtils = null;

    private OkHttpClientUtils(Context context) {
        okHttpClient = getOkHttpSingletonInstance();
        //开启响应缓存  CookiePolicy.ACCEPT_ORIGINAL_SERVER:接收服务器端缓存的内容
        okHttpClient.setCookieHandler(new CookieManager(null, CookiePolicy.ACCEPT_ORIGINAL_SERVER));
        //设置缓存的目录和大小
        int cacheSize = 10 << 20;  //表示10MB
        Cache cache = new Cache(context.getCacheDir(), cacheSize);
        okHttpClient.setCache(cache);

        //设置合理的超时
        okHttpClient.setConnectTimeout(15, TimeUnit.SECONDS);
        okHttpClient.setReadTimeout(20, TimeUnit.SECONDS);
        okHttpClient.setWriteTimeout(20, TimeUnit.SECONDS);

        //下面的设置默认就设置好了,可无需设置
        okHttpClient.setHostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });
    }

    //获取单例的OkHttpClientUtils对象
    public static OkHttpClientUtils getOkHttpClientUtils(Context context) {
        if (okHttpUtils == null) {
            synchronized (OkHttpClientUtils.class) {
                if (okHttpUtils == null) {
                    okHttpUtils = new OkHttpClientUtils(context);
                }
            }
        }
        return okHttpUtils;
    }

    //获取单例的OkHttpClient对象
    public static OkHttpClient getOkHttpSingletonInstance() {
        if (okHttpClient == null) {
            synchronized (OkHttpClientUtils.class) {
                if (okHttpClient == null) {
                    okHttpClient = new OkHttpClient();
                }
            }
        }
        return okHttpClient;
    }

    ////////////////////////////////////////////////////////
    //get方式访问网络
    ////////////////////////////////////////////////////////

    /**
     * @param url 请求网络的url连接
     * @param tag 当前连接的标记
     * @return 返回Request对象
     */
    private Request buildGetRequest(String url, Object tag) {
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        if (tag != null) {
            builder.tag(tag);
        }
        return builder.build();
    }

    /**
     * @param url 传入的url
     * @return 返回Response对象
     * @throws IOException 连接失败异常
     */
    private Response buildResponse(String url, Object tag) throws IOException {
        Request request = buildGetRequest(url, tag);
        Response response = okHttpClient.newCall(request).execute();
        return response;
    }

    ////////////////////////////////////////////////////////
    //get同步方式访问网络
    ////////////////////////////////////////////////////////

    /**
     * @param url 传入的url
     * @return 返回响应体内容
     * @throws IOException 连接失败异常
     */
    private ResponseBody buildResponseBody(String url, Object tag) throws IOException {
        Response response = buildResponse(url, tag);
        if (response.isSuccessful()) {
            return response.body();
        }
        return null;
    }

    /**
     * @param url 传入的url
     * @return 返回相应体的字符串形式
     * @throws IOException 连接失败异常
     */
    public static String getStringFromUrl(Context context, String url, Object tag) throws IOException {
        ResponseBody responseBody = getOkHttpClientUtils(context).buildResponseBody(url, tag);
        if (responseBody != null) {
            return responseBody.string();
        }
        return null;
    }

    /**
     * @param url 传入的url
     * @return 返回相应体的byte形式
     * @throws IOException 连接失败异常
     */
    public static byte[] getByteFromUrl(Context context, String url, Object tag) throws IOException {
        ResponseBody responseBody = getOkHttpClientUtils(context).buildResponseBody(url, tag);
        if (responseBody != null) {
            return responseBody.bytes();
        }
        return null;
    }

    /**
     * @param url 传入的url
     * @return 返回相应体的流形式
     * @throws IOException 连接失败异常
     */
    public static InputStream getStreamFromUrl(Context context, String url, Object tag) throws IOException {
        ResponseBody responseBody = getOkHttpClientUtils(context).buildResponseBody(url, tag);
        if (responseBody != null) {
            return responseBody.byteStream();
        }
        return null;
    }

    /**
     *根据设置的tag,取消正在执行的网络访问
     */
    public static void cancelCall(String tag){
        getOkHttpSingletonInstance().cancel(tag);
    }

    ////////////////////////////////////////////////////////
    //get异步方式访问网络
    ////////////////////////////////////////////////////////
    public static void getDataAsync(Context context, String url, Callback callback, String tag) {
        Request request = getOkHttpClientUtils(context).buildGetRequest(url, tag);
        getOkHttpSingletonInstance().newCall(request).enqueue(callback);
    }
}
