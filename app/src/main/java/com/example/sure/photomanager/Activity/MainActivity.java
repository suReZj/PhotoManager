package com.example.sure.photomanager.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.sure.photomanager.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import adapter.FragmentAdapter;
import bean.Photo;
import fragment.AlbumFragment;
import fragment.CollectFragment;
import fragment.OtherFragment;
import fragment.SecretFragment;

public class MainActivity extends AppCompatActivity {
    private AppBarLayout mAppBarLayout;
    private Toolbar mToolbar;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private List<Fragment> mFragmentList = new ArrayList<>();
    private AlbumFragment mAlbumFragment;
    private OtherFragment mOtherFragment;
    private CollectFragment mCollectFragment;
    private SecretFragment mSecretFragment;
    private FragmentAdapter mFragmentAdapter;
    private List<String> mTitleList = new ArrayList<>();
    private final int REQUEST_CAMERA_PERMISSION = 0;
    private List<Photo> mPhotoList = new ArrayList<>();
    private HashMap<String, List<Photo>> mAllPhotosTemp;
    private Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    private String mSystemAlbum = "DCIM/Camera";
    private String mSystemPath = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        requestPermission();
        initView();
        setListener();
        getData();
    }

    public void initView() {
        mTitleList.add(getString(R.string.album));
        mTitleList.add(getString(R.string.other));
        mTitleList.add(getString(R.string.collect));
        mTitleList.add(getString(R.string.secret));
        mAppBarLayout = findViewById(R.id.activity_main_abl);
        mToolbar = findViewById(R.id.activity_main_toolbar);
        mTabLayout = findViewById(R.id.activity_main_tl);
        mViewPager = findViewById(R.id.activity_main_vp);

        mAlbumFragment = new AlbumFragment();
        mFragmentList.add(mAlbumFragment);
        mOtherFragment = new OtherFragment();
        mFragmentList.add(mOtherFragment);
        mCollectFragment = new CollectFragment();
        mFragmentList.add(mCollectFragment);
        mSecretFragment = new SecretFragment();
        mFragmentList.add(mSecretFragment);

        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        mToolbar.setTitleTextColor(getResources().getColor(R.color.white));

        mFragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), mFragmentList, mTitleList);
        mViewPager.setAdapter(mFragmentAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    public void setListener() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.photograph:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void requestPermission() {
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Log.e("CameraNew", "Lacking privileges to access camera service, please request permission first.");
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE
            }, REQUEST_CAMERA_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length <= 0 || grantResults[0] == PackageManager.PERMISSION_DENIED) {
                // Permission denied
                Toast.makeText(this, "User denied permission, can't use take photo feature.",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "1111111111",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void getData() {
        mAllPhotosTemp = new HashMap<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                String[] projImage = {MediaStore.Images.Media._ID
                        , MediaStore.Images.Media.DATA
                        , MediaStore.Images.Media.SIZE
                        , MediaStore.Images.Media.DISPLAY_NAME};
                Cursor mCursor = getContentResolver().query(mImageUri,
                        projImage,
                        MediaStore.Images.Media.MIME_TYPE + "=? or " + MediaStore.Images.Media.MIME_TYPE + "=? or " +
                                MediaStore.Images.Media.MIME_TYPE + "=? or " + MediaStore.Images.Media.MIME_TYPE + "=?",
                        new String[]{"image/jpeg", "image/png", "image/jpg", "image/bmp"},
                        MediaStore.Images.Media.DATE_MODIFIED + " desc");

                if (mCursor != null) {
                    while (mCursor.moveToNext()) {
                        // 获取图片的路径
                        String path = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DATA));
                        int size = mCursor.getInt(mCursor.getColumnIndex(MediaStore.Images.Media.SIZE)) / 1024;
                        String displayName = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));
                        // 获取该图片的父路径名
                        String dirPath = new File(path).getParentFile().getAbsolutePath();
                        int date = mCursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED);
                        Log.e("date",date+"");


                        if (dirPath.contains(mSystemAlbum) && mSystemPath != null) {
                            mSystemPath = dirPath;
                        }
                        if (mAllPhotosTemp.containsKey(dirPath)) {
                            List<Photo> data = mAllPhotosTemp.get(dirPath);
                            data.add(new Photo(path, size, displayName, dirPath));
                            continue;
                        } else {
                            List<Photo> data = new ArrayList<>();
                            data.add(new Photo(path, size, displayName, dirPath));
                            mAllPhotosTemp.put(dirPath, data);
                        }
                    }
                    mCursor.close();
                }
                //更新界面
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }
        }).start();
    }
}
