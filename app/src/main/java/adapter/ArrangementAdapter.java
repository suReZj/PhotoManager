package adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sure.photomanager.R;

import org.litepal.LitePal;

import java.util.List;

import bean.ArrangementAlbum;
import bean.SortPhoto;

public class ArrangementAdapter extends RecyclerView.Adapter<ArrangementAdapter.ViewHolder> {
    private List<ArrangementAlbum> mList;
    private Context mContext;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImage;
        private TextView mNameTv;
        private TextView mSumTv;
        private RelativeLayout mLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            mImage = itemView.findViewById(R.id.arrangement_rv_item_image);
            mNameTv = itemView.findViewById(R.id.arrangement_rv_item_name);
            mSumTv = itemView.findViewById(R.id.arrangement_rv_item_sum);
            mLayout = itemView.findViewById(R.id.arrangement_rv_item_layout);
        }
    }

    public ArrangementAdapter(List<ArrangementAlbum> mList) {
        this.mList = mList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.arrangement_rv_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        SortPhoto sortPhoto = LitePal.where("mLocalPath = ?", mList.get(position).getmList().get(0)).find(SortPhoto.class).get(0);
//        Glide.with(mContext).load(mList.get(position).getmList().get(0)).into(holder.mImage);
        Glide.with(mContext).load(sortPhoto.getmByte()).into(holder.mImage);
        holder.mNameTv.setText(mList.get(position).getName());
        holder.mSumTv.setText(String.valueOf(mList.get(position).getSum()));
        if (mOnItemClickLitener != null) {
            holder.mLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickLitener.onItemClick(holder, position);
                }
            });
            holder.mLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnItemClickLitener.onItemLongClick(holder, position);
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void refreshData(List<ArrangementAlbum> list) {
        mList = list;
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

    public String getPath(int position) {
        return mList.get(position).getName();
    }
}
