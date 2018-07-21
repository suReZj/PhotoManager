package adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sure.photomanager.R;

import java.util.ArrayList;
import java.util.List;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder>{
    private List<String> mTimeList;
    private Context mContext;

    static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView mTv;
        private RecyclerView mRv;
        public ViewHolder(View itemView) {
            super(itemView);
            mTv=itemView.findViewById(R.id.album_rv_item_tv);
            mRv=itemView.findViewById(R.id.album_rv_item_rv);
        }
    }

    public AlbumAdapter(List<String> mTimeList) {
        this.mTimeList = mTimeList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.album_rv_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTv.setText(mTimeList.get(position));
        List<String> list=new ArrayList<>();
        list.add("111");
        list.add("222");
        AlbumDetailAdapter albumDetailAdapter=new AlbumDetailAdapter(mTimeList.get(position),list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        holder.mRv.setLayoutManager(linearLayoutManager);
        holder.mRv.setAdapter(albumDetailAdapter);
    }

    @Override
    public int getItemCount() {
        return mTimeList.size();
    }
}
