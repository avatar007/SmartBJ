package com.studio.smartbj.utils;

import android.content.Context;

import com.squareup.okhttp.Cache;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.Map;
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
     * 根据设置的tag,取消正在执行的网络访问
     */
    public static void cancelCall(String tag) {
        okHttpClient.cancel(tag);
    }

    ////////////////////////////////////////////////////////
    //get异步方式访问网络
    ////////////////////////////////////////////////////////
    public static void getDataAsync(Context context, String url, Callback callback, String tag) {
        Request request = getOkHttpClientUtils(context).buildGetRequest(url, tag);
        okHttpClient.newCall(request).enqueue(callback);
    }

    ////////////////////////////////////////////////////////
    //post同步方式访问网络
    ////////////////////////////////////////////////////////

    /**
     * 通过requestBody的方式进行post请求
     *
     * @param url
     * @param requestBody
     * @return Request对象
     */
    private Request buildPostRequest(String url, RequestBody requestBody) {
        Request.Builder builder = new Request.Builder();
        builder.url(url).post(requestBody);
        return builder.build();
    }

    /**
     * 通过Request对象获取Response的字符串表达形式
     *
     * @param url
     * @param requestBody
     * @return
     * @throws IOException
     */
    private String postRequestBody(String url, RequestBody requestBody) throws IOException {
        Request request = buildPostRequest(url, requestBody);
        Response response = okHttpClient.newCall(request).execute();
        if (response.isSuccessful()) {
            return response.body().string();
        }
        return null;
    }

    /**
     * 将键值对封装到RequestBody中
     *
     * @param map 传入多个键值对的map对象
     * @return
     */
    private RequestBody buildRequestBody(Map<String, String> map) {
        FormEncodingBuilder builder = new FormEncodingBuilder();
        if (map != null && !map.isEmpty()) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                builder.add(entry.getKey(), entry.getValue());
            }
        }
        return builder.build();
    }

    /**
     * post同步网络访问,提交键值对
     *
     * @param url
     * @param map
     * @return
     * @throws IOException
     */
    public static String postKeyValuePair(Context context, String url, Map<String, String> map) throws IOException {
        RequestBody requestBody = getOkHttpClientUtils(context).buildRequestBody(map);
        return getOkHttpClientUtils(context).postRequestBody(url, requestBody);
    }

    /**
     * post同步上传文件以及其他表单控件(提交分块请求)
     *
     * @param context
     * @param url           网络地址
     * @param map           提交给服务器的表单信息键值对
     * @param files         提交的文件
     * @param formFieldName 每个需要提交文件的对应的input的name值
     * @return
     * @throws IOException
     */
    public static String postUploadFiles(Context context, String url, Map<String, String> map, File[] files, String[] formFieldName) throws IOException {
        RequestBody requestBody = getOkHttpClientUtils(context).buildRequestBody(map, files, formFieldName);
        return getOkHttpClientUtils(context).postRequestBody(url, requestBody);
    }

    /**
     * 生成提交分块请求时的RequestBody对象
     *
     * @param map
     * @param files
     * @param formFieldName
     * @return
     */
    private RequestBody buildRequestBody(Map<String, String> map, File[] files, String[] formFieldName) {
        MultipartBuilder builder = new MultipartBuilder();
        //第一部分提交:文件以外的input的数据
        if (map != null) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                builder.addPart(Headers.of("Content-Disposition", "form-data;name=\"" + entry.getKey() + "\""), RequestBody.create(null, entry.getValue()));
            }
        }
        //第二部分提交:上传文件控件的数据
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                String fileName = file.getName();
                RequestBody requestBody = RequestBody.create(MediaType.parse(getMimeType(fileName)), file);
                builder.addPart(Headers.of("Content-Disposition", "form-data;name=\"" + formFieldName[i] + "\";filename=\"" + fileName + "\""),
                        requestBody);
            }
        }
        return builder.build();
    }

    private String getMimeType(String fileName) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(fileName);
        if (contentTypeFor != null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }


    ////////////////////////////////////////////////////////
    //post异步方式访问网络
    ////////////////////////////////////////////////////////

    /**
     * post异步请求网络,提交ResponseBody对象
     *
     * @param url
     * @param requestBody
     * @param callback
     */
    private void postResponseBodyAsync(String url, RequestBody requestBody, Callback callback) {
        Request request = buildPostRequest(url, requestBody);
        okHttpClient.newCall(request).enqueue(callback);
    }

    /**
     * post异步请求,提交键值对
     *
     * @param context
     * @param url
     * @param map
     * @param callback
     * @return
     */
    public static void postKeyValuePair(Context context, String url, Map<String, String> map, Callback callback) {
        RequestBody requestBody = getOkHttpClientUtils(context).buildRequestBody(map);
        getOkHttpClientUtils(context).postResponseBodyAsync(url, requestBody, callback);
    }
}
