package bean;

import org.litepal.crud.LitePalSupport;

import java.util.List;

public class User extends LitePalSupport{
    private String mPhone;
    private String mPassword;
    private List<String> mList;

    public User() {
    }

    public User(String mPhone, String mPassword, List<String> mList) {
        this.mPhone = mPhone;
        this.mPassword = mPassword;
        this.mList = mList;
    }

    public String getmPhone() {
        return mPhone;
    }

    public void setmPhone(String mPhone) {
        this.mPhone = mPhone;
    }

    public String getmPassword() {
        return mPassword;
    }

    public void setmPassword(String mPassword) {
        this.mPassword = mPassword;
    }

    public List<String> getmList() {
        return mList;
    }

    public void setmList(List<String> mList) {
        this.mList = mList;
    }
}
