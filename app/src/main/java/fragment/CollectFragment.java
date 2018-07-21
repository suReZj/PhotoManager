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

import adapter.CollectAdapter;
import adapter.OtherAdapter;

public class CollectFragment extends Fragment{
    private RecyclerView mRv;
    private List<String> mList=new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mList.add("111");
        mList.add("222");
        mList.add("333");
        mList.add("444");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.collect_fragment,null);
        mRv=view.findViewById(R.id.collect_fragment_rv);
        CollectAdapter adapter=new CollectAdapter(mList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRv.setAdapter(adapter);
        mRv.setLayoutManager(linearLayoutManager);
        return view;
    }
}
