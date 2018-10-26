package com.pasyappagent.pasy.modul.register;

/**
 * Created by Dhimas on 11/22/17.
 */

public interface RegisterPresenter {
    void register(String name, String mobile, String email, String refferalId);

    void sendCode(String mobile);

    void checkRefferalId(String code);
}
