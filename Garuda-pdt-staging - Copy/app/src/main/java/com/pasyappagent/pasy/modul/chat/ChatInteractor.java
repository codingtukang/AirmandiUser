package com.pasyappagent.pasy.modul.chat;

import android.content.Context;

import com.pasyappagent.pasy.component.network.gson.GLogo;

import java.util.List;

import rx.Observable;

/**
 * Created by Dhimas on 1/3/18.
 */

public interface ChatInteractor {
    Observable<List<GLogo>> uploadImage(Context context, String source);
}
