package com.studio.smartbj.base.imp;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;

import com.studio.smartbj.base.BasePager;

/**
 * 智慧服务的内容
 */
public class SmartServicePager extends BasePager {
    public SmartServicePager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        System.out.println("智慧服务页面初始化好了");
        TextView tv = new TextView(mActivity);
        tv.setText("智慧服务");
        tv.setTextColor(Color.RED);
        tv.setTextSize(40);
        tv.setGravity(Gravity.CENTER);
        fl_content.addView(tv);

        //修改顶部标题
        tv_title.setText("生活");
    }
}
