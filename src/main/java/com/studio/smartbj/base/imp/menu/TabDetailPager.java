package com.studio.smartbj.base.imp.menu;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.studio.smartbj.R;
import com.studio.smartbj.base.BaseDetailPager;
import com.studio.smartbj.domian.NewsMenu;
import com.studio.smartbj.domian.TopNewsBean;
import com.studio.smartbj.utils.CacheUtils;
import com.studio.smartbj.utils.ConstantValue;
import com.studio.smartbj.utils.OKHttpUtils;
import com.studio.smartbj.view.TopNewsViewPager;
import com.viewpagerindicator.CirclePageIndicator;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017/3/19.
 */
public class TabDetailPager extends BaseDetailPager {
    private NewsMenu.TabData mTabData;
    private TopNewsViewPager vp_top_news;
    private String mUrl;
    private ArrayList<TopNewsBean.ViewPagerNews> mTopnews;
    private CirclePageIndicator mIndicator;
    private TextView tv_title;
    private ListView lv_tab_data;
    private ArrayList<TopNewsBean.ListViewNews> mListNews;

    public TabDetailPager(Activity mActivity, NewsMenu.TabData tabData) {
        super(mActivity);
        this.mTabData = tabData;
        mUrl = ConstantValue.PATH + mTabData.url;
    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.pager_tab_detail, null);
        lv_tab_data = (ListView) view.findViewById(R.id.lv_tab_data);
        View headerView = View.inflate(mActivity, R.layout.list_heard_view, null);
        vp_top_news = (TopNewsViewPager) headerView.findViewById(R.id.vp_top_news);
        mIndicator = (CirclePageIndicator) headerView.findViewById(R.id.indicator);
        tv_title = (TextView) headerView.findViewById(R.id.tv_title);
        lv_tab_data.addHeaderView(headerView);
        return view;
    }

    @Override
    public void initData() {
        String cacheJson = CacheUtils.getCache(mActivity, mUrl);
        if (!TextUtils.isEmpty(cacheJson)) {
            processJson(cacheJson);
        }
        loadDataFromServer();
    }

    private void loadDataFromServer() {
        new Thread() {
            @Override
            public void run() {
                try {
                    final String json = OKHttpUtils.loadStringFromUrl(mUrl);
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (json != null) {
                                processJson(json);
                                CacheUtils.putCache(mActivity, mUrl, json);
                            } else {
                                Toast.makeText(mActivity, "服务器连接失败", Toast.LENGTH_LONG);
                            }
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }

    private void processJson(String json) {
        Gson gson = new Gson();
        TopNewsBean mTopNews = gson.fromJson(json, TopNewsBean.class);
        if (mTopNews != null) {
            //顶部viewPager的数据
            mTopnews = mTopNews.data.topnews;
            //底部listView的数据
            mListNews = mTopNews.data.news;
        }

        vp_top_news.setAdapter(new TopNewsAdapter());
        mIndicator.setViewPager(vp_top_news);
        mIndicator.setSnap(true);
        mIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tv_title.setText(mTopnews.get(position).title);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        // 更新第一个头条新闻标题
        tv_title.setText(mListNews.get(0).title);
        // 默认让第一个选中(解决页面销毁后重新初始化时,Indicator仍然保留上次圆点位置的bug)
        mIndicator.onPageSelected(0);
        lv_tab_data.setAdapter(new ListNewsAdapter());
    }

    private class TopNewsAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mTopnews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            TopNewsBean.ViewPagerNews pagerNews = mTopnews.get(position);
            ImageView iv = new ImageView(mActivity);
            Picasso.with(mActivity).load(pagerNews.topimage)
                    .placeholder(R.mipmap.topnews_item_default).fit()
                    .config(Bitmap.Config.RGB_565).into(iv);
            container.addView(iv);
            return iv;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    class ListNewsAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mListNews.size();
        }

        @Override
        public TopNewsBean.ListViewNews getItem(int position) {
            return mListNews.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TopNewsBean.ListViewNews data = mListNews.get(position);
            if (convertView == null) {
                convertView = View.inflate(mActivity, R.layout.list_item_news, null);
            }
            MyHolder holder = MyHolder.getHolder(convertView);
            holder.tv_title.setText(data.title);
            holder.tv_date.setText(data.pubdate);
            Picasso.with(mActivity).load(data.listimage)
                    .placeholder(R.mipmap.pic_item_list_default).fit()
                    .config(Bitmap.Config.RGB_565).into(holder.iv_icon);
            return convertView;
        }
    }

    private static class MyHolder {
        ImageView iv_icon;
        TextView tv_title;
        TextView tv_date;

        public MyHolder(View convertView) {
            iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
            tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            tv_date = (TextView) convertView.findViewById(R.id.tv_date);
        }

        public static MyHolder getHolder(View convertView) {
            MyHolder holder = (MyHolder) convertView.getTag();
            if (holder == null) {
                holder = new MyHolder(convertView);
                convertView.setTag(holder);
            }
            return holder;
        }
    }
}
