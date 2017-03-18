package com.studio.smartbj.Fragment;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.studio.smartbj.MainActivity;
import com.studio.smartbj.R;
import com.studio.smartbj.base.imp.NewsCenterPager;
import com.studio.smartbj.domian.NewsMenu;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/3/15.
 */
public class LeftMenuFragment extends BaseFragment {
    private ArrayList<NewsMenu.LeftMenu> mMenuData;
    private ListView ll_left_menu;
    private int curPos;
    private MyAdapter mAdapter;
    private int mCurItemData;

    @Override
    public View initView() {
        View view = View.inflate(getActivity(), R.layout.fragment_leftmenu, null);
        ll_left_menu = (ListView) view.findViewById(R.id.ll_left_menu);
        return view;
    }

    @Override
    public void initData() {

    }
    public void setData(ArrayList<NewsMenu.LeftMenu> data){
        curPos = 0;
        mMenuData = data;
        mAdapter = new MyAdapter();
        ll_left_menu.setAdapter(mAdapter);
        ll_left_menu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                curPos = position;
                mAdapter.notifyDataSetChanged();
                //选中条目后关闭侧边栏
                toggle();
                setCurItemData(position);
            }
        });
    }

    private void toggle() {
        MainActivity mainActivity = (MainActivity) mActivity;
        SlidingMenu slidingMenu = mainActivity.getSlidingMenu();
        slidingMenu.toggle();
    }

    private void setCurItemData(int position) {
        MainActivity mainActivity = (MainActivity) mActivity;
        //获取contentFragment
        ContentFragment fragment = mainActivity.getContentFragment();
        //通过contentFragment获取新闻中心的对象
        NewsCenterPager pager = fragment.getNewsCenterPager();
        //根据索引设置对应条目的数据
        pager.setCurDetailPager(position);
    }

    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mMenuData.size();
        }

        @Override
        public NewsMenu.LeftMenu getItem(int position) {
            return mMenuData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null){
                convertView = View.inflate(getActivity(),R.layout.left_menu_item,null);
            }
            TextView tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            tv_title.setText(getItem(position).title);

            if (curPos == position){
                tv_title.setEnabled(true);
            }else{
                tv_title.setEnabled(false);
            }
            return convertView;
        }
    }
}
