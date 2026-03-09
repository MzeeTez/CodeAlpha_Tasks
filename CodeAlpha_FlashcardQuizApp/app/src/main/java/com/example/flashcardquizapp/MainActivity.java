package com.example.flashcardquizapp;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Flashcard> flashcardList;
    private int currentIndex = 0;
    private boolean isShowingAnswer = false;

    private TextView tvQuestionText, tvAnswerText, tvCounter;
    private ProgressBar progressBar;
    private ImageButton btnPrevious, btnNext, btnEdit, btnDelete;
    private Button btnShowAnswer;
    private ExtendedFloatingActionButton fabAdd;
    private FrameLayout cardContainer;
    private CardView cardFront, cardBack;

    private AnimatorSet frontAnim;
    private AnimatorSet backAnim;

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

        flashcardList = new ArrayList<>();
        loadDefaultFlashcards();

        // View Binding
        tvQuestionText = findViewById(R.id.tvQuestionText);
        tvAnswerText = findViewById(R.id.tvAnswerText);
        tvCounter = findViewById(R.id.tvCounter);
        progressBar = findViewById(R.id.progressBar);
        btnPrevious = findViewById(R.id.btnPrevious);
        btnNext = findViewById(R.id.btnNext);
        btnEdit = findViewById(R.id.btnEdit);
        btnDelete = findViewById(R.id.btnDelete);
        btnShowAnswer = findViewById(R.id.btnShowAnswer);
        fabAdd = findViewById(R.id.fabAdd);
        cardContainer = findViewById(R.id.cardContainer);
        cardFront = findViewById(R.id.cardFront);
        cardBack = findViewById(R.id.cardBack);

        // Load Animations
        float scale = getApplicationContext().getResources().getDisplayMetrics().density;
        cardFront.setCameraDistance(8000 * scale);
        cardBack.setCameraDistance(8000 * scale);
        frontAnim = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.front_animator);
        backAnim = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.back_animator);

        updateUI();

        // Flip Triggers (Button OR tapping the card)
        btnShowAnswer.setOnClickListener(v -> flipCard());
        cardContainer.setOnClickListener(v -> flipCard());

        // Navigation
        btnNext.setOnClickListener(v -> {
            if (flashcardList.isEmpty()) return;
            resetCardToFront();
            currentIndex = (currentIndex + 1) % flashcardList.size();
            updateUI();
        });

        btnPrevious.setOnClickListener(v -> {
            if (flashcardList.isEmpty()) return;
            resetCardToFront();
            currentIndex = (currentIndex - 1 + flashcardList.size()) % flashcardList.size();
            updateUI();
        });

        // CRUD Actions
        fabAdd.setOnClickListener(v -> showAddEditDialog(false));

        btnEdit.setOnClickListener(v -> {
            if (!flashcardList.isEmpty()) showAddEditDialog(true);
        });

        btnDelete.setOnClickListener(v -> {
            if (!flashcardList.isEmpty()) {
                flashcardList.remove(currentIndex);
                if (currentIndex >= flashcardList.size() && currentIndex > 0) currentIndex--;
                resetCardToFront();
                updateUI();
                Toast.makeText(this, "Flashcard deleted", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void flipCard() {
        if (flashcardList.isEmpty()) return;

        if (isShowingAnswer) {
            frontAnim.setTarget(cardBack);
            backAnim.setTarget(cardFront);
            frontAnim.start();
            backAnim.start();
            isShowingAnswer = false;
            btnShowAnswer.setText("Show Answer");
        } else {
            frontAnim.setTarget(cardFront);
            backAnim.setTarget(cardBack);
            frontAnim.start();
            backAnim.start();
            isShowingAnswer = true;
            btnShowAnswer.setText("Show Question");
        }
    }

    private void resetCardToFront() {
        if (isShowingAnswer) {
            cardFront.setAlpha(1.0f);
            cardFront.setRotationY(0.0f);
            cardBack.setAlpha(0.0f);
            cardBack.setRotationY(-180.0f);
            isShowingAnswer = false;
            btnShowAnswer.setText("Show Answer");
        }
    }

    private void loadDefaultFlashcards() {
        // Adding 30 default flashcards!
        flashcardList.add(new Flashcard("What is the powerhouse of the cell?", "Mitochondria"));
        flashcardList.add(new Flashcard("Who wrote 'To Kill a Mockingbird'?", "Harper Lee"));
        flashcardList.add(new Flashcard("What is the chemical symbol for Gold?", "Au"));
        flashcardList.add(new Flashcard("What year did the Titanic sink?", "1912"));
        flashcardList.add(new Flashcard("What is the largest planet in our solar system?", "Jupiter"));
        flashcardList.add(new Flashcard("What is the speed of light?", "299,792 km/s"));
        flashcardList.add(new Flashcard("Who painted the Mona Lisa?", "Leonardo da Vinci"));
        flashcardList.add(new Flashcard("What is the capital of Japan?", "Tokyo"));
        flashcardList.add(new Flashcard("What element does 'O' represent on the periodic table?", "Oxygen"));
        flashcardList.add(new Flashcard("How many continents are there on Earth?", "Seven"));
        flashcardList.add(new Flashcard("Who developed the theory of relativity?", "Albert Einstein"));
        flashcardList.add(new Flashcard("What is the smallest prime number?", "2"));
        flashcardList.add(new Flashcard("What is the longest river in the world?", "The Nile"));
        flashcardList.add(new Flashcard("In what year did World War II end?", "1945"));
        flashcardList.add(new Flashcard("What is the freezing point of water in Fahrenheit?", "32°F"));
        flashcardList.add(new Flashcard("Which planet is known as the Red Planet?", "Mars"));
        flashcardList.add(new Flashcard("Who was the first President of the United States?", "George Washington"));
        flashcardList.add(new Flashcard("What is the main ingredient in guacamole?", "Avocado"));
        flashcardList.add(new Flashcard("What is the hardest natural substance on Earth?", "Diamond"));
        flashcardList.add(new Flashcard("How many bones are in the adult human body?", "206"));
        flashcardList.add(new Flashcard("What is the capital of Australia?", "Canberra"));
        flashcardList.add(new Flashcard("Who wrote the play 'Romeo and Juliet'?", "William Shakespeare"));
        flashcardList.add(new Flashcard("What gas do plants absorb from the atmosphere?", "Carbon Dioxide"));
        flashcardList.add(new Flashcard("What is the largest ocean on Earth?", "Pacific Ocean"));
        flashcardList.add(new Flashcard("What currency is used in the United Kingdom?", "Pound Sterling"));
        flashcardList.add(new Flashcard("Who is known as the 'Father of Computers'?", "Charles Babbage"));
        flashcardList.add(new Flashcard("What is the square root of 144?", "12"));
        flashcardList.add(new Flashcard("Which country hosted the 2016 Summer Olympics?", "Brazil"));
        flashcardList.add(new Flashcard("What is the chemical symbol for Iron?", "Fe"));
        flashcardList.add(new Flashcard("What is the tallest mountain in the world?", "Mount Everest"));
    }

    private void updateUI() {
        progressBar.setMax(flashcardList.isEmpty() ? 1 : flashcardList.size());

        if (flashcardList.isEmpty()) {
            tvQuestionText.setText("No flashcards left.\nClick 'Add' below.");
            tvCounter.setText("0 / 0");
            progressBar.setProgress(0);
            btnPrevious.setEnabled(false);
            btnNext.setEnabled(false);
            btnEdit.setVisibility(View.GONE);
            btnDelete.setVisibility(View.GONE);
            btnShowAnswer.setEnabled(false);
        } else {
            Flashcard currentCard = flashcardList.get(currentIndex);
            tvQuestionText.setText(currentCard.getQuestion());
            tvAnswerText.setText(currentCard.getAnswer());
            tvCounter.setText((currentIndex + 1) + " / " + flashcardList.size());
            progressBar.setProgress(currentIndex + 1);

            btnPrevious.setEnabled(true);
            btnNext.setEnabled(true);
            btnEdit.setVisibility(View.VISIBLE);
            btnDelete.setVisibility(View.VISIBLE);
            btnShowAnswer.setEnabled(true);
        }
    }

    private void showAddEditDialog(boolean isEdit) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_edit_flashcard, null);
        builder.setView(view);

        EditText etQuestion = view.findViewById(R.id.etQuestion);
        EditText etAnswer = view.findViewById(R.id.etAnswer);

        if (isEdit) {
            Flashcard currentCard = flashcardList.get(currentIndex);
            etQuestion.setText(currentCard.getQuestion());
            etAnswer.setText(currentCard.getAnswer());
            builder.setTitle("Edit Flashcard");
        } else {
            builder.setTitle("Add Flashcard");
        }

        builder.setPositiveButton("Save", (dialog, which) -> {
            String question = etQuestion.getText().toString().trim();
            String answer = etAnswer.getText().toString().trim();

            if (!question.isEmpty() && !answer.isEmpty()) {
                if (isEdit) {
                    Flashcard currentCard = flashcardList.get(currentIndex);
                    currentCard.setQuestion(question);
                    currentCard.setAnswer(answer);
                } else {
                    flashcardList.add(new Flashcard(question, answer));
                    currentIndex = flashcardList.size() - 1;
                }
                resetCardToFront();
                updateUI();
            } else {
                Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }
}