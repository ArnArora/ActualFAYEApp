package com.example.actualfayeapp.ui.studytips;
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
import com.example.actualfayeapp.ui.studytips.StudytipsViewModel;

public class StudytipsFragment extends Fragment{
    private StudytipsViewModel studytipsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        studytipsViewModel =
                new ViewModelProvider(this).get(StudytipsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_studytips, container, false);
        final TextView textView = root.findViewById(R.id.studytipstitle);
        studytipsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}
