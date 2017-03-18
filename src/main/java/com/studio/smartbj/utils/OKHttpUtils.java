package com.studio.smartbj.utils;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Administrator on 2017/3/16.
 */
public class OKHttpUtils {
    private static final OkHttpClient mOkHttpClient = new OkHttpClient();

    /**
     * @param url 传入的url
     * @return 返回请求类对象
     */
    private static Request getRequestFormUrl(String url) {
        Request request = new Request.Builder().url(url).build();
        return request;
    }

    /**
     * @param url 传入的url
     * @return 返回Response对象
     * @throws IOException 连接失败异常
     */
    private static Response getResponseFromUrl(String url) throws IOException {
        Request request = OKHttpUtils.getRequestFormUrl(url);
        Response response = mOkHttpClient.newCall(request).execute();
        return response;
    }

    /**
     * @param url 传入的url
     * @return 返回响应体内容
     * @throws IOException 连接失败异常
     */
    private static ResponseBody getResponseBodyFormUrl(String url) throws IOException {
        Response response = OKHttpUtils.getResponseFromUrl(url);
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
    public static String loadStringFromUrl(String url) throws IOException {
        ResponseBody responseBody = OKHttpUtils.getResponseBodyFormUrl(url);
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
    public static byte[] loadByteFromUrl(String url) throws IOException {
        ResponseBody responseBody = OKHttpUtils.getResponseBodyFormUrl(url);
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
    public static InputStream loadStreamFromUrl(String url) throws IOException {
        ResponseBody responseBody = OKHttpUtils.getResponseBodyFormUrl(url);
        if (responseBody != null) {
            return responseBody.byteStream();
        }
        return null;
    }
}
