package com.ly.disney;

import android.app.Activity;

/**
 * Created by liuyan on 2016/8/16.
 */
import java.lang.reflect.Field;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.TabHost.OnTabChangeListener;

public class TabsActivity extends Activity {
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_tab);
        final TabHost tabs=(TabHost)findViewById(R.id.tabhost);
        final TabWidget tabWidget=(TabWidget) findViewById(android.R.id.tabs);
        int width = 50;
        int height = 90;
        Field mBottomLeftStrip;
        Field mBottomRightStrip;
        tabs.setup();
        tabs.addTab(tabs.newTabSpec("first tab")
                .setIndicator("管理",getResources().getDrawable(R.drawable.icon_user_normal))
                .setContent(R.id.tab1));
        tabs.addTab(tabs.newTabSpec("second tab")
                .setIndicator("实时定位",getResources().getDrawable(R.drawable.icon_record_normal))
                .setContent(R.id.tab2));
        tabs.addTab(tabs.newTabSpec("third tab")
                .setIndicator("历史轨迹",getResources().getDrawable(R.drawable.icon_record_pressed))
                .setContent(R.id.tab3));

        tabs.setCurrentTab(0);


        for (int i =0; i <tabWidget.getChildCount(); i++) {
            /**
             * 设置高度、宽度，不过宽度由于设置为fill_parent，在此对它没效果
             */
            tabWidget.getChildAt(i).getLayoutParams().height = height;
            tabWidget.getChildAt(i).getLayoutParams().width = width;


            /**
             * 设置tab中标题文字的颜色，不然默认为黑色
             */
            final TextView tv = (TextView) tabWidget.getChildAt(i).findViewById(android.R.id.title);

            tv.setTextColor(this.getResources().getColorStateList(android.R.color.darker_gray));
            tv.setTextSize(15);




            /**
             * 此方法是为了去掉系统默认的色白的底角
             *
             * 在 TabWidget中mBottomLeftStrip、mBottomRightStrip
             * 都是私有变量，但是我们可以通过反射来获取
             *
             * 由于还不知道Android 2.2的接口是怎么样的，现在先加个判断好一些
             */
           /* if (Float.valueOf(Build.VERSION.RELEASE) <= 2.1) {
                try {
                    mBottomLeftStrip = tabWidget.getClass().getDeclaredField ("mBottomLeftStrip");
                    mBottomRightStrip = tabWidget.getClass().getDeclaredField ("mBottomRightStrip");
                    if(!mBottomLeftStrip.isAccessible()) {
                        mBottomLeftStrip.setAccessible(true);
                    }
                    if(!mBottomRightStrip.isAccessible()){
                        mBottomRightStrip.setAccessible(true);
                    }
                    mBottomLeftStrip.set(tabWidget, getResources().getDrawable (R.drawable.icon_user_pressed));
                    mBottomRightStrip.set(tabWidget, getResources().getDrawable (R.drawable.icon_user_pressed));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
            }*/
            View vvv = tabWidget.getChildAt(i);
            if(tabs.getCurrentTab()==i){
                vvv.setBackgroundDrawable(getResources().getDrawable(R.drawable.icon_user_pressed));
            }
            else {
                vvv.setBackgroundDrawable(getResources().getDrawable(R.drawable.icon_user_normal));
            }

        }

        /**
         * 当点击tab选项卡的时候，更改当前的背景
         */
        tabs.setOnTabChangedListener(new OnTabChangeListener(){
            @Override
            public void onTabChanged(String tabId) {
                // TODO Auto-generated method stub
                for (int i =0; i < tabWidget.getChildCount(); i++) {
                    View vvv = tabWidget.getChildAt(i);
                    if(tabs.getCurrentTab()==i){
                        vvv.setBackgroundDrawable(getResources().getDrawable(R.drawable.icon_user_pressed));
                    }
                    else {
                        vvv.setBackgroundDrawable(getResources().getDrawable(R.drawable.icon_user_normal));
                    }
                }
            }});


    }
}