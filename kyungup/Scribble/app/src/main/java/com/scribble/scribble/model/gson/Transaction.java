package com.scribble.scribble.model.gson;


import io.realm.RealmList;
import io.realm.RealmObject;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.annotations.PrimaryKey;

public class Transaction  extends RealmObject{
    @SerializedName("blockNumber")
    @Expose
    private Long blockNumber;

    @SerializedName("timeStamp")
    @Expose
    private Integer timeStamp;

    @SerializedName("hash")
    @Expose
    @PrimaryKey
    private String hash;

    @SerializedName("from")
    @Expose
    private String from;

    @SerializedName("to")
    @Expose
    private String to;

    @SerializedName("input")
    @Expose
    private String input;


    public String getHash(){ return hash;}
}
