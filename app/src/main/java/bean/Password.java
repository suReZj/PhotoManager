package bean;

import org.litepal.crud.LitePalSupport;

public class Password extends LitePalSupport{
    private String mPass;

    public Password() {
    }

    public Password(String mPass) {
        this.mPass = mPass;
    }

    public String getmPass() {
        return mPass;
    }

    public void setmPass(String mPass) {
        this.mPass = mPass;
    }
}
