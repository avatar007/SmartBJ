package com.studio.smartbj;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView tv = new TextView(this);
        tv.setText("我是智慧北京的欢迎界面,很炫的一个界面");
        tv.setTextColor(Color.RED);
        tv.setTextSize(40);
        setContentView(tv);
    }
}
