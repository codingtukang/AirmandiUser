package com.pasyappagent.pasy.component.network;

import android.net.SSLCertificateSocketFactory;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pasyappagent.pasy.BuildConfig;
import com.pasyappagent.pasy.component.util.DateHelper;
import com.pasyappagent.pasy.component.util.MethodUtil;
import com.pasyappagent.pasy.component.util.PreferenceManager;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.ConnectionSpec;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.TlsVersion;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Url;

/**
 * Created by Dhimas on 10/6/17.
 */

public class NetworkManager {
    public static NetworkService instance;
    public static Retrofit retrofit;

    private static final int CONNECT_TIME_OUT = 300 * 1000;
    private static final int READ_TIME_OUT = 300 * 1000;

    public static synchronized NetworkService getInstance(){
        instance = null;
        final String sessionToken = PreferenceManager.getSessionToken();

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.connectTimeout(CONNECT_TIME_OUT, TimeUnit.MILLISECONDS);
        httpClient.readTimeout(READ_TIME_OUT, TimeUnit.MILLISECONDS);
        httpClient.addNetworkInterceptor(interceptor);

//        final TrustManager[] trustAllCerts = new TrustManager[]{
//                new X509TrustManager() {
//                    @Override
//                    public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
//                    }
//
//                    @Override
//                    public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
//
//                    }
//
//                    @Override
//                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
//                        return new java.security.cert.X509Certificate[]{};
//                    }
//                }
//        };
//        try {
//            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
//            keyStore.load(null, null);
//
//            SSLContext sslContext = SSLContext.getInstance("TLS");
//
//            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
//            trustManagerFactory.init(keyStore);
//            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
//            keyManagerFactory.init(keyStore, "keystore_pass".toCharArray());
//            sslContext.init(null, trustAllCerts, new SecureRandom());
//
//            httpClient.sslSocketFactory(sslContext.getSocketFactory())
//                    .hostnameVerifier(new HostnameVerifier() {
//                        @Override
//                        public boolean verify(String hostname, SSLSession session) {
//                            return true;
//                        }
//                    });
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        } catch (CertificateException e) {
//            e.printStackTrace();
//        } catch (KeyStoreException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (UnrecoverableKeyException e) {
//            e.printStackTrace();
//        } catch (KeyManagementException e) {
//            e.printStackTrace();
//        }
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                String input = bodyToString(chain.request().body());
                if (TextUtils.isEmpty(input)) {
                    input = chain.request().url().toString().replace(BuildConfig.SERVER_URL, "");
                } else if (chain.request().url().toString().equals(BuildConfig.SERVER_URL + "/uploads/image")) {
                    input = "ref=customers&type=avatar";
                }

                if (input.equalsIgnoreCase("/cashbacks/redeem")) {
                    input = "";
                }
                String unixTime = String.valueOf(DateHelper.getUnixTime());
                String HMac = "";

                try {
                    if (input.contains("/merchants")) {
                         HMac = MethodUtil.getHMac(input, unixTime, true);
                    } else {
                        HMac = MethodUtil.getHMac(input, unixTime, false);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    HMac = "";
                }

//                Log.d("HMac - url", chain.request().url().toString());
//                Log.d("HMac - input", input);
//                Log.d("HMac - time", unixTime);
//                Log.d("HMac - HMac", HMac);


                Request original = chain.request();

                Request request = original.newBuilder()
                        .header("Accept", "application/pasy.v1+json")
                        .header("content-type", "application/x-www-form-urlencoded")
                        .header("App-ID", "mobile")
                        .header("AccessToken", sessionToken)
                        .header("cache-control", "no-cache")
                        .header("Time", unixTime)
                        .header("Hmac", HMac)
                        .build();

                Response response = chain.proceed(request);

                return response;
            }
        });

        ConnectionSpec spec = new ConnectionSpec.Builder(ConnectionSpec.CLEARTEXT)
                .build();

        httpClient.connectionSpecs(Collections.singletonList(spec));

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();


        retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(httpClient.build())
                .build();



        instance = retrofit.create(NetworkService.class);
        return instance;
    }

    public static String bodyToString(final RequestBody request) {
        try {
            final RequestBody copy = request;
            final Buffer buffer = new Buffer();
            if (copy != null)
                copy.writeTo(buffer);
            else
                return "";
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }
}
