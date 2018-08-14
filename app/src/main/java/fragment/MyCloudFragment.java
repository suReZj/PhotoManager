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
import bean.User;

public class MyCloudFragment extends Fragment {
    private RecyclerView mRv;
    private List<UploadPhoto> mList;
    private CloudPhotoAdapter mAdapter;
    private int mSize;

    public MyCloudFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        List<User> user = LitePal.findAll(User.class);
        mList = LitePal.where("mPhone = ?", user.get(0).getmPhone()).order("mDate desc").find(UploadPhoto.class);
        for (int i = 0; i < mList.size(); i++) {
            mSize = mSize + (int) (mList.get(i).getmSize() / 1024);
        }

//        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_cloud_fragment, null);
        mRv = view.findViewById(R.id.my_cloud_fragment_rv);


        RecyclerView.LayoutManager mLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        mAdapter = new CloudPhotoAdapter(mList);
        mRv.setAdapter(mAdapter);
        mRv.setLayoutManager(mLayoutManager);

        mAdapter.setOnItemClickLitener(new CloudPhotoAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(CloudPhotoAdapter.ViewHolder view, int index) {

            }

            @Override
            public void onItemLongClick(CloudPhotoAdapter.ViewHolder view, int index) {

            }
        });
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        EventBus.getDefault().unregister(this);
    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void Event(UploadPhoto event) {
//        mAdapter.changeData();
//    }

    public void changeData() {
        if (mAdapter != null) {
            mAdapter.changeData();
        }
    }

    public void deleteImage() {
        mAdapter.deleteImage();
    }

    public int getmSize() {
        return mSize;
    }

    public boolean getIsSelected() {
        if(mAdapter!=null){
            return mAdapter.getIsSelected();
        }
        return false;
    }

    public void cancelSelected() {
        mAdapter.cancelSelected();
    }
}
