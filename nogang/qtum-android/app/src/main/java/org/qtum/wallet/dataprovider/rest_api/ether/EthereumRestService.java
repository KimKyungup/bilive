package org.qtum.wallet.dataprovider.rest_api.ether;

import org.qtum.wallet.model.gson.history_ether.TxListResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

interface EthereumRestService {
    @GET("/api?module=account&action=txlist&startblock&startblock=0&endblock=99999999&sort=desc&apikey=ZCEJNHRGJPTEJPC5PDN96WYDVCX7T3499U")
    Observable<TxListResponse> getTransactionList(@Query("address") String address, @Query("page") Integer page, @Query("offset") Integer offset);

    @GET("/api?module=account&action=txlist&startblock=0&endblock=99999999&sort=asc&apikey=ZCEJNHRGJPTEJPC5PDN96WYDVCX7T3499U")
    Call<TxListResponse> getCallTransactionList(@Path("address") String address);
}
