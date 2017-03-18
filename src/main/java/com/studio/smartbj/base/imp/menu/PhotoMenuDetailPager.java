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
public class PhotoMenuDetailPager extends BaseDetailPager {
    public PhotoMenuDetailPager(Activity mActivity) {
        super(mActivity);
    }

    @Override
    public View initView() {
        TextView view = new TextView(mActivity);
        view.setText("菜单详情页-组图");
        view.setTextColor(Color.RED);
        view.setGravity(Gravity.CENTER);
        view.setTextSize(30);
        return view;
    }
}
