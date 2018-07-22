package adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.sure.photomanager.R;

import java.util.List;

import bean.Photo;

public class AlbumDetailAdapter extends RecyclerView.Adapter<AlbumDetailAdapter.ViewHolder>{
    private Context mContext;
    private List<Photo> mPhotoList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView mImage;
        public ViewHolder(View itemView) {
            super(itemView);
            mImage=itemView.findViewById(R.id.album_detail_rv_item_image);
        }
}

    public AlbumDetailAdapter(List<Photo> mPhotoList) {
        this.mPhotoList = mPhotoList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.album_detail_rv_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Glide.with(mContext).load(mPhotoList.get(position).getmLocalPath()).into(holder.mImage);
    }

    @Override
    public int getItemCount() {
        return mPhotoList.size();
    }
}
