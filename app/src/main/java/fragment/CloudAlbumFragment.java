package fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.sure.photomanager.Activity.LoginActivity;
import com.example.sure.photomanager.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;
import org.litepal.crud.callback.UpdateOrDeleteCallback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import adapter.FragmentAdapter;
import bean.User;
import event.LoginEvent;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CloudAlbumFragment extends Fragment {
    private List<String> mTitleList = new ArrayList<>();
    private List<Fragment> mFragmentList = new ArrayList<>();
    private TabLayout mTablayout;
    private ViewPager mViewPager;
    private FragmentAdapter mAdapter;
    private MyCloudFragment mMyCloudFragment;
    private MyFileFragment mMyFileFragment;
    private Button mLoginBtn;
    private Button mExpandBtn;
    private TextView mNameText;

    public CloudAlbumFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTitleList.add(getString(R.string.myCloud));
        mTitleList.add(getString(R.string.myFile));
        mMyCloudFragment = new MyCloudFragment();
        mMyFileFragment = new MyFileFragment();
        mFragmentList.add(mMyCloudFragment);
        mFragmentList.add(mMyFileFragment);
        mAdapter = new FragmentAdapter(getActivity().getSupportFragmentManager(), mFragmentList, mTitleList);
        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cloud_fragment, null);
        mTablayout = view.findViewById(R.id.cloud_fragment_tl);
        mViewPager = view.findViewById(R.id.cloud_fragment_vp);
        mLoginBtn = view.findViewById(R.id.cloud_fragment_login_btn);
        mExpandBtn = view.findViewById(R.id.cloud_fragment_expand);
        mNameText = view.findViewById(R.id.cloud_fragment_clickTv);
        mViewPager.setAdapter(mAdapter);
        mTablayout.setupWithViewPager(mViewPager);
        setListener();
        refresh();
        return view;
    }

    public void setListener() {
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
        mExpandBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LitePal.deleteAllAsync(User.class).listen(new UpdateOrDeleteCallback() {
                    @Override
                    public void onFinish(int rowsAffected) {
                        refresh();
                    }
                });
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(LoginEvent event) {
        refresh();
    }

    public void refresh() {
        List<User> list = LitePal.findAll(User.class);
        if (list.size() == 0) {
            mLoginBtn.setVisibility(View.VISIBLE);
            mTablayout.setVisibility(View.GONE);
            mViewPager.setVisibility(View.GONE);
            mExpandBtn.setVisibility(View.GONE);
            mNameText.setText(getString(R.string.clickToLogin));
        } else {
            mLoginBtn.setVisibility(View.GONE);
            mTablayout.setVisibility(View.VISIBLE);
            mViewPager.setVisibility(View.VISIBLE);
            mExpandBtn.setVisibility(View.VISIBLE);
            mNameText.setText(list.get(0).getmPhone());
        }
    }
}
