package com.studio.smartbj.base.imp;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.studio.smartbj.base.BasePager;

/**
 * 首页的内容
 */
public class HomePager extends BasePager {
    public HomePager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        System.out.println("首页初始化好了");
        TextView tv = new TextView(mActivity);
        tv.setText("首页");
        tv.setTextColor(Color.RED);
        tv.setTextSize(40);
        tv.setGravity(Gravity.CENTER);
        fl_content.addView(tv);

        //首页页面不显示左侧菜单栏
        btn_menu.setVisibility(View.GONE);
        tv_title.setText("首页");
    }
}
