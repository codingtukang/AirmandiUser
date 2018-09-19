package com.pasyappagent.pasy.component.util;

import android.content.Context;

import com.orhanobut.hawk.Hawk;
import com.orhanobut.hawk.HawkBuilder;
import com.pasyappagent.pasy.component.network.gson.GAgent;
import com.pasyappagent.pasy.component.network.gson.GTopup;
import com.pasyappagent.pasy.component.network.response.ParkingResponse;
import com.pasyappagent.pasy.modul.scanqr.QuickResponse;

/**
 * Created by Dhimas on 10/9/17.
 */

public class PreferenceManager {

    private static final String SESSION_TOKEN = "sessionToken";
    private static final String IS_LOGIN = "isLogin";
    private static final String USER_LOGIN = "userLogin";
    private static final String AGENT = "agent";
    private static final String TOPUP = "topup";
    private static final String REFFERAL_ID = "refferalId";
    private static final String IS_AKUPAY = "isAkupay";
    private static final String QR_RESPONSE = "qrResponse";
    private static final String PARKING_ID = "parkingId";
    private static final String IMEI = "imei";

    public PreferenceManager(Context context) {
//        Hawk.init(context)
//                .setEncryptionMethod(HawkBuilder.EncryptionMethod.HIGHEST)
//                .setStorage(HawkBuilder.newSharedPrefStorage(context))
//                .setPassword("P@ssw0rd123")
//                .build();
        Hawk.init(context).build();
    }

    public static String getSessionToken() {
        return Hawk.get(SESSION_TOKEN, "");
    }

    public static void setSessionToken(String token) {
        Hawk.put(SESSION_TOKEN, token);
    }

    public static void logIn(String token, String name, String mobile) {
        Hawk.put(IS_LOGIN, true);
        Hawk.put(SESSION_TOKEN, token);
        Hawk.put(USER_LOGIN, new String[]{name, mobile});
    }

    public static void logOut() {
        Hawk.put(USER_LOGIN, null);
        Hawk.put(IS_LOGIN, false);
        Hawk.put(SESSION_TOKEN, "");
        Hawk.put(AGENT, null);
    }

    public static String[] getUser() {
        return Hawk.get(USER_LOGIN);
    }

    public static Boolean isLogin() {
        return Hawk.get(IS_LOGIN, false);
    }

    public static void setAgent(GAgent agent) {
        Hawk.put(AGENT, agent);
    }

    public static GAgent getAgent() {
        return Hawk.get(AGENT);
    }

    public static void setTopup(GTopup topup) {
        Hawk.put(TOPUP, topup);
    }

    public static GTopup getTopup() {
        return Hawk.get(TOPUP);
    }

    public static void setRefferalId(String id) {
        Hawk.put(REFFERAL_ID, id);
    }

    public static String getRefferalId() {
        return Hawk.get(REFFERAL_ID,"");
    }

    public static void setStatusAkupay(boolean isAkupay) {
        Hawk.put(IS_AKUPAY, isAkupay);
    }

    public static boolean getStatusAkupay() {
        return Hawk.get(IS_AKUPAY, false);
    }

    public static void setQrResponse(QuickResponse reseponse) {
        Hawk.put(QR_RESPONSE, reseponse);
    }

    public static  QuickResponse getQrResponse() {
        return Hawk.get(QR_RESPONSE);
    }

    public static void setParkingId(String transactionId) {
        Hawk.put(PARKING_ID, transactionId);
    }

    public static String getParkingId() {
        return Hawk.get(PARKING_ID);
    }

    public static void emptyParkingId() {
        setParkingId("");
    }

    public static String getImei() {
        return Hawk.get(IMEI);
    }

    public static void setImei(String imei) {
        Hawk.put(IMEI, imei);
    }
}
