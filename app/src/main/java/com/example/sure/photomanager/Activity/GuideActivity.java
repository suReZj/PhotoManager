package com.example.sure.photomanager.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sure.photomanager.R;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;
import org.litepal.crud.callback.SaveCallback;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import bean.Album;
import bean.Photo;
import untils.DateUtil;

public class GuideActivity extends AppCompatActivity {
    private final int REQUEST_CAMERA_PERMISSION = 0;
    private Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    private String mSystemAlbum = "DCIM/Camera";
    private String mSystemPath = null;
    private Button mBtn;
    private TextView mTv;
    private ProgressBar mPb;
    private String mSelfPath = "System/Photo";
    private String mSelfName = "系统相册";
    private List<Album> mAlbumList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guide_activity);
        mBtn = findViewById(R.id.guide_activity_btn);
        mPb = findViewById(R.id.guide_activity_pb);
        mTv = findViewById(R.id.guide_activity_tv);
        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GuideActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        requestPermission();
        getData();

    }


    public void requestPermission() {
        if (ActivityCompat.checkSelfPermission(GuideActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(GuideActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(GuideActivity.this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(GuideActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(GuideActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                ) {
            Log.e("CameraNew", "Lacking privileges to access camera service, please request permission first.");
            ActivityCompat.requestPermissions(GuideActivity.this, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.INTERNET,
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
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

            }
        }
    }

    public void getData() {
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
                                MediaStore.Images.Media.MIME_TYPE + "=?",
                        new String[]{"image/jpeg", "image/png", "image/jpg"},
                        MediaStore.Images.Media.DATE_TAKEN + " desc");

                int length = LitePal.findAll(Photo.class).size();
                if (mCursor != null && (length != mCursor.getCount())) {
                    LitePal.deleteAll(Photo.class);
                    ExifInterface exifInterface = null;
                    String longitude = null;//经度
                    String latitude = null;//纬度
                    String date = null;
                    while (mCursor.moveToNext()) {
                        // 获取图片的路径
                        String path = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DATA));
                        int size = mCursor.getInt(mCursor.getColumnIndex(MediaStore.Images.Media.SIZE)) / 1024;
                        String displayName = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));
                        // 获取该图片的父路径名
                        String dirPath = new File(path).getParentFile().getAbsolutePath();

                        if (dirPath.contains(mSystemAlbum) && mSystemPath == null) {
                            mSystemPath = dirPath;
                        }

                        try {
                            exifInterface = new ExifInterface(path);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        List<Photo> existPhoto = LitePal.where("mLocalPath = ?", path).find(Photo.class);

                        if (existPhoto.size() > 0) {
                            continue;
                        } else {
                            longitude = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
                            latitude = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
                            date = exifInterface.getAttribute(ExifInterface.TAG_DATETIME);
                            String latRef = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF);
                            String lngRef = exifInterface.getAttribute
                                    (ExifInterface.TAG_GPS_LONGITUDE_REF);
                            if (latitude != null && longitude != null) {
                                latitude = String.valueOf(convertRationalLatLonToFloat(latitude, latRef));
                                longitude = String.valueOf(convertRationalLatLonToFloat(longitude, lngRef));
                            }

//                            if (date != null && date.length() > 0) {
//                                date = date.substring(0, 10);
//                                date = DateUtil.changeTime(date);
//                            }
                            Photo photo = new Photo(path, size, displayName, dirPath, date, longitude, latitude);
                            photo.saveAsync().listen(new SaveCallback() {
                                @Override
                                public void onFinish(boolean success) {

                                }
                            });
                            List<Album> albumList = LitePal.where("mPath = ?", dirPath).find(Album.class);
                            if (albumList.size() > 0) {

                            } else {
                                String name = dirPath;
                                int index = name.lastIndexOf("/");
                                name = name.substring(index + 1, name.length());
                                Album album = new Album(dirPath, name);
                                album.saveAsync().listen(new SaveCallback() {
                                    @Override
                                    public void onFinish(boolean success) {

                                    }
                                });
                            }
                        }
                    }
                    mCursor.close();

                }
                //更新界面
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mBtn.setBackground(getResources().getDrawable(R.color.open_btn_color));
                        mPb.setVisibility(View.GONE);
                        mTv.setText("");
                        mBtn.setVisibility(View.VISIBLE);
                        Log.e("Photo Count=========", LitePal.findAll(Photo.class).size() + "");
                        Log.e("Album Count=========", LitePal.findAll(Album.class).size() + "");
                    }
                });
            }
        }).start();
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

    private float convertRationalLatLonToFloat(
            String rationalString, String ref) {

        String[] parts = rationalString.split(",");

        String[] pair;
        pair = parts[0].split("/");
        double degrees = Double.parseDouble(pair[0].trim())
                / Double.parseDouble(pair[1].trim());

        pair = parts[1].split("/");
        double minutes = Double.parseDouble(pair[0].trim())
                / Double.parseDouble(pair[1].trim());

        pair = parts[2].split("/");
        double seconds = Double.parseDouble(pair[0].trim())
                / Double.parseDouble(pair[1].trim());

        double result = degrees + (minutes / 60.0) + (seconds / 3600.0);
        if ((ref.equals("S") || ref.equals("W"))) {
            return (float) -result;
        }
        return (float) result;
    }

}
