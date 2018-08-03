package fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sure.photomanager.R;

import java.util.ArrayList;
import java.util.List;

import adapter.FragmentAdapter;

public class CloudAlbumFragment extends Fragment {
    private List<String> mTitleList=new ArrayList<>();
    private List<Fragment> mFragmentList=new ArrayList<>();
    private TabLayout mTablayout;
    private ViewPager mViewPager;
    private FragmentAdapter mAdapter;
    private MyCloudFragment mMyCloudFragment;
    private MyFileFragment mMyFileFragment;

    public CloudAlbumFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTitleList.add(getString(R.string.myCloud));
        mTitleList.add(getString(R.string.myFile));
        mMyCloudFragment=new MyCloudFragment();
        mMyFileFragment=new MyFileFragment();
        mFragmentList.add(mMyCloudFragment);
        mFragmentList.add(mMyFileFragment);
        mAdapter=new FragmentAdapter(getActivity().getSupportFragmentManager(),mFragmentList,mTitleList);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cloud_fragment, null);
        mTablayout=view.findViewById(R.id.cloud_fragment_tl);
        mViewPager=view.findViewById(R.id.cloud_fragment_vp);
        mViewPager.setAdapter(mAdapter);
        mTablayout.setupWithViewPager(mViewPager);
        return view;
    }
}
