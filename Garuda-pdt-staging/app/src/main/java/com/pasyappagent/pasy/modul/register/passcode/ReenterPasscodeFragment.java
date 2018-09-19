package com.pasyappagent.pasy.modul.register.passcode;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.andrognito.pinlockview.IndicatorDots;
import com.andrognito.pinlockview.PinLockListener;
import com.andrognito.pinlockview.PinLockView;
import com.pasyappagent.pasy.R;
import com.pasyappagent.pasy.component.util.PreferenceManager;

/**
 * Created by Dhimas on 11/23/17.
 */

public class ReenterPasscodeFragment extends Fragment {
    private OnCompleteReenterCode onReenterCode;
    public static final String PASSCODE = "passcode";
    private String tempCode;
    private ImageView reenterImg;

    public ReenterPasscodeFragment newInstance(String beforeCode, boolean isForgot) {
        ReenterPasscodeFragment fragment = new ReenterPasscodeFragment();
        Bundle bundle = new Bundle();
        bundle.putString(PASSCODE, beforeCode);
        bundle.putBoolean("is_forgot", isForgot);
        fragment.setArguments(bundle);
        return fragment;
    }

    public void setReenterCodeListener(OnCompleteReenterCode onCompleteReenterCode) {
        onReenterCode = onCompleteReenterCode;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.passcode_layout, container, false);
        tempCode = getArguments().getString(PASSCODE);
        reenterImg = (ImageView) view.findViewById(R.id.image_passcode);

//        if (!PreferenceManager.getStatusAkupay()) {
//            reenterImg.setImageDrawable(getResources().getDrawable(R.drawable.pasy_agent));
//        }

        initComponent(view);
        initEvent();
        return view;
    }

    private void initComponent(View view) {
        TextView greetingTxt = (TextView) view.findViewById(R.id.greeting_text);
        boolean isForgot = getArguments().getBoolean("is_forgot", false);
        if (isForgot) {
            greetingTxt.setText("Konfirmasi ulang PIN Baru Anda");
        } else {
            greetingTxt.setText("KONFIRMASI PIN PENGAMAN");
        }
        PinLockView mPinLockView = (PinLockView) view.findViewById(R.id.pin_lock_view);
        mPinLockView.setPinLockListener(mPinLockListener);

        IndicatorDots mIndicatorDots = (IndicatorDots) view.findViewById(R.id.indicator_dots);
        mPinLockView.attachIndicatorDots(mIndicatorDots);
    }

    private PinLockListener mPinLockListener = new PinLockListener() {
        @Override
        public void onComplete(String pin) {
            if (onReenterCode != null) {
                if (tempCode.equalsIgnoreCase(pin)) {
                    onReenterCode.onMatchCode(pin);
                } else {
                    onReenterCode.onNotMatchCode();
                }
            }
        }

        @Override
        public void onEmpty() {

        }

        @Override
        public void onPinChange(int pinLength, String intermediatePin) {

        }
    };

    private void initEvent() {

    }

    public interface OnCompleteReenterCode{
        void onMatchCode(String code);

        void onNotMatchCode();
    }
}
