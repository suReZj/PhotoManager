package adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.example.sure.photomanager.Activity.MainActivity;
import com.example.sure.photomanager.R;

import org.greenrobot.eventbus.EventBus;
import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import bean.SelectPhoto;
import bean.UploadPhoto;
import bean.User;
import event.HideTopEvent;
import event.SelectedUploadPhoto;
import event.ShowImageEvent;

public class CloudPhotoAdapter extends RecyclerView.Adapter<CloudPhotoAdapter.ViewHolder> {
    private Context mContext;
    private List<UploadPhoto> mList;
    private List<SelectedUploadPhoto> mSelectedList = new ArrayList<>();
    private boolean mIsSelected;
    private int mSelectSum = 0;
    private List<String> mSelectedPath = new ArrayList<>();

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImage;
        private RelativeLayout mLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            mImage = itemView.findViewById(R.id.cloud_photo_rv_item_image);
            mLayout = itemView.findViewById(R.id.cloud_photo_rv_item_layout);
            int width = ((Activity) mImage.getContext()).getWindowManager().getDefaultDisplay().getWidth();
            ViewGroup.LayoutParams params = mImage.getLayoutParams();
            //设置图片的相对于屏幕的宽高比
            params.width = (width - 6) / 3;
            params.height = width / 3;
            mImage.setLayoutParams(params);
        }
    }

    public CloudPhotoAdapter(List<UploadPhoto> mList) {
        this.mList = mList;
        for (int i = 0; i < mList.size(); i++) {
            SelectedUploadPhoto selectedUploadPhoto = new SelectedUploadPhoto(mList.get(i));
            mSelectedList.add(selectedUploadPhoto);
        }
        mIsSelected = false;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.cloud_photo_rv_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Glide.with(mContext).load(mList.get(position).getNewPath()).into(holder.mImage);
        if (mSelectedList.get(position).ismIsSelected()) {
            holder.mLayout.setForeground(mContext.getDrawable(R.mipmap.mask));
        } else {
            holder.mLayout.setForeground(mContext.getDrawable(R.color.noMask));
        }
        if (mOnItemClickLitener != null) {
            holder.mImage.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnItemClickLitener.onItemLongClick(holder, position);
                    if (!mIsSelected) {
                        mSelectedList.get(position).setmIsSelected(true);
                        Log.e("position", position + "");
                        Log.e("position", mSelectedList.get(position).ismIsSelected() + "");
                        notifyItemChanged(position);
                        mSelectSum++;
                        mIsSelected = true;
                        EventBus.getDefault().post(new HideTopEvent(true));
                        MainActivity.showBottom(true);
                        mSelectedPath.add(mSelectedList.get(position).getOriginalPath());
                    } else {
                        EventBus.getDefault().post(new ShowImageEvent(mSelectedList.get(position).getNewPath()));
                    }
                    return false;
                }
            });

            holder.mImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickLitener.onItemClick(holder, position);
                    if (mIsSelected) {
                        if (!mSelectedList.get(position).ismIsSelected()) {
                            mSelectedList.get(position).setmIsSelected(true);
                            mSelectSum++;
                            mSelectedPath.add(mSelectedList.get(position).getOriginalPath());
                            notifyItemChanged(position);
                        } else {
                            mSelectedList.get(position).setmIsSelected(false);
                            mSelectSum--;
                            for (int i = 0; i < mSelectedPath.size(); i++) {
                                if (mSelectedPath.get(i).equals(mSelectedList.get(position).getOriginalPath())) {
                                    mSelectedPath.remove(i);
                                    break;
                                }
                            }
                            if (mSelectSum == 0) {
                                mIsSelected = false;
                                EventBus.getDefault().post(new HideTopEvent(false));
                                MainActivity.showBottom(false);
                            }
                            notifyItemChanged(position);
                        }
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mSelectedList.size();
    }

    public void changeData() {
        mSelectedList.clear();
        List<User> user = LitePal.findAll(User.class);
        this.mList = LitePal.where("mPhone = ?", user.get(0).getmPhone()).order("mDate desc").find(UploadPhoto.class);
        for (int i = 0; i < mList.size(); i++) {
            SelectedUploadPhoto selectedUploadPhoto = new SelectedUploadPhoto(mList.get(i));
            mSelectedList.add(selectedUploadPhoto);
        }
        notifyDataSetChanged();
    }

    public interface OnItemClickLitener {
        void onItemClick(ViewHolder view, int index);

        void onItemLongClick(ViewHolder view, int index);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    public void deleteImage() {
        mIsSelected = false;
//        int sum = mSelectedList.size();
//        for (int i = 0; i < sum; i++) {
//            if (this.mSelectedList.get(i).ismIsSelected()) {
////                mSelectedList.remove(i);
////                notifyItemChanged(i);
//                LitePal.deleteAll(UploadPhoto.class, "originalPath = ?", mList.get(i).getOriginalPath());
//                Log.e("originalPath = ?", mList.get(i).getOriginalPath());
//            }
//        }

        int sum = mSelectedPath.size();
        for (int i = 0; i < sum; i++) {
            LitePal.deleteAll(UploadPhoto.class, "originalPath = ?", mSelectedPath.get(i));
            Log.e("originalPath = ?", mList.get(i).getOriginalPath());
        }

        mSelectedPath.clear();

        List<User> user = LitePal.findAll(User.class);
        mList = LitePal.where("mPhone = ?", user.get(0).getmPhone()).order("mDate desc").find(UploadPhoto.class);
        mSelectedList.clear();
        for (int i = 0; i < mList.size(); i++) {
            SelectedUploadPhoto selectedUploadPhoto = new SelectedUploadPhoto(mList.get(i));
            mSelectedList.add(selectedUploadPhoto);
        }

        mSelectSum = 0;
        notifyDataSetChanged();
        MainActivity.showBottom(false);
        EventBus.getDefault().post(new HideTopEvent(false));
    }

    public boolean getIsSelected() {
        return this.mIsSelected;
    }

    public void cancelSelected() {
        for (int i = 0; i < mSelectedList.size(); i++) {
            if (mSelectedList.get(i).ismIsSelected()) {
                mSelectedList.get(i).setmIsSelected(false);
                notifyItemChanged(i);
            }
        }
        mIsSelected = false;
        MainActivity.showBottom(false);
        EventBus.getDefault().post(new HideTopEvent(false));
        mSelectedPath.clear();
    }
}
