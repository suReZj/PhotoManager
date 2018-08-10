package fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sure.photomanager.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;

import java.util.List;

import adapter.CloudPhotoAdapter;
import bean.UploadPhoto;

public class MyCloudFragment extends Fragment {
    private RecyclerView mRv;
    private List<UploadPhoto> mList;
    private CloudPhotoAdapter mAdapter;

    public MyCloudFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mList = LitePal.findAll(UploadPhoto.class);
        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_cloud_fragment, null);
        mRv = view.findViewById(R.id.my_cloud_fragment_rv);

        mAdapter = new CloudPhotoAdapter(mList);
        RecyclerView.LayoutManager mLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        mRv.setAdapter(mAdapter);
        mRv.setLayoutManager(mLayoutManager);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(UploadPhoto event) {
        mList = LitePal.findAll(UploadPhoto.class);
        mAdapter.notifyDataSetChanged();
    }
}
