package org.qtum.wallet.ui.fragment.fragment_scribble;

public class ScribbleItem {

    private String body;
    private String info;

    public ScribbleItem() {
        body = "";
        info = "";
    }

    public ScribbleItem(String body, String info){
        this.body = body;
        this.info = info;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
