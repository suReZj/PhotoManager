package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
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
    private List<ViewHolder> mViewList;

    class ViewHolder {
        public TextView mName;
        public TextView mSum;
        public CheckBox mCb;
        public String mSelectName;

        public TextView getmName() {
            return mName;
        }

        public void setmName(TextView mName) {
            this.mName = mName;
        }

        public TextView getmSum() {
            return mSum;
        }

        public void setmSum(TextView mSum) {
            this.mSum = mSum;
        }

        public CheckBox getmCb() {
            return mCb;
        }

        public void setmCb(CheckBox mCb) {
            this.mCb = mCb;
        }

        public String getmSelectName() {
            return mSelectName;
        }

        public void setmSelectName(String mSelectName) {
            this.mSelectName = mSelectName;
        }
    }

    public DialogAdapter(Context mContext, int mCount, List<ArrangementAlbum> mList) {
        this.mContext = mContext;
        this.mCount = mCount;
        this.mList = mList;
        mInflater = LayoutInflater.from(mContext);
        mViewList = new ArrayList<>();
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
            viewHolder = new ViewHolder();
            viewHolder.mName = convertView.findViewById(R.id.dialog_list_item_name);
            viewHolder.mSum = convertView.findViewById(R.id.dialog_list_item_sum);
            viewHolder.mCb = convertView.findViewById(R.id.dialog_list_item_cb);
            viewHolder.mName.setText(mList.get(position).getName());
            viewHolder.mSum.setText(String.valueOf(mList.get(position).getSum()));
            viewHolder.mSelectName = mList.get(position).getName();
            mViewList.add(viewHolder);
        }
        return convertView;
    }

    public List<String> getCbList() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < mViewList.size(); i++) {
            if (mViewList.get(i).mCb.isChecked()) {
                list.add(mViewList.get(i).getmSelectName());
            }
        }
        return list;
    }

}
