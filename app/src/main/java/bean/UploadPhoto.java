package bean;

import org.litepal.crud.LitePalSupport;

public class UploadPhoto extends LitePalSupport {
    private String newPath;
    private String mPhone;


    private String originalPath;
    private long mSize;
    private String mDisplayName;
    private String mAlbumName;
    private String mDate;
    private String mLongitude;//经度
    private String mLatitude;//纬度

    public UploadPhoto() {
    }

    public UploadPhoto(String newPath, String mPhone, Photo photo) {
        this.newPath = newPath;
        this.mPhone = mPhone;
        this.originalPath = photo.getmLocalPath();
        this.mSize = photo.getmSize();
        this.mDisplayName = photo.getmDisplayName();
        this.mAlbumName = photo.getmAlbumName();
        this.mDate = photo.getmDate();
        this.mLongitude = photo.getmLongitude();
        this.mLatitude = photo.getmLatitude();
    }

    public String getNewPath() {
        return newPath;
    }

    public void setNewPath(String newPath) {
        this.newPath = newPath;
    }

    public String getmPhone() {
        return mPhone;
    }

    public void setmPhone(String mPhone) {
        this.mPhone = mPhone;
    }

    public String getOriginalPath() {
        return originalPath;
    }

    public void setOriginalPath(String originalPath) {
        this.originalPath = originalPath;
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
}
