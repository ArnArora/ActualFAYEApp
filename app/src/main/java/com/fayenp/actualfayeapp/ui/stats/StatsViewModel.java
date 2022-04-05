package com.fayenp.actualfayeapp.ui.stats;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
public class StatsViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public StatsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("mate im here");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
