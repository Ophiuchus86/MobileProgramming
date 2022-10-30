package com.github.ophiuchus86.lab123.contract;

import androidx.fragment.app.Fragment;
import com.github.ophiuchus86.lab123.models.Deposit;

public interface AppContract {
    void toInputScreen(Fragment target);
    void toCurrencyScreen(Fragment target, Deposit deposit);
    void toResultScreen(Fragment target, Deposit deposit);
    void toMenuScreen();
    //Exit from the current screen
    void cancel();

}
