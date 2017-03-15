package com.studio.smartbj.Fragment;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Administrator on 2017/3/15.
 */
public class ContentFragment extends BaseFragment {
    @Override
    public View initView() {
        TextView tv = new TextView(mActivity);
        tv.setText("我是显示内容的页面");
        tv.setTextSize(30);
        tv.setTextColor(Color.RED);
        return tv;
    }

    @Override
    public void initData() {

    }
}
