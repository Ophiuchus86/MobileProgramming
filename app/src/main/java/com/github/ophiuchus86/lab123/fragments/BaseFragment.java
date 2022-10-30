package com.github.ophiuchus86.lab123.fragments;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.github.ophiuchus86.lab123.contract.AppContract;

public class BaseFragment extends Fragment {
    private AppContract appContract;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.appContract = (AppContract) context;
    }
    @Override
    public void onDetach() {
        super.onDetach();
        this.appContract = null;
    }
    final AppContract getAppContract() {
        return appContract;
    }
}
