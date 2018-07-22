package bean;

import android.media.ExifInterface;

public class Photo {
    private String mLocalPath;
    private long mSize;
    private String mDisplayName;
    private String mAlbumName;
    private ExifInterface mExifInterface;

    public Photo() {
    }

    public Photo(String mLocalPath, long mSize, String mDisplayName, String mAlbumName, ExifInterface mExifInterface) {
        this.mLocalPath = mLocalPath;
        this.mSize = mSize;
        this.mDisplayName = mDisplayName;
        this.mAlbumName = mAlbumName;
        this.mExifInterface = mExifInterface;
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

    public ExifInterface getmExifInterface() {
        return mExifInterface;
    }

    public void setmExifInterface(ExifInterface mExifInterface) {
        this.mExifInterface = mExifInterface;
    }
}
