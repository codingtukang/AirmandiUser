package com.pasyappagent.pasy.modul.requestbalance;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.NumberFormat;
import android.net.Uri;
import android.opengl.ETC1;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Base64;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.andrognito.pinlockview.IndicatorDots;
import com.andrognito.pinlockview.PinLockListener;
import com.andrognito.pinlockview.PinLockView;
import com.jakewharton.rxbinding.view.RxView;
import com.pasyappagent.pasy.BaseActivity;
import com.pasyappagent.pasy.R;
import com.pasyappagent.pasy.component.network.NetworkManager;
import com.pasyappagent.pasy.component.network.NetworkService;
import com.pasyappagent.pasy.component.network.gson.GAgent;
import com.pasyappagent.pasy.component.util.Constant;
import com.pasyappagent.pasy.component.util.MethodUtil;
import com.pasyappagent.pasy.component.util.PreferenceManager;
import com.pasyappagent.pasy.modul.CommonInterface;
import com.pasyappagent.pasy.modul.checkpasscode.CheckPasscodeActivity;
import com.pasyappagent.pasy.modul.home.HomePageActivity;
import com.pasyappagent.pasy.modul.login.passcode.PasscodeLoginView;
import com.pasyappagent.pasy.modul.register.otp.OtpActivity;

import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import rx.functions.Action1;

/**
 * Created by Dhimas on 11/29/17.
 */

public class RequestBalanceActivity extends BaseActivity implements CommonInterface, RequestBalanceView {
    //    private EditText inputMobile;
//    private EditText inputTransfer;
    private IndicatorDots mIndicatorDots;
    private TextView inputAmount;
    //    private EditText inputNotes;
    private Button sendBtn;
    private CircleImageView avatarImageView;
    private TextView destCustNameTV;
    private String destCustId;
    private String destCustPhoneNumber;
    private String destCustName;
    private String destCustAvatar;
    private RequestBalancePresenter mPresenter;
    //    private ImageView phoneBook;
    public static int REQUEST_CODE_PICK_CONTACTS = 101;
    private Uri uriContact;
    private String contactID;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_request_balance;
    }

    @Override
    protected void setContentViewOnChild() {
        setToolbarTitle("REQUEST");
//        inputMobile = (EditText) findViewById(R.id.sender_mobile);
//        inputTransfer = (EditText) findViewById(R.id.amount_transfer);
//        inputNotes = (EditText) findViewById(R.id.notes_transfer);
        sendBtn = (Button) findViewById(R.id.send_btn);
//        phoneBook = (ImageView) findViewById(R.id.phone_book);

        PinLockView mPinLockView = (PinLockView) findViewById(R.id.pin_lock_view);
        mPinLockView.setPinLockListener(mPinLockListener);
        mIndicatorDots = (IndicatorDots) findViewById(R.id.indicator_dots);
        mPinLockView.attachIndicatorDots(mIndicatorDots);
        inputAmount = (TextView) findViewById(R.id.indicator_number);
        destCustNameTV = (TextView) findViewById(R.id.dest_cust_name_tv);
        avatarImageView = (CircleImageView) findViewById(R.id.dest_image_contact);

        inputAmount.setText("0");

        destCustId = getIntent().getStringExtra("dest_cust_id");
        destCustPhoneNumber = getIntent().getStringExtra("dest_cust_phone_number");
        destCustName = getIntent().getStringExtra("dest_cust_name");

        destCustAvatar = getIntent().getStringExtra("dest_cust_avatar");

        destCustNameTV.setText(destCustName);

        if (destCustAvatar != null && !destCustAvatar.equals("")){
            byte[] decodedString = Base64.decode(destCustAvatar, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            avatarImageView.setImageBitmap(decodedByte);
        }
        initEvent();
    }

    @Override
    protected void onCreateAtChild() {
        mPresenter = new RequestBalancePresenterImpl(this, this);
    }

    @Override
    protected void onBackBtnPressed() {
        onBackPressed();
    }

    @Override
    protected void onSubmitBtnPressed() {

    }

    private void initEvent() {
        RxView.clicks(sendBtn).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (!TextUtils.isEmpty(inputAmount.getText().toString())) {
                    Intent intent = new Intent(RequestBalanceActivity.this, CheckPasscodeActivity.class);
                    String[] user = PreferenceManager.getUser();
                    GAgent agent = PreferenceManager.getAgent();
                    String phone = "0";
                    if (user != null && user[1] != null) {
                        phone = user[1];
                    } else if (agent != null && agent.mobile != null) {
                        phone = agent.mobile;
                    }
                    intent.putExtra(OtpActivity.MOBILE, phone);
                    startActivityForResult(intent, 1);
                }
            }
        });
    }

    @Override
    public void showProgressLoading() {
        progressBar.show(this, "", false, null);
    }

    @Override
    public void hideProgresLoading() {
        progressBar.getDialog().dismiss();
    }

    @Override
    public NetworkService getService() {
        return NetworkManager.getInstance();
    }

    @Override
    public void onFailureRequest(String msg) {
        MethodUtil.showCustomToast(this, msg, R.drawable.ic_error_login);
        if (msg.equalsIgnoreCase(Constant.EXPIRED_SESSION) || msg.equalsIgnoreCase(Constant.EXPIRED_ACCESS_TOKEN)) {
            goToLoginPage1(this);
        }
    }

    @Override
    public void onSuccessRequestBalance() {
        String isBack = getIntent().getStringExtra("is_back");
        if (isBack != null){
            MethodUtil.showCustomToast(this, "Request Transfer Sukses", R.drawable.success);
            onBackBtnPressed();
        }
        else {
            MethodUtil.showCustomToast(this, "Request Transfer Sukses", R.drawable.success);
            Intent intent = new Intent(this, HomePageActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 10 && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startActivityForResult(new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI), REQUEST_CODE_PICK_CONTACTS);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            mPresenter.requestBalance(destCustId, inputAmount.getText().toString(), "");
        }

    }

//    private void retrieveContactNumber() {
//
//        String contactNumber = null;
//
//        // getting contacts ID
//        Cursor cursorID = getContentResolver().query(uriContact,
//                new String[]{ContactsContract.Contacts._ID},
//                null, null, null);
//
//        if (cursorID.moveToFirst()) {
//
//            contactID = cursorID.getString(cursorID.getColumnIndex(ContactsContract.Contacts._ID));
//        }
//
//        cursorID.close();
//
////        Log.d(TAG, "Contact ID: " + contactID);
//
//        // Using the contact ID now we will get contact phone number
//        Cursor cursorPhone = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
//                new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},
//
//                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? AND " +
//                        ContactsContract.CommonDataKinds.Phone.TYPE + " = " +
//                        ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE,
//
//                new String[]{contactID},
//                null);
//
//        if (cursorPhone.moveToFirst()) {
//            contactNumber = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//            contactNumber = contactNumber.replace("+62", "0").replace("-","").replace(" ","");
//            inputMobile.setText(contactNumber);
//        }
//
//        cursorPhone.close();
//
////        Log.d(TAG, "Contact Phone Number: " + contactNumber);
//    }

    private PinLockListener mPinLockListener = new PinLockListener() {
        @Override
        public void onComplete(String pin) {
//            mPresenter.login(mobile, pin, PreferenceManager.getImei());
        }

        @Override
        public void onEmpty() {
        }


        @SuppressLint("NewApi")
        @Override
        public void onPinChange(int pinLength, String intermediatePin) {
            inputAmount.setText(NumberFormat.getCurrencyInstance(new Locale("en", "US"))
                    .format(Double.parseDouble(intermediatePin)).replace("$","").replace(".00",""));
        }
    };
}
