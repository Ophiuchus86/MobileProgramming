package com.github.ophiuchus86.lab123.services;

import com.github.ophiuchus86.lab123.models.Deposit;

public interface CalculationSubscriber {
    void onCalculationCompleted(Deposit deposit, float[] values);
}
