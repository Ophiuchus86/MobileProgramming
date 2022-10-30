package com.github.ophiuchus86.lab123.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.github.ophiuchus86.lab123.BuildConfig;
import com.github.ophiuchus86.lab123.R;

public class MenuFragment extends BaseFragment {
    private static final String KEY_STUDENT = "STUDENT";
    private Button startCalcButton;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {

        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_menu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.quitButton).setOnClickListener(v -> {
                    getActivity().finish();
        });

        startCalcButton = view.findViewById(R.id.startCalcButton);
        startCalcButton.setOnClickListener(v -> {
            getAppContract().toInputScreen(this);
        });

        TextView versionEditText = view.findViewById(R.id.versionTextView);
         if (versionEditText != null) {
                versionEditText.setText(getString(
                        R.string.app_version,
                        BuildConfig.VERSION_NAME
                ));
            }
        }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}

