package com.studio.smartbj.Fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2017/3/15.
 */
public abstract class BaseFragment extends Fragment {
    public Activity mActivity;

    @Override
    //fragment创建时调用的方法
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
    }

    @Nullable
    @Override
    //fragment初始化布局时调用的方法
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = initView();
        return view;
    }

    @Override
    //当fragment依附的activity创建好时调用的方法
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();//依附的activity的Oncreated方法执行好时,初始化fragment数据
    }

    //初始化view方法,不知道子类如何实现,让子类自己去实现
    public abstract View initView();

    //初始化data方法,不知道子类如何实现,让子类自己去实现
    public abstract void initData();
}
