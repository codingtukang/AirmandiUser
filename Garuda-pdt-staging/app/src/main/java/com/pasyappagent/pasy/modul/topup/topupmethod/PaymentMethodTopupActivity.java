package com.pasyappagent.pasy.modul.topup.topupmethod;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jakewharton.rxbinding.view.RxView;
import com.pasyappagent.pasy.BaseActivity;
import com.pasyappagent.pasy.R;
import com.pasyappagent.pasy.component.adapter.RecyBanksAdapter;
import com.pasyappagent.pasy.component.network.NetworkManager;
import com.pasyappagent.pasy.component.network.NetworkService;
import com.pasyappagent.pasy.component.network.gson.GBank;
import com.pasyappagent.pasy.component.network.gson.GBanks;
import com.pasyappagent.pasy.component.network.response.TopupResponse;
import com.pasyappagent.pasy.component.network.response.TransactionTopupResponse;
import com.pasyappagent.pasy.component.util.Constant;
import com.pasyappagent.pasy.component.util.MethodUtil;
import com.pasyappagent.pasy.component.util.PreferenceManager;
import com.pasyappagent.pasy.modul.CommonInterface;
import com.pasyappagent.pasy.modul.creditcard.inputcreditcard.InputCreditcardActivity;
import com.pasyappagent.pasy.modul.scanqr.QuickResponse;
import com.pasyappagent.pasy.modul.topup.topuptransfer.TransferTopupActivity;
import com.pasyappagent.pasy.modul.topup.topupvirtualaccount.VirtualAccountActivity;

import org.parceler.Parcels;
import org.w3c.dom.Text;

import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import rx.functions.Action1;

import static com.pasyappagent.pasy.modul.creditcard.inputcreditcard.InputCreditcardActivity.CARD_TYPE;
import static com.pasyappagent.pasy.modul.creditcard.inputcreditcard.InputCreditcardActivity.MERCHANT_ID;
import static com.pasyappagent.pasy.modul.creditcard.inputcreditcard.InputCreditcardActivity.NOTE;
import static com.pasyappagent.pasy.modul.creditcard.inputcreditcard.InputCreditcardActivity.TOTAL_AMOUNT;
import static com.pasyappagent.pasy.modul.creditcard.inputcreditcard.InputCreditcardActivity.VOUCHER_ID;

/**
 * Created by Dhimas on 9/29/17.
 */

public class PaymentMethodTopupActivity extends BaseActivity implements RecyBanksAdapter.OnClickItem, PaymentMethodTopupView, CommonInterface{
    public static final String AMOUNT = "amount";
    public static final String UNIQUE = "unique";
    public static final String ORDER_ID = "orderId";
    public static final String VOUCHER = "voucher";
    public static final String EXPIRED = "expired";
    public static final String IS_USING_CC = "isUsingCC";
    public static final String IS_USING_VA = "isUsingVA";
    public static final String QUICK_RESPONSE = "quickResponse";
    public static final String PAYMENT_ID = "paymentId";


    private RecyclerView listBank;
    private RecyBanksAdapter mAdapter;
    private RelativeLayout menuBanks;
    private LinearLayout containerReview;
    private LinearLayout dropdownBankList;
    private ImageView iconPayment;
    private TextView bankPayment;
    private TextView topupText;
    private TextView infoTopup;
    private TextView orderIdText;
    private TextView amountTopup;
    private TextView uniqueTopup;
    private TextView voucherAmount;
    private TextView totalAmount;
    private PaymentMethodTopupPresenter mPresenter;
    private List<GBanks> banks;
    private Button nextBtn;
    private String bankId;
    private String accountId;
    private String amount;
    private String expired;
    private boolean isCC;
    private boolean isVA;
    private TextView fee;
    private LinearLayout containerCC;
    private String paymentId;
    private QuickResponse quickResponse;
    private String voucher = "0";

    @Override
    protected int getLayoutResourceId() {
        return R.layout.method_payment_topup;
    }

    @Override
    protected void setContentViewOnChild() {
        setToolbarTitle("METODE PEMBAYARAN");
        nextBtn = (Button) findViewById(R.id.next_btn);
        listBank = (RecyclerView) findViewById(R.id.list_bank);
        mAdapter = new RecyBanksAdapter(this);
        listBank.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        listBank.setAdapter(mAdapter);
        menuBanks = (RelativeLayout) findViewById(R.id.menu_banks);
        containerReview = (LinearLayout) findViewById(R.id.container_review);
        dropdownBankList = (LinearLayout) findViewById(R.id.dropdown_bank_list);
        iconPayment = (ImageView) findViewById(R.id.icon_payment);
        bankPayment = (TextView) findViewById(R.id.bank_payment);
        topupText = (TextView) findViewById(R.id.topup_saldo);
        infoTopup = (TextView) findViewById(R.id.info_saldo);
        orderIdText = (TextView) findViewById(R.id.order_id_text);
        amountTopup = (TextView) findViewById(R.id.amount_topup);
        uniqueTopup = (TextView) findViewById(R.id.unique_topup);
        voucherAmount = (TextView) findViewById(R.id.amount_voucher_topup);
        totalAmount = (TextView) findViewById(R.id.total_amount);
        fee = (TextView) findViewById(R.id.fee);
        containerCC = (LinearLayout) findViewById(R.id.cc_method);

        amount = getIntent().getStringExtra(AMOUNT);
        paymentId = getIntent().getStringExtra(PAYMENT_ID);
        String unique = !TextUtils.isEmpty(getIntent().getStringExtra(UNIQUE)) ? getIntent().getStringExtra(UNIQUE) : "0";
        String orderId = getIntent().getStringExtra(ORDER_ID);
        voucher = getIntent().getStringExtra(VOUCHER);
        expired = getIntent().getStringExtra(EXPIRED);
        isVA = getIntent().getBooleanExtra(IS_USING_VA, false);
        isCC = getIntent().getBooleanExtra(IS_USING_CC, false);
        if (isCC) {
            containerReview.setVisibility(View.VISIBLE);
            dropdownBankList.setVisibility(View.GONE);
            containerCC.setVisibility(View.VISIBLE);
        }

        if (isVA) {
            containerReview.setVisibility(View.VISIBLE);
            dropdownBankList.setVisibility(View.GONE);
            containerCC.setVisibility(View.GONE);
        }

        String topupText = "Rp " + MethodUtil.toCurrencyFormat(amount.replace(".", ""));
        String[] user = PreferenceManager.getUser();

        orderIdText.setText(orderId);
        amountTopup.setText(MethodUtil.toCurrencyFormat(amount));
        uniqueTopup.setText(MethodUtil.toCurrencyFormat(unique));
        String userSaldo = user != null && user[1] != null ? user[1] : "";
        infoTopup.setText(String.format(getResources().getString(R.string.pengisian_saldo), userSaldo));
        this.topupText.setText(topupText);
        if (!TextUtils.isEmpty(voucher)) {
            voucherAmount.setText("- " + MethodUtil.toCurrencyFormat(voucher));
        } else {
            voucherAmount.setText("0");
            voucher = "0";
        }
        int total;
        if (isCC) {
            quickResponse = Parcels.unwrap(getIntent().getParcelableExtra(QUICK_RESPONSE));
            quickResponse.setOrderId(orderId);
            total = Integer.parseInt(quickResponse.getTotalAmount());
            fee.setText(quickResponse.getFee());
        } else {
            amount = !TextUtils.isEmpty(amount) ? amount : "0";
            unique = !TextUtils.isEmpty(unique) ? unique : "0";
            voucher = !TextUtils.isEmpty(voucher) ? voucher : "0";
            total = Integer.parseInt(amount) + Integer.parseInt(unique) - Integer.parseInt(voucher);
        }

//        int total = Integer.parseInt(amount) - Integer.parseInt(voucher);
        totalAmount.setText(MethodUtil.toCurrencyFormat(total + ""));
        initEvent();
    }

    @Override
    protected void onCreateAtChild() {
        mPresenter = new PaymentMethodTopupPresenterImpl(this, this);
        mPresenter.requestBanks();
    }

    @Override
    protected void onBackBtnPressed() {
        onBackPressed();
    }

    @Override
    public void onClick(int position) {
        containerReview.setVisibility(View.VISIBLE);
        dropdownBankList.setVisibility(View.GONE);
        menuBanks.setVisibility(View.VISIBLE);
        if (banks.get(position).logo != null) {
            iconPayment.setVisibility(View.VISIBLE);
            Glide.with(this).load(banks.get(position).logo.base_url + "/" + banks.get(position).logo.path)
                    .dontAnimate().into(iconPayment);
        } else {
            iconPayment.setVisibility(View.GONE);
        }
        bankPayment.setText(banks.get(position).name);
        bankId = banks.get(position).id;
        if (banks.get(position).accounts != null) {
            accountId = banks.get(position).accounts.id;
        }
    }

    private void initEvent() {
        RxView.clicks(nextBtn).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (isCC) {
                    Intent intentCC = new Intent(PaymentMethodTopupActivity.this, InputCreditcardActivity.class);
                    intentCC.putExtra(TOTAL_AMOUNT, amount);
                    intentCC.putExtra(VOUCHER_ID, "");
                    intentCC.putExtra(MERCHANT_ID, "");
                    intentCC.putExtra(CARD_TYPE, "credit");
                    intentCC.putExtra(NOTE, "topup " + orderIdText.getText().toString());
                    intentCC.putExtra(PAYMENT_ID, paymentId);
                    intentCC.putExtra(InputCreditcardActivity.IS_TOPUP, true);
                    intentCC.putExtra(QUICK_RESPONSE, Parcels.wrap(quickResponse));
                    startActivity(intentCC);
                } else if (isVA) {
                    mPresenter.confirmPayment(orderIdText.getText().toString(), bankId, accountId);
                } else {
                    if (TextUtils.isEmpty(bankId)) {
                        MethodUtil.showCustomToast(PaymentMethodTopupActivity.this, "Harap pilih metode bayar", R.drawable.ic_error_login);
                    } else {
                        mPresenter.confirmPayment(orderIdText.getText().toString(), bankId, accountId);
                    }
                }

            }
        });

        RxView.clicks(menuBanks).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (containerReview.isShown()) {
                    containerReview.setVisibility(View.GONE);
                    dropdownBankList.setVisibility(View.VISIBLE);
                    menuBanks.setVisibility(View.GONE);
                } else {
                    containerReview.setVisibility(View.VISIBLE);
                    dropdownBankList.setVisibility(View.GONE);
                }

            }
        });
    }

    @Override
    public void onSuccessRequest(TopupResponse response) {

        TransactionTopupResponse tResponse = new TransactionTopupResponse();
        for (GBanks bank : banks) {
            if (bank.id.equalsIgnoreCase(response.bank_id)) {
                tResponse.setBankAccount(bank.accounts.account_no);
                tResponse.setBankName(bank.accounts.account_name);
                tResponse.setBankId(bankId);
                tResponse.setAccountId(accountId);
            }
        }
        tResponse.setOrderId(orderIdText.getText().toString());
        tResponse.setTopupSaldo(totalAmount.getText().toString().replace(".", ""));
        String[] dateTime = MethodUtil.formatDateAndTime(response.created_at);
        tResponse.setDate(dateTime[0]);
        tResponse.setTime(dateTime[1]);
        tResponse.setInfo("Top up saldo " + amount.replace(".", ""));
        tResponse.setExpiredAt(response.expired_at);
        tResponse.setCreateAt(response.created_at);
        if (response.status == Integer.parseInt(Constant.TOPUP_STATUS_SUCCESS)) {
            tResponse.setSuccess(true);
        }
        if (isVA) {
            Intent intent = new Intent(this, VirtualAccountActivity.class);
            intent.putExtra(VirtualAccountActivity.EXPIRED_DATE, response.expired_at);
            intent.putExtra(VirtualAccountActivity.AMOUNT_VA, totalAmount.getText().toString().replace(".",""));
            intent.putExtra(TransferTopupActivity.TOPUP_RESPONSE, Parcels.wrap(tResponse));
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, TransferTopupActivity.class);
            intent.putExtra(TransferTopupActivity.TOPUP_RESPONSE, Parcels.wrap(tResponse));
            startActivity(intent);
        }

    }

    @Override
    public void onSuccessRequestBanks(List<GBanks> listBank) {
        banks = listBank;
        mAdapter.setData(listBank);
        if (isVA && listBank != null) {
            for (GBanks bank : listBank) {
                if (!TextUtils.isEmpty(bank.va_fee)) {
                    fee.setText(MethodUtil.toCurrencyFormat(bank.va_fee));
                    bankId = bank.id;
                    accountId = bank.accounts.id;
                    voucher = !TextUtils.isEmpty(voucher) ? voucher : "0";
                    int total = Integer.parseInt(amount) + Integer.parseInt(bank.va_fee) - Integer.parseInt(voucher);
                    totalAmount.setText(MethodUtil.toCurrencyFormat(total + ""));
                }
            }
        }
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
}
