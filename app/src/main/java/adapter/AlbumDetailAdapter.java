package adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.sure.photomanager.R;

import java.util.ArrayList;
import java.util.List;

import bean.SelectPhoto;

public class AlbumDetailAdapter extends RecyclerView.Adapter<AlbumDetailAdapter.ViewHolder> {
    private Context mContext;
    private List<SelectPhoto> mSelectList = new ArrayList<>();

    static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImage;
        private RelativeLayout mLayout;
        private FrameLayout mFl;

        public ViewHolder(View itemView) {
            super(itemView);
            mImage = itemView.findViewById(R.id.album_detail_rv_item_image);
            mLayout = itemView.findViewById(R.id.album_detail_rv_item_rl);
            mFl = itemView.findViewById(R.id.album_detail_rv_item_fl);
            int width = ((Activity) mImage.getContext()).getWindowManager().getDefaultDisplay().getWidth();
            ViewGroup.LayoutParams params = mImage.getLayoutParams();
            //设置图片的相对于屏幕的宽高比
            params.width = (width - 12) / 3;
            params.height = width / 3;
            mImage.setLayoutParams(params);
        }

        public void setMask(Drawable color){
//                mFl.setForeground(color);
        }
    }

    public AlbumDetailAdapter(List<SelectPhoto> mList) {
        this.mSelectList = mList;
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
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (mSelectList.get(position).ismIsSelected()) {
            Glide.with(mContext).load(mSelectList.get(position).getmLocalPath()).listener(new RequestListener<String, GlideDrawable>() {
                @Override
                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                    return false;
                }

                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                    holder.mLayout.setBackground(mContext.getDrawable(R.color.tab_checked));
                    holder.mLayout.setForeground(mContext.getDrawable(R.color.maskColor));
                    return false;
                }
            }).centerCrop().into(holder.mImage);
        } else {
            Glide.with(mContext).load(mSelectList.get(position).getmLocalPath()).listener(new RequestListener<String, GlideDrawable>() {
                @Override
                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                    return false;
                }

                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                    holder.mLayout.setBackground(mContext.getDrawable(R.color.white));
                    holder.mLayout.setForeground(mContext.getDrawable(R.color.noMask));
                    return false;
                }
            }).centerCrop().into(holder.mImage);
        }

        if (mOnItemClickLitener != null) {
            holder.mImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickLitener.onItemClick(holder, position);
                }
            });
            holder.mImage.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnItemClickLitener.onItemLongClick(holder, position);
                    return false;
                }
            });
        }

//        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                EventBus.getDefault().post(mSelectList.get(position).getmLocalPath());
//
//                return false;
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return mSelectList.size();
    }


    public interface OnItemClickLitener {
        void onItemClick(AlbumDetailAdapter.ViewHolder view, int index);

        void onItemLongClick(AlbumDetailAdapter.ViewHolder view, int index);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    }


