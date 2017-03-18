package com.studio.smartbj.Fragment;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.studio.smartbj.MainActivity;
import com.studio.smartbj.R;
import com.studio.smartbj.base.BasePager;
import com.studio.smartbj.base.imp.GovAffairsPager;
import com.studio.smartbj.base.imp.HomePager;
import com.studio.smartbj.base.imp.NewsCenterPager;
import com.studio.smartbj.base.imp.SettingPager;
import com.studio.smartbj.base.imp.SmartServicePager;
import com.studio.smartbj.view.NoScrollViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/15.
 */
public class ContentFragment extends BaseFragment {

    private RadioGroup rg_group;
    private NoScrollViewPager vp_content;
    private List<BasePager> mPagerList = new ArrayList<>();

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_content, null);
        rg_group = (RadioGroup) view.findViewById(R.id.rg_group);
        vp_content = (NoScrollViewPager) view.findViewById(R.id.vp_content);
        return view;
    }

    @Override
    public void initData() {
        mPagerList.add(new HomePager(mActivity));
        mPagerList.add(new NewsCenterPager(mActivity));
        mPagerList.add(new SmartServicePager(mActivity));
        mPagerList.add(new GovAffairsPager(mActivity));
        mPagerList.add(new SettingPager(mActivity));

        vp_content.setAdapter(new MyPagerAdapter());

        rg_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_home:
                        vp_content.setCurrentItem(0, false);
                        break;
                    case R.id.rb_news:
                        vp_content.setCurrentItem(1, false);
                        break;
                    case R.id.rb_smart:
                        vp_content.setCurrentItem(2, false);
                        break;
                    case R.id.rb_gov:
                        vp_content.setCurrentItem(3, false);
                        break;
                    case R.id.rb_setting:
                        vp_content.setCurrentItem(4, false);
                        break;
                }
            }
        });

        vp_content.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                //当pager选中时候加载数据
                mPagerList.get(position).initData();
                if (position == 0 || position == mPagerList.size() - 1){
                    setSlidingMenuEnable(false);//索引为0和mPagerList.size() - 1没有左侧菜单栏,让其无法拉出
                }else {
                    setSlidingMenuEnable(true);
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        mPagerList.get(0).initData();//默认先加载首页的数据
        setSlidingMenuEnable(false);//默认首页不能拉出侧边栏
    }

    private void setSlidingMenuEnable(boolean b) {
        MainActivity mainActivity = (MainActivity) getActivity();
        SlidingMenu slidingMenu = mainActivity.getSlidingMenu();
        if (b){
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        }else {
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        }
    }

    private class MyPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mPagerList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            BasePager basePager = mPagerList.get(position);
            View rootView = basePager.mRootView;
            container.addView(rootView);
            //viewPager是预加载2个页面的数据,为了节省流量,在viewPager点中条目时候在加载数据
            //basePager.initData();//初始化布局后初始化数据
            return rootView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    //获取新闻中心的对象
    public NewsCenterPager getNewsCenterPager(){
        NewsCenterPager pager = (NewsCenterPager) mPagerList.get(1);
        return pager;
    }
}
