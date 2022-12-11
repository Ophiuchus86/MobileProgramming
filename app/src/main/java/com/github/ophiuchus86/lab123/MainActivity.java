package com.github.ophiuchus86.lab123;

import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import com.github.ophiuchus86.lab123.contract.AppContract;
import com.github.ophiuchus86.lab123.fragments.CurrencyFragment;
import com.github.ophiuchus86.lab123.fragments.InputFragment;
import com.github.ophiuchus86.lab123.fragments.MenuFragment;
import com.github.ophiuchus86.lab123.fragments.ResultFragment;
import com.github.ophiuchus86.lab123.models.Deposit;
import com.github.ophiuchus86.lab123.services.CalculationService;

import java.util.UUID;

public class MainActivity extends AppCompatActivity implements AppContract {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadCurrencyPrices();
        if (savedInstanceState == null)
            toMenuScreen();
    }

    private void launchFragment(@Nullable Fragment target, Fragment fragment) {
        if (target != null) {
            fragment.setTargetFragment(target, 0);
        }
        String tag = UUID.randomUUID().toString();
        getSupportFragmentManager().beginTransaction()
                .addToBackStack(null)
                .replace(R.id.fragmentContainer, fragment, tag)
                .commit();
    }

    @Override
    public void toInputScreen(Fragment target) {
        launchFragment(target, InputFragment.newInstance());
    }

    @Override
    public void toCurrencyScreen(Fragment target, Deposit deposit) {
        launchFragment(target, CurrencyFragment.newInstance(deposit));
    }

    @Override
    public void toResultScreen(Fragment target, Deposit deposit) {
        ResultFragment resultFragment = ResultFragment.newInstance();
        CalculationService.register(resultFragment);
        launchFragment(target, resultFragment);

        Intent intent = new Intent(this, CalculationService.class);
        intent.setAction(CalculationService.ACTION_CALCULATE);
        intent.putExtra(Deposit.ARG_DEPOSIT, deposit);
        startService(intent);
    }

    @Override
    public void toMenuScreen(){
        launchFragment(null, new MenuFragment());
    }

    @Override
    public void cancel() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count <= 1) {
            finish();
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }

    private void loadCurrencyPrices(){
        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        Deposit.DOLLAR_PRICE_START = prefs.getFloat("DOLLAR_PRICE_START", 0f);
        Deposit.DOLLAR_PRICE_END = prefs.getFloat("DOLLAR_PRICE_END", 0f);
        Deposit.EURO_PRICE_START = prefs.getFloat("EURO_PRICE_START", 0f);
        Deposit.EURO_PRICE_END = prefs.getFloat("EURO_PRICE_END", 0f);
    }

}