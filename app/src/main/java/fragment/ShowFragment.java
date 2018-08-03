package fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.sure.photomanager.Activity.ShowActivity;
import com.example.sure.photomanager.R;

import org.greenrobot.eventbus.EventBus;

import event.DeleteAndShowNextEvent;
import widght.SmoothImageView;

public class ShowFragment extends Fragment{
    private SmoothImageView mImage;
    private String mLocalPath;

    public ShowFragment() {
    }

    @SuppressLint("ValidFragment")
    public ShowFragment(String path) {
        this.mLocalPath=path;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.show_fragment, null);
        mImage=view.findViewById(R.id.show_fragment_iv);
        Glide.with(getActivity()).load(mLocalPath).into(mImage);
        mImage.setTransformOutListener(new SmoothImageView.OnTransformOutListener() {
            @Override
            public void onTransformOut() {
                Toast.makeText(getContext(),"delete",Toast.LENGTH_SHORT).show();
                EventBus.getDefault().post(new DeleteAndShowNextEvent());
            }
        });
        return view;
    }
}
