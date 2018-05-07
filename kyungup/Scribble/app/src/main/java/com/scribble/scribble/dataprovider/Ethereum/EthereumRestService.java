package com.scribble.scribble.dataprovider.Ethereum;

import com.scribble.scribble.model.gson.Transaction;
import com.scribble.scribble.model.gson.TxListResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

interface EthereumRestService {
    @GET("/api?module=account&action=txlist&startblock=0&endblock=99999999&sort=asc&apikey=ZCEJNHRGJPTEJPC5PDN96WYDVCX7T3499U")
    Observable<TxListResponse> getTransactionList(@Query("address") String address);

    @GET("/api?module=account&action=txlist&startblock=0&endblock=99999999&sort=asc&apikey=ZCEJNHRGJPTEJPC5PDN96WYDVCX7T3499U")
    Call<TxListResponse> getCallTransactionList(@Query("address") String address);
}
