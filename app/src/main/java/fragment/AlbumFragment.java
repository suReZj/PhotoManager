package fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.sure.photomanager.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import adapter.AlbumAdapter;
import bean.Photo;
import bean.SortPhoto;
import event.RefreshData;

public class AlbumFragment extends Fragment {
    private RecyclerView mRv;
    private String mSystemPath = "DCIM/Camera";
    private List<Photo> mList = new ArrayList<>();
    private AlbumAdapter mAdapter;

    public AlbumFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.album_fragment, null);
        mRv = view.findViewById(R.id.album_fragment_rv);
        getData();
        setListener();
        return view;
    }

    public void getData() {
        List<Photo> newList = LitePal.where("mLocalPath like ?", "%" + mSystemPath + "%").order("mDate desc").find(Photo.class);
        mList.clear();
        for (int i = 0; i < newList.size(); i++) {
            if (!newList.get(i).ismIsSort()) {
                mList.add(newList.get(i));
            }
        }
        mAdapter = new AlbumAdapter(mList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRv.setLayoutManager(linearLayoutManager);
        mRv.setAdapter(mAdapter);
    }

    public void setListener() {

    }

    public void cancelSelected() {
        mAdapter.cancelSelected();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(RefreshData event) {
        if(event.getmMode().equals("delete")){
            Toast.makeText(getContext(), "successfully deleted", Toast.LENGTH_SHORT).show();
        }else if(event.getmMode().equals("lock")){
            Toast.makeText(getContext(), "successfully lock", Toast.LENGTH_SHORT).show();
        }else if(event.getmMode().equals("sort")){
            Toast.makeText(getContext(), "Successful classification", Toast.LENGTH_SHORT).show();
        }
        refreshData();
    }

    public List<String> getSelectPhotoList() {
        return mAdapter.getSelectPhotoList();
    }

    public void selectAll() {
        mAdapter.selectAll();
    }

    public void cancelSelectAll() {
        mAdapter.cancelSelectAll();
    }

    public void refreshData() {
        List<Photo> newList = LitePal.where("mLocalPath like ?", "%" + mSystemPath + "%").order("mDate desc").find(Photo.class);
        mList.clear();
        for (int i = 0; i < newList.size(); i++) {
            if (!newList.get(i).ismIsSort()) {
                mList.add(newList.get(i));
            }
        }
        mAdapter.refreshData(mList);
//        mAdapter.refreshData(newList);
    }

    public boolean getIsSelected() {
        return mAdapter.ismIsSelected();
    }
}
