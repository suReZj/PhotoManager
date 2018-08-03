package bean;

import java.util.List;

public class ArrangementAlbum {
    private String name;
    private int sum;
    private List<Photo> mList;

    public ArrangementAlbum() {
    }

    public ArrangementAlbum(String name, int sum, List<Photo> mList) {
        this.name = name;
        this.sum = sum;
        this.mList = mList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public List<Photo> getmList() {
        return mList;
    }

    public void setmList(List<Photo> mList) {
        this.mList = mList;
    }
}
