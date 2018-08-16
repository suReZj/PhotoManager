package adapter;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.sure.photomanager.R;

import org.litepal.LitePal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import bean.ArrangementAlbum;
import untils.FileUtil;

public class ShowAlbumAdapter extends RecyclerView.Adapter<ShowAlbumAdapter.ViewHolder> {
    private List<ArrangementAlbum> mList;
    private Context mContext;
    private String mName = "Add Album";
    private Handler mHandler = new Handler();

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImage;
        private TextView mTv;
        private RelativeLayout mLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            mImage = itemView.findViewById(R.id.show_album_rv_item_image);
            mTv = itemView.findViewById(R.id.show_album_rv_item_tv);
            mLayout = itemView.findViewById(R.id.show_album_rv_item_layout);
        }
    }

    public ShowAlbumAdapter(List<ArrangementAlbum> list) {
//        list.add(new ArrangementAlbum(mName,0,null));
        this.mList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.show_album_rv_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (mList.get(position).getName().equals(mName)) {
            holder.mImage.setImageResource(R.mipmap.ic_addalbum);
            holder.mTv.setText(R.string.add_album);

            if (mOnItemClickLitener != null) {
                holder.mLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemClickLitener.onCreateClick(holder, position);
                    }
                });
            }

        } else {
            holder.mImage.setImageResource(R.mipmap.ic_add);
            String text = mList.get(position).getName();
            holder.mTv.setText(text);
            if (mOnItemClickLitener != null) {
                holder.mLayout.setOnClickListener(new View.OnClickListener() {
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
        void onItemClick(ViewHolder view, int index);

        void onCreateClick(ViewHolder view, int index);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    public void refreshData() {

        mList = LitePal.findAll(ArrangementAlbum.class);
        for (int i = 0; i < mList.size(); i++) {
            if (mList.get(i).getName().equals("delete")) {
                mList.remove(i);
            }
        }
        mList.add(new ArrangementAlbum("Add Album", 0, null));
        notifyDataSetChanged();
    }

    public String getAlbum(int index) {
        return mList.get(index).getName();
    }
}
