package com.studio.smartbj.base.imp;

import android.app.Activity;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.studio.smartbj.Fragment.LeftMenuFragment;
import com.studio.smartbj.MainActivity;
import com.studio.smartbj.base.BaseDetailPager;
import com.studio.smartbj.base.BasePager;
import com.studio.smartbj.base.imp.menu.InteractMenuDetailPager;
import com.studio.smartbj.base.imp.menu.NewsMenuDetailPager;
import com.studio.smartbj.base.imp.menu.PhotoMenuDetailPager;
import com.studio.smartbj.base.imp.menu.TopicMenuDetailPager;
import com.studio.smartbj.domian.NewsMenu;
import com.studio.smartbj.utils.CacheUtils;
import com.studio.smartbj.utils.ConstantValue;
import com.studio.smartbj.utils.OKHttpUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 新闻中心的内容
 */
public class NewsCenterPager extends BasePager {

    private NewsMenu mNewsMenu;
    private List<BaseDetailPager> detailPagerList = new ArrayList<>();

    public NewsCenterPager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        String json = CacheUtils.getCache(mActivity, ConstantValue.CATEGORY_URL);
        if (json != null) {
            processJson(json);//缓存不为空,获取缓存的json去解析
        }
        //不管有没有缓存,都网络加载下数据,为了让用户在加载时候能看到数据
        getDataFromServer();
    }

    private void getDataFromServer() {
        new Thread() {
            @Override
            public void run() {
                try {
                    final String json = OKHttpUtils.loadStringFromUrl(ConstantValue.CATEGORY_URL);
                    if (json != null) {
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                processJson(json);
                                //将json作为缓存写入到sp中
                            }
                        });
                        CacheUtils.putCache(mActivity, ConstantValue.CATEGORY_URL, json);
                    } else {
                        Toast.makeText(mActivity, "网络连接失败", Toast.LENGTH_SHORT);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void processJson(String json) {
        Gson gson = new Gson();
        mNewsMenu = gson.fromJson(json, NewsMenu.class);
        MainActivity mainActivity = (MainActivity) mActivity;
        LeftMenuFragment leftMenuFragment = mainActivity.getLeftMenuFragment();
        leftMenuFragment.setData(mNewsMenu.data);

        //添加四个菜单详情页
        detailPagerList.add(new NewsMenuDetailPager(mActivity));
        detailPagerList.add(new TopicMenuDetailPager(mActivity));
        detailPagerList.add(new PhotoMenuDetailPager(mActivity));
        detailPagerList.add(new InteractMenuDetailPager(mActivity));

        //默认先加载第一个页面
        setCurDetailPager(0);
    }

    public void setCurDetailPager(int position) {
        View view = detailPagerList.get(position).mRootView;
        fl_content.removeAllViews();//帧布局添加数据前先移除所有数据,否则会重叠
        fl_content.addView(view);

        //初始化数据
        detailPagerList.get(position).initData();

        //更改显示标题(左侧菜单栏的标题)
        tv_title.setText(mNewsMenu.data.get(position).title);
    }
}
