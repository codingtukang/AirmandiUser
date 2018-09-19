package com.pasyappagent.pasy;

import android.app.Application;
import android.support.multidex.MultiDexApplication;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.midtrans.sdk.corekit.core.SdkCoreFlowBuilder;
import com.pasyappagent.pasy.component.util.Constant;
import com.pasyappagent.pasy.component.util.PreferenceManager;
//import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;
import java.net.URISyntaxException;

//import io.fabric.sdk.android.Fabric;
import io.socket.client.IO;
import io.socket.client.Socket;

import static com.pasyappagent.pasy.BuildConfig.BASE_URL;
import static com.pasyappagent.pasy.BuildConfig.CLIENT_KEY;

/**
 * Created by Dhimas on 9/19/17.
 */

public class PasyAgentApplication extends MultiDexApplication {
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        initPrefrences();
        SdkCoreFlowBuilder.init(this, CLIENT_KEY, BASE_URL)
                .enableLog(true)
                .buildSDK();
        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
    }

    private void initPrefrences() {
        new PreferenceManager(this);
    }

    private Socket mSocket;
    {
        try {
            mSocket = IO.socket("https://livechat.pampasy.id");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public Socket getSocket() {
        return mSocket;
    }

    public FirebaseAnalytics getFirebaseAnalytics(){
        return mFirebaseAnalytics;
    }
}
