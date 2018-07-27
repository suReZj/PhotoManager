package com.example.sure.photomanager.Activity;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;


import com.example.sure.photomanager.R;

import fragment.HomeFragment;
import widght.BottomNavigationViewHelper;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView mBottomNavigationView;
    private FragmentManager mFragmentManager;
    private HomeFragment mHomeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        initView();
        setListener();
    }

    public void initView() {
        mBottomNavigationView = findViewById(R.id.main_activity_BNV);
        BottomNavigationViewHelper.disableShiftMode(mBottomNavigationView);
        mFragmentManager=getSupportFragmentManager();
        mHomeFragment=new HomeFragment();
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        ft.add(R.id.main_activity_fl, mHomeFragment);
        ft.show(mHomeFragment);
        ft.commit();
    }

    public void setListener() {
        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_bottom_home:
                        break;
                    case R.id.navigation_bottom_intelligent:
                        break;
                    case R.id.navigation_bottom_cloudAlbum:
                        break;
                    case R.id.navigation_bottom_personalCenter:
                        break;
                    default:
                        return false;
                }
                return true;
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
