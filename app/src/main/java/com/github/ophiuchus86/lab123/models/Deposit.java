package com.github.ophiuchus86.lab123.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;
import java.util.List;

public class Deposit implements Parcelable {
    public static final float DOLLAR_PRICE_START = 32.34f;
    public static final float DOLLAR_PRICE_END = 37.21f;
    public static final float EURO_PRICE_START = 33.11f;
    public static final float EURO_PRICE_END = 38.42f;
    public static final String ARG_DEPOSIT = "DEPOSIT";
    public static final List<String> CURRENCIES = Arrays.asList("Долар", "Євро");

    private int income;
    private float percent;
    private String currency;

    public Deposit(int income, float percent, String currency) {
        this.income = income;
        this.percent = percent;
        this.currency = currency;

    }

    public int getIncome() {
        return income;
    }

    public float getPercent() {
        return percent;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency){
        if(CURRENCIES.contains(currency))
            this.currency = currency;
    }

    public static boolean isValidData(int income, float percent){
        return !(income < 0 || percent < 0 || percent > 100);
    }

    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.income);
        dest.writeFloat(this.percent);
        dest.writeString(this.currency);
    }
    protected Deposit(Parcel in) {
        this.income = in.readInt();
        this.percent = in.readFloat();
        this.currency = in.readString();
    }
    public static final Creator<Deposit> CREATOR = new Creator<Deposit>() {
        @Override
        public Deposit createFromParcel(Parcel source) {
            return new Deposit(source);
        }
        @Override
        public Deposit[] newArray(int size) {
            return new Deposit[size];
        }
    };

    public static float getCurrencyPriceStart(String currency){
        switch (currency){
            case "Долар": return DOLLAR_PRICE_START;
            case "Євро": return EURO_PRICE_START;
            default: return 0f;
        }
    }

    public static float getCurrencyPriceEnd(String currency){
        switch (currency){
            case "Долар": return DOLLAR_PRICE_END;
            case "Євро": return EURO_PRICE_END;
            default: return 0f;
        }
    }

}