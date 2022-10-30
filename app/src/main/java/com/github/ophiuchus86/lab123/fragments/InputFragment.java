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
import com.google.android.material.textfield.TextInputEditText;

public class InputFragment extends BaseFragment{
    public static InputFragment newInstance() {
        return new InputFragment();
    }

    @Override
    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_input, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.nextButton).setOnClickListener(v -> {
            String incomeStr = ((TextView)view.findViewById(R.id.incomeEditText)).getText().toString();
            String percentStr = ((TextView)view.findViewById(R.id.percentEditText)).getText().toString();

            // Перевірка полей на заповненість
            if(incomeStr.isEmpty() || percentStr.isEmpty()) {
                Toast.makeText(getContext(), R.string.fill_fields, Toast.LENGTH_SHORT).show();
                return;
            }
            int income = Integer.parseInt(incomeStr);
            float percent = Float.parseFloat(percentStr);

            // Перевірка даних на коректність
            if(!Deposit.isValidData(income, percent)){
                Toast.makeText(getContext(), R.string.invalid_data, Toast.LENGTH_SHORT).show();
                return;
            }
            getAppContract().toCurrencyScreen(this, new Deposit(income, percent, null));
        });
    }

}