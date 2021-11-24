package com.example.actualfayeapp.ui.mentalhealth;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.actualfayeapp.R;
import com.example.actualfayeapp.ui.mentalhealth.MentalHealthViewModel;

public class MentalHealthFragment extends Fragment{
    private MentalHealthViewModel mentalHealthViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mentalHealthViewModel =
                new ViewModelProvider(this).get(MentalHealthViewModel.class);
        View root = inflater.inflate(R.layout.fragment_mentalhealth, container, false);
        final TextView textView = root.findViewById(R.id.text_mentalhealth);
        mentalHealthViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}
