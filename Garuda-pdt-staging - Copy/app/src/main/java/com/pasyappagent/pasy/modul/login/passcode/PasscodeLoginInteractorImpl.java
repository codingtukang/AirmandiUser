package com.pasyappagent.pasy.modul.login.passcode;

import android.content.Context;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import com.pasyappagent.pasy.component.network.NetworkService;
import com.pasyappagent.pasy.component.network.response.AgentResponse;
import com.pasyappagent.pasy.component.util.PreferenceManager;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Dhimas on 11/24/17.
 */

public class PasscodeLoginInteractorImpl implements PasscodeLoginInteractor {
    private NetworkService mService;

    public PasscodeLoginInteractorImpl(NetworkService service) {
        mService = service;

    }
    @Override
    public Observable<AgentResponse> login(String mobile, String passcode, String imei) {
//        TelephonyManager telephonyManager = (TelephonyManager) Context.getSystemService(Context.TELEPHONY_SERVICE);
//        String imei = telephonyManager.getDeviceId();
        PreferenceManager.setImei(imei);
        return mService.login(mobile, passcode, imei).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }
}
