package org.qtum.wallet.ui.fragment.fragment_main_write;

import java.util.Date;

public class Post {

    private String body;
    private String info;

    public Post () {
        body = "";
        info = "";
    }

    public Post (String body, String info){
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
