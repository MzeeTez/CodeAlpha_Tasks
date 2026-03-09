package com.example.fitnesstrackerapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        AppDatabase db = AppDatabase.getDatabase(requireContext());
        String todayDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        int totalSteps = db.fitnessDao().getTotalStepsForDate(todayDate);
        int totalCalories = db.fitnessDao().getTotalCaloriesForDate(todayDate);

        TextView textSteps = view.findViewById(R.id.textSteps);
        ProgressBar progressSteps = view.findViewById(R.id.progressSteps);
        TextView textCalories = view.findViewById(R.id.textCalories);

        textSteps.setText(String.valueOf(totalSteps));
        progressSteps.setProgress(totalSteps);
        textCalories.setText(String.valueOf(totalCalories));

        return view;
    }
}