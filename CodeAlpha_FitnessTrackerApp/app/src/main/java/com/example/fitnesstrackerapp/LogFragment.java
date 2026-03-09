package com.example.fitnesstrackerapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LogFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_log, container, false);

        EditText inputExercise = view.findViewById(R.id.inputExercise);
        EditText inputDuration = view.findViewById(R.id.inputDuration);
        EditText inputCalories = view.findViewById(R.id.inputCalories);
        Button btnSave = view.findViewById(R.id.btnSave);

        AppDatabase db = AppDatabase.getDatabase(requireContext());
        String todayDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        btnSave.setOnClickListener(v -> {
            String exercise = inputExercise.getText().toString();
            String durationStr = inputDuration.getText().toString();
            String calsStr = inputCalories.getText().toString();

            if (exercise.isEmpty() && durationStr.isEmpty() && calsStr.isEmpty()) {
                Toast.makeText(requireContext(), "Enter some data", Toast.LENGTH_SHORT).show();
                return;
            }

            FitnessRecord record = new FitnessRecord();
            record.date = todayDate;
            record.exerciseType = exercise;
            // Map the input to workoutDurationMinutes instead of steps
            record.workoutDurationMinutes = durationStr.isEmpty() ? 0 : Integer.parseInt(durationStr);
            record.caloriesBurned = calsStr.isEmpty() ? 0 : Integer.parseInt(calsStr);

            db.fitnessDao().insert(record);
            Toast.makeText(requireContext(), "Activity Saved!", Toast.LENGTH_SHORT).show();

            inputExercise.setText("");
            inputDuration.setText("");
            inputCalories.setText("");
        });

        return view;
    }
}