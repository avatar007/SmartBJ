package com.studio.smartbj;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

import com.studio.smartbj.utils.ConstantValue;
import com.studio.smartbj.utils.SpUtils;

import cn.sharesdk.framework.ShareSDK;

public class SplashActivity extends AppCompatActivity {

    private RelativeLayout rl_root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        rl_root = (RelativeLayout) findViewById(R.id.rl_root);
        initAnimation();
        //初始化shareSDK
        ShareSDK.initSDK(this);
    }

    private void initAnimation() {
        //旋转动画
        RotateAnimation ra = new RotateAnimation(0,360, Animation.RELATIVE_TO_SELF,0.5f,
                Animation.RELATIVE_TO_SELF,0.5f);
        ra.setDuration(1000);
        ra.setFillAfter(true);
        //缩放动画
        ScaleAnimation sa = new ScaleAnimation(0,1,0,1,Animation.RELATIVE_TO_SELF,0.5f,
                Animation.RELATIVE_TO_SELF,0.5f);
        sa.setDuration(1000);
        sa.setFillAfter(true);
        //透明动画
        AlphaAnimation aa = new AlphaAnimation(0,1);
        aa.setDuration(2000);
        aa.setFillAfter(true);
        //动画集合
        AnimationSet as = new AnimationSet(true);
        as.addAnimation(ra);
        as.addAnimation(sa);
        as.addAnimation(aa);

        rl_root.startAnimation(as);
        as.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //动画执行完毕时调用,跳转到新手引导页或主页
                boolean isFirst = SpUtils.getBoolean(getApplicationContext(), ConstantValue.IS_FIRST_ENTER, true);
                if (isFirst){
                    //第一次进入,跳转到新手引导
                    startActivity(new Intent(getApplicationContext(),GuideActivity.class));
                }else {
                    //不是第一次进入,跳转到主页
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                }

                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }
}
