package com.pasyappagent.pasy;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.jakewharton.rxbinding.view.RxView;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.pasyappagent.pasy.component.dialog.CustomProgressBar;
import com.pasyappagent.pasy.component.network.NetworkManager;
import com.pasyappagent.pasy.component.network.ResponeError;
import com.pasyappagent.pasy.component.network.gson.GAgent;
import com.pasyappagent.pasy.component.util.MethodUtil;
import com.pasyappagent.pasy.component.util.PreferenceManager;
import com.pasyappagent.pasy.modul.chat.ChatActivity;
import com.pasyappagent.pasy.modul.checkpasscode.CheckPasscodeActivity;
import com.pasyappagent.pasy.modul.creditcard.savedcreditcard.SavedCreditcardActivity;
import com.pasyappagent.pasy.modul.editprofile.EditProfileActivity;
import com.pasyappagent.pasy.modul.home.HomePageActivity;
import com.pasyappagent.pasy.modul.home.transaction.AllTransactionActivity;
import com.pasyappagent.pasy.modul.login.LoginActivity;
import com.pasyappagent.pasy.modul.premium.agent.AgentPremiumActivity;
import com.pasyappagent.pasy.modul.premium.agent.PremiumActivity;
import com.pasyappagent.pasy.modul.promo.PromoActivity;
import com.pasyappagent.pasy.modul.register.otp.OtpActivity;
import com.pasyappagent.pasy.modul.sendbalance.SendBalanceActivity;

import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;
import io.socket.client.Socket;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


/**
 * Created by Dhimas on 9/19/17.
 */

public abstract class BaseActivity extends AppCompatActivity{
    protected  static  int CHANGE_PASSCODE = 11;
    protected Toolbar toolbar;
    protected ImageView backBtn;
    protected ImageView closeBtn;
    protected ImageView menuBtn;
    protected TextView toolbarTitle;
    protected LinearLayout header;
    protected ImageView imageHeader;
    protected SlidingMenu mSlidingMenu;
    protected LinearLayout chatToolbarWrapper;
    protected FrameLayout normalToolbarWrapper;
    protected CircleImageView avatarImage;
    protected TextView nameTV;
    protected TextView onlineStatusTV;
    protected Button submitButton;

    public static CustomProgressBar progressBar = new CustomProgressBar();
    protected TextView changePassode;
    private CircleImageView profile;
    private Timer timer;
    public Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        setContentView(getLayoutResourceId());
        initComponent();
        setEventBackButton();
        setContentViewOnChild();
        onCreateAtChild();
    }

    private Context getContext() {
        return context;
    }

    protected abstract int getLayoutResourceId();

    protected abstract void setContentViewOnChild();

    protected abstract void onCreateAtChild();

    protected abstract void onBackBtnPressed();

    protected abstract void onSubmitBtnPressed();

    private void initComponent() {
        header = (LinearLayout) findViewById(R.id.header);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        backBtn = (ImageView) findViewById(R.id.hometoolbar_imgBtnBack);
        closeBtn = (ImageView) findViewById(R.id.hometoolbar_imgBtnClose);
        menuBtn = (ImageView) findViewById(R.id.hometoolbar_imgBtnMenu);
        toolbarTitle = (TextView) findViewById(R.id.hometoolbar_title);
        imageHeader = (ImageView) findViewById(R.id.hometoolbar_logo);
        profile = (CircleImageView) findViewById(R.id.profile);
        submitButton = (Button) findViewById(R.id.submit_button);

        chatToolbarWrapper = (LinearLayout) findViewById(R.id.chat_toolbar_layout);
        normalToolbarWrapper = (FrameLayout) findViewById(R.id.normal_toolbar_layout);

        avatarImage = (CircleImageView) findViewById(R.id.chat_avatar);
        nameTV = (TextView) findViewById(R.id.chat_name);
        onlineStatusTV = (TextView) findViewById(R.id.chat_status);

        chatToolbarWrapper.setVisibility(View.GONE);
//        setSlidingMenu(this);

        RxView.clicks(submitButton).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                onSubmitBtnPressed();
            }
        });

        RxView.clicks(menuBtn).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
//                if (mSlidingMenu.isMenuShowing()) {
//                    mSlidingMenu.toggle();
//                } else {
//                    mSlidingMenu.showMenu(true);
//                }
                menuButtonPressed();
            }
        });



        RxView.clicks(profile).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                startActivity(new Intent(getApplicationContext(), EditProfileActivity.class));
            }
        });



    }

    protected void menuButtonPressed(){

    }

    protected void setSlidingMenu(Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        mSlidingMenu = new SlidingMenu(context);
        mSlidingMenu.setMode(SlidingMenu.LEFT);
        mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        mSlidingMenu.setShadowWidthRes(R.dimen.d2);
        mSlidingMenu.setFadeDegree(0.0f);
        mSlidingMenu.setBehindWidth(metrics.widthPixels / 3 + (metrics.widthPixels / 2));
        mSlidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        mSlidingMenu.setMenu(R.layout.hamburger);
        mSlidingMenu.setShadowDrawable(R.drawable.sidemenu_shadow);

        ImageView close = (ImageView) mSlidingMenu.findViewById(R.id.close_menu);
        TextView logout = (TextView) mSlidingMenu.findViewById(R.id.logout);
        TextView menu = (TextView)  mSlidingMenu.findViewById(R.id.menu);
        TextView editProfile = (TextView)  mSlidingMenu.findViewById(R.id.edit_profile);
        TextView premiumMember = (TextView)  mSlidingMenu.findViewById(R.id.premium_member);
        TextView sendAmount = (TextView)  mSlidingMenu.findViewById(R.id.send_amount);
        TextView transactionHistory = (TextView)  mSlidingMenu.findViewById(R.id.transaction_history);
        TextView customerService = (TextView)  mSlidingMenu.findViewById(R.id.customer_service);
        TextView saveCard = (TextView)  mSlidingMenu.findViewById(R.id.save_card);
        changePassode = (TextView) mSlidingMenu.findViewById(R.id.ubah_pin);

        RxView.clicks(close).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                mSlidingMenu.toggle();
            }
        });

        RxView.clicks(saveCard).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                mSlidingMenu.toggle();
                startActivity(new Intent(getApplicationContext(), SavedCreditcardActivity.class));
            }
        });

        RxView.clicks(logout).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
//                PreferenceManager.logOut();
//                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
//                finish();
                logoutAction(false);
//                goToLoginPage();
            }
        });

        RxView.clicks(menu).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                mSlidingMenu.toggle();
                Intent intent = new Intent(getApplicationContext(), HomePageActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        RxView.clicks(premiumMember).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                mSlidingMenu.toggle();
                startActivity(new Intent(getApplicationContext(), PremiumActivity.class));
            }
        });

        RxView.clicks(editProfile).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                mSlidingMenu.toggle();
                startActivity(new Intent(getApplicationContext(), EditProfileActivity.class));
            }
        });

        RxView.clicks(sendAmount).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                mSlidingMenu.toggle();
                startActivity(new Intent(getApplicationContext(), SendBalanceActivity.class));
            }
        });

        RxView.clicks(customerService).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                mSlidingMenu.toggle();
                PasyAgentApplication app = (PasyAgentApplication) getApplication();
                Socket mSocket = app.getSocket();
                GAgent agent = PreferenceManager.getAgent();
                mSocket.emit("join room", agent.id);
                Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                String[] user = PreferenceManager.getUser();
                intent.putExtra("username", user[0]);
                intent.putExtra("numUsers", 2);
                startActivity(intent);

            }
        });

        RxView.clicks(transactionHistory).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                mSlidingMenu.toggle();
                startActivity(new Intent(getApplicationContext(), AllTransactionActivity.class));
            }
        });

    }

    protected void hideProfile() {
        profile.setVisibility(View.GONE);
    }

    protected void setToolbarTitle(String title) {
        backBtn.setVisibility(View.VISIBLE);
        closeBtn.setVisibility(View.GONE);
        menuBtn.setVisibility(View.GONE);
        toolbarTitle.setVisibility(View.VISIBLE);
        toolbarTitle.setText(title);
        toolbarTitle.setTextColor(getResources().getColor(R.color.black));
        imageHeader.setVisibility(View.GONE);
        profile.setVisibility(View.GONE);
        header.setBackgroundColor(getResources().getColor(R.color.white));
    }

    protected void setToolbarTitleWithSubmitButton(String title) {
        backBtn.setVisibility(View.VISIBLE);
        closeBtn.setVisibility(View.GONE);
        menuBtn.setVisibility(View.GONE);
        toolbarTitle.setVisibility(View.VISIBLE);
        toolbarTitle.setText(title);
        toolbarTitle.setTextColor(getResources().getColor(R.color.black));
        imageHeader.setVisibility(View.GONE);
        profile.setVisibility(View.GONE);
        submitButton.setVisibility(View.VISIBLE);
        header.setBackgroundColor(getResources().getColor(R.color.white));
    }

    protected void setChatToolbar(String avatar, String name, String onlineStatus) {
        backBtn.setVisibility(View.VISIBLE);
        closeBtn.setVisibility(View.GONE);
        menuBtn.setVisibility(View.GONE);
        imageHeader.setVisibility(View.GONE);
        profile.setVisibility(View.GONE);
        nameTV.setText(name);
        onlineStatusTV.setText(onlineStatus);
        header.setBackgroundColor(getResources().getColor(R.color.white));
        normalToolbarWrapper.setVisibility(View.GONE);
        chatToolbarWrapper.setVisibility(View.VISIBLE);

        if (avatarImage != null && avatar != null && !avatar.equals("")){
            byte[] decodedString = Base64.decode(avatar, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            avatarImage.setImageBitmap(decodedByte);
        }
    }

    protected void setToolbarTitleWithCloseButton(String title) {
        backBtn.setVisibility(View.GONE);
        closeBtn.setVisibility(View.VISIBLE);
        menuBtn.setVisibility(View.GONE);
        toolbarTitle.setVisibility(View.VISIBLE);
        toolbarTitle.setText(title);
        toolbarTitle.setTextColor(getResources().getColor(R.color.black));
        imageHeader.setVisibility(View.GONE);
        profile.setVisibility(View.GONE);
        header.setBackgroundColor(getResources().getColor(R.color.white));
    }


    protected void setToolbarLogo() {
        backBtn.setVisibility(View.GONE);
        closeBtn.setVisibility(View.GONE);
        toolbarTitle.setVisibility(View.GONE);
        imageHeader.setVisibility(View.VISIBLE);
        header.setBackgroundColor(getResources().getColor(R.color.white));
    }

    private void setEventBackButton() {
        RxView.clicks(backBtn).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                onBackBtnPressed();
            }
        });
        RxView.clicks(closeBtn).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                onBackBtnPressed();
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (progressBar != null && progressBar.getDialog() != null) {
            progressBar.getDialog().dismiss();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        GAgent agent = PreferenceManager.getAgent();
        if (profile != null && agent.avatar_base64 != null && !agent.avatar_base64.equals("")){
            byte[] decodedString = Base64.decode(agent.avatar_base64, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            profile.setImageBitmap(decodedByte);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void logoutAction(final boolean isFromBackground) {
        if (!isFromBackground) {
            progressBar.show(this, "", false, null);
        }
        logout().subscribe(new Subscriber<JsonObject>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (!isFromBackground) {
                    progressBar.getDialog().dismiss();
                }
                showError(ResponeError.getErrorMessage(e));
            }

            @Override
            public void onNext(JsonObject jsonObject) {
                if (!isFromBackground) {
                    progressBar.getDialog().dismiss();
                }
                if (PreferenceManager.isLogin()) {
                    goToLoginPage();
                }
            }
        });
    }

    private void showError(String msg) {
        MethodUtil.showCustomToast(this, msg, R.drawable.ic_error_login);
    }

    public void goToLoginPage() {
            PreferenceManager.logOut();
            Intent intent = new Intent(BaseActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();

    }

    protected void goToLoginPage1(Context context) {
        PreferenceManager.logOut();
        Intent intent = new Intent(context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();

    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    Observable<JsonObject> logout(){
        GAgent agent = PreferenceManager.getAgent();
        String userId = "";
        if (agent != null)
        userId = agent.id;
        return NetworkManager.getInstance().logout(userId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).unsubscribeOn(Schedulers.io());
    }
}
