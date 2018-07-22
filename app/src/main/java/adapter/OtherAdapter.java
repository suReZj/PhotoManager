package adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sure.photomanager.R;

import java.util.HashMap;
import java.util.List;

import bean.Photo;

public class OtherAdapter extends RecyclerView.Adapter<OtherAdapter.ViewHolder> {
    private Context mContext;
    private List<String> mAlbumList;
    private HashMap<String, List<Photo>> mAllPhotosTemp;

    static class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView mImage;
        private TextView mTv;
        public ViewHolder(View itemView) {
            super(itemView);
            mImage=itemView.findViewById(R.id.other_rv_item_image);
            mTv=itemView.findViewById(R.id.other_rv_item_tv);
        }
    }

    public OtherAdapter(List<String> mAlbumList, HashMap<String, List<Photo>> mAllPhotosTemp) {
        this.mAlbumList = mAlbumList;
        this.mAllPhotosTemp = mAllPhotosTemp;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.other_rv_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String name=mAlbumList.get(position);
        int index=name.lastIndexOf("/");
        name=name.substring(index+1,name.length());
        holder.mTv.setText(name+"（"+mAllPhotosTemp.get(mAlbumList.get(position)).size()+"）");
        Glide.with(mContext).load(mAllPhotosTemp.get(mAlbumList.get(position)).get(0).getmLocalPath()).into(holder.mImage);
    }

    @Override
    public int getItemCount() {
        return mAlbumList.size();
    }
}
