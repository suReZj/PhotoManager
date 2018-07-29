package fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sure.photomanager.R;

public class IntelligentFragment extends Fragment {
    private Toolbar mToolbar;
    private AppCompatActivity mAppCompatActivity;

    public IntelligentFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.intelligent_fragment, null);
//        mToolbar=view.findViewById(R.id.intelligent_fragment_toolbar);
//        mAppCompatActivity.setSupportActionBar(mToolbar);
//        ActionBar actionBar = mAppCompatActivity.getSupportActionBar();
//        if (actionBar != null) {
//            actionBar.setDisplayHomeAsUpEnabled(false);
//        }
        return view;
    }
}
