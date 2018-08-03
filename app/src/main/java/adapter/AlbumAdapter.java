package adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sure.photomanager.Activity.MainActivity;
import com.example.sure.photomanager.Activity.ShowActivity;
import com.example.sure.photomanager.R;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import bean.Photo;
import bean.SelectPhoto;
import event.SelectSumEvent;
import event.ShowImageEvent;


public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {
    private List<String> mTimeList;
    private HashMap<String, List<SelectPhoto>> mMapList;
    private Context mContext;
    private int mSelectedSum = 0;
    private List<String> mSelectedList;
    private HashMap<String, List<String>> mSelected;
    private boolean mIsSelected = false;//判断状态

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTv;
        private RecyclerView mRv;

        public ViewHolder(View itemView) {
            super(itemView);
            mTv = itemView.findViewById(R.id.album_rv_item_tv);
            mRv = itemView.findViewById(R.id.album_rv_item_rv);
        }
    }

    public AlbumAdapter(List<Photo> mPhotoList) {
        mTimeList = new ArrayList<>();
        mMapList = new HashMap<>();
        mSelectedList = new ArrayList<>();
        mSelected = new HashMap<>();

        String time = null;
        for (int i = 0; i < mPhotoList.size(); i++) {
            time = mPhotoList.get(i).getmDate();
            if (time != null) {
                time = time.substring(0, 10);
            }
            if (time != null && (!getTimeList().contains(time))) {
                List<SelectPhoto> list = new ArrayList<>();
                list.add(new SelectPhoto(mPhotoList.get(i), false));
                getTimeList().add(time);
                this.mMapList.put(time, list);
                continue;
            }
            if (time != null && (getTimeList().contains(time))) {
                this.mMapList.get(time).add(new SelectPhoto(mPhotoList.get(i), false));
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.album_rv_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.mTv.setText(mTimeList.get(position));
        final AlbumDetailAdapter albumDetailAdapter = new AlbumDetailAdapter(mMapList.get(mTimeList.get(position)));
        RecyclerView.LayoutManager mLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        holder.mRv.setLayoutManager(mLayoutManager);
        holder.mRv.setAdapter(albumDetailAdapter);
        albumDetailAdapter.setOnItemClickLitener(new AlbumDetailAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(AlbumDetailAdapter.ViewHolder view, int index) {
                if (!mIsSelected) {
                    //                EventBus.getDefault().post(new ShowImageEvent(mMapList.get(mTimeList.get(position)).get(index).getmLocalPath()));
                    Intent intent = new Intent(mContext, ShowActivity.class);
                    intent.putExtra("path", mMapList.get(mTimeList.get(position)).get(index).getmLocalPath());
                    int pos=0;
//                    for(int i=0;i<position;i++){
//                        mMapList.get(mTimeList.get(position)).
//                    }
                    mContext.startActivity(intent);
                } else {
                    if (mMapList.get(mTimeList.get(position)).get(index).ismIsSelected()) {
                        mMapList.get(mTimeList.get(position)).get(index).setmIsSelected(false);
                        albumDetailAdapter.notifyItemChanged(index);
                        mSelectedSum--;
                        EventBus.getDefault().post(new SelectSumEvent(mSelectedSum));
                        mSelectedList.remove(mMapList.get(mTimeList.get(position)).get(index).getmLocalPath());
                        if (mSelectedSum == 0) {
                            MainActivity.hideBottom();
                        }
                        if (mSelected.containsKey(mTimeList.get(position))) {
                            mSelected.get(mTimeList.get(position)).remove(index + "");
                        }

                        if (mSelectedSum == 0)
                            mIsSelected = false;
                        view.setMask(mContext.getDrawable(R.color.noMask));

                    } else {
                        MainActivity.setvisibility();
                        mMapList.get(mTimeList.get(position)).get(index).setmIsSelected(true);
                        albumDetailAdapter.notifyItemChanged(index);
                        mSelectedSum++;
                        EventBus.getDefault().post(new SelectSumEvent(mSelectedSum));
                        mSelectedList.add(mMapList.get(mTimeList.get(position)).get(index).getmLocalPath());
                        if (mSelected.containsKey(mTimeList.get(position))) {
                            mSelected.get(mTimeList.get(position)).add(index + "");
                        } else {
                            List<String> list = new ArrayList<>();
                            list.add(index + "");
                            mSelected.put(mTimeList.get(position), list);
                        }
                        view.setMask(mContext.getDrawable(R.color.maskColor));
                        if (mSelectedSum > 0)
                            mIsSelected = true;
                    }
                }

            }

            @Override
            public void onItemLongClick(AlbumDetailAdapter.ViewHolder view, int index) {
                if (mIsSelected) {
                    EventBus.getDefault().post(new ShowImageEvent(mMapList.get(mTimeList.get(position)).get(index).getmLocalPath()));
                }else {
                    MainActivity.setvisibility();
                    mMapList.get(mTimeList.get(position)).get(index).setmIsSelected(true);
                    albumDetailAdapter.notifyItemChanged(index);
                    mSelectedSum++;
                    EventBus.getDefault().post(new SelectSumEvent(mSelectedSum));
                    mSelectedList.add(mMapList.get(mTimeList.get(position)).get(index).getmLocalPath());
                    if (mSelected.containsKey(mTimeList.get(position))) {
                        mSelected.get(mTimeList.get(position)).add(index + "");
                    } else {
                        List<String> list = new ArrayList<>();
                        list.add(index + "");
                        mSelected.put(mTimeList.get(position), list);
                    }
                    view.setMask(mContext.getDrawable(R.color.maskColor));
                    if (mSelectedSum > 0)
                        mIsSelected = true;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTimeList.size();
    }

    public List<String> getTimeList() {
        return mTimeList;
    }

    public List<String> getSelectedList() {
        return mSelectedList;
    }

    public void cancelSelected() {
        mSelectedList.clear();
        mSelectedSum = 0;
        mIsSelected=false;
        for (String s : mSelected.keySet()) {
            List<String> list = mSelected.get(s);
            for (int i = 0; i < list.size(); i++) {
                mMapList.get(s).get(Integer.parseInt(list.get(i))).setmIsSelected(false);
            }
        }
        List<SelectPhoto> list;
        for(int i=0;i<mTimeList.size();i++){
            list=mMapList.get(mTimeList.get(i));
            for(int j=0;j<list.size();j++){
                if(list.get(j).ismIsSelected()){
                    list.get(j).setmIsSelected(false);
                }
            }
        }
        notifyDataSetChanged();
        MainActivity.hideBottom();
    }

    public List<String> getSelectPhotoList() {
        return mSelectedList;
    }

    public void selectAll(){
        List<SelectPhoto> list;
        for(int i=0;i<mTimeList.size();i++){
            list=mMapList.get(mTimeList.get(i));
            for(int j=0;j<list.size();j++){
                if(!list.get(j).ismIsSelected()){
                    mSelectedSum++;
                    list.get(j).setmIsSelected(true);
                }
            }
        }
        EventBus.getDefault().post(new SelectSumEvent(mSelectedSum));
        notifyDataSetChanged();
    }

    public void cancelSelectAll(){
        List<SelectPhoto> list;
        for(int i=0;i<mTimeList.size();i++){
            list=mMapList.get(mTimeList.get(i));
            for(int j=0;j<list.size();j++){
                if(list.get(j).ismIsSelected()){
                    mSelectedSum--;
                    list.get(j).setmIsSelected(false);
                }
            }
        }
        EventBus.getDefault().post(new SelectSumEvent(mSelectedSum));
        notifyDataSetChanged();
    }
}
