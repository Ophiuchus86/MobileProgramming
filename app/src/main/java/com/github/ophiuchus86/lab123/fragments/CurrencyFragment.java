package com.github.ophiuchus86.lab123.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.github.ophiuchus86.lab123.R;
import com.github.ophiuchus86.lab123.models.Deposit;

public class CurrencyFragment extends BaseFragment{
    private static final String KEY_CURRENCY = "CURRENCY";
    private Spinner currencySpinner;
    public static CurrencyFragment newInstance(Deposit deposit) {
        Bundle args = new Bundle();
        args.putParcelable(Deposit.ARG_DEPOSIT, deposit);
        CurrencyFragment fragment = new CurrencyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_currency, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Deposit deposit = getArguments().getParcelable(Deposit.ARG_DEPOSIT);
        currencySpinner = view.findViewById(R.id.currencySpinner);
        String selectedCurrency = null;

        // Відображуємо дані введені в минулому екрані
        TextView incomeTextView = view.findViewById(R.id.incomeTextView);
        incomeTextView.setText(String.valueOf(deposit.getIncome()));
        TextView percentTextView = view.findViewById(R.id.percentTextView);
        percentTextView.setText(String.valueOf(deposit.getPercent()));

        if (savedInstanceState != null) {
            // EditText fields save/restore input automatically
            // so need to restore only Spinner data
            selectedCurrency = savedInstanceState.getString(KEY_CURRENCY);
        }

        view.findViewById(R.id.calcButton).setOnClickListener(v -> {
            String currency = currencySpinner.getSelectedItem().toString();
            deposit.setCurrency(currency);
            getAppContract().toResultScreen(this, deposit);
        });

        setupCurrencySpinner(selectedCurrency);
    }

    private void setupCurrencySpinner(@Nullable String selectedGroup) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getContext(),
                R.layout.item_currency,
                Deposit.CURRENCIES
        );
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        currencySpinner.setAdapter(adapter);
        if (selectedGroup != null) {
            int index = Deposit.CURRENCIES.indexOf(selectedGroup);
            if (index != -1) currencySpinner.setSelection(index);
        }
    }

}
