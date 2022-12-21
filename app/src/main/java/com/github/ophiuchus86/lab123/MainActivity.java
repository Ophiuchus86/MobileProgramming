package com.github.ophiuchus86.lab123;

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

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements AppContract {
    private ExecutorService executorService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadCurrencyPrices();
        executorService = Executors.newSingleThreadExecutor();
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
        ResultFragment resultFragment = new ResultFragment();
        launchFragment(target, resultFragment);
        executorService.submit(() -> {
            calculate(deposit, resultFragment);
        });
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

    private void calculate(Deposit deposit, ResultFragment resultFragment){
        Log.d("MainActivity", "Entered calculate method");

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

        resultFragment.onCalculationCompleted(deposit, new float[]{Sy, Sc, W, Sh, Sl, H, R_});
    }

}