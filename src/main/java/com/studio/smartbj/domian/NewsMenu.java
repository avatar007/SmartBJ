package com.studio.smartbj.domian;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/3/17.
 */
public class NewsMenu {
    public String retcode;
    public ArrayList<LeftMenu> data;
    public ArrayList<Integer> extend;

    public class LeftMenu {
        public int id;
        public String title;
        public int type;
        public ArrayList<TabData> children;
    }

    public class TabData{
        public int id;
        public String title;
        public int type;
        public String url;
    }

}
