package com.studio.smartbj.base;

import android.app.Activity;
import android.view.View;

/**
 * Created by Administrator on 2017/3/18.
 */
public abstract class BaseDetailPager {
    public Activity mActivity;
    public View mRootView;
    public BaseDetailPager(Activity mActivity){
        this.mActivity = mActivity;
        mRootView = initView();
    }
    //初始化view
    public abstract View initView();

    //初始化数据
    public void initData(){

    }
}
