package com.pasyappagent.pasy.modul.termcondition;

import com.pasyappagent.pasy.BaseActivity;
import com.pasyappagent.pasy.R;

/**
 * Created by Dhimas on 12/29/17.
 */

public class TermConditionActivity extends BaseActivity {
    @Override
    protected int getLayoutResourceId() {
        return R.layout.termcondition_activity;
    }

    @Override
    protected void setContentViewOnChild() {
        setToolbarTitle(getResources().getString(R.string.syarat_ketentuan));
    }

    @Override
    protected void onCreateAtChild() {

    }

    @Override
    protected void onBackBtnPressed() {
        onBackPressed();
    }
}
