package com.studio.smartbj.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Administrator on 2017/3/20.
 */
public class TopNewsViewPager extends ViewPager {

    public TopNewsViewPager(Context context) {
        super(context);
    }

    public TopNewsViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 重写事件分发方法:看情况去决定所有父类是否拦截滑动事件
     * 1.向左滑动到最后一个页面时候拦截
     * 2.向右滑动,第一个界面时拦截
     * 3.上下滑动时拦截
     *
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        float mDownX = 0;
        float mDownY = 0;
        //先请求不要拦截
        getParent().requestDisallowInterceptTouchEvent(true);
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = ev.getX();
                mDownY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float moveX = ev.getX();
                float moveY = ev.getY();
                int disX = (int) (moveX - mDownX);
                int disY = (int) (moveY - mDownY);
                if (Math.abs(disX) > Math.abs(disY)) {//左右移动
                    int currentItem = getCurrentItem();
                    if (disX > 0) {//向右滑
                        if (currentItem == 0) {
                            getParent().requestDisallowInterceptTouchEvent(false);
                        }
                    } else {//向左滑
                        if (currentItem == getAdapter().getCount() - 1) {
                            getParent().requestDisallowInterceptTouchEvent(false);
                        }
                    }

                } else {//上下移动
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

}
