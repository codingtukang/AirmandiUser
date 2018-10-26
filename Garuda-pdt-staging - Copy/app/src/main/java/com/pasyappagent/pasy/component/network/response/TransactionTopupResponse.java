package com.pasyappagent.pasy.component.network.response;

import android.text.TextUtils;

import org.parceler.Parcel;

/**
 * Created by Dhimas on 11/28/17.
 */

@Parcel
public class TransactionTopupResponse {
    public String topupSaldo;
    public String orderId;
    public String bankName;
    public String bankAccount;
    public String bankLogo;
    public String bankId;
    public String accountId;
    public String time;
    public String date;
    public String notes;
    public boolean isSuccess;
    public String info;
    public String expiredAt;
    public String createAt;
    public boolean isTopupTransaction;
    public boolean isFromHome;
    public String status;
    public boolean isFail;

    public String getNotes() {
        if (TextUtils.isEmpty(notes)) {
            return "";
        }
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public boolean isFail() {
        return isFail;
    }

    public void setFail(boolean fail) {
        isFail = fail;
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public String getStatus() {
        if (status == null) {
            return "";
        }
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getExpiredAt() {
        return expiredAt;
    }

    public void setExpiredAt(String expiredAt) {
        this.expiredAt = expiredAt;
    }

    public boolean isFromHome() {
        return isFromHome;
    }

    public void setFromHome(boolean fromHome) {
        isFromHome = fromHome;
    }

    public boolean isTopupTransaction() {
        return isTopupTransaction;
    }

    public void setTopupTransaction(boolean topupTransaction) {
        isTopupTransaction = topupTransaction;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getTopupSaldo() {
        if (topupSaldo == null) {
            return "0";
        }
        return topupSaldo;
    }

    public void setTopupSaldo(String topupSaldo) {
        this.topupSaldo = topupSaldo;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getBankLogo() {
        return bankLogo;
    }

    public void setBankLogo(String bankLogo) {
        this.bankLogo = bankLogo;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
