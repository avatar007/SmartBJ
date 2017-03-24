package com.studio.smartbj.base.imp.menu;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.picasso.Picasso;
import com.studio.smartbj.NewsDetailActivity;
import com.studio.smartbj.R;
import com.studio.smartbj.base.BaseDetailPager;
import com.studio.smartbj.domian.NewsMenu;
import com.studio.smartbj.domian.TopNewsBean;
import com.studio.smartbj.utils.CacheUtils;
import com.studio.smartbj.utils.ConstantValue;
import com.studio.smartbj.utils.OkHttpClientUtils;
import com.studio.smartbj.utils.SpUtils;
import com.studio.smartbj.view.PullToRefreshListView;
import com.studio.smartbj.view.TopNewsViewPager;
import com.viewpagerindicator.CirclePageIndicator;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017/3/19.
 */
public class TabDetailPager extends BaseDetailPager implements AdapterView.OnItemClickListener {
    private NewsMenu.TabData mTabData;
    private TopNewsViewPager vp_top_news;
    private String mUrl;
    private ArrayList<TopNewsBean.ViewPagerNews> mTopnews;
    private CirclePageIndicator mIndicator;
    private TextView tv_title;
    private PullToRefreshListView lv_tab_data;
    private ArrayList<TopNewsBean.ListViewNews> mListNews;
    private String mMoreUrl;
    private ListNewsAdapter mListNewsAdapter;
    private Handler mHandler = new Handler();

    public TabDetailPager(Activity mActivity, NewsMenu.TabData tabData) {
        super(mActivity);
        this.mTabData = tabData;
        mUrl = ConstantValue.PATH + mTabData.url;
    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.pager_tab_detail, null);
        lv_tab_data = (PullToRefreshListView) view.findViewById(R.id.lv_tab_data);
        View headerView = View.inflate(mActivity, R.layout.list_heard_view, null);
        vp_top_news = (TopNewsViewPager) headerView.findViewById(R.id.vp_top_news);
        mIndicator = (CirclePageIndicator) headerView.findViewById(R.id.indicator);
        tv_title = (TextView) headerView.findViewById(R.id.tv_title);
        lv_tab_data.addHeaderView(headerView);

        lv_tab_data.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadDataFromServer(mUrl, false);
            }

            @Override
            public void onLoadMore() {
                if (!TextUtils.isEmpty(mMoreUrl)) {//有更多数据
                    loadDataFromServer(ConstantValue.PATH + mMoreUrl, true);
                } else { //没有更多数据了
                    Toast.makeText(mActivity, "没有更多数据了", Toast.LENGTH_SHORT).show();
                    lv_tab_data.onRefreshComplete(true);//没有更多数据也要收起加载更多
                }
            }
        });
        //listView的点击事件
        lv_tab_data.setOnItemClickListener(this);
        return view;
    }

    @Override
    public void initData() {
        String cacheJson = CacheUtils.getCache(mActivity, mUrl);
        if (!TextUtils.isEmpty(cacheJson)) {
            processJson(cacheJson, false);
        }
        loadDataFromServer(mUrl, false);
    }

    /**
     * @param url    访问网络的url
     * @param isMore 是否是加载更多的网络访问
     */
    private void loadDataFromServer(final String url, final boolean isMore) {
        OkHttpClientUtils.getDataAsync(mActivity, url, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mActivity, "网络连接失败", Toast.LENGTH_SHORT).show();
                        lv_tab_data.onRefreshComplete(false);
                    }
                });
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String json = response.body().string();
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            processJson(json, isMore);
                            if (!isMore) {
                                CacheUtils.putCache(mActivity, mUrl, json);
                            }
                            lv_tab_data.onRefreshComplete(true);
                        }
                    });

                }
            }
        }, "main");//tag标记,可在onDestroy方法中取消连接,okHttpClient.cancel(tag);
    }

    /**
     * @param json   服务器返回的json
     * @param isMore 是否是加载更多
     */
    private void processJson(String json, boolean isMore) {
        Gson gson = new Gson();
        TopNewsBean mTopNews = gson.fromJson(json, TopNewsBean.class);
        mMoreUrl = mTopNews.data.more;
        if (!isMore) {//不是加载更多的数据解析
            //顶部viewPager的数据
            mTopnews = mTopNews.data.topnews;
            //底部listView的数据
            mListNews = mTopNews.data.news;

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
            mListNewsAdapter = new ListNewsAdapter();
            lv_tab_data.setAdapter(mListNewsAdapter);
        } else {//加载更多的数据解析
            ArrayList<TopNewsBean.ListViewNews> mMoreListNews = mTopNews.data.news;
            mListNews.addAll(mMoreListNews);
            mListNewsAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //标记已读未读
        int headerViewsCount = lv_tab_data.getHeaderViewsCount();//获取头布局个数
        position = position - headerViewsCount;
        TopNewsBean.ListViewNews listViewNews = mListNews.get(position);
        String read_ids = SpUtils.getString(mActivity, ConstantValue.READ_ID, "");
        if (!read_ids.contains(listViewNews.id + "")) {
            read_ids = read_ids + listViewNews.id + ",";
            SpUtils.putString(mActivity, ConstantValue.READ_ID, read_ids);
        }
        //点击后修改文字显示颜色,属于局部刷新,全局刷新是用adapter直接刷新
        TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
        tv_title.setTextColor(Color.GRAY);

        //点击后跳转到新闻详细页面(webView)
        Intent intent = new Intent(mActivity, NewsDetailActivity.class);
        intent.putExtra("url", listViewNews.url);
        mActivity.startActivity(intent);
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

            //初始化listView条目时候也要根据sp中存储的已读条目修改显示颜色
            String read_ids = SpUtils.getString(mActivity, ConstantValue.READ_ID, "");
            if (read_ids.contains(data.id + "")) {
                holder.tv_title.setTextColor(Color.GRAY);
            } else {//listView的重用机制,要把未读的条目也设置颜色
                holder.tv_title.setTextColor(Color.BLACK);
            }

            //picasso加载图片
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
