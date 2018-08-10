package adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.sure.photomanager.R;

import java.util.List;

public class PrivatePasswordAdapter extends RecyclerView.Adapter<PrivatePasswordAdapter.ViewHolder> {
    private List<String> mList;
    private Context mContext;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView mCardView;
        private Button mBtn;

        public ViewHolder(View itemView) {
            super(itemView);
            mCardView = itemView.findViewById(R.id.activity_private_password_rv_cv);
            mBtn = itemView.findViewById(R.id.activity_private_password_rv_btn);
            int width = ((Activity) mBtn.getContext()).getWindowManager().getDefaultDisplay().getWidth();
            ViewGroup.LayoutParams params = mBtn.getLayoutParams();
            //设置图片的相对于屏幕的宽高比
            params.width = (width - 12) / 3;
            mBtn.setLayoutParams(params);
        }
    }

    public PrivatePasswordAdapter(List<String> mList) {
        this.mList = mList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.private_password_activity_rv_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mBtn.setText(mList.get(position));
        if (mList.get(position).equals("delete")) {
            if (mOnItemClickLitener != null) {
                holder.mBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemClickLitener.onItemDeleteClick(holder, position);
                    }
                });
            }
        } else if (mList.get(position).equals("sure")) {
            if (mOnItemClickLitener != null) {
                holder.mBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemClickLitener.onItemSureClick(holder, position);
                    }
                });
            }
        } else {
            if (mOnItemClickLitener != null) {
                holder.mBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemClickLitener.onItemClick(holder, position);
                    }
                });
            }
        }

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public interface OnItemClickLitener {
        void onItemClick(PrivatePasswordAdapter.ViewHolder view, int index);

        void onItemSureClick(PrivatePasswordAdapter.ViewHolder view, int index);

        void onItemDeleteClick(PrivatePasswordAdapter.ViewHolder view, int index);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    public String getNumber(int position) {
        return mList.get(position);
    }
}
