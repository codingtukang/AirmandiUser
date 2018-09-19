package com.pasyappagent.pasy.modul.register.passcode;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
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
 * Created by Dhimas on 11/15/17.
 */

public class EnterPasscodeFragment extends Fragment {
    private static final String TAG = "TAG";
    private TextView greetingTxt;
    private IndicatorDots mIndicatorDots;
    private OnCompleteInput onCompleteInput;
    private ImageView passcodeImg;

    public static EnterPasscodeFragment newInstance(boolean isForgot) {
        EnterPasscodeFragment fragment = new EnterPasscodeFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("is_forgot", isForgot);
        fragment.setArguments(bundle);
        return fragment;
    }

    public void setOnCompleteListener(OnCompleteInput onComplete) {
        onCompleteInput = onComplete;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.passcode_layout, container, false);
        greetingTxt = (TextView) view.findViewById(R.id.greeting_text);
        passcodeImg = (ImageView) view.findViewById(R.id.image_passcode);
//        if (!PreferenceManager.getStatusAkupay()) {
//            passcodeImg.setImageDrawable(getResources().getDrawable(R.drawable.pasy_agent));
//        }
        boolean isForgot = getArguments().getBoolean("is_forgot", false);
        if (isForgot) {
            greetingTxt.setText("Masukkan PIN Baru Anda");
        } else {
            greetingTxt.setText("ATUR 6 DIGIT PIN PENGAMAN");
        }

        initComponent(view);
        return view;
    }

    private void initComponent(View view) {
        PinLockView mPinLockView = (PinLockView) view.findViewById(R.id.pin_lock_view);
        mPinLockView.setPinLockListener(mPinLockListener);

        mIndicatorDots = (IndicatorDots) view.findViewById(R.id.indicator_dots);
        mPinLockView.attachIndicatorDots(mIndicatorDots);
    }

    private PinLockListener mPinLockListener = new PinLockListener() {
        @Override
        public void onComplete(String pin) {
            Log.d(TAG, "Pin complete: " + pin);
            onCompleteInput.setCode(pin);
        }

        @Override
        public void onEmpty() {
            Log.d(TAG, "Pin empty");
        }

        @Override
        public void onPinChange(int pinLength, String intermediatePin) {
            Log.d(TAG, "Pin changed, new length " + pinLength + " with intermediate pin " + intermediatePin);
        }
    };

    public interface OnCompleteInput{
        void setCode(String code);
    }
}
