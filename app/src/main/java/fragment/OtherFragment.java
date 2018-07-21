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

import adapter.OtherAdapter;

public class OtherFragment extends Fragment {
    private RecyclerView mRv;
    private List<String> mAlbumList=new ArrayList<>();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAlbumList.add("111");
        mAlbumList.add("222");
        mAlbumList.add("333");
        mAlbumList.add("444");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.other_fragment,null);
        mRv=view.findViewById(R.id.other_fragment_rv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRv.setLayoutManager(linearLayoutManager);
        OtherAdapter adapter=new OtherAdapter(mAlbumList);
        mRv.setAdapter(adapter);
        return view;
    }
}
