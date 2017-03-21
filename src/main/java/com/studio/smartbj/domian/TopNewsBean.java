package com.studio.smartbj.domian;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/3/20.
 */
public class TopNewsBean {
    public NewsTab data;

    public class NewsTab {
        public String more;
        public ArrayList<ListViewNews> news;
        public ArrayList<ViewPagerNews> topnews;
    }

    public class ListViewNews {
        public int id;
        public String listimage;
        public String title;
        public String url;
        public String pubdate;
        public String type;
    }

    public class ViewPagerNews {
        public String topimage;
        public String title;
        public String url;
        public String pubdate;
    }
}
