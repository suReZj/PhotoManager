package event;

import bean.Photo;
import bean.SelectPhoto;

public class ShowImageEvent {
    private String mLocalpath;

    public ShowImageEvent(String mLocalpath) {
        this.mLocalpath = mLocalpath;
    }

    public String getmLocalpath() {
        return mLocalpath;
    }

    public void setmLocalpath(String mLocalpath) {
        this.mLocalpath = mLocalpath;
    }



}
