package fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.sure.photomanager.Activity.PrivatePasswordActivity;
import com.example.sure.photomanager.Activity.SubscriptionActivity;
import com.example.sure.photomanager.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;
import org.litepal.crud.callback.UpdateOrDeleteCallback;

import java.util.List;

import bean.UploadPhoto;
import bean.User;
import de.hdodenhof.circleimageview.CircleImageView;
import event.LoginEvent;

public class PersonalFragment extends Fragment {
    private LinearLayout mPrivateAlbum;
    private ProgressBar mPb;
    private int mSize;
    private TextView mNameTv;
    private TextView mSizeTv;
    private List<UploadPhoto> mList;
    private CircleImageView mImage;
    private Button mExpand;


    public PersonalFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.personal_fragment, null);
        mPrivateAlbum = view.findViewById(R.id.personal_fragment_private_album);
        mNameTv = view.findViewById(R.id.personal_fragment_name);
        mSizeTv = view.findViewById(R.id.personal_fragment_xxxM);
        mPb = view.findViewById(R.id.personal_fragment_progressBar);
        mImage = view.findViewById(R.id.personal_fragment_icon);
        mExpand = view.findViewById(R.id.personal_fragment_expand);
        setListener();
        setProgress();
        return view;
    }

    public void setListener() {
        mPrivateAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), PrivatePasswordActivity.class);
                startActivity(intent);
            }
        });

        mImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LitePal.deleteAllAsync(User.class).listen(new UpdateOrDeleteCallback() {
                    @Override
                    public void onFinish(int rowsAffected) {
//                        refresh();
                        EventBus.getDefault().post(new LoginEvent());
                    }
                });
            }
        });

        mExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), SubscriptionActivity.class);
                startActivity(intent);
            }
        });
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(LoginEvent event) {
        setProgress();
    }


    public void setProgress() {
        List<User> user = LitePal.findAll(User.class);
        mSize = 0;
        if (user.size() > 0) {
            mList = LitePal.where("mPhone = ?", user.get(0).getmPhone()).order("mDate desc").find(UploadPhoto.class);
            for (int i = 0; i < mList.size(); i++) {
                mSize = mSize + (int) (mList.get(i).getmSize() / 1024);
            }
            mPb.setProgress(mSize);
            mSizeTv.setText(mSize + getString(R.string.space_size));
            mNameTv.setText(user.get(0).getmPhone());
        } else {
            mSizeTv.setText("0" + getString(R.string.space_size));
            mPb.setProgress(0);
            mNameTv.setText(getString(R.string.go_album));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
