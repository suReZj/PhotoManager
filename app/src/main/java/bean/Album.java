package bean;

import org.litepal.crud.LitePalSupport;

public class Album extends LitePalSupport {
    private String mPath;
    private String mName;

    public Album() {
    }

    public Album(String mPath, String mName) {
        this.mPath = mPath;
        this.mName = mName;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmPath() {
        return mPath;
    }

    public void setmPath(String mPath) {
        this.mPath = mPath;
    }
}
