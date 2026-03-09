package com.example.fitnesstrackerapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class EditProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        EditText etName = findViewById(R.id.etEditName);
        Button btnSave = findViewById(R.id.btnSaveProfile);

        // Load the current name to display in the EditText
        SharedPreferences prefs = getSharedPreferences("UserProfile", MODE_PRIVATE);
        String currentName = prefs.getString("UserName", "Fitness Enthusiast");
        etName.setText(currentName);

        // Save the new name when button is clicked
        btnSave.setOnClickListener(v -> {
            String newName = etName.getText().toString().trim();
            if (!newName.isEmpty()) {
                prefs.edit().putString("UserName", newName).apply();
                Toast.makeText(this, "Profile Saved", Toast.LENGTH_SHORT).show();
                finish(); // Close activity and go back to ProfileFragment
            } else {
                Toast.makeText(this, "Name cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });
    }
}