package com.pasyappagent.pasy.modul.chat;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import com.pasyappagent.pasy.component.network.NetworkService;
import com.pasyappagent.pasy.component.network.ResponeError;
import com.pasyappagent.pasy.component.network.gson.GLogo;
import com.pasyappagent.pasy.modul.CommonInterface;

import java.io.File;
import java.util.List;

import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Dhimas on 12/22/17.
 */

public class ChatPresenterImpl implements ChatPresenter {
    private ChatView mView;
    private CommonInterface cInterface;
    private NetworkService mService;
    private ChatInteractor mInteractor;

    public ChatPresenterImpl(CommonInterface commonInterface, ChatView view) {
        mView = view;
        cInterface = commonInterface;
        mService = cInterface.getService();
        mInteractor = new ChatInteractorImpl(mService);
    }

    @Override
    public void uploadChat(Context context, String path) {
        cInterface.showProgressLoading();
        mInteractor.uploadImage(context, path).subscribe(new Subscriber<List<GLogo>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                cInterface.hideProgresLoading();
                cInterface.onFailureRequest(ResponeError.getErrorMessage(e));
            }

            @Override
            public void onNext(List<GLogo> gLogos) {
                cInterface.hideProgresLoading();
                mView.onSuccessUploadChat(gLogos.get(0));
            }
        });
//        uploadChatRequest(context, path).subscribe(new Subscriber<List<GLogo>>() {
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                cInterface.hideProgresLoading();
//                cInterface.onFailureRequest(ResponeError.getErrorMessage(e));
//            }
//
//            @Override
//            public void onNext(List<GLogo> gLogos) {
//                cInterface.hideProgresLoading();
//                mView.onSuccessUploadChat(gLogos.get(0));
//            }
//        });
    }

//    private Observable<List<GLogo>> uploadChatRequest(final Context context, String source) {
//        File file = null;
//        try {
//            file = new Compressor.Builder(context)
//                    .setMaxWidth(1024)
//                    .setMaxHeight(1024)
//                    .setQuality(65)
//                    .setCompressFormat(Bitmap.CompressFormat.JPEG)
//                    .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath())
//                    .build()
//                    .compressToFile(new File(source));
//        } catch (NullPointerException | IllegalArgumentException e) {
//            file = new File(source);
//        }
//
//        if (file == null) return Observable.just(null);
//
//        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
//        MultipartBody.Part fbody = MultipartBody.Part.createFormData("file[]", file.getName(), requestFile);
//        RequestBody ref = RequestBody.create(MediaType.parse("text/plain"), "agents");
//        RequestBody type = RequestBody.create(MediaType.parse("text/plain"), "chat");

//        FormBody.Builder formBuilder = new FormBody.Builder()
//                .addEncoded("ref","agents")
//                .addEncoded("type","chat")
//                .addEncoded("file[]",source);
//
//        RequestBody formBody = formBuilder.build();

//        return mService.uploadChat(fbody).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .unsubscribeOn(Schedulers.io());
//    }


}
