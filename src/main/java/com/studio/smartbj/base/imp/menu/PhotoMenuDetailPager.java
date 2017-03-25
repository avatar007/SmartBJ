package com.studio.smartbj.base.imp.menu;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.picasso.Picasso;
import com.studio.smartbj.R;
import com.studio.smartbj.base.BaseDetailPager;
import com.studio.smartbj.domian.PhotosBean;
import com.studio.smartbj.utils.CacheUtils;
import com.studio.smartbj.utils.ConstantValue;
import com.studio.smartbj.utils.OkHttpClientUtils;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017/3/18.
 */
public class PhotoMenuDetailPager extends BaseDetailPager implements View.OnClickListener {
    private ImageButton btn_photo;
    private ListView lv_photo;
    private GridView gv_photo;
    private ArrayList<PhotosBean.PhotoNews> mNewsList;

    public PhotoMenuDetailPager(Activity mActivity, ImageButton btn_photo) {
        super(mActivity);
        this.btn_photo = btn_photo;
    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.pager_photos_menu_detail, null);
        lv_photo = (ListView) view.findViewById(R.id.lv_photo);
        gv_photo = (GridView) view.findViewById(R.id.gv_photo);
        return view;
    }

    @Override
    public void initData() {
        String cache = CacheUtils.getCache(mActivity, ConstantValue.PHOTOS_URL);
        if (!TextUtils.isEmpty(cache)) {
            processData(cache);
        }

        getDataFromServer();
    }

    private void getDataFromServer() {
        OkHttpClientUtils.getDataAsync(mActivity, ConstantValue.PHOTOS_URL, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Toast.makeText(mActivity, "网络访问失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response != null) {
                    final String json = response.body().string();
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            processData(json);
                            CacheUtils.putCache(mActivity, ConstantValue.PHOTOS_URL, json);
                        }
                    });
                }

            }
        }, "PHOTO");
    }

    protected void processData(String result) {
        Gson gson = new Gson();
        PhotosBean photosBean = gson.fromJson(result, PhotosBean.class);

        mNewsList = photosBean.data.news;

        lv_photo.setAdapter(new PhotoAdapter());
        gv_photo.setAdapter(new PhotoAdapter());// gridview的布局结构和listview完全一致,
        // 所以可以共用一个adapter
    }

    class PhotoAdapter extends BaseAdapter {

        //private BitmapUtils mBitmapUtils;
        //private MyBitmapUtils mBitmapUtils;

       /* public PhotoAdapter() {
            mBitmapUtils = new MyBitmapUtils();
            //mBitmapUtils = new BitmapUtils(mActivity);
//			mBitmapUtils
//					.configDefaultLoadingImage(R.drawable.pic_item_list_default);
        }*/

        @Override
        public int getCount() {
            return mNewsList.size();
        }

        @Override
        public PhotosBean.PhotoNews getItem(int position) {
            return mNewsList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(mActivity, R.layout.list_item_photos, null);
            }
            ViewHolder holder = ViewHolder.getHolder(convertView);
            PhotosBean.PhotoNews item = getItem(position);
            holder.tvTitle.setText(item.title);
            Picasso.with(mActivity).load(item.listimage).placeholder(R.mipmap.pic_item_list_default)
                    .into(holder.ivPic);
            return convertView;
        }
    }

    static class ViewHolder {
        ImageView ivPic;
        TextView tvTitle;

        public ViewHolder(View convertView) {
            ivPic = (ImageView) convertView.findViewById(R.id.iv_pic);
            tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
        }

        public static ViewHolder getHolder(View convertView) {
            ViewHolder holder = (ViewHolder) convertView.getTag();
            if (holder == null) {
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            }
            return holder;
        }
    }

    private boolean isListView = true;// 标记当前是否是listview展示

    @Override
    public void onClick(View v) {
        if (isListView) {
            // 切成gridview
            lv_photo.setVisibility(View.GONE);
            gv_photo.setVisibility(View.VISIBLE);
            btn_photo.setImageResource(R.mipmap.icon_pic_list_type);

            isListView = false;
        } else {
            // 切成listview
            lv_photo.setVisibility(View.VISIBLE);
            gv_photo.setVisibility(View.GONE);
            btn_photo.setImageResource(R.mipmap.icon_pic_grid_type);

            isListView = true;
        }
    }

}
