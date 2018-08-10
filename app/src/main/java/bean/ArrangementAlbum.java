package bean;

import org.litepal.crud.LitePalSupport;

import java.util.List;

public class ArrangementAlbum extends LitePalSupport{
    private String name;
    private int sum;
    private List<String> mList;

    public ArrangementAlbum() {
    }

    public ArrangementAlbum(String name, int sum, List<String> mList) {
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

    public List<String> getmList() {
        return mList;
    }

    public void setmList(List<String> mList) {
        this.mList = mList;
    }
}
