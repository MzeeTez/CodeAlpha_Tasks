package com.example.fitnesstrackerapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ProfileFragment extends Fragment {

    private TextView tvProfileName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        tvProfileName = view.findViewById(R.id.tvProfileName);
        TextView tvEditProfile = view.findViewById(R.id.tvEditProfile);

        // Click listener for the Edit Profile button
        tvEditProfile.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), EditProfileActivity.class);
            startActivity(intent);
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Update the name when the user returns from the EditProfileActivity
        if (getActivity() != null) {
            SharedPreferences prefs = getActivity().getSharedPreferences("UserProfile", Context.MODE_PRIVATE);
            String savedName = prefs.getString("UserName", "Fitness Enthusiast");
            tvProfileName.setText(savedName);
        }
    }
}