package com.example.sure.photomanager.Activity;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.sure.photomanager.R;

import java.util.ArrayList;
import java.util.List;

import adapter.FragmentAdapter;
import fragment.AlbumFragment;
import fragment.CollectFragment;
import fragment.OtherFragment;
import fragment.SecretFragment;

public class MainActivity extends AppCompatActivity {
    private AppBarLayout mAppBarLayout;
    private Toolbar mToolbar;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private List<Fragment> mFragmentList=new ArrayList<>();
    private AlbumFragment mAlbumFragment;
    private OtherFragment mOtherFragment;
    private CollectFragment mCollectFragment;
    private SecretFragment mSecretFragment;
    private FragmentAdapter mFragmentAdapter;
    private List<String> mTitleList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        initView();
        setListener();
    }

    public void initView(){
        mTitleList.add(getString(R.string.album));
        mTitleList.add(getString(R.string.other));
        mTitleList.add(getString(R.string.collect));
        mTitleList.add(getString(R.string.secret));
        mAppBarLayout=findViewById(R.id.activity_main_abl);
        mToolbar=findViewById(R.id.activity_main_toolbar);
        mTabLayout=findViewById(R.id.activity_main_tl);
        mViewPager=findViewById(R.id.activity_main_vp);

        mAlbumFragment=new AlbumFragment();
        mFragmentList.add(mAlbumFragment);
        mOtherFragment=new OtherFragment();
        mFragmentList.add(mOtherFragment);
        mCollectFragment=new CollectFragment();
        mFragmentList.add(mCollectFragment);
        mSecretFragment=new SecretFragment();
        mFragmentList.add(mSecretFragment);

        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        mToolbar.setTitleTextColor(getResources().getColor(R.color.white));

        mFragmentAdapter=new FragmentAdapter(getSupportFragmentManager(),mFragmentList,mTitleList);
        mViewPager.setAdapter(mFragmentAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    public void setListener(){

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
}
