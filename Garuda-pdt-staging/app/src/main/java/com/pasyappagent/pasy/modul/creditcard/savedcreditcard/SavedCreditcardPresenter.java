package com.pasyappagent.pasy.modul.creditcard.savedcreditcard;

import com.pasyappagent.pasy.component.network.NetworkService;

/**
 * Created by Dhimas on 2/6/18.
 */

public interface SavedCreditcardPresenter {
    void getListCard();

    void showList(int countCard);

    void onSelectCard(String tokenId, String cardName, String lastDigit);

    void onDeleteCard(String cardId);

    void getSelectCreditCard();
}
