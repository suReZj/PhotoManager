package adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.sure.photomanager.R;

import java.util.List;

import bean.PrivatePhoto;

public class PrivateAdapter extends RecyclerView.Adapter<PrivateAdapter.ViewHolder> {
    private List<PrivatePhoto> mList;
    private Context mContext;

    static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImage;

        public ViewHolder(View itemView) {
            super(itemView);
            mImage = itemView.findViewById(R.id.private_activity_rv_item_image);
            int width = ((Activity) mImage.getContext()).getWindowManager().getDefaultDisplay().getWidth();
            ViewGroup.LayoutParams params = mImage.getLayoutParams();
            //设置图片的相对于屏幕的宽高比
            params.width = (width - 6) / 3;
            params.height = width / 3;
            mImage.setLayoutParams(params);
        }
    }

    public PrivateAdapter(List<PrivatePhoto> mList) {
        this.mList = mList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.private_activity_rv_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Glide.with(mContext).load(mList.get(position).getmByte()).into(holder.mImage);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
