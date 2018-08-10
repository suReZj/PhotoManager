package com.example.sure.photomanager.Activity;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sure.photomanager.R;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import adapter.PrivatePasswordAdapter;
import bean.Password;

public class PrivatePasswordActivity extends AppCompatActivity {
    private RecyclerView mRv;
    private List<String> mList;
    private PrivatePasswordAdapter mAdapter;
    private EditText mEt1;
    private EditText mEt2;
    private EditText mEt3;
    private EditText mEt4;
    private String mInput = "";
    private List<Password> mPassword;
    private ImageView mBackImage;
    private TextView mTv;
    private boolean mIsCreate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.private_password_activity);
        mPassword = LitePal.findAll(Password.class);
        initView();
        setListener();
    }

    public void initView() {
        mRv = findViewById(R.id.activity_private_password_rv);
        mEt1 = findViewById(R.id.activity_private_password_et1);
        mEt2 = findViewById(R.id.activity_private_password_et2);
        mEt3 = findViewById(R.id.activity_private_password_et3);
        mEt4 = findViewById(R.id.activity_private_password_et4);
        mBackImage = findViewById(R.id.activity_private_password_back);
        mTv = findViewById(R.id.activity_private_password_tv);
        mList = new ArrayList<>();
        mList.add("1");
        mList.add("2");
        mList.add("3");
        mList.add("4");
        mList.add("5");
        mList.add("6");
        mList.add("7");
        mList.add("8");
        mList.add("9");
        mList.add("delete");
        mList.add("0");
        mList.add("sure");
        mAdapter = new PrivatePasswordAdapter(mList);
        mRv.setAdapter(mAdapter);
        RecyclerView.LayoutManager mLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        mRv.setLayoutManager(mLayoutManager);

        if (mPassword.size() == 0) {
            mTv.setText(R.string.create_password);
            mIsCreate = true;
        } else {
            mTv.setText(R.string.enter_password);
        }
    }

    public void setListener() {
        mAdapter.setOnItemClickLitener(new PrivatePasswordAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(PrivatePasswordAdapter.ViewHolder view, int index) {
                switch (mInput.length()) {
                    case 0:
                        mEt1.setText(mAdapter.getNumber(index));
                        mInput = mInput.concat(mAdapter.getNumber(index));
                        Log.e("mInput", mInput);
                        break;
                    case 1:
                        mEt2.setText(mAdapter.getNumber(index));
                        mInput = mInput.concat(mAdapter.getNumber(index));
                        Log.e("mInput", mInput);
                        break;
                    case 2:
                        mEt3.setText(mAdapter.getNumber(index));
                        mInput = mInput.concat(mAdapter.getNumber(index));
                        Log.e("mInput", mInput);
                        break;
                    case 3:
                        mEt4.setText(mAdapter.getNumber(index));
                        mInput = mInput.concat(mAdapter.getNumber(index));
                        Log.e("mInput", mInput);
                        break;
                }
            }

            @Override
            public void onItemSureClick(PrivatePasswordAdapter.ViewHolder view, int index) {
                if (mInput.length() == 4) {
                    if (mIsCreate) {
                        Password password = new Password(mInput);
                        password.save();
                        mPassword = LitePal.findAll(Password.class);
                        mIsCreate = false;
                        mTv.setText(R.string.enter_password);
                        Toast.makeText(PrivatePasswordActivity.this, "Created successfully", Toast.LENGTH_SHORT).show();
                        mInput = "";
                        mEt1.setText("");
                        mEt2.setText("");
                        mEt3.setText("");
                        mEt4.setText("");
                    } else {
                        if (mInput.equals(mPassword.get(0).getmPass())) {
//                            Toast.makeText(PrivatePasswordActivity.this, "The password is correct", Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(PrivatePasswordActivity.this,PrivateActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(PrivatePasswordActivity.this, "The password is error", Toast.LENGTH_SHORT).show();
                            mInput = "";
                            mEt1.setText("");
                            mEt2.setText("");
                            mEt3.setText("");
                            mEt4.setText("");
                        }
                    }
                }

            }

            @Override
            public void onItemDeleteClick(PrivatePasswordAdapter.ViewHolder view, int index) {
                switch (mInput.length()) {
                    case 1:
                        mEt1.setText("");
                        mInput = "";
                        break;
                    case 2:
                        mEt2.setText("");
                        mInput = mInput.substring(0, 1);
                        break;
                    case 3:
                        mEt3.setText("");
                        mInput = mInput.substring(0, 2);
                        break;
                    case 4:
                        mEt4.setText("");
                        mInput = mInput.substring(0, 3);
                        break;
                }
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
}
