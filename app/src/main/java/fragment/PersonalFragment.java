package fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.sure.photomanager.Activity.PrivatePasswordActivity;
import com.example.sure.photomanager.R;

public class PersonalFragment extends Fragment {
    private LinearLayout mPrivateAlbum;
    public PersonalFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.personal_fragment, null);
        mPrivateAlbum=view.findViewById(R.id.personal_fragment_private_album);
        setListener();
        return view;
    }

    public void setListener(){
        mPrivateAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), PrivatePasswordActivity.class);
                startActivity(intent);
            }
        });
    }
}
