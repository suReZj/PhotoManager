package adapter;

import android.content.Context;
import android.media.ExifInterface;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sure.photomanager.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import bean.Photo;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {
    private List<String> mTimeList;
    private HashMap<String, List<Photo>> mMapList;
    private Context mContext;
    private List<Photo> mPhotoList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTv;
        private RecyclerView mRv;

        public ViewHolder(View itemView) {
            super(itemView);
            mTv = itemView.findViewById(R.id.album_rv_item_tv);
            mRv = itemView.findViewById(R.id.album_rv_item_rv);
        }
    }

    public AlbumAdapter(List<Photo> mPhotoList) {
        this.mPhotoList = mPhotoList;
        mTimeList=new ArrayList<>();
        mMapList=new HashMap<>();

        String time=null;
        for (int i = 0; i < mPhotoList.size(); i++) {
            time=mPhotoList.get(i).getmDate();
            if(time!=null){
                time=time.substring(0,10);
            }
            if(time!=null&&(!getTimeList().contains(time))){
                List<Photo> list=new ArrayList<>();
                list.add(mPhotoList.get(i));
                getTimeList().add(time);
                this.mMapList.put(time,list);
                continue;
            }
            if(time!=null&&(getTimeList().contains(time))){
                this.mMapList.get(time).add(mPhotoList.get(i));
            }
        }
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
        AlbumDetailAdapter albumDetailAdapter = new AlbumDetailAdapter(mMapList.get(mTimeList.get(position)));
        RecyclerView.LayoutManager mLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        holder.mRv.setLayoutManager(mLayoutManager);
        holder.mRv.setAdapter(albumDetailAdapter);
    }

    @Override
    public int getItemCount() {
        return mTimeList.size();
    }

    public List<String> getTimeList(){
        return mTimeList;
    }

}
