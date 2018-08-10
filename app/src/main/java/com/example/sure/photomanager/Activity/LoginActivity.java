package com.example.sure.photomanager.Activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.sure.photomanager.R;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import bean.User;
import event.LoginEvent;
import gson.RegisterAndLoginGson;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {
    private ImageView mBackImage;
    private EditText mPhoneEt;
    private EditText mPasswordEt;
    private Button mLoginBtn;
    private Button mRegisterBtn;
    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    Toast.makeText(LoginActivity.this, getString(R.string.failure), Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    Toast.makeText(LoginActivity.this, getString(R.string.success), Toast.LENGTH_SHORT).show();
                    Bundle bundle = msg.getData();
                    String name = bundle.get("name").toString();
                    String passwor = bundle.get("password").toString();
                    User user = new User();
                    user.setmPhone(name);
                    user.setmPassword(passwor);
                    user.save();
                    EventBus.getDefault().post(new LoginEvent());
                    finish();
                    break;
                case 2:
                    Toast.makeText(LoginActivity.this, getString(R.string.success), Toast.LENGTH_SHORT).show();
                    break;
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//透明状态栏
        // 状态栏字体设置为深色，SYSTEM_UI_FLAG_LIGHT_STATUS_BAR 为SDK23增加
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

//            // 部分机型的statusbar会有半透明的黑色背景
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);// SDK21

        initView();
        setListener();
    }

    public void initView() {
        mBackImage = findViewById(R.id.login_activity_back);
        mPhoneEt = findViewById(R.id.login_activity_phone);
        mPasswordEt = findViewById(R.id.login_activity_password);
        mLoginBtn = findViewById(R.id.login_activity_login);
        mRegisterBtn = findViewById(R.id.login_activity_register);
    }

    public void setListener() {
        mBackImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((String.valueOf(mPasswordEt.getText()).length() > 0) && (String.valueOf(mPhoneEt.getText()).length() > 0)) {
                    loginUser(String.valueOf(mPhoneEt.getText()), String.valueOf(mPasswordEt.getText()), false);
                }
            }
        });

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((String.valueOf(mPasswordEt.getText()).length() > 0) && (String.valueOf(mPhoneEt.getText()).length() > 0)) {
                    loginUser(String.valueOf(mPhoneEt.getText()), String.valueOf(mPasswordEt.getText()), true);
//                    registerUser(String.valueOf(mPhoneEt.getText()), String.valueOf(mPasswordEt.getText()));
                }
            }
        });
    }


    public void registerUser(String name, String password) {
        //1.创建OkHttpClient对象
        OkHttpClient okHttpClient = new OkHttpClient();
        //2.通过new FormBody()调用build方法,创建一个RequestBody,可以用add添加键值对
        RequestBody requestBody = new FormBody.Builder().add("username", name).add("password", password).add("email", name).build();
        //3.创建Request对象，设置URL地址，将RequestBody作为post方法的参数传入
        Request request = new Request.Builder().url("http://www.ligohan.com:8080/springboot-security-demo/api/sys-user/save").post(requestBody).build();
        //4.创建一个call对象,参数就是Request请求对象
        Call call = okHttpClient.newCall(request);
        //5.请求加入调度,重写回调方法
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Gson gson = new Gson();
                RegisterAndLoginGson registerAndLoginGson = gson.fromJson(response.body().string(), RegisterAndLoginGson.class);
                if (registerAndLoginGson.isIs_success()) {
//                    Toast.makeText(LoginActivity.this, getString(R.string.registration_success), Toast.LENGTH_SHORT).show();
                    Message msg = handler.obtainMessage();
                    msg.what = 2;
                    handler.sendMessage(msg);
                } else {
                    Message msg = handler.obtainMessage();
                    msg.what = 0;
                    handler.sendMessage(msg);
                }
            }
        });
    }

    public void loginUser(final String name, final String password, final boolean isRegister) {
        //1.创建OkHttpClient对象
        OkHttpClient okHttpClient = new OkHttpClient();
        //2.通过new FormBody()调用build方法,创建一个RequestBody,可以用add添加键值对
        RequestBody requestBody = new FormBody.Builder().add("username", name).add("password", password).build();
        //3.创建Request对象，设置URL地址，将RequestBody作为post方法的参数传入
        Request request = new Request.Builder().url("http://www.ligohan.com:8080/springboot-security-demo/api/sys-user/login").post(requestBody).build();
        //4.创建一个call对象,参数就是Request请求对象
        Call call = okHttpClient.newCall(request);
        //5.请求加入调度,重写回调方法
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Gson gson = new Gson();
                RegisterAndLoginGson registerAndLoginGson = gson.fromJson(response.body().string(), RegisterAndLoginGson.class);
                if (isRegister) {
                    if (registerAndLoginGson.isResult()) {//用户已经存在
                        Message msg = handler.obtainMessage();
                        msg.what = 0;
                        handler.sendMessage(msg);
                    } else {
                        registerUser(name, password);
                    }
                } else {
                    if (registerAndLoginGson.isResult()) {
                        Message msg = handler.obtainMessage();
                        msg.what = 1;
                        Bundle bundle = new Bundle();
                        bundle.putString("name", name);
                        bundle.putString("password", password);
                        msg.setData(bundle);
                        handler.sendMessage(msg);
                    } else {
                        Message msg = handler.obtainMessage();
                        msg.what = 0;
                        handler.sendMessage(msg);
                    }
                }
            }
        });
    }

}
