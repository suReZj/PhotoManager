package bean;

import android.media.ExifInterface;

import org.litepal.crud.LitePalSupport;

import java.util.List;


public class Photo extends LitePalSupport {
    private String mLocalPath;
    private long mSize;
    private String mDisplayName;
    private String mAlbumName;
    private String mDate;
    private String mLongitude;//经度
    private String mLatitude;//纬度

    private boolean mIsSort;
    private List<String> sortName;

    public Photo() {
    }

    public Photo(String mLocalPath, long mSize, String mDisplayName, String mAlbumName, String mDate, String mLongitude, String mLatitude, boolean sort, List<String> Name) {
        this.mLocalPath = mLocalPath;
        this.mSize = mSize;
        this.mDisplayName = mDisplayName;
        this.mAlbumName = mAlbumName;
        this.mDate = mDate;
        this.mLongitude = mLongitude;
        this.mLatitude = mLatitude;
        this.mIsSort = sort;
        this.sortName = Name;
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

    public boolean ismIsSort() {
        return mIsSort;
    }

    public void setmIsSort(boolean mIsSort) {
        this.mIsSort = mIsSort;
    }

    public List<String> getSortName() {
        return sortName;
    }

    public void setSortName(List<String> sortName) {
        this.sortName = sortName;
    }
}
