package com.studio.smartbj.base.imp;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.studio.smartbj.base.BasePager;

/**
 * 设置的内容
 */
public class SettingPager extends BasePager {
    public SettingPager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        System.out.println("设置页面初始化好了");
        TextView tv = new TextView(mActivity);
        tv.setText("设置");
        tv.setTextColor(Color.RED);
        tv.setTextSize(40);
        tv.setGravity(Gravity.CENTER);
        fl_content.addView(tv);

        //修改顶部标题
        tv_title.setText("设置");

        //设置页面不显示左侧菜单栏
        btn_menu.setVisibility(View.GONE);
    }
}
