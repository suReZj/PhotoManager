package adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.sure.photomanager.Activity.ShowActivity;
import com.example.sure.photomanager.R;

import java.util.List;

import bean.ArrangementAlbum;

public class ArrangementDetailAdapter extends RecyclerView.Adapter<ArrangementDetailAdapter.ViewHolder> {
    private List<String> mList;
    private Context mContext;
    private String mAlbumName;


    static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImage;

        public ViewHolder(View itemView) {
            super(itemView);
            mImage = itemView.findViewById(R.id.arrangement_detail_rv_item_image);
            int width = ((Activity) mImage.getContext()).getWindowManager().getDefaultDisplay().getWidth();
            ViewGroup.LayoutParams params = mImage.getLayoutParams();
            //设置图片的相对于屏幕的宽高比
            params.width = (width - 6) / 3;
            params.height = width / 3;
            mImage.setLayoutParams(params);
        }
    }

    public ArrangementDetailAdapter(List<String> mList, String name) {
        this.mList = mList;
        this.mAlbumName = name;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.arrangement_detail_rv_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Glide.with(mContext).load(mList.get(position)).into(holder.mImage);
        holder.mImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ShowActivity.class);
                intent.putExtra("path", mList.get(position));
                intent.putExtra("name", mAlbumName);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


}
