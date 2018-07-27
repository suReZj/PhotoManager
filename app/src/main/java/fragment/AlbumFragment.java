package fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sure.photomanager.R;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

import java.util.ArrayList;
import java.util.List;

import adapter.AlbumAdapter;
import bean.Photo;

public class AlbumFragment extends Fragment {
    private RecyclerView mRv;
    private String mSystemPath="DCIM/Camera";
    private List<Photo> mList=new ArrayList<>();
    private AlbumAdapter mAdapter;

    public AlbumFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.album_fragment, null);
        mRv=view.findViewById(R.id.album_fragment_rv);
        getData();
        return view;
    }

    public void getData(){
        mList= LitePal.where("mLocalPath like ?","%"+mSystemPath+"%").find(Photo.class);
        mAdapter=new AlbumAdapter(mList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRv.setLayoutManager(linearLayoutManager);
        mRv.setAdapter(mAdapter);
    }
}
