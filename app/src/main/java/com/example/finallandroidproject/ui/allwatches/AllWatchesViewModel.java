package com.example.finallandroidproject.ui.allwatches;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.LiveData;

public class AllWatchesViewModel extends ViewModel {
    private final MutableLiveData<String> mText;

    public AllWatchesViewModel() {
        mText = new MutableLiveData<>();
    }

    public LiveData<String> getText() {
        return mText;
    }
}
