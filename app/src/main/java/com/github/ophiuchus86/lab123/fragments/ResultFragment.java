package com.github.ophiuchus86.lab123.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.github.ophiuchus86.lab123.R;
import com.github.ophiuchus86.lab123.models.Deposit;
import com.github.ophiuchus86.lab123.services.CalculationService;
import com.github.ophiuchus86.lab123.services.CalculationSubscriber;
import org.w3c.dom.Text;

public class ResultFragment extends BaseFragment implements CalculationSubscriber {
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

    public static ResultFragment newInstance() {
        Bundle args = new Bundle();
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
        view.findViewById(R.id.okButton).setOnClickListener(v -> {
            getAppContract().toMenuScreen();
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        CalculationService.unregister(this);
    }

    @Override
    public void onCalculationCompleted(Deposit deposit, float[] values) {
        Log.d("ResultFragment", "onCalculation triggered!");
        M = deposit.getIncome();
        p = deposit.getPercent();
        currency = deposit.getCurrency();
        Cstart = Deposit.getCurrencyPriceStart(currency);
        Cend = Deposit.getCurrencyPriceEnd(currency);
        Sy = (int)values[0];
        Sc = values[1];
        W = values[2];
        Sh = values[3];
        Sl = values[4];
        H = values[5];
        R_ = values[6];
        setTextResults();
    }

    private void setTextResults(){
        getActivity().runOnUiThread(() -> {
            View view = getView();
            if(view == null)
                return;
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
        });
    }

}
