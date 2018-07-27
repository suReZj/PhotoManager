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

import adapter.ArrangementAdapter;
import widght.MyDecorationUtil;

public class ArrangementFragment extends Fragment {
    private RecyclerView mRv;
    private List<String> mList;
    private ArrangementAdapter mAdapter;

    public ArrangementFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.arrangement_fragment, null);
        mRv = view.findViewById(R.id.arrangement_fragment_rv);
        mList = new ArrayList<>();
        mList.add("111");
        mList.add("111");
        mList.add("111");
        mList.add("111");
        mList.add("111");
        mList.add("111");
        mList.add("111");
        mList.add("111");
        mList.add("111");
        mList.add("111");
        mList.add("111");
        mList.add("111");
        mList.add("111");
        mList.add("111");
        mList.add("111");
        mAdapter = new ArrangementAdapter(mList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRv.setLayoutManager(linearLayoutManager);
        mRv.setAdapter(mAdapter);
        mRv.addItemDecoration(new MyDecorationUtil(getContext(), MyDecorationUtil.VERTICAL_LIST, R.drawable.activity_recyclerview_divider));
        return view;
    }
}
