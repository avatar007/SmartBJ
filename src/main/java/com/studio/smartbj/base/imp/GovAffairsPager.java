package com.studio.smartbj.base.imp;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;

import com.studio.smartbj.base.BasePager;

/**
 * 政务的内容
 */
public class GovAffairsPager extends BasePager {
    public GovAffairsPager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        System.out.println("政务页面初始化好了");
        TextView tv = new TextView(mActivity);
        tv.setText("政务");
        tv.setTextColor(Color.RED);
        tv.setTextSize(40);
        tv.setGravity(Gravity.CENTER);
        fl_content.addView(tv);

        //修改顶部标题
        tv_title.setText("人口管理");
    }
}
