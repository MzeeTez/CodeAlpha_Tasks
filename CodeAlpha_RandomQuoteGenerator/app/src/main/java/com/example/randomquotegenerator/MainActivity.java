package com.example.randomquotegenerator;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    // Main views
    private TextView tvQuote, tvAuthor;
    private Button btnNewQuote;
    private ImageView btnShare, btnCopy, btnSave;
    private BottomNavigationView bottomNavigation;

    // Layout wrappers for toggling screens
    private View layoutRandom, layoutCategories, layoutMyQuotes;

    // List Views & Data
    private ListView listViewCategories, listViewMyQuotes;
    private ArrayAdapter<String> myQuotesAdapter;
    private List<String> myQuotesList;

    // Data Storage
    private SharedPreferences sharedPreferences;
    private Set<String> savedQuotesSet;

    // Sample Quotes
    private final String[][] quotes = {
            {"The only limit to our realization of tomorrow will be our doubts of today.", "Franklin D. Roosevelt"},
            {"The greatest glory in living lies not in never falling, but in rising every time we fall.", "Nelson Mandela"},
            {"The way to get started is to quit talking and begin doing.", "Walt Disney"},
            {"Your time is limited, so don't waste it living someone else's life.", "Steve Jobs"},
            {"If life were predictable it would cease to be life, and be without flavor.", "Eleanor Roosevelt"},
            {"Life is what happens when you're busy making other plans.", "John Lennon"}
    };

    // Sample Categories
    private final String[] categories = {"Motivation", "Life", "Success", "Love", "Wisdom", "Funny", "Friendship"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize Views
        tvQuote = findViewById(R.id.tvQuote);
        tvAuthor = findViewById(R.id.tvAuthor);
        btnNewQuote = findViewById(R.id.btnNewQuote);
        btnShare = findViewById(R.id.btnShare);
        btnCopy = findViewById(R.id.btnCopy);
        btnSave = findViewById(R.id.btnSave); // New save button
        bottomNavigation = findViewById(R.id.bottomNavigation);

        // Initialize Layouts
        layoutRandom = findViewById(R.id.layoutRandom);
        layoutCategories = findViewById(R.id.layoutCategories);
        layoutMyQuotes = findViewById(R.id.layoutMyQuotes);

        // Initialize ListViews
        listViewCategories = findViewById(R.id.listViewCategories);
        listViewMyQuotes = findViewById(R.id.listViewMyQuotes);

        // --- Setup Categories List ---
        ArrayAdapter<String> catAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, categories);
        listViewCategories.setAdapter(catAdapter);
        listViewCategories.setOnItemClickListener((parent, view, position, id) -> {
            // For now, selecting a category will take you back to random quotes
            bottomNavigation.setSelectedItemId(R.id.nav_random);
            Toast.makeText(MainActivity.this, "Viewing " + categories[position] + " quotes", Toast.LENGTH_SHORT).show();
        });

        // --- Setup Saved Quotes using SharedPreferences ---
        sharedPreferences = getSharedPreferences("QuoteAppDB", MODE_PRIVATE);
        // Load saved quotes or create a new empty set
        savedQuotesSet = new HashSet<>(sharedPreferences.getStringSet("MySavedQuotes", new HashSet<>()));
        myQuotesList = new ArrayList<>(savedQuotesSet);

        myQuotesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, myQuotesList);
        listViewMyQuotes.setAdapter(myQuotesAdapter);

        // Display initial random quote
        displayRandomQuote();

        // Button Click Listeners
        btnNewQuote.setOnClickListener(v -> displayRandomQuote());
        btnShare.setOnClickListener(v -> shareQuote());
        btnCopy.setOnClickListener(v -> copyQuote());
        btnSave.setOnClickListener(v -> saveQuote());

        // Handle Bottom Navigation Menu Clicks (Toggling Layout Visibility)
        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            // Hide everything first
            layoutRandom.setVisibility(View.GONE);
            layoutCategories.setVisibility(View.GONE);
            layoutMyQuotes.setVisibility(View.GONE);

            // Show selected view
            if (itemId == R.id.nav_random) {
                layoutRandom.setVisibility(View.VISIBLE);
                return true;
            } else if (itemId == R.id.nav_categories) {
                layoutCategories.setVisibility(View.VISIBLE);
                return true;
            } else if (itemId == R.id.nav_my_quotes) {
                layoutMyQuotes.setVisibility(View.VISIBLE);
                return true;
            }
            return false;
        });
    }

    private void displayRandomQuote() {
        Random random = new Random();
        int index = random.nextInt(quotes.length);

        tvQuote.setText(quotes[index][0]);
        tvAuthor.setText("- " + quotes[index][1]);
    }

    private void saveQuote() {
        String formattedQuote = "\"" + tvQuote.getText().toString() + "\"\n" + tvAuthor.getText().toString();

        if (!savedQuotesSet.contains(formattedQuote)) {
            // Add to lists
            savedQuotesSet.add(formattedQuote);
            myQuotesList.add(formattedQuote);

            // Save to SharedPreferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putStringSet("MySavedQuotes", new HashSet<>(savedQuotesSet));
            editor.apply();

            // Update List UI
            myQuotesAdapter.notifyDataSetChanged();
            Toast.makeText(this, "Quote saved!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Quote already saved.", Toast.LENGTH_SHORT).show();
        }
    }

    private void shareQuote() {
        String shareBody = "\"" + tvQuote.getText().toString() + "\"\n" + tvAuthor.getText().toString();
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share Quote via"));
    }

    private void copyQuote() {
        String copyBody = "\"" + tvQuote.getText().toString() + "\"\n" + tvAuthor.getText().toString();
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Quote", copyBody);
        clipboard.setPrimaryClip(clip);

        Toast.makeText(MainActivity.this, "Quote copied to clipboard", Toast.LENGTH_SHORT).show();
    }
}