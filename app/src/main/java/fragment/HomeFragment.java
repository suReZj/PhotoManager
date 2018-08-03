package fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.sure.photomanager.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import adapter.FragmentAdapter;
import event.SelectSumEvent;
import event.ShowToolbarEvent;

public class HomeFragment extends Fragment {
    private TabLayout mTablayout;
    private ViewPager mViewPager;
    private List<Fragment> mList = new ArrayList<>();
    private List<String> mTitleList = new ArrayList<>();
    private FragmentAdapter mFragmentAdapter;
    private AlbumFragment mAlbumFragment;
    private ArrangementFragment mArrangementFragment;
    private RelativeLayout mRelativeLayout;
    private ImageView mCancelImage;
    private TextView mTv;
    private TextView mSelectAll;

    public HomeFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        mTitleList.add(getString(R.string.myAlbum));
        mTitleList.add(getString(R.string.arrangement));
        mAlbumFragment = new AlbumFragment();
        mArrangementFragment = new ArrangementFragment();
        mList.add(mAlbumFragment);
        mList.add(mArrangementFragment);
        mFragmentAdapter = new FragmentAdapter(getActivity().getSupportFragmentManager(), mList, mTitleList);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, null);
        mTablayout = view.findViewById(R.id.home_fragment_tl);
        mViewPager = view.findViewById(R.id.home_fragment_vp);
        mRelativeLayout = view.findViewById(R.id.home_fragment_rl);
        mCancelImage = view.findViewById(R.id.home_fragment_cancel);
        mTv = view.findViewById(R.id.home_fragment_choose);
        mSelectAll = view.findViewById(R.id.home_fragment_select_all);
        mViewPager.setAdapter(mFragmentAdapter);
        mTablayout.setupWithViewPager(mViewPager);
        setListener();
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void showToolbarEvent(ShowToolbarEvent event) {
        showToolbar(event.ismFlag());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void showSumEvent(SelectSumEvent event) {
        if(event.getmSum()==0){
            mTv.setText(getString(R.string.please_select));
        }else {
            mTv.setText("");
            String text = getString(R.string.select).concat(String.valueOf(event.getmSum()));
            mTv.setText(text);
        }
    }

    public void cancelSelected() {
        mAlbumFragment.cancelSelected();
    }


    public List<String> getSelectPhotoList() {
        return mAlbumFragment.getSelectPhotoList();
    }

    public void showToolbar(boolean flag) {
        if (flag) {
            mRelativeLayout.setVisibility(View.VISIBLE);
            mTablayout.setVisibility(View.GONE);
        } else {
            mRelativeLayout.setVisibility(View.GONE);
            mTablayout.setVisibility(View.VISIBLE);
        }
    }

    public void setListener() {
        mCancelImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelSelected();
                mSelectAll.setText(getString(R.string.select_all));
            }
        });

        mSelectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mAlbumFragment.selectAll();
//                mSelectAll.setText(getString(R.string.cancel_select));
                if (mSelectAll.getText().toString().equals(getString(R.string.cancel_select))) {
                    mSelectAll.setText(getString(R.string.select_all));
                    mAlbumFragment.cancelSelectAll();
                }else {
                    mSelectAll.setText(getString(R.string.cancel_select));
                    mAlbumFragment.selectAll();
                }
            }
        });
    }
}
