package com.example.sure.photomanager.Activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.example.sure.photomanager.R;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;


public class SearchActivity extends AppCompatActivity {
    private MaterialSearchBar mSearchBar;
    private TagFlowLayout mFlowLayout1;
    private TagFlowLayout mFlowLayout2;
    private TagFlowLayout mFlowLayout3;
    private TagFlowLayout mFlowLayout4;
    private String[] mHistory = new String[]
            {"自拍", "美食", "乌鲁木齐", "内蒙古", "风景", "美女", "合照"};
    private LayoutInflater mInflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//透明状态栏
        // 状态栏字体设置为深色，SYSTEM_UI_FLAG_LIGHT_STATUS_BAR 为SDK23增加
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

//            // 部分机型的statusbar会有半透明的黑色背景
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);// SDK21
        initView();
    }

    public void initView() {
        mInflater = LayoutInflater.from(this);
//        mEt=findViewById(R.id.search_activity_et);
        mFlowLayout1 = findViewById(R.id.search_activity_flow1);
        mFlowLayout2 = findViewById(R.id.search_activity_flow2);
        mFlowLayout3 = findViewById(R.id.search_activity_flow3);
        mFlowLayout4 = findViewById(R.id.search_activity_flow4);
        mFlowLayout1.setAdapter(new TagAdapter<String>(mHistory) {

            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) mInflater.inflate(R.layout.flowlayout_item_tv,
                        mFlowLayout1, false);
                tv.setText(s);
                return tv;
            }
        });
        mFlowLayout2.setAdapter(new TagAdapter<String>(mHistory) {

            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) mInflater.inflate(R.layout.flowlayout_item_tv,
                        mFlowLayout2, false);
                tv.setText(s);
                return tv;
            }
        });
        mFlowLayout3.setAdapter(new TagAdapter<String>(mHistory) {

            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) mInflater.inflate(R.layout.flowlayout_item_tv,
                        mFlowLayout3, false);
                tv.setText(s);
                return tv;
            }
        });
        mFlowLayout4.setAdapter(new TagAdapter<String>(mHistory) {

            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) mInflater.inflate(R.layout.flowlayout_item_tv,
                        mFlowLayout4, false);
                tv.setText(s);
                return tv;
            }
        });
    }

    public void setListener() {

    }


//    //实现真正的沉浸式
//    @Override
//    public void onWindowFocusChanged(boolean hasFocus) {
//        super.onWindowFocusChanged(hasFocus);
//        if (hasFocus && Build.VERSION.SDK_INT >= 19) {
//            View decorView = getWindow().getDecorView();
//            decorView.setSystemUiVisibility(
//                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                            | View.SYSTEM_UI_FLAG_FULLSCREEN
//                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
//        }
////        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//透明状态栏
////        // 状态栏字体设置为深色，SYSTEM_UI_FLAG_LIGHT_STATUS_BAR 为SDK23增加
////        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
//
//////            // 部分机型的statusbar会有半透明的黑色背景
////        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
////        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
////        getWindow().setStatusBarColor(Color.TRANSPARENT);// SDK21
//    }
//
//
//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
//            View v = getCurrentFocus();
//            if (isShouldHideInput(v, ev)) {
//                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                if (imm != null) {
//                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
//                    v.clearFocus();
//                    onWindowFocusChanged(true);
//                }
//            }
//            return super.dispatchTouchEvent(ev);
//        }
//        // 必不可少，否则所有的组件都不会有TouchEvent了
//        if (getWindow().superDispatchTouchEvent(ev)) {
//            return true;
//        }
//        return onTouchEvent(ev);
//    }
//
//    /**
//     * 点击输入框外 隐藏软键盘
//     * @param v
//     * @param event
//     * @return
//     */
//    public  boolean isShouldHideInput(View v, MotionEvent event) {
//        if (v != null && (v instanceof EditText)) {
//            int[] leftTop = { 0, 0 };
//            //获取输入框当前的location位置
//            v.getLocationInWindow(leftTop);
//            int left = leftTop[0];
//            int top = leftTop[1];
//            int bottom = top + v.getHeight();
//            int right = left + v.getWidth();
//            if (event.getX() > left && event.getX() < right
//                    && event.getY() > top && event.getY() < bottom) {
//                // 点击的是输入框区域，保留点击EditText的事件
//                return false;
//            } else {
//                return true;
//            }
//        }
//        return false;
//    }
}
