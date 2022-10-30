package com.github.ophiuchus86.lab123.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.github.ophiuchus86.lab123.R;
import com.github.ophiuchus86.lab123.models.Deposit;
import org.w3c.dom.Text;

public class ResultFragment extends BaseFragment {
    private int M;  // місячний дохід
    private float p;  // процент доходу в депозит
    private String currency; // Валюта
    private float Cstart;  // курс валюти на початок року
    private float Cend;  // курс валюти на кінець року
    private int Sy;  // Річний дохід
    private float Sc;  // Витрачено на обмін валюти за рік
    private float W;  // Кількість валюти придбаної за рік
    private float Sh;  // Гривнева вартість придбаної валюти на кінець року
    private float Sl;  // Гривневий залишок
    private float H;  // Сума гривневого залишку та гривневої вартості валюти на кінець року
    private float R_;  // Сума заощадження

    public static ResultFragment newInstance(Deposit deposit) {
        Bundle args = new Bundle();
        args.putParcelable(Deposit.ARG_DEPOSIT, deposit);
        ResultFragment fragment = new ResultFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_result, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        makeCalculation();
        setTextResults(view);
        view.findViewById(R.id.okButton).setOnClickListener(v -> {
            getAppContract().toMenuScreen();
        });
    }

    private void makeCalculation(){
        Deposit deposit = getArguments().getParcelable(Deposit.ARG_DEPOSIT);
        M = deposit.getIncome();  // місячний дохід
        p = deposit.getPercent() / 100;  // процент доходу в депозит
        currency = deposit.getCurrency();
        Cstart = Deposit.getCurrencyPriceStart(currency);
        Cend = Deposit.getCurrencyPriceEnd(currency);

        Sy = M * 12;  // Річний дохід
        Sc = Sy * p;  // Витрачено на обмін валюти за рік
        W = 0;  // Кількість валюти придбаної за рік
        for(int i = 1; i <= 12; i++){
            float Ci = Cstart + i * (Cend - Cstart) / 12;
            W += p * M / Ci;
        }
        Sh = W * Cend;  // Гривнева вартість придбаної валюти на кінець року
        Sl = Sy - Sc;  // Гривневий залишок
        H = Sh + Sl;  // Сума гривневого залишку та гривневої вартості валюти на кінець року
        R_ = H - Sy;  // Сума заощадження
    }

    private void setTextResults(View view){
        ((TextView) view.findViewById(R.id.incomeTextView)).setText(String.valueOf(M));
        ((TextView) view.findViewById(R.id.percentTextView)).setText(String.format("%.2f", p * 100));
        ((TextView) view.findViewById(R.id.currencyTextView)).setText(currency);
        ((TextView) view.findViewById(R.id.currencyYearTextView)).setText(String.format("%.2f-%.2f", Cstart, Cend));
        ((TextView) view.findViewById(R.id.incomeYearTextView)).setText(String.valueOf(Sy));
        ((TextView) view.findViewById(R.id.spentTextView)).setText(String.format("%.2f", Sc));
        ((TextView) view.findViewById(R.id.currencyPurchasedTextView)).setText(String.format("%.2f", W));
        ((TextView) view.findViewById(R.id.currencyPurchasedUAHCostTextView)).setText(String.format("%.2f", Sh));
        ((TextView) view.findViewById(R.id.uahRemainsTextView)).setText(String.format("%.2f", Sl));
        ((TextView) view.findViewById(R.id.uahCostNRemainsTextView)).setText(String.format("%.2f", H));
        ((TextView) view.findViewById(R.id.savingsTextView)).setText(String.format("%.2f", R_));

    }

}
