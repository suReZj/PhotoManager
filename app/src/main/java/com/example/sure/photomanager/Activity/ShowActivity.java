package com.example.sure.photomanager.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;


import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.sure.photomanager.R;
import com.google.gson.Gson;
import com.nanchen.compresshelper.CompressHelper;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.GridHolder;
import com.orhanobut.dialogplus.OnItemClickListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adapter.ShowAlbumAdapter;
import adapter.ShowFragmentAdapter;
import bean.ArrangementAlbum;
import bean.Password;
import bean.Photo;
import bean.PrivatePhoto;
import bean.UploadPhoto;
import bean.User;
import event.DeleteAndShowNextEvent;
import event.LoginEvent;
import event.RefreshData;
import event.UploadEvent;
import fragment.ShowFragment;
import gson.RegisterAndLoginGson;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import untils.DateUtil;
import untils.FileUtil;
import untils.WordUtil;
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
    private String mOperationPath = null;
    private LocationManager locationManager;
    private TextView mSumTv;

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    Toast.makeText(ShowActivity.this, "Successful upload", Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    Toast.makeText(ShowActivity.this, "Upload failed", Toast.LENGTH_SHORT).show();
                    break;
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_activity);
        EventBus.getDefault().register(this);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mPath = getIntent().getStringExtra("path");


        initView();
        setListener();
    }

    public void initView() {
        if (getIntent().getStringExtra("name") != null) {
            mList = LitePal.findAll(ArrangementAlbum.class);
            for (int i = 0; i < mList.size(); i++) {
                if (mList.get(i).getName().equals("delete")) {
                    mList.remove(i);
                }

            }
            for (int i = 0; i < mList.size(); i++) {
                if (mList.get(i).getName().equals(getIntent().getStringExtra("name"))) {
                    mList.remove(i);
                }
            }

            mPhotoList = new ArrayList<>();
            List<String> albumPath = LitePal.where("name = ?", getIntent().getStringExtra("name")).find(ArrangementAlbum.class).get(0).getmList();
            for (int i = 0; i < albumPath.size(); i++) {
                Photo photo = LitePal.where("mLocalPath = ?", albumPath.get(i)).find(Photo.class).get(0);
                mPhotoList.add(photo);
            }

            mList.add(new ArrangementAlbum("Add Album", 0, null));


        } else {
            List<Photo> photos = LitePal.where("mLocalPath like ?", "%" + mSystemPath + "%").order("mDate desc").find(Photo.class);
            mPhotoList = new ArrayList<>();
            for (int i = 0; i < photos.size(); i++) {
                if (!photos.get(i).ismIsSort()) {
                    mPhotoList.add(photos.get(i));
                }
            }
//        mPhotoList = LitePal.where("mLocalPath like ?", "%" + mSystemPath + "%").order("mDate desc").find(Photo.class);
            mList = LitePal.findAll(ArrangementAlbum.class);
            for (int i = 0; i < mList.size(); i++) {
                if (mList.get(i).getName().equals("delete")) {
                    mList.remove(i);
                }
            }
            mList.add(new ArrangementAlbum("Add Album", 0, null));
        }


        mViewPager = findViewById(R.id.show_activity_viewPager);
        mRv = findViewById(R.id.show_activity_rv);
        mPrivateImage = findViewById(R.id.show_activity_private_image);
        mCloudImage = findViewById(R.id.show_activity_cloud_image);
        mShareImage = findViewById(R.id.show_activity_share_image);
        mBackImage = findViewById(R.id.show_activity_back_image);
        mPlaceTv = findViewById(R.id.show_activity_place_tv);
        mTimeTv = findViewById(R.id.show_activity_time_tv);
        mSumTv = findViewById(R.id.show_activity_sum);
        setSum();


        for (int i = 0; i < mPhotoList.size(); i++) {
            if (mPhotoList.get(i).getmLocalPath().equals(mPath)) {
                mIndex = i;
                mTimeTv.setText((mIndex + 1) + "/" + mPhotoList.size() + " " + DateUtil.changeTime(mPhotoList.get(mIndex).getmDate()));
                String city;
                city = getLocation(mPhotoList.get(mIndex).getmLatitude(), mPhotoList.get(mIndex).getmLongitude());
                if (city.length() != 0) {
                    mPlaceTv.setText(city);
                }

            }
            mFragment = new ShowFragment(mPhotoList.get(i).getmLocalPath());
            mFragmentList.add(mFragment);
        }


        mAdapter = new ShowFragmentAdapter(getSupportFragmentManager(), mFragmentList);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(mIndex);
        mViewPager.setPageTransformer(true, new DepthPageTransformer());

        mAlbumAdapter = new ShowAlbumAdapter(mList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRv.setAdapter(mAlbumAdapter);
        mRv.setLayoutManager(linearLayoutManager);

        mSimpleAdapter = new SimpleAdapter(this, getData(), R.layout.share_grid_item,
                new String[]{"img", "txt"}, new int[]{R.id.img_item, R.id.txt_item});

    }


    public void setSum() {
        List<ArrangementAlbum> albums = LitePal.where("name = ?", "delete").find(ArrangementAlbum.class);
        if(albums.size()==0){
            mSumTv.setVisibility(View.GONE);
        }else {
            mSumTv.setVisibility(View.VISIBLE);
            mSumTv.setText(albums.get(0).getSum()+"");
        }
    }


    public void setListener() {
        mAlbumAdapter.setOnItemClickLitener(new ShowAlbumAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(ShowAlbumAdapter.ViewHolder view, final int index) {
                mOperationPath = mPhotoList.get(mViewPager.getCurrentItem()).getmLocalPath();
                if (getIntent().getStringExtra("name") == null) {
                    changeViewPager();
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        List<String> list = new ArrayList<>();
                        list.add(mAlbumAdapter.getAlbum(index));
                        List<String> mPathList = new ArrayList<>();
                        mPathList.add(mOperationPath);
//                        try {
//                            FileUtil.catchStreamToFile(mPathList, list, ShowActivity.this);
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
                        FileUtil.sortPhoto(mPathList, list, ShowActivity.this);

                    }
                }).run();
            }

            @Override
            public void onCreateClick(ShowAlbumAdapter.ViewHolder view, int index) {
                new MaterialDialog.Builder(ShowActivity.this)
                        .title(R.string.sort_to_new_album)
                        .content(R.string.sort_to_new_album_content)
                        .inputType(
                                InputType.TYPE_CLASS_TEXT
                                        | InputType.TYPE_TEXT_VARIATION_PERSON_NAME
                                        | InputType.TYPE_TEXT_FLAG_CAP_WORDS)
                        .positiveText(R.string.sort)
                        .negativeText(R.string.cancel)
                        .alwaysCallInputCallback() // this forces the callback to be invoked with every input change
                        .input(R.string.hint, 0, false, new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                            }
                        })
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull final MaterialDialog dialog, @NonNull DialogAction which) {
                                mOperationPath = mPhotoList.get(mViewPager.getCurrentItem()).getmLocalPath();
                                changeViewPager();
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        List<String> list = new ArrayList<>();
                                        list.add(String.valueOf(dialog.getInputEditText().getText()));
                                        List<String> mPathList = new ArrayList<>();
                                        mPathList.add(mOperationPath);
//                                        try {
//                                            FileUtil.catchStreamToFile(mPathList, list, ShowActivity.this);
//                                        } catch (IOException e) {
//                                            e.printStackTrace();
//                                        }
                                        FileUtil.sortPhoto(mPathList, list, ShowActivity.this);

                                    }
                                }).run();
                            }
                        })
                        .show();
            }
        });


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
                mTimeTv.setText((position + 1) + "/" + mPhotoList.size() + " " + DateUtil.changeTime(mPhotoList.get(position).getmDate()));
                String city;
                city = getLocation(mPhotoList.get(position).getmLatitude(), mPhotoList.get(position).getmLongitude());
                if (city.length() != 0) {
                    mPlaceTv.setText(city);
                }
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

        mPrivateImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Password> mPassword = LitePal.findAll(Password.class);
                if (mPassword.size() == 0) {
                    Intent intent = new Intent(ShowActivity.this, PrivatePasswordActivity.class);
                    startActivity(intent);
                } else {
                    mOperationPath = mPhotoList.get(mViewPager.getCurrentItem()).getmLocalPath();
                    changeViewPager();
                    final List<String> mPath = new ArrayList<>();
                    mPath.add(mOperationPath);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            lockPhoto(mPath);
                        }
                    }).run();
                }

            }
        });

        mCloudImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<User> list = LitePal.findAll(User.class);
                if (list.size() == 0) {
                    Toast.makeText(ShowActivity.this, "Please Login", Toast.LENGTH_SHORT).show();
                } else {
                    mOperationPath = mPhotoList.get(mViewPager.getCurrentItem()).getmLocalPath();
                    upLoadPhoto(mOperationPath);
                }
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

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void Event(DeleteAndShowNextEvent event) {
        mOperationPath = mPhotoList.get(mViewPager.getCurrentItem()).getmLocalPath();

        changeViewPager();

        final List<String> list = new ArrayList<>();
        list.add("delete");
        final List<String> mPathList = new ArrayList<>();
        mPathList.add(mOperationPath);

        new Thread(new Runnable() {
            @Override
            public void run() {
                FileUtil.deletePhoto(mPathList, list, ShowActivity.this);
            }
        }).run();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(RefreshData event) {
        if (event.getmMode().equals("lock")) {
            Toast.makeText(ShowActivity.this, "successfully lock", Toast.LENGTH_SHORT).show();
        }if(event.getmMode().equals("delete")){
            setSum();
        }
        if (getIntent().getStringExtra("name") == null) {
            mAlbumAdapter.refreshData();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void changeViewPager() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mFragmentList.remove(mViewPager.getCurrentItem());
                mPhotoList.remove(mViewPager.getCurrentItem());
                mAdapter.notifyDataSetChanged();

                mViewPager.setCurrentItem(mIndex);
//                mTimeTv.setText((mIndex + 1) + "/" + mPhotoList.size() + " " + getString(R.string.time));
                mTimeTv.setText((mIndex + 1) + "/" + mPhotoList.size() + " " + DateUtil.changeTime(mPhotoList.get(mIndex).getmDate()));
                String city;
                city = getLocation(mPhotoList.get(mIndex).getmLatitude(), mPhotoList.get(mIndex).getmLongitude());
                if (city.length() != 0) {
                    mPlaceTv.setText(city);
                }
            }
        });
    }


    public void lockPhoto(final List<String> photo) {
        PrivatePhoto privatePhoto;
        Photo photo1;

        for (int i = 0; i < photo.size(); i++) {
            photo1 = LitePal.where("mLocalPath = ?", photo.get(i)).find(Photo.class).get(0);
            File file = new File(photo1.getmLocalPath());
            File newFile = CompressHelper.getDefault(this).compressToFile(file);
            byte[] byt = Bitmap2Bytes(BitmapFactory.decodeFile(newFile.getAbsolutePath()));
            privatePhoto = new PrivatePhoto(photo1, byt);
//            privatePhoto = new PrivatePhoto(photo1, null);
            privatePhoto.save();
            photo1.delete();
        }


//        new Thread(new Runnable() {
//            @Override
//            public void run() {
        FileUtil.privateImage(photo, ShowActivity.this);
//            }
//        }).run();
    }

    public byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return baos.toByteArray();
    }

    public String getLocation(String lati, String longi) {
        String city = "";
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return city;
        }


        if (lati == null || longi == null) {

        } else {
            double latitude = Double.valueOf(lati);
            double longitude = Double.valueOf(longi);
            double[] data = {latitude, longitude};
            Location location = locationManager
                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//        if (location != null) {
//            latitude = location.getLatitude(); // 经度
//            longitude = location.getLongitude(); // 纬度
//            double[] data = {latitude, longitude};
//        }

            List<Address> addList = null;
            Geocoder ge = new Geocoder(getApplicationContext());
            try {
                addList = ge.getFromLocation(data[0], data[1], 1);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if (addList != null && addList.size() > 0) {
                for (int i = 0; i < addList.size(); i++) {
                    Address ad = addList.get(i);
                    city = ad.getLocality();
                }
            }
            Log.e("city", city.toString());
        }
        if (city.length() != 0) {
            city = city.substring(0, city.length() - 1);
            city = WordUtil.getInstance().getSelling(city.toString());
            String city1 = city.substring(1, city.length());
            String first = city.substring(0, 1).toUpperCase();
            city = first.concat(city1);
        }
        return city;
    }


    public void upLoadPhoto(String path) {
        final List<User> list = LitePal.findAll(User.class);
        final List<Photo> photo = LitePal.where("mLocalPath = ?", path).find(Photo.class);
        OkHttpClient mOkHttpClent = new OkHttpClient();
        File file = new File(path);
        Log.e("oldName", file.getName());
        File newFile = CompressHelper.getDefault(this).compressToFile(file);
        Log.e("NewName", newFile.getName());

        MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        RequestBody body = RequestBody.create(MediaType.parse("image/*"), newFile);
        final String filename = file.getName();
        // 参数分别为， 请求key ，文件名称 ， RequestBody
        requestBody.addFormDataPart("file", filename, body);

        Request request = new Request.Builder().url("http://www.ligohan.com:8080/springboot-security-demo/api/file/upload").post(requestBody.build()).build();


        Call call = mOkHttpClent.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("faile", "onFailure: " + e);
                Message msg = handler.obtainMessage();
                msg.what = 1;
                handler.sendMessage(msg);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e("success", "成功" + response);
                Gson gson = new Gson();
                RegisterAndLoginGson registerAndLoginGson = gson.fromJson(response.body().string(), RegisterAndLoginGson.class);
                if (registerAndLoginGson.isIs_success()) {
                    UploadPhoto uploadPhoto = new UploadPhoto("http://www.ligohan.com:8080/springboot-security-demo/api/file/download?fileName=" + filename, list.get(0).getmPhone(), photo.get(0));
                    uploadPhoto.save();
                    Message msg = handler.obtainMessage();
                    msg.what = 0;
                    handler.sendMessage(msg);
                    EventBus.getDefault().post(new UploadEvent());
                    EventBus.getDefault().post(new LoginEvent());
                } else {
                    Message msg = handler.obtainMessage();
                    msg.what = 1;
                    handler.sendMessage(msg);
                }

            }
        });
    }
}
