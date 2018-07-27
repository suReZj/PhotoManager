package fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sure.photomanager.R;

import java.util.ArrayList;
import java.util.List;

import adapter.AlbumAdapter;
import adapter.FragmentAdapter;
import bean.Photo;

public class HomeFragment extends Fragment {
    private TabLayout mTablayout;
    private ViewPager mViewPager;
    private List<Fragment> mList = new ArrayList<>();
    private List<String> mTitleList = new ArrayList<>();
    private FragmentAdapter mFragmentAdapter;
    private AlbumFragment mAlbumFragment;
    private ArrangementFragment mArrangementFragment;
    private FragmentManager mFragmentManager;

    public HomeFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        mViewPager.setAdapter(mFragmentAdapter);
        mTablayout.setupWithViewPager(mViewPager);
        return view;
    }

}
