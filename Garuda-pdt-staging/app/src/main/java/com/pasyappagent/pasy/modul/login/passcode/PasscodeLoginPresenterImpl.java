package com.pasyappagent.pasy.modul.login.passcode;

import com.pasyappagent.pasy.component.network.ResponeError;
import com.pasyappagent.pasy.component.network.gson.GAgent;
import com.pasyappagent.pasy.component.network.response.AgentResponse;
import com.pasyappagent.pasy.component.util.PreferenceManager;
import com.pasyappagent.pasy.modul.CommonInterface;
import com.pasyappagent.pasy.modul.register.passcode.PasscodeInteractor;
import com.pasyappagent.pasy.modul.register.passcode.PasscodeInteractorImpl;

import rx.Subscriber;

/**
 * Created by Dhimas on 11/24/17.
 */

public class PasscodeLoginPresenterImpl implements PasscodeLoginPresenter {
    private CommonInterface cInterface;
    private PasscodeLoginView mView;
    private PasscodeLoginInteractor mInteractor;

    public PasscodeLoginPresenterImpl(CommonInterface cInterface, PasscodeLoginView view) {
        mView = view;
        this.cInterface = cInterface;
        mInteractor = new PasscodeLoginInteractorImpl(this.cInterface.getService());
    }

    @Override
    public void login(String mobile, String passcode, String imei) {
        cInterface.showProgressLoading();
        mInteractor.login(mobile, passcode, imei).subscribe(new Subscriber<AgentResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                cInterface.hideProgresLoading();
                cInterface.onFailureRequest(ResponeError.getErrorMessage(e));
            }

            @Override
            public void onNext(AgentResponse agentResponse) {
                cInterface.hideProgresLoading();
                GAgent agent = agentResponse.customers;
                PreferenceManager.logIn(agent.access_token, agent.name, agent.mobile);
                PreferenceManager.setAgent(agent);
                mView.onSuccessRequest();
            }
        });
    }
}
