package com.github.ophiuchus86.lab123.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import androidx.annotation.Nullable;
import com.github.ophiuchus86.lab123.BuildConfig;
import com.github.ophiuchus86.lab123.models.Deposit;

import java.util.ArrayList;

public class CalculationService extends IntentService {
    public static final String TAG = CalculationService.class.getSimpleName();
    public static final String ACTION_CALCULATE = BuildConfig.APPLICATION_ID + ".ACTION_CALCULATE";
    private static ArrayList<CalculationSubscriber> subscribers = new ArrayList<>();
    /**
     * Must have an empty constructor
     */
    public CalculationService() {
        super(CalculationService.class.getSimpleName());
    }

    public static void register(CalculationSubscriber subscriber){
        Log.d(TAG, "Registered!");
        subscribers.add(subscriber);
    }

    public static void unregister(CalculationSubscriber subscriber){
        Log.d(TAG, "Unregistered!");
        subscribers.remove(subscriber);
    }

    void sendToSubscribers(Deposit deposit, float[] values){
        for (CalculationSubscriber subscriber: subscribers) {
            subscriber.onCalculationCompleted(deposit, values);
        }
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d(TAG, "Entered onHandleIntent");
        if (intent == null) return;
        String action = intent.getAction();
        if (action == null) return;

        Deposit deposit = intent.getParcelableExtra(Deposit.ARG_DEPOSIT);
        try {
            float[] values = handleAction(deposit);
            sendToSubscribers(deposit, values);
        } catch (InterruptedException e) {
            Log.e(TAG, "Job has been interrupted");
        }
    }
    private float[] handleAction(Deposit deposit) throws InterruptedException {
        Log.d(TAG, "Entered handleAction");

        int M = deposit.getIncome();  // місячний дохід
        float p = deposit.getPercent() / 100;  // процент доходу в депозит
        String currency = deposit.getCurrency();
        float Cstart = Deposit.getCurrencyPriceStart(currency);
        float Cend = Deposit.getCurrencyPriceEnd(currency);

        float Sy = M * 12;  // Річний дохід
        float Sc = Sy * p;  // Витрачено на обмін валюти за рік
        float W = 0;  // Кількість валюти придбаної за рік
        for(int i = 1; i <= 12; i++){
            float Ci = Cstart + i * (Cend - Cstart) / 12;
            W += p * M / Ci;
        }
        float Sh = W * Cend;  // Гривнева вартість придбаної валюти на кінець року
        float Sl = Sy - Sc;  // Гривневий залишок
        float H = Sh + Sl;  // Сума гривневого залишку та гривневої вартості валюти на кінець року
        float R_ = H - Sy;  // Сума заощадження

        return new float[]{Sy, Sc, W, Sh, Sl, H, R_};
    }
}

