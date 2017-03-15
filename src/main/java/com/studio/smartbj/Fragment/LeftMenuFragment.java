package com.studio.smartbj.Fragment;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Administrator on 2017/3/15.
 */
public class LeftMenuFragment extends BaseFragment {
    @Override
    public View initView() {
        TextView tv = new TextView(mActivity);
        tv.setText("我是左侧菜单的页面");
        tv.setTextSize(30);
        tv.setTextColor(Color.BLACK);
        return tv;
    }

    @Override
    public void initData() {

    }
}
