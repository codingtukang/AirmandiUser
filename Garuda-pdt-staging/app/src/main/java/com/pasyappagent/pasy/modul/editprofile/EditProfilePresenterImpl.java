package com.pasyappagent.pasy.modul.editprofile;

import android.text.TextUtils;

import com.pasyappagent.pasy.component.network.ResponeError;
import com.pasyappagent.pasy.component.network.gson.GAgent;
import com.pasyappagent.pasy.component.util.PreferenceManager;
import com.pasyappagent.pasy.modul.CommonInterface;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Dhimas on 12/23/17.
 */

public class EditProfilePresenterImpl implements EditProfilePresenter {
    EditProfileView mView;
    CommonInterface cInterface;

    public EditProfilePresenterImpl(CommonInterface commonInterface, EditProfileView view) {
        cInterface = commonInterface;
        mView = view;
    }

    @Override
    public void updateProfile(String name, String mobile, String email) {
        if (isValidData(name, mobile, email)) {
            cInterface.showProgressLoading();
            updateProfileRequest(name, mobile, email).subscribe(new Subscriber<GAgent>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    cInterface.hideProgresLoading();
                    cInterface.onFailureRequest(ResponeError.getErrorMessage(e));
                }

                @Override
                public void onNext(GAgent gAgent) {
                    cInterface.hideProgresLoading();
                    PreferenceManager.setAgent(gAgent);
                    PreferenceManager.setSessionToken(gAgent.access_token);
                    mView.onSuccessEdit();
                }
            });
        }

    }

    private Boolean isValidData(String name, String mobile, String email) {
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(mobile) || TextUtils.isEmpty(email)) {
            cInterface.onFailureRequest("Harap isi kolom yang masih kosong");
            return false;
        }
        return true;
    }

    private Observable<GAgent> updateProfileRequest(String name, String mobile, String email) {
        return cInterface.getService().updateProfile(name, mobile, email).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }
}
