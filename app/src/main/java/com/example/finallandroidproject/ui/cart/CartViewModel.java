package com.example.finallandroidproject.ui.cart;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.LiveData;

public class CartViewModel extends ViewModel {
    private final MutableLiveData<String> mText;

    public CartViewModel() {
        mText = new MutableLiveData<>();
    }

    public LiveData<String> getText() {
        return mText;
    }
}
