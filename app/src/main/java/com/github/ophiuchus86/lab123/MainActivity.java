package com.github.ophiuchus86.lab123;

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

public class MainActivity extends AppCompatActivity implements AppContract {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        launchFragment(target, ResultFragment.newInstance(deposit));
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
}