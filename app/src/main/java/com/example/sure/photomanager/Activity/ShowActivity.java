package com.example.sure.photomanager.Activity;

import android.os.Build;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;


import com.example.sure.photomanager.R;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.GridHolder;
import com.orhanobut.dialogplus.OnItemClickListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adapter.ShowAlbumAdapter;
import adapter.ShowFragmentAdapter;
import bean.ArrangementAlbum;
import bean.Photo;
import event.DeleteAndShowNextEvent;
import fragment.ShowFragment;
import widght.DepthPageTransformer;
import widght.MyViewPager;

public class ShowActivity extends AppCompatActivity {
    private MyViewPager mViewPager;
    private ShowFragmentAdapter mAdapter;
    private ShowFragment mFragment;
    private List<ShowFragment> mFragmentList = new ArrayList<>();
    private List<String> mPathList = new ArrayList<>();
    private String mPath;
    private RecyclerView mRv;
    private List<ArrangementAlbum> mList = new ArrayList<>();
    private ShowAlbumAdapter mAlbumAdapter;
    private ImageView mPrivateImage;
    private ImageView mCloudImage;
    private ImageView mShareImage;
    private SimpleAdapter mSimpleAdapter;
    private int[] mImage = {R.mipmap.twitter, R.mipmap.instagram,
            R.mipmap.facebook};
    private String[] mImageName = {"Twitter", "Instagram", "Facebook"};
    private List<Map<String, Object>> mDataList = new ArrayList<>();
    private String mSystemPath = "DCIM/Camera";
    private List<Photo> mPhotoList;
    private ImageView mBackImage;
    private TextView mPlaceTv;
    private TextView mTimeTv;
    private int mIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_activity);
        EventBus.getDefault().register(this);

        mPath = getIntent().getStringExtra("path");
        mList.add(new ArrangementAlbum("aaa", 0, null));
        mList.add(new ArrangementAlbum("bbb", 0, null));
        initView();
        setListener();
    }

    public void initView() {
        mPhotoList = LitePal.where("mLocalPath like ?", "%" + mSystemPath + "%").find(Photo.class);
        mViewPager = findViewById(R.id.show_activity_viewPager);
        mRv = findViewById(R.id.show_activity_rv);
        mPrivateImage = findViewById(R.id.show_activity_private_image);
        mCloudImage = findViewById(R.id.show_activity_cloud_image);
        mShareImage = findViewById(R.id.show_activity_share_image);
        mBackImage = findViewById(R.id.show_activity_back_image);
        mPlaceTv = findViewById(R.id.show_activity_place_tv);
        mTimeTv = findViewById(R.id.show_activity_time_tv);

        for (int i = 0; i < mPhotoList.size(); i++) {
            if (mPhotoList.get(i).getmLocalPath().equals(mPath)) {
                mIndex = i;
                mTimeTv.setText((mIndex+1) + "/" + mPhotoList.size() + " " + getString(R.string.time));
            }
            mFragment = new ShowFragment(mPhotoList.get(i).getmLocalPath());
            mFragmentList.add(mFragment);
        }


        mAdapter = new ShowFragmentAdapter(getSupportFragmentManager(), mFragmentList);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(mIndex - 1);
        mViewPager.setPageTransformer(true, new DepthPageTransformer());

        mAlbumAdapter = new ShowAlbumAdapter(mList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRv.setAdapter(mAlbumAdapter);
        mRv.setLayoutManager(linearLayoutManager);

        mSimpleAdapter = new SimpleAdapter(this, getData(), R.layout.share_grid_item,
                new String[]{"img", "txt"}, new int[]{R.id.img_item, R.id.txt_item});

    }

    public void setListener() {
        mShareImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogPlus dialog = DialogPlus.newDialog(ShowActivity.this)
                        .setContentHolder(new GridHolder(3))
                        .setGravity(Gravity.CENTER)
                        .setAdapter(mSimpleAdapter)
                        .setExpanded(false, 500)
                        .setHeader(R.layout.share_dialog_header)
                        .setOnItemClickListener(new OnItemClickListener() {
                            @Override
                            public void onItemClick(DialogPlus dialog, Object item, View view, int position) {
                            }
                        })
                        .setExpanded(true)  // This will enable the expand feature, (similar to android L share dialog)
                        .create();
                dialog.show();
            }
        });

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mTimeTv.setText((position + 1) + "/" + mPhotoList.size() + " " + getString(R.string.time));
                mIndex = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

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

    private List<Map<String, Object>> getData() {
        for (int i = 0; i < mImage.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("img", mImage[i]);
            map.put("txt", mImageName[i]);
            mDataList.add(map);
        }
        return mDataList;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(DeleteAndShowNextEvent event) {
        mViewPager.setCurrentItem(mViewPager.getCurrentItem()+1);
        mIndex++;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
