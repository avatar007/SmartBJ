package com.studio.smartbj;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.studio.smartbj.utils.ConstantValue;
import com.studio.smartbj.utils.SpUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/14.
 */
public class GuideActivity extends Activity {

    private ViewPager vp_pager;
    private Button btn_start;
    private LinearLayout ll_container;
    private int[] resID = {R.mipmap.guide_1, R.mipmap.guide_2, R.mipmap.guide_3};
    private List<ImageView> mImageList;
    private ImageView iv_red_point;
    private int mRedPointDis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        initUI();
        initData();
    }

    private void initData() {
        mImageList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            ImageView image = new ImageView(this);
            image.setBackgroundResource(resID[i]);
            mImageList.add(image);

            //添加灰色圆点
            ImageView point = new ImageView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            if (i > 0) {
                params.leftMargin = 10;
            }
            point.setImageResource(R.drawable.shape_point_gray);
            ll_container.addView(point, params);
        }
        vp_pager.setAdapter(new MyPagerAdapter());
        vp_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override//滚动过程中让红点跟随pager同步移动
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //pager滚动过程中调用的方法,positionOffset:滑动过程中的百分比,从0到0.99999再到0(滑动到下一页归零)
                ll_container.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {//获取视图树添加layout方法结束后的回调,layout结束后可以获取控件的左上右下值
                        ll_container.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        mRedPointDis = ll_container.getChildAt(1).getLeft() - ll_container.getChildAt(0).getLeft();
                    }
                });
                int marginLeft = (int) (mRedPointDis * positionOffset + position * mRedPointDis);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) iv_red_point.getLayoutParams();
                params.leftMargin = marginLeft;
                iv_red_point.setLayoutParams(params);
            }

            @Override
            public void onPageSelected(int position) {
                //当选中viewPager的某一个pager调用的方法
                if (position == mImageList.size() - 1) {
                    btn_start.setVisibility(View.VISIBLE);
                } else {
                    btn_start.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initUI() {
        vp_pager = (ViewPager) findViewById(R.id.vp_pager);
        btn_start = (Button) findViewById(R.id.btn_start);
        ll_container = (LinearLayout) findViewById(R.id.ll_container);
        iv_red_point = (ImageView) findViewById(R.id.iv_red_point);
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                SpUtils.putBoolean(getApplicationContext(), ConstantValue.IS_FIRST_ENTER, false);
                finish();
            }
        });
    }

    private class MyPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return mImageList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView view = mImageList.get(position);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
