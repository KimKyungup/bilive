package org.qtum.wallet.dataprovider.rest_api.qtum;

import org.qtum.wallet.model.gson.BlockChainInfo;

import org.qtum.wallet.model.gson.CallSmartContractRequest;
import org.qtum.wallet.model.gson.ContractParams;
import org.qtum.wallet.model.gson.DGPInfo;
import org.qtum.wallet.model.gson.ExistContractResponse;
import org.qtum.wallet.model.gson.FeePerKb;
import org.qtum.wallet.model.gson.QstoreContractType;
import org.qtum.wallet.model.gson.call_smart_contract_response.CallSmartContractResponse;
import org.qtum.wallet.model.gson.history.History;
import org.qtum.wallet.model.gson.history.HistoryResponse;
import org.qtum.wallet.model.gson.SendRawTransactionRequest;
import org.qtum.wallet.model.gson.SendRawTransactionResponse;
import org.qtum.wallet.model.gson.UnspentOutput;
import org.qtum.wallet.model.gson.history.TransactionReceipt;
import org.qtum.wallet.model.gson.token_history.TokenHistoryResponse;

import java.util.HashMap;
import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

interface QtumRestService {

    @GET("/outputs/unspent/{address}")
    Observable<List<UnspentOutput>> getOutputsUnspent(@Path("address") String address);

    @GET("/history/{address}/{limit}/{offset}")
    Observable<List<History>> getHistoryList(@Path("address") String address, @Path("limit") int limit, @Path("offset") int offset);

    @GET("/history/{address}/{limit}/{offset}")
    Observable<HistoryResponse> getHistoryResponse(@Path("address") String address, @Path("limit") int limit, @Path("offset") int offset);

    @GET("/blockchain/info")
    Observable<BlockChainInfo> getBlockChainInfo();

    @POST("/send-raw-transaction")
    Observable<SendRawTransactionResponse> sendRawTransaction(@Body SendRawTransactionRequest sendRawTransactionRequest);

    @POST("/contracts/{addressContract}/call")
    Observable<CallSmartContractResponse> callSmartContract(@Path("addressContract") String addressContract, @Body CallSmartContractRequest callSmartContractRequest);

    @GET("/outputs/unspent")
    Observable<List<UnspentOutput>> getUnspentOutputsForSeveralAddresses(@Query("addresses[]") List<String> addresses);

    @GET("/history/{limit}/{offset}")
    Observable<HistoryResponse> getHistoryListForSeveralAddresses(@Path("limit") int limit, @Path("offset") int offset, @Query("addresses[]") List<String> addresses);

    @GET("/transactions/{tx_hash}")
    Observable<History> getTransaction(@Path("tx_hash") String txHash);

    @GET("/contracts/{contract_id}/abi")
    Observable<Object> getAbiByContractId(@Path("contract_id") String contractId);

    @GET("/estimate-fee-per-kb")
    Observable<FeePerKb> getEstimateFeePerKb(@Query("nBlocks") int nBlocks);

    @GET("/contracts/types")
    Observable<List<QstoreContractType>> getContractTypes();

    @GET("/blockchain/dgpinfo")
    Observable<DGPInfo> getDGPInfo();

    @GET("/contracts/{addressContract}/params?keys=symbol,decimals,name,totalSupply")
    Observable<ContractParams> getContractParams(@Path("addressContract") String addressContract);


    @GET("/contracts/{addressContract}/exists")
    Observable<ExistContractResponse> isContractExist(@Path("addressContract") String addressContract);

    @GET("/qrc20/{qrc20ContractAddress}/transfers")
    Observable<TokenHistoryResponse> getTokenHistoryList(@Path("qrc20ContractAddress") String qrc20ContractAddress, @Query("limit") int limit, @Query("offset") int offset, @Query("addresses[]") List<String> addresses);

    @GET("/transactions/{txhash}/receipt")
    Observable<List<TransactionReceipt>> getTransactionReceipt(@Path("txhash") String txHash);

}