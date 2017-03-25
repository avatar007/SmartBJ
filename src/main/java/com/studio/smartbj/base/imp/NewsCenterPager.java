package com.studio.smartbj.base.imp;

import android.app.Activity;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
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
import com.studio.smartbj.utils.OkHttpClientUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 新闻中心的内容
 */
public class NewsCenterPager extends BasePager {

    private NewsMenu mNewsMenu;
    private List<BaseDetailPager> detailPagerList;
    private Handler mHandler = new Handler();

    public NewsCenterPager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        String json = CacheUtils.getCache(mActivity, ConstantValue.CATEGORY_URL);
        if (!TextUtils.isEmpty(json)) {
            processJson(json);//缓存不为空,获取缓存的json去解析
        }
        //不管有没有缓存,都网络加载下数据,为了让用户在加载时候能看到数据
        getDataFromServer();
    }

    private void getDataFromServer() {
        OkHttpClientUtils.getDataAsync(mActivity, ConstantValue.CATEGORY_URL, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mActivity, "网络连接失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(final Response response) throws IOException {
                if (response.isSuccessful()) {
                    //根据返回的response对象获取responseBody对象,再获取responseBody的String类型json
                    final String json = response.body().string();
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            processJson(json);
                            //将json作为缓存写入到sp中
                            CacheUtils.putCache(mActivity, ConstantValue.CATEGORY_URL, json);
                        }
                    });
                }
            }
        }, "centerPager");
    }

    private void processJson(String json) {
        Gson gson = new Gson();
        mNewsMenu = gson.fromJson(json, NewsMenu.class);
        MainActivity mainActivity = (MainActivity) mActivity;
        LeftMenuFragment leftMenuFragment = mainActivity.getLeftMenuFragment();
        leftMenuFragment.setData(mNewsMenu.data);

        //添加四个菜单详情页
        detailPagerList = new ArrayList<>();
        detailPagerList.add(new NewsMenuDetailPager(mActivity, mNewsMenu.data.get(0).children));
        detailPagerList.add(new TopicMenuDetailPager(mActivity));
        detailPagerList.add(new PhotoMenuDetailPager(mActivity,btn_photo));
        detailPagerList.add(new InteractMenuDetailPager(mActivity));

        //默认先加载第一个页面
        setCurDetailPager(0);
    }

    public void setCurDetailPager(int position) {
        BaseDetailPager pager = detailPagerList.get(position);
        View view = pager.mRootView;
        fl_content.removeAllViews();//帧布局添加数据前先移除所有数据,否则会重叠
        fl_content.addView(view);

        //初始化数据
        pager.initData();

        //更改显示标题(左侧菜单栏的标题)
        tv_title.setText(mNewsMenu.data.get(position).title);

        // 如果是组图页面, 需要显示切换按钮
        if (pager instanceof PhotoMenuDetailPager) {
            btn_photo.setVisibility(View.VISIBLE);
        } else {
            // 隐藏切换按钮
            btn_photo.setVisibility(View.GONE);
        }
    }
}
