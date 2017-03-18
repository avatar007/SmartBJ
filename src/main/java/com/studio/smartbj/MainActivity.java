package com.studio.smartbj;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Window;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.studio.smartbj.Fragment.ContentFragment;
import com.studio.smartbj.Fragment.LeftMenuFragment;

/**
 * Created by Administrator on 2017/3/14.
 */
public class MainActivity extends SlidingFragmentActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        //使用slidingMenu添加左侧菜单栏
        setBehindContentView(R.layout.left_menu);
        SlidingMenu slidingMenu = getSlidingMenu();
        slidingMenu.setBehindOffset(200);
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        initFragment();
    }

    private void initFragment() {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        //fragment替换主页
        transaction.replace(R.id.fl_content, new ContentFragment(), "TAG_CONTENT");
        //fragment替换左侧菜单栏
        transaction.replace(R.id.fl_left_menu, new LeftMenuFragment(), "TAG_LEFT_MENU");
        transaction.commit();
    }

    public LeftMenuFragment getLeftMenuFragment(){
        FragmentManager fm = getFragmentManager();
        LeftMenuFragment fragment = (LeftMenuFragment) fm.findFragmentByTag("TAG_LEFT_MENU");
        return fragment;
    }

    public ContentFragment getContentFragment(){
        FragmentManager fm = getFragmentManager();
        ContentFragment fragment = (ContentFragment) fm.findFragmentByTag("TAG_CONTENT");
        return fragment;
    }
}
