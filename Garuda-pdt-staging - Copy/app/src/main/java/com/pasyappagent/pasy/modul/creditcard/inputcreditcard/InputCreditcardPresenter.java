package com.pasyappagent.pasy.modul.creditcard.inputcreditcard;

import android.widget.EditText;

import com.pasyappagent.pasy.component.util.TextWatcherAdapter;

/**
 * Created by Dhimas on 2/2/18.
 */

public interface InputCreditcardPresenter {
    void reformatCardNumber(String current, EditText editText, TextWatcherAdapter textWatcherAdapter);
}
