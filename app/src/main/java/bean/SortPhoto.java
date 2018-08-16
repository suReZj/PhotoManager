package bean;

import org.litepal.crud.LitePalSupport;

public class SortPhoto extends LitePalSupport {
    private String mLocalPath;
    private long mSize;
    private String mDisplayName;
    private String mAlbumName;
    private String mDate;
    private String mLongitude;//经度
    private String mLatitude;//纬度
    private String mSortAlbumName;

    public SortPhoto() {
    }

    public SortPhoto(Photo photo, String name) {
        this.mLocalPath = photo.getmLocalPath();
        this.mSize = photo.getmSize();
        this.mDisplayName = photo.getmDisplayName();
        this.mAlbumName = photo.getmAlbumName();
        this.mDate = photo.getmDate();
        this.mLongitude = photo.getmLongitude();
        this.mLatitude = photo.getmLatitude();
        this.mSortAlbumName = name;
    }

    public String getmLocalPath() {
        return mLocalPath;
    }

    public void setmLocalPath(String mLocalPath) {
        this.mLocalPath = mLocalPath;
    }

    public long getmSize() {
        return mSize;
    }

    public void setmSize(long mSize) {
        this.mSize = mSize;
    }

    public String getmDisplayName() {
        return mDisplayName;
    }

    public void setmDisplayName(String mDisplayName) {
        this.mDisplayName = mDisplayName;
    }

    public String getmAlbumName() {
        return mAlbumName;
    }

    public void setmAlbumName(String mAlbumName) {
        this.mAlbumName = mAlbumName;
    }

    public String getmDate() {
        return mDate;
    }

    public void setmDate(String mDate) {
        this.mDate = mDate;
    }

    public String getmLongitude() {
        return mLongitude;
    }

    public void setmLongitude(String mLongitude) {
        this.mLongitude = mLongitude;
    }

    public String getmLatitude() {
        return mLatitude;
    }

    public void setmLatitude(String mLatitude) {
        this.mLatitude = mLatitude;
    }

    public String getmSortAlbumName() {
        return mSortAlbumName;
    }

    public void setmSortAlbumName(String mSortAlbumName) {
        this.mSortAlbumName = mSortAlbumName;
    }
}
