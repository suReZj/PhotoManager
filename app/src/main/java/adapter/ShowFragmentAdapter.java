package adapter;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;

import java.util.List;

import fragment.ShowFragment;

public class ShowFragmentAdapter extends FragmentStatePagerAdapter {
    private FragmentManager mFragmentManager;
    private List<ShowFragment> mFragmentList;

    public ShowFragmentAdapter(FragmentManager mFragmentManager, List<ShowFragment> mFragmentList) {
        super(mFragmentManager);
        this.mFragmentManager = mFragmentManager;
        this.mFragmentList = mFragmentList;
    }

    @Override
    public ShowFragment getItem(int position) {
        ShowFragment showFragment = mFragmentList.get(position);
        return showFragment;
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return PagerAdapter.POSITION_NONE;
    }
}