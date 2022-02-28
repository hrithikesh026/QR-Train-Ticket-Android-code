package com.ticket;

public interface RechargeSuccessListener {
    void onSuccess(String jsonString);
    void onFailed();
}
