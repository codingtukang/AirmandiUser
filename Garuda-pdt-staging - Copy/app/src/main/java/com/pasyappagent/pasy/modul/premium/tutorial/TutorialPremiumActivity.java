package com.pasyappagent.pasy.modul.premium.tutorial;

import android.content.Intent;
import android.widget.Button;
import android.widget.ScrollView;

import com.jakewharton.rxbinding.view.RxView;
import com.pasyappagent.pasy.BaseActivity;
import com.pasyappagent.pasy.R;
import com.pasyappagent.pasy.component.util.PreferenceManager;
import com.pasyappagent.pasy.modul.premium.agent.AgentPremiumActivity;
import com.pasyappagent.pasy.modul.premium.register.RegisterPremiumAgentActivity;

import rx.functions.Action1;

/**
 * Created by Dhimas on 12/25/17.
 */

public class TutorialPremiumActivity extends BaseActivity {
    private Button next;
    private ScrollView scrollView;
    public static String PREMIUM_USER = "premiumUser";

    @Override
    protected int getLayoutResourceId() {
//        if (!PreferenceManager.getStatusAkupay()) {
//            return R.layout.tutorial_premium_agent_layout;
//        }
        return R.layout.tutorial_premium_layout;
    }

    @Override
    protected void setContentViewOnChild() {
        setToolbarTitle("AGEN PREMIUM");
        next = (Button) findViewById(R.id.next_btn);
        scrollView = (ScrollView) findViewById(R.id.scrollview_tutorial);
        scrollView.setVerticalScrollBarEnabled(false);
        final boolean isPremiumUser = getIntent().getBooleanExtra(PREMIUM_USER, false);
        RxView.clicks(next).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (isPremiumUser) {
                    startActivity(new Intent(TutorialPremiumActivity.this, AgentPremiumActivity.class));
                } else {
                    startActivity(new Intent(TutorialPremiumActivity.this, RegisterPremiumAgentActivity.class));
                }
            }
        });
    }

    @Override
    protected void onCreateAtChild() {

    }

    @Override
    protected void onBackBtnPressed() {
        onBackPressed();
    }

    @Override
    protected void onSubmitBtnPressed() {

    }
}
