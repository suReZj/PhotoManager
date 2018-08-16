package com.example.sure.photomanager.Activity;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sure.photomanager.R;

import org.litepal.LitePal;

import java.util.List;

import adapter.ArrangementDetailAdapter;
import bean.ArrangementAlbum;

public class ArrangementDetailActivity extends AppCompatActivity {
    private ImageView mBackImage;
    private TextView mNameTv;
    private TextView mSumTv;
    private RecyclerView mRv;
    private String mPath;
    private List<String> mList;
    private ArrangementDetailAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.arrangement_detail_activity);
        mPath = getIntent().getStringExtra("path");
        mList = LitePal.where("name = ?", mPath).find(ArrangementAlbum.class).get(0).getmList();
        initView();
        setListener();
    }

    public void initView() {
        mBackImage = findViewById(R.id.arrangement_detail_activity_back);
        mNameTv = findViewById(R.id.arrangement_detail_activity_name);
        mSumTv = findViewById(R.id.arrangement_detail_activity_sum);
        mRv = findViewById(R.id.arrangement_detail_activity_rv);

        mAdapter = new ArrangementDetailAdapter(mList,mPath);
        RecyclerView.LayoutManager mLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        mRv.setAdapter(mAdapter);
        mRv.setLayoutManager(mLayoutManager);

        mNameTv.setText(mPath);
        mSumTv.setText(String.valueOf(mList.size()));
    }

    public void setListener() {
        mBackImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    //实现真正的沉浸式
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && Build.VERSION.SDK_INT >= 19) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }
}
