package adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sure.photomanager.Activity.MainActivity;
import com.example.sure.photomanager.Activity.ShowActivity;
import com.example.sure.photomanager.R;
import com.google.gson.Gson;
import com.nanchen.compresshelper.CompressHelper;

import org.greenrobot.eventbus.EventBus;
import org.litepal.LitePal;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import bean.Photo;
import bean.SelectPhoto;
import bean.UploadPhoto;
import bean.User;
import event.SelectSumEvent;
import event.ShowImageEvent;
import event.UploadEvent;
import gson.RegisterAndLoginGson;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import untils.DateUtil;


public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {
    private List<String> mTimeList;
    private HashMap<String, List<SelectPhoto>> mMapList;
    private Context mContext;
    private int mSelectedSum = 0;
    private List<String> mSelectedList;
    private HashMap<String, List<String>> mSelected;
    private boolean mIsSelected = false;//判断状态
    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    Toast.makeText(mContext, "Successful upload", Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    Toast.makeText(mContext, "Upload failed", Toast.LENGTH_SHORT).show();
                    break;
            }
            return false;
        }
    });

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTv;
        private RecyclerView mRv;
        private Button mBtn;

        public ViewHolder(View itemView) {
            super(itemView);
            mTv = itemView.findViewById(R.id.album_rv_item_tv);
            mRv = itemView.findViewById(R.id.album_rv_item_rv);
            mBtn = itemView.findViewById(R.id.album_rv_item_btn);
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
                time = DateUtil.changeTime(time);
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
                    Intent intent = new Intent(mContext, ShowActivity.class);
                    intent.putExtra("path", mMapList.get(mTimeList.get(position)).get(index).getmLocalPath());
                    int pos = 0;
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
        });


        holder.mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<User> list = LitePal.findAll(User.class);
                if (list.size() == 0) {
                    Toast.makeText(mContext, "Please Login", Toast.LENGTH_SHORT).show();
                } else {
                    upLoadImage(mMapList.get(mTimeList.get(position)).get(0).getmLocalPath());
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
        mIsSelected = false;
        for (String s : mSelected.keySet()) {
            List<String> list = mSelected.get(s);
            for (int i = 0; i < list.size(); i++) {
                mMapList.get(s).get(Integer.parseInt(list.get(i))).setmIsSelected(false);
            }
        }
        List<SelectPhoto> list;
        for (int i = 0; i < mTimeList.size(); i++) {
            list = mMapList.get(mTimeList.get(i));
            for (int j = 0; j < list.size(); j++) {
                if (list.get(j).ismIsSelected()) {
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

    public void selectAll() {
        List<SelectPhoto> list;
        for (int i = 0; i < mTimeList.size(); i++) {
            list = mMapList.get(mTimeList.get(i));
            for (int j = 0; j < list.size(); j++) {
                if (!list.get(j).ismIsSelected()) {
                    mSelectedSum++;
                    list.get(j).setmIsSelected(true);
                }
            }
        }
        EventBus.getDefault().post(new SelectSumEvent(mSelectedSum));
        notifyDataSetChanged();
    }

    public void cancelSelectAll() {
        List<SelectPhoto> list;
        for (int i = 0; i < mTimeList.size(); i++) {
            list = mMapList.get(mTimeList.get(i));
            for (int j = 0; j < list.size(); j++) {
                if (list.get(j).ismIsSelected()) {
                    mSelectedSum--;
                    list.get(j).setmIsSelected(false);
                }
            }
        }
        EventBus.getDefault().post(new SelectSumEvent(mSelectedSum));
        notifyDataSetChanged();
    }

    public void refreshData(List<Photo> mPhotoList) {
        mTimeList = new ArrayList<>();
        mMapList = new HashMap<>();
        mSelectedList = new ArrayList<>();
        mSelected = new HashMap<>();
        mSelectedSum = 0;
        mIsSelected = false;

        String time = null;
        for (int i = 0; i < mPhotoList.size(); i++) {
            time = mPhotoList.get(i).getmDate();
            if (time != null) {
                time = time.substring(0, 10);
                time = DateUtil.changeTime(time);
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
        EventBus.getDefault().post(new SelectSumEvent(mSelectedSum));
        notifyDataSetChanged();
        MainActivity.hideBottom();
    }

    public boolean ismIsSelected() {
        return mIsSelected;
    }

    public void setmIsSelected(boolean mIsSelected) {
        this.mIsSelected = mIsSelected;
    }


    public void upLoadImage(String path) {
        final List<User> list = LitePal.findAll(User.class);
        final List<Photo> photo = LitePal.where("mLocalPath = ?", path).find(Photo.class);
        OkHttpClient mOkHttpClent = new OkHttpClient();
        File file = new File(path);
        Log.e("oldName", file.getName());
        File newFile = CompressHelper.getDefault(mContext).compressToFile(file);
        Log.e("NewName", newFile.getName());

        MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        RequestBody body = RequestBody.create(MediaType.parse("image/*"), newFile);
        final String filename = file.getName();
        // 参数分别为， 请求key ，文件名称 ， RequestBody
        requestBody.addFormDataPart("file", filename, body);

        Request request = new Request.Builder().url("http://www.ligohan.com:8080/springboot-security-demo/api/file/upload").post(requestBody.build()).build();


        Call call = mOkHttpClent.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("faile", "onFailure: " + e);
                Message msg = handler.obtainMessage();
                msg.what = 1;
                handler.sendMessage(msg);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e("success", "成功" + response);
                Gson gson = new Gson();
                RegisterAndLoginGson registerAndLoginGson = gson.fromJson(response.body().string(), RegisterAndLoginGson.class);
                if (registerAndLoginGson.isIs_success()) {
                    UploadPhoto uploadPhoto = new UploadPhoto("http://www.ligohan.com:8080/springboot-security-demo/api/file/download?fileName=" + filename, list.get(0).getmPhone(), photo.get(0));
                    uploadPhoto.save();
                    Message msg = handler.obtainMessage();
                    msg.what = 0;
                    handler.sendMessage(msg);
                    EventBus.getDefault().post(new UploadEvent());
                } else {
                    Message msg = handler.obtainMessage();
                    msg.what = 1;
                    handler.sendMessage(msg);
                }

            }
        });

    }
}
