package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sure.photomanager.R;

import java.util.ArrayList;
import java.util.List;

import bean.ArrangementAlbum;

public class DialogAdapter extends BaseAdapter {
    private Context mContext;
    private int mCount;
    private LayoutInflater mInflater;
    private List<ArrangementAlbum> mList;

    class ViewHolder {
        public ImageView mImage;
        public TextView mTv;
    }

    public DialogAdapter(Context mContext, int mCount, List<ArrangementAlbum> mList) {
        this.mContext = mContext;
        this.mCount = mCount;
        this.mList = mList;
        mInflater=LayoutInflater.from(mContext);
    }


    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.dialog_list_item, parent, false);
            viewHolder=new ViewHolder();

        }
        return convertView;
    }


}
