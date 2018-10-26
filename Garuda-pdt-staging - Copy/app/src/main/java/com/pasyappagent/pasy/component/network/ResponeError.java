package com.pasyappagent.pasy.component.network;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.adapter.rxjava.HttpException;

/**
 * Created by Dhimas on 10/9/17.
 */

public class ResponeError {
    private static final String ERROR_BAD_REQUEST = "Terjadi kesalahan koneksi, tunggu beberapa saat lagi";
    private static final String ERROR_UNEXPECTED = "Terjadi kesalahan koneksi, tunggu beberapa saat lagi";
    private static final String ERROR_NO_NETWORK_CONNECTION = "Tidak ada koneksi internet";

    public static String getErrorMessage(Throwable throwable) {
        throwable.printStackTrace();
        if (throwable instanceof IOException) return ERROR_NO_NETWORK_CONNECTION;

        if (throwable instanceof HttpException) {
            if (((HttpException) throwable).code() >= 400) {
                try {
                    return new JSONObject(((HttpException) throwable).response().errorBody().source().readUtf8()).getString("message");
                } catch (IOException | JSONException e) {
                    return ERROR_BAD_REQUEST;
                }
            }
        }

        return ERROR_UNEXPECTED;
    }

    public static String getErrorMessage(NetworkService mNetworkService ,Throwable throwable) {
        throwable.printStackTrace();
        if (throwable instanceof IOException) return ERROR_NO_NETWORK_CONNECTION;

        if (throwable instanceof HttpException) {
            if (((HttpException) throwable).code() >= 400) {
                if (((HttpException) throwable).code() == 403) {

                } else {
                    try {
                        return new JSONObject(((HttpException) throwable).response().errorBody().source().readUtf8()).getString("message");
                    } catch (IOException | JSONException e) {
                        return ERROR_BAD_REQUEST;
                    }
                }

            }
        }

        return ERROR_UNEXPECTED;
    }

    public static String getErrorTokenMessage(Throwable throwable) {
        throwable.printStackTrace();
        try {
            return throwable.getMessage().toString();
        } catch (Exception e) {
            return "";
        }
    }


}
