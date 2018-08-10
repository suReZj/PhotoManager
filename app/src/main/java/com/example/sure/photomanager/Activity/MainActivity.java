package com.example.sure.photomanager.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;


import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.sure.photomanager.R;
import com.nanchen.compresshelper.CompressHelper;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.GridHolder;
import com.orhanobut.dialogplus.OnItemClickListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adapter.DialogAdapter;
import bean.ArrangementAlbum;
import bean.Photo;
import bean.PrivatePhoto;
import event.LoginEvent;
import event.RefreshData;
import event.ShowImageEvent;
import event.ShowToolbarEvent;
import fragment.CloudAlbumFragment;
import fragment.HomeFragment;
import fragment.IntelligentFragment;
import fragment.PersonalFragment;
import me.majiajie.pagerbottomtabstrip.NavigationController;
import me.majiajie.pagerbottomtabstrip.PageNavigationView;
import me.majiajie.pagerbottomtabstrip.item.BaseTabItem;
import me.majiajie.pagerbottomtabstrip.listener.OnTabItemSelectedListener;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import untils.DensityUtil;
import untils.FileUtil;
import widght.BottomNavigationViewHelper;
import widght.Mydialog;
import widght.OnlyIconItemView;
import widght.SpecialTab;
import widght.SpecialTabRound;

public class MainActivity extends AppCompatActivity {
    private static BottomNavigationView mBottomNavigationView;
    private FragmentManager mFragmentManager;
    private HomeFragment mHomeFragment;
    private IntelligentFragment mIntelligentFragment;
    private CloudAlbumFragment mCloudAlbumFragment;
    private PersonalFragment mPersonalFragment;
    private static PageNavigationView mPageNavigationView;
    private NavigationController mNavigationController;
    private SimpleAdapter mSimpleAdapter;
    private int[] mImage = {R.mipmap.twitter, R.mipmap.instagram,
            R.mipmap.facebook};
    private String[] mImageName = {"Twitter", "Instagram", "Facebook"};
    private List<Map<String, Object>> mDataList = new ArrayList<>();
    private List<ArrangementAlbum> mArrangemnetAlbumList = new ArrayList<>();
    private Mydialog mDialog;
    private DialogPlus arrangeMentDialog;
    private MaterialDialog mLodingDiaolg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        EventBus.getDefault().register(this);
        initView();
        setListener();
    }

    public void initView() {
        mDialog = new Mydialog(this, R.style.BottomDialog);
        mBottomNavigationView = findViewById(R.id.main_activity_BNV);

        mPageNavigationView = findViewById(R.id.tab);

        mNavigationController = mPageNavigationView.custom()
                .addItem(newItem(R.mipmap.ic_upload, R.mipmap.ic_upload))
                .addItem(newItem(R.mipmap.ic_lock, R.mipmap.ic_lock))
                .addItem(newRoundItem(R.mipmap.ic_addto, R.mipmap.ic_addto, ""))
                .addItem(newItem(R.mipmap.ic_share, R.mipmap.ic_share))
                .addItem(newItem(R.mipmap.ic_delete, R.mipmap.ic_delete))
                .build();


        BottomNavigationViewHelper.disableShiftMode(mBottomNavigationView);

        mFragmentManager = getSupportFragmentManager();
        mHomeFragment = new HomeFragment();
        mIntelligentFragment = new IntelligentFragment();
        mCloudAlbumFragment = new CloudAlbumFragment();
        mPersonalFragment = new PersonalFragment();
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        ft.add(R.id.main_activity_fl, mHomeFragment);
        ft.add(R.id.main_activity_fl, mIntelligentFragment);
        ft.add(R.id.main_activity_fl, mCloudAlbumFragment);
        ft.add(R.id.main_activity_fl, mPersonalFragment);
        ft.show(mHomeFragment);
        ft.hide(mIntelligentFragment);
        ft.hide(mCloudAlbumFragment);
        ft.hide(mPersonalFragment);
        ft.commit();

        mSimpleAdapter = new SimpleAdapter(this, getData(), R.layout.share_grid_item,
                new String[]{"img", "txt"}, new int[]{R.id.img_item, R.id.txt_item});


    }

    public void setListener() {

        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_bottom_home:
                        FragmentTransaction ft = mFragmentManager.beginTransaction();
                        ft.hide(mIntelligentFragment);
                        ft.hide(mCloudAlbumFragment);
                        ft.show(mHomeFragment);
                        ft.hide(mPersonalFragment);
                        ft.commit();
                        break;
                    case R.id.navigation_bottom_intelligent:
                        ft = mFragmentManager.beginTransaction();
                        ft.show(mIntelligentFragment);
                        ft.hide(mHomeFragment);
                        ft.hide(mCloudAlbumFragment);
                        ft.hide(mPersonalFragment);
                        ft.commit();
                        break;
                    case R.id.navigation_bottom_cloudAlbum:
                        ft = mFragmentManager.beginTransaction();
                        ft.show(mCloudAlbumFragment);
                        ft.hide(mHomeFragment);
                        ft.hide(mIntelligentFragment);
                        ft.hide(mPersonalFragment);
                        ft.commit();
                        break;
                    case R.id.navigation_bottom_personalCenter:
                        ft = mFragmentManager.beginTransaction();
                        ft.show(mPersonalFragment);
                        ft.hide(mHomeFragment);
                        ft.hide(mIntelligentFragment);
                        ft.hide(mCloudAlbumFragment);
                        ft.commit();
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });

//        mCancelImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mHomeFragment.cancelSelected();
//            }
//        });


        mNavigationController.addTabItemSelectedListener(new OnTabItemSelectedListener() {
            @Override
            public void onSelected(int index, int old) {
                switch (index) {
                    case 0:
                        upLoadImage(mHomeFragment.getSelectPhotoList().get(0));
                        break;
                    case 1:
                        lockPhoto(mHomeFragment.getSelectPhotoList());
//                        mLodingDiaolg = new MaterialDialog.Builder(MainActivity.this)
//                                .title(R.string.encrypting)
//                                .content(R.string.please_wait)
//                                .progress(true, 0)
//                                .progressIndeterminateStyle(true)
//                                .show();
                        break;
                    case 2:
                        showArrangementDialog();
                        break;
                    case 3:
                        showShare();
                        break;
                    case 4:
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    List<String> list = new ArrayList<>();
                                    list.add("delete");
                                    FileUtil.catchStreamToFile(mHomeFragment.getSelectPhotoList(), list, MainActivity.this);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).run();
                        break;
                }
            }

            @Override
            public void onRepeat(int index) {
                switch (index) {
                    case 0:
                        upLoadImage(mHomeFragment.getSelectPhotoList().get(0));
                        break;
                    case 1:
                        lockPhoto(mHomeFragment.getSelectPhotoList());
                        break;
                    case 2:
                        showArrangementDialog();
                        break;
                    case 3:
                        showShare();
                        break;
                    case 4:
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    List<String> list = new ArrayList<>();
                                    list.add("delete");
                                    FileUtil.catchStreamToFile(mHomeFragment.getSelectPhotoList(), list, MainActivity.this);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).run();
                        break;
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

    public static void setvisibility() {
//        mBottomNavigationView1.setVisibility(View.VISIBLE);
        mPageNavigationView.setVisibility(View.VISIBLE);
        mBottomNavigationView.setVisibility(View.GONE);
        EventBus.getDefault().post(new ShowToolbarEvent(true));
    }

    public static void hideBottom() {
//        mBottomNavigationView1.setVisibility(View.GONE);
        mPageNavigationView.setVisibility(View.GONE);
        mBottomNavigationView.setVisibility(View.VISIBLE);
        EventBus.getDefault().post(new ShowToolbarEvent(false));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(ShowImageEvent event) {
        showImageDialog(event.getmLocalpath());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(RefreshData event) {
        if (arrangeMentDialog != null && arrangeMentDialog.isShowing()) {
            arrangeMentDialog.dismiss();
        }
        if (mLodingDiaolg != null && mLodingDiaolg.isShowing()) {
            mLodingDiaolg.dismiss();
        }
    }

    private void showImageDialog(String path) {
        View contentView = LayoutInflater.from(this).inflate(R.layout.dialog_content_circle, null);

        final ImageView imageView = (ImageView) contentView.findViewById(R.id.dialog_image);
        final int width = getWindowManager().getDefaultDisplay().getWidth();
        final ViewGroup.LayoutParams param = imageView.getLayoutParams();
        param.width = width;

        Glide.with(this).load(path).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                double scale = (double) (resource.getHeight()) / (double) (resource.getWidth());
                param.height = (int) (width * scale);
                if (param.height > getWindowManager().getDefaultDisplay().getHeight()) {
                    param.height = getWindowManager().getDefaultDisplay().getHeight();
                }
                imageView.setLayoutParams(param);
                imageView.setImageBitmap(resource);
            }
        });
        mDialog.setContentView(contentView);
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) contentView.getLayoutParams();
        params.width = getResources().getDisplayMetrics().widthPixels - DensityUtil.dp2px(this, 16f);
        params.bottomMargin = DensityUtil.dp2px(this, 8f);
        contentView.setLayoutParams(params);
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.getWindow().setGravity(Gravity.CENTER);
        mDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
//        bottomDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.show();
    }

    private void showShare() {
        DialogPlus dialog = DialogPlus.newDialog(MainActivity.this)
                .setGravity(Gravity.CENTER)
                .setAdapter(mSimpleAdapter)
                .setExpanded(false, 500)
                .setHeader(R.layout.share_dialog_header)
                .setContentHolder(new GridHolder(3))
                .setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(DialogPlus dialog, Object item, View view, int position) {
                    }
                })
                .setExpanded(false)// This will enable the expand feature, (similar to android L share dialog)
                .create();
        dialog.show();
    }

    /**
     * 正常tab
     */
    private BaseTabItem newItem(int drawable, int checkedDrawable, String text) {
        SpecialTab mainTab = new SpecialTab(this);
        mainTab.initialize(drawable, checkedDrawable, text);
        mainTab.setTextDefaultColor(0xFF888888);
        mainTab.setTextCheckedColor(0xFF009688);
        return mainTab;
    }

    /**
     * 圆形tab
     */
    private BaseTabItem newRoundItem(int drawable, int checkedDrawable, String text) {
        SpecialTabRound mainTab = new SpecialTabRound(this);
        mainTab.initialize(drawable, checkedDrawable, text);
        mainTab.setTextDefaultColor(0xFF888888);
        mainTab.setTextCheckedColor(0xFF009688);
        return mainTab;
    }

    //创建一个Item
    private BaseTabItem newItem(int drawable, int checkedDrawable) {
        OnlyIconItemView onlyIconItemView = new OnlyIconItemView(this);
        onlyIconItemView.initialize(drawable, checkedDrawable);
        return onlyIconItemView;
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


    public void showArrangementDialog() {
        mArrangemnetAlbumList = LitePal.findAll(ArrangementAlbum.class);
        final DialogAdapter adapter = new DialogAdapter(this, mArrangemnetAlbumList.size(), mArrangemnetAlbumList);
        arrangeMentDialog = DialogPlus.newDialog(this)
                .setAdapter(adapter)
                .setExpanded(false, 500)
                .setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(DialogPlus dialog, Object item, View view, int position) {
                    }
                })
                .setHeader(R.layout.arrangement_dialog_header)
                .setFooter(R.layout.arrangement_dialog_footer)
                .setGravity(Gravity.CENTER)
                .setContentWidth(1000)
                .create();
        TextView addTv = arrangeMentDialog.getFooterView().findViewById(R.id.arrangement_dialog_footer_add);
        Button createBtn = arrangeMentDialog.getHeaderView().findViewById(R.id.arrangement_dialog_header_btn);

        addTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final List<String> list = adapter.getCbList();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            FileUtil.catchStreamToFile(mHomeFragment.getSelectPhotoList(), list, MainActivity.this);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).run();
            }
        });

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(MainActivity.this)
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
                                mDialog.dismiss();
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            List<String> list = new ArrayList<>();
                                            list.add(String.valueOf(dialog.getInputEditText().getText()));
                                            FileUtil.catchStreamToFile(mHomeFragment.getSelectPhotoList(), list, MainActivity.this);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }).run();
                            }
                        })
                        .show();
            }
        });
        arrangeMentDialog.show();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_UP:
                onTouchEvent(ev);
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_UP:
                if (mDialog.isShowing()) {
                    mDialog.dismiss();
                }
        }
        return super.onTouchEvent(event);
    }


    public void upLoadImage(String path) {
        OkHttpClient mOkHttpClent = new OkHttpClient();
        File file = new File(path);
        Log.e("oldName", file.getName());
        File newFile = CompressHelper.getDefault(this).compressToFile(file);
        Log.e("NewName", newFile.getName());

        MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        RequestBody body = RequestBody.create(MediaType.parse("image/*"), newFile);
        String filename = file.getName();
        // 参数分别为， 请求key ，文件名称 ， RequestBody
        requestBody.addFormDataPart("file", filename, body);

        Request request = new Request.Builder().url("http://www.ligohan.com:8080/springboot-security-demo/api/file/upload").post(requestBody.build()).build();



        Call call = mOkHttpClent.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("faile", "onFailure: " + e);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e("success", "成功" + response);
                Log.e("success", "成功" + response.body().string());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "成功", Toast.LENGTH_SHORT).show();
                    }
                });
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


        new Thread(new Runnable() {
            @Override
            public void run() {
                FileUtil.privateImage(photo, MainActivity.this);
            }
        }).run();
    }

    public byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return baos.toByteArray();
    }

    @Override
    public void onBackPressed() {
        if (mHomeFragment.getIsSelected()) {
            mHomeFragment.cancelSelected();
        } else {
            super.onBackPressed();
        }
    }
}
