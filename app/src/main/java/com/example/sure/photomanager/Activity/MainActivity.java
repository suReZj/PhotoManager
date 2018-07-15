package com.example.sure.photomanager.Activity;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.sure.photomanager.R;

public class MainActivity extends AppCompatActivity {
    private AppBarLayout mAppBarLayout;
    private Toolbar mToolbar;
    private RecyclerView mRv;
    private TabLayout mTabLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        setListener();
    }

    public void initView(){
        mAppBarLayout=findViewById(R.id.activity_main_abl);
        mToolbar=findViewById(R.id.activity_main_toolbar);
        mRv=findViewById(R.id.activity_main_rv);
        mTabLayout=findViewById(R.id.activity_main_tl);

        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        mToolbar.setTitleTextColor(getResources().getColor(R.color.white));
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
