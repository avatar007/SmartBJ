package com.studio.smartbj.base.imp.menu;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.studio.smartbj.MainActivity;
import com.studio.smartbj.R;
import com.studio.smartbj.base.BaseDetailPager;
import com.studio.smartbj.domian.NewsMenu;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/18.
 */
public class NewsMenuDetailPager extends BaseDetailPager implements ViewPager.OnPageChangeListener, View.OnClickListener {
    private ViewPager vp_tab_pager;
    private ListView lv_tab_pager;
    private ArrayList<NewsMenu.TabData> children;
    private List<TabDetailPager> mTabPagerList;
    private TabPageIndicator mIndicator;
    private ImageButton btn_next;

    public NewsMenuDetailPager(Activity mActivity, ArrayList<NewsMenu.TabData> children) {
        super(mActivity);
        this.children = children;
    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.pager_news_menu_detail, null);
        vp_tab_pager = (ViewPager) view.findViewById(R.id.vp_tab_pager);
        //lv_tab_pager = (ListView) view.findViewById(R.id.lv_tab_pager);
        mIndicator = (TabPageIndicator) view.findViewById(R.id.indicator);
        btn_next = (ImageButton) view.findViewById(R.id.btn_next);
        return view;
    }

    @Override
    public void initData() {
        mTabPagerList = new ArrayList<>();
        for (int i = 0; i < children.size(); i++) {
            TabDetailPager pager = new TabDetailPager(mActivity, children.get(i));
            mTabPagerList.add(pager);
        }
        vp_tab_pager.setAdapter(new TabPagerAdapter());
        mIndicator.setViewPager(vp_tab_pager);//必须在viewPager设置数据适配器后添加到indicator中
        //注意:::因为indicator和viewPager是同步的,所以设置点击事件设置indicator的,因为indicator绑定了viewPager
        mIndicator.setOnPageChangeListener(this);
        btn_next.setOnClickListener(this);
    }

    private class TabPagerAdapter extends PagerAdapter {
        @Override
        public CharSequence getPageTitle(int position) {
            NewsMenu.TabData data = children.get(position);
            return data.title;
        }

        @Override
        public int getCount() {
            return mTabPagerList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            TabDetailPager pager = mTabPagerList.get(position);
            View view = pager.mRootView;
            container.addView(view);
            pager.initData();
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (position == 0) {
            setSlidingMenuEnable(true);
        } else {
            setSlidingMenuEnable(false);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * 开启或禁用侧边栏
     */
    protected void setSlidingMenuEnable(boolean enable) {
        // 获取侧边栏对象
        MainActivity mainUI = (MainActivity) mActivity;
        SlidingMenu slidingMenu = mainUI.getSlidingMenu();
        if (enable) {
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        } else {
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        }
    }

    @Override
    public void onClick(View v) {
        int currentItem = vp_tab_pager.getCurrentItem();
        currentItem++;
        vp_tab_pager.setCurrentItem(currentItem);
    }
}
