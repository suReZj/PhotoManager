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

import java.util.ArrayList;
import java.util.List;

import adapter.AlbumAdapter;

public class AlbumFragment extends Fragment{
    private RecyclerView mRv;
    private List<String> mTimeList=new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTimeList.add("111");
        mTimeList.add("222");
        mTimeList.add("333");
        mTimeList.add("444");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.album_fragment, null);
        mRv=view.findViewById(R.id.album_fragment_rv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        AlbumAdapter adapter=new AlbumAdapter(mTimeList);
        mRv.setAdapter(adapter);
        mRv.setLayoutManager(linearLayoutManager);
        return view;
    }
}
