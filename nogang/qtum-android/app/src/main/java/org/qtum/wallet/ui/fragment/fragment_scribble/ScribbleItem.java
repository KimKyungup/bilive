package org.qtum.wallet.ui.fragment.fragment_scribble;

public class ScribbleItem {

    public String body;
    public String info;

    public ScribbleItem() {
        body = "";
        info = "";
    }

    public ScribbleItem(String body, String info){
        this.body = body;
        this.info = info;
    }
}
