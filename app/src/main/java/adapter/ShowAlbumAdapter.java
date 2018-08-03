package adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sure.photomanager.R;

import java.util.List;

import bean.ArrangementAlbum;

public class ShowAlbumAdapter extends RecyclerView.Adapter<ShowAlbumAdapter.ViewHolder> {
    private List<ArrangementAlbum> mList;
    private Context mContext;
    private String mName="Add Album";

    static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImage;
        private TextView mTv;
        public ViewHolder(View itemView) {
            super(itemView);
            mImage=itemView.findViewById(R.id.show_album_rv_item_image);
            mTv=itemView.findViewById(R.id.show_album_rv_item_tv);
        }
    }

    public ShowAlbumAdapter(List<ArrangementAlbum> list) {
        list.add(new ArrangementAlbum(mName,0,null));
        this.mList=list;
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
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(mList.get(position).getName().equals(mName)){
            holder.mImage.setImageResource(R.drawable.ic_add_black_24dp);
            holder.mTv.setText(R.string.add_album);
        }else {
            holder.mImage.setImageResource(R.drawable.ic_arrow_downward_black_24dp);
            String text=mList.get(position).getName();
            holder.mTv.setText(text);
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
