package com.studio.smartbj.base.imp.menu;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.studio.smartbj.base.BaseDetailPager;

/**
 * Created by Administrator on 2017/3/18.
 */
public class NewsMenuDetailPager extends BaseDetailPager {
    public NewsMenuDetailPager(Activity mActivity) {
        super(mActivity);
    }

    @Override
    public View initView() {
        TextView view = new TextView(mActivity);
        view.setText("菜单详情页-新闻");
        view.setTextColor(Color.RED);
        view.setTextSize(30);
        view.setGravity(Gravity.CENTER);
        return view;
    }
}
