package com.ticket;

import com.ticket.objects.TrainDetails;

import java.util.ArrayList;

public interface TrainListSuccessEventListener {
    void onSuccess(ArrayList<TrainDetails> TrainDetailsArrayList);
    void onFailed();
}
