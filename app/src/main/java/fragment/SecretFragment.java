package fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.sure.photomanager.R;

import widght.PasswordInputView;

public class SecretFragment extends Fragment {
    private PasswordInputView mPassWordInputView;
    String password="1105";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.secret_fragment,null);
        mPassWordInputView=view.findViewById(R.id.secret_fragment_password);
        mPassWordInputView.requestFocus();
        mPassWordInputView.setOnFinishListener(new PasswordInputView.OnFinishListener() {
            @Override
            public void setOnPasswordFinished() {
                if(password.equals(mPassWordInputView.getText().toString())&&(mPassWordInputView.getText().toString().length()==4)) {
                    Toast.makeText(getContext(), "success", Toast.LENGTH_SHORT).show();
                }
                    if(!password.equals(mPassWordInputView.getText().toString())&&(mPassWordInputView.getText().toString().length()==4)){
                    Toast.makeText(getContext(), "failed", Toast.LENGTH_SHORT).show();
                mPassWordInputView.setText("");}
            }
        });
        return view;
    }
}
