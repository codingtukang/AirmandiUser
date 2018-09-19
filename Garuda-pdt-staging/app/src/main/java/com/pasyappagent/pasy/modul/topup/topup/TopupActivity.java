package com.pasyappagent.pasy.modul.topup.topup;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aigestudio.wheelpicker.WheelPicker;
import com.jakewharton.rxbinding.view.RxView;
import com.pasyappagent.pasy.BaseActivity;
import com.pasyappagent.pasy.R;
import com.pasyappagent.pasy.component.network.NetworkManager;
import com.pasyappagent.pasy.component.network.NetworkService;
import com.pasyappagent.pasy.component.network.gson.GTopup;
import com.pasyappagent.pasy.component.network.gson.GVoucher;
import com.pasyappagent.pasy.component.util.Constant;
import com.pasyappagent.pasy.component.util.MethodUtil;
import com.pasyappagent.pasy.component.util.PreferenceManager;
import com.pasyappagent.pasy.modul.CommonInterface;
import com.pasyappagent.pasy.modul.creditcard.inputcreditcard.InputCreditcardActivity;
import com.pasyappagent.pasy.modul.scanqr.QuickResponse;
import com.pasyappagent.pasy.modul.topup.topupcreditcard.CreditcardTopupActivity;
import com.pasyappagent.pasy.modul.topup.topupmethod.PaymentMethodTopupActivity;
import com.pasyappagent.pasy.modul.verify.VerifyActivity;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Action1;

import static com.pasyappagent.pasy.modul.topup.topupmethod.PaymentMethodTopupActivity.QUICK_RESPONSE;

/**
 * Created by Dhimas on 9/27/17.
 */

public class TopupActivity extends BaseActivity implements TopupView, CommonInterface{
    private static final String VOUCHER_NOT_VALID = "Voucher is not valid.";
    private RelativeLayout dropdownTopup;
    private RelativeLayout containerTopup;
    private Button selectedTopupValueBtn;
    private WheelPicker wheelPicker;
    private int position;
    private List<String> listPrice;
    private TextView topupValueText;
    private EditText inputTopupValue;
    private Button nextBtn;
    private String voucherId;
    private ImageView statusLogo;
    private EditText inputVoucher;
    private TopupPresenter mPresenter;
    private RadioButton ccRadionBtn;
    private RadioButton transRadionBtn;
    private RadioButton virtualAccountRadioBtn;
    private ImageView verifyId;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.topup_layout;
    }

    @Override
    protected void setContentViewOnChild() {
        setToolbarTitle("TOPUP");
        dropdownTopup = (RelativeLayout) findViewById(R.id.container_topup_dialog);
        containerTopup = (RelativeLayout) findViewById(R.id.container_topup_value);
        topupValueText = (TextView) findViewById(R.id.value_topup_txt);
        inputTopupValue = (EditText) findViewById(R.id.input_value_topup);
        wheelPicker = (WheelPicker) findViewById(R.id.aselole);
        selectedTopupValueBtn = (Button) findViewById(R.id.select_btn);
        nextBtn = (Button) findViewById(R.id.next_btn);
        statusLogo = (ImageView) findViewById(R.id.status_voucher_logo);
        inputVoucher = (EditText) findViewById(R.id.voucher_field);
        ccRadionBtn = (RadioButton) findViewById(R.id.creditcard_radio);
        transRadionBtn = (RadioButton) findViewById(R.id.transfer_radio);
        virtualAccountRadioBtn = (RadioButton) findViewById(R.id.virtual_account);
        verifyId = (ImageView) findViewById(R.id.verify_id);


//        ccRadionBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                transRadionBtn.setChecked(false);
//            }
//        });
//
//        transRadionBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                ccRadionBtn.setChecked(false);
//            }
//        });

        listPrice = new ArrayList<>();
        listPrice.add("25.000");
        listPrice.add("50.000");
        listPrice.add("100.000");
        listPrice.add("200.000");
        listPrice.add("500.000");
        listPrice.add("1.000.000");
        listPrice.add("Masukkan Nominal");

        inputVoucher.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    if (!TextUtils.isEmpty(inputVoucher.getText().toString()) &&
                            inputVoucher.getText().toString().length() > 4) {
                        mPresenter.checkVoucher(inputVoucher.getText().toString(), getAmount());
                    }
                }
                return false;
            }
        });

        inputVoucher.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    if (!TextUtils.isEmpty(inputVoucher.getText().toString()) &&
                            inputVoucher.getText().toString().length() > 4) {
                        mPresenter.checkVoucher(inputVoucher.getText().toString(), getAmount());
                    }
                }
            }
        });

        RxView.clicks(verifyId).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                startActivity(new Intent(TopupActivity.this, VerifyActivity.class));
            }
        });

        RxView.clicks(dropdownTopup).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                showTopup();
            }
        });

        RxView.clicks(containerTopup).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                containerTopup.setVisibility(View.GONE);
            }
        });

        RxView.clicks(selectedTopupValueBtn).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                String topupValue = listPrice.get(position);
                if (position != 6) {
                    topupValueText.setText(topupValue);
                } else {
                    topupValueText.setVisibility(View.GONE);
                    inputTopupValue.setVisibility(View.VISIBLE);
                    inputTopupValue.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(inputTopupValue, InputMethodManager.SHOW_IMPLICIT);
                }
                containerTopup.setVisibility(View.GONE);
            }
        });

        RxView.clicks(inputTopupValue).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                String valueTopup = inputTopupValue.getText().toString();
                topupValueText.setText(MethodUtil.toCurrencyFormat(valueTopup));
                topupValueText.setVisibility(View.VISIBLE);
                inputTopupValue.setVisibility(View.GONE);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(inputTopupValue.getWindowToken(), 0);
            }
        });

        RxView.clicks(nextBtn).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (ccRadionBtn.isChecked()) {
                    mPresenter.topup(getAmount(), voucherId, "1");
                } else if(transRadionBtn.isChecked()) {
                    mPresenter.topup(getAmount(), voucherId, "0");
                } else if (virtualAccountRadioBtn.isChecked()) {
                    mPresenter.topup(getAmount(), voucherId, "4");
                }

            }
        });

    }

    @Override
    protected void onCreateAtChild() {
        mPresenter = new TopupPresenterImpl(this, this);
    }

    @Override
    protected void onBackBtnPressed() {
        onBackPressed();
    }

    private void showTopup() {
        containerTopup.setVisibility(View.VISIBLE);
        wheelPicker.setData(listPrice);
        wheelPicker.setIndicatorColor(ContextCompat.getColor(getApplicationContext(), R.color.azure));
        wheelPicker.setCurtainColor(ContextCompat.getColor(getApplicationContext(), R.color.blue_agent));
        wheelPicker.setVisibleItemCount(3);
        wheelPicker.setCurved(true);
        wheelPicker.setClickable(true);
        wheelPicker.setOnItemSelectedListener(new WheelPicker.OnItemSelectedListener() {
            @Override
            public void onItemSelected(WheelPicker picker, Object data, int position) {
                setPosition(position);
            }
        });
    }

    private void setPosition(int position) {
        this.position = position;
    }

    private String getAmount() {
        String amount;
        if (position != 6) {
            amount = topupValueText.getText().toString().replace(".", "");
        } else {
            amount = inputTopupValue.getText().toString().replace(".", "");
        }
        return amount;
    }

    @Override
    public void onSuccessRequest(GTopup topup) {
        Intent intent = new Intent(TopupActivity.this, PaymentMethodTopupActivity.class);
        intent.putExtra(PaymentMethodTopupActivity.AMOUNT, topup.amount);
        intent.putExtra(PaymentMethodTopupActivity.UNIQUE, topup.unique);
        intent.putExtra(PaymentMethodTopupActivity.ORDER_ID, topup.order_id);
        intent.putExtra(PaymentMethodTopupActivity.VOUCHER, topup.voucher);
        intent.putExtra(PaymentMethodTopupActivity.EXPIRED, topup.expired_at);
        intent.putExtra(InputCreditcardActivity.IS_TOPUP, true);
        intent.putExtra(PaymentMethodTopupActivity.IS_USING_VA, virtualAccountRadioBtn.isChecked());
        intent.putExtra(PaymentMethodTopupActivity.IS_USING_CC, ccRadionBtn.isChecked());
        if (ccRadionBtn.isChecked()) {
            QuickResponse quickResponse = new QuickResponse();
            quickResponse.setAmount(topup.amount);
            quickResponse.setTotalAmount(topup.payment.amount);
            int fee = Integer.parseInt(topup.payment.amount) - Integer.parseInt(topup.amount);
            quickResponse.setFee(fee+"");
            quickResponse.setMerchant_id(topup.customer.id);
            quickResponse.setPaymentId(topup.payment.id);
            quickResponse.setNotes(topup.payment.object_type);
            quickResponse.setCreateAt(topup.payment.created_at);
            intent.putExtra(QUICK_RESPONSE, Parcels.wrap(quickResponse));
        }


        startActivity(intent);

    }


    @Override
    public void onSuccessRequestVoucher(GVoucher voucher) {
        voucherId = voucher.id;
        statusLogo.setImageDrawable(getDrawable(R.drawable.voucher_accept));
        statusLogo.setVisibility(View.VISIBLE);
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
        switch (msg) {
            case VOUCHER_NOT_VALID:
                statusLogo.setImageDrawable(getDrawable(R.drawable.voucher_eject));
                MethodUtil.showCustomToast(this, msg, 0);
                statusLogo.setVisibility(View.VISIBLE);
                voucherId = "";
                break;
            case "v":
                break;
            default:
                MethodUtil.showCustomToast(this, msg, R.drawable.ic_error_login);
                if (msg.equalsIgnoreCase(Constant.EXPIRED_SESSION) || msg.equalsIgnoreCase(Constant.EXPIRED_ACCESS_TOKEN)) {
                    goToLoginPage1(this);
                }
        }
    }
}
