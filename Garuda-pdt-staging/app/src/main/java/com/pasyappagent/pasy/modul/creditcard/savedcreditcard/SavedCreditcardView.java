package com.pasyappagent.pasy.modul.creditcard.savedcreditcard;

import com.pasyappagent.pasy.component.adapter.RecyCreditcardAdapter;
import com.pasyappagent.pasy.component.network.gson.GCard;

import java.util.List;

/**
 * Created by Dhimas on 2/14/18.
 */

public interface SavedCreditcardView {
    void onSuccessListCreditcard(List<GCard> cardList);

    void onSuccessDeleteCard();

}
