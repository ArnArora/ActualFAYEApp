package com.example.actualfayeapp.ui.mentalhealth;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
public class MentalHealthViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public MentalHealthViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
