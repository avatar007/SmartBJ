package com.studio.smartbj.utils;

import android.content.Context;

/**
 * 缓存工具类,url作为key,json作为值,存储在sp中
 * Created by Administrator on 2017/3/18.
 */
public class CacheUtils {
    public static void putCache(Context ctx,String url, String value) {
        SpUtils.putString(ctx,url,value);
    }

    public static String getCache(Context ctx,String url){
        return SpUtils.getString(ctx,url,"");
    }
}
