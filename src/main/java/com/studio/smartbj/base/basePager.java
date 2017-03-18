package com.studio.smartbj.base;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.studio.smartbj.MainActivity;
import com.studio.smartbj.R;

/**
 * Created by Administrator on 2017/3/16.
 */
public abstract class BasePager {
    public Activity mActivity;
    public View mRootView;
    public TextView tv_title;
    public ImageButton btn_menu;
    public FrameLayout fl_content;

    public BasePager(Activity activity) {
        mActivity = activity;
        mRootView = initView();
    }

    public View initView() {
        View view = View.inflate(mActivity, R.layout.base_pager, null);
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        btn_menu = (ImageButton) view.findViewById(R.id.btn_menu);
        fl_content = (FrameLayout) view.findViewById(R.id.fl_content);

        btn_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle();
            }
        });
        return view;
    }

    protected void toggle() {
        MainActivity mainActivity = (MainActivity) mActivity;
        SlidingMenu slidingMenu = mainActivity.getSlidingMenu();
        slidingMenu.toggle();
    }

    public abstract void initData();
}
