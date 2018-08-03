package event;

public class ShowToolbarEvent {
    private boolean mFlag;

    public ShowToolbarEvent(boolean mFlag) {
        this.mFlag = mFlag;
    }

    public boolean ismFlag() {
        return mFlag;
    }

    public void setmFlag(boolean mFlag) {
        this.mFlag = mFlag;
    }
}
