package event;

public class HideTopEvent {
    private boolean mIsHide;

    public HideTopEvent(boolean mIsHide) {
        this.mIsHide = mIsHide;
    }

    public boolean ismIsHide() {
        return mIsHide;
    }

    public void setmIsHide(boolean mIsHide) {
        this.mIsHide = mIsHide;
    }
}
