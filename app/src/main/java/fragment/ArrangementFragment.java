package fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sure.photomanager.Activity.ArrangementDetailActivity;
import com.example.sure.photomanager.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;
import org.litepal.crud.callback.FindMultiCallback;

import java.util.ArrayList;
import java.util.List;

import adapter.ArrangementAdapter;
import bean.ArrangementAlbum;
import event.RefreshData;
import widght.MyDecorationUtil;

public class ArrangementFragment extends Fragment {
    private RecyclerView mRv;
    private List<ArrangementAlbum> mList;
    private ArrangementAdapter mAdapter;

    public ArrangementFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.arrangement_fragment, null);
        mRv = view.findViewById(R.id.arrangement_fragment_rv);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRv.setLayoutManager(linearLayoutManager);
        mRv.addItemDecoration(new MyDecorationUtil(getContext(), MyDecorationUtil.VERTICAL_LIST, R.drawable.activity_recyclerview_divider));
        getData();
        return view;
    }

    public void getData() {
        mList = LitePal.findAll(ArrangementAlbum.class);
        mAdapter = new ArrangementAdapter(mList);
        mRv.setAdapter(mAdapter);
        mAdapter.setOnItemClickLitener(new ArrangementAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(ArrangementAdapter.ViewHolder view, int index) {
                Intent intent = new Intent(getContext(), ArrangementDetailActivity.class);
                intent.putExtra("path", mAdapter.getPath(index));
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(ArrangementAdapter.ViewHolder view, int index) {

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(RefreshData event) {
        refreshData();
    }

    public void refreshData() {
        mList = LitePal.findAll(ArrangementAlbum.class);
        mAdapter.refreshData(mList);
    }
}
