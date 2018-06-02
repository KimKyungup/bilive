package org.qtum.wallet.dataprovider.rest_api.ether;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.qtum.wallet.model.gson.history_ether.TxListResponse;

import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

public class EthereumService {

    private static EthereumService sEthService;
    private EthereumRestService mServiceApi;

    public static EthereumService newInstance() {
        if (sEthService == null) {
            sEthService = new EthereumService();
        }
        return sEthService;
    }

    private EthereumService(){
        try {
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {

                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient.Builder client = new OkHttpClient.Builder();
            client.interceptors().add(httpLoggingInterceptor);
            client.readTimeout(180, TimeUnit.SECONDS);
            client.connectTimeout(180, TimeUnit.SECONDS);

//            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
//            keyStore.load(null, null);

            SSLContext sslContext = SSLContext.getInstance("TLS");

//            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
//            trustManagerFactory.init(keyStore);
//            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
//            keyManagerFactory.init(keyStore, "keystore_pass".toCharArray());

            sslContext.init(null, trustAllCerts, new SecureRandom());
            client.sslSocketFactory(sslContext.getSocketFactory())
                    .hostnameVerifier(new HostnameVerifier() {
                        @Override
                        public boolean verify(String hostname, SSLSession session) {
                            return true;
                        }
                    });

            Gson gson = new GsonBuilder().setLenient().create();

            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://api-rinkeby.etherscan.io")
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(client.build())
                    .build();

            mServiceApi = retrofit.create(EthereumRestService.class);
        } catch (Exception ignored) {
        }
    }



    public Observable<TxListResponse> getTransactionList(String address, Integer page, Integer size){
        return mServiceApi.getTransactionList(address, page, size);
    }

    public Call<TxListResponse> getCallTransactionList(String address){
        return mServiceApi.getCallTransactionList(address);
    }

}
