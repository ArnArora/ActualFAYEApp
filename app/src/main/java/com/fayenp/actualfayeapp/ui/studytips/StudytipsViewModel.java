package com.fayenp.actualfayeapp.ui.studytips;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class StudytipsViewModel extends ViewModel{
    private MutableLiveData<String> mText;

    public StudytipsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
