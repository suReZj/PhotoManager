package com.example.sure.photomanager.Activity;

import android.content.Intent;
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
import org.litepal.crud.callback.UpdateOrDeleteCallback;

import java.util.List;

import adapter.PrivateAdapter;
import bean.Password;
import bean.PrivatePhoto;

public class PrivateActivity extends AppCompatActivity {
    private RecyclerView mRv;
    private List<PrivatePhoto> mList;
    private PrivateAdapter mAdapter;
    private ImageView mBackImage;
    private TextView mSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.private_activity);
        mList = LitePal.order("mDate desc").find(PrivatePhoto.class);
        initView();
        setListener();
    }

    public void initView() {
        mRv = findViewById(R.id.private_activity_rv);
        mBackImage = findViewById(R.id.private_activity_back);
        if (mList != null && mList.size() != 0) {
            mAdapter = new PrivateAdapter(mList);
            RecyclerView.LayoutManager mLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
            mRv.setAdapter(mAdapter);
            mRv.setLayoutManager(mLayoutManager);
        }

        mSetting = findViewById(R.id.private_activity_setting);
    }

    public void setListener() {
        mBackImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LitePal.deleteAllAsync(Password.class).listen(new UpdateOrDeleteCallback() {
                    @Override
                    public void onFinish(int rowsAffected) {
                        Intent intent = new Intent(PrivateActivity.this, PrivatePasswordActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
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
