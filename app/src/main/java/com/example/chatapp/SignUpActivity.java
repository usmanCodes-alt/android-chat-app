package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chatapp.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {

    private Button signUpButton;
    private TextView alreadyHaveAccountTextView;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText usernameEditText;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Objects.requireNonNull(getSupportActionBar()).hide();

        signUpButton = findViewById(R.id.sign_up_button);
        emailEditText = findViewById(R.id.email_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);
        usernameEditText = findViewById(R.id.username_edit_text);
        alreadyHaveAccountTextView = findViewById(R.id.login_text_view);

        firebaseAuth = FirebaseAuth.getInstance();      // used to sign up and sign in user
        database = FirebaseDatabase.getInstance();      // used to save username, email, passwords to database
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(R.string.creating_account);
        progressDialog.setMessage("We are creating your account");

        alreadyHaveAccountTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(intent);
            }
        });

        // Sign-up the user using username, email and password
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(emailEditText.getText().toString())
                || TextUtils.isEmpty(passwordEditText.getText().toString())
                || TextUtils.isEmpty(usernameEditText.getText().toString())) {
                    Toast.makeText(SignUpActivity.this,
                            "Please provide all Information",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                progressDialog.show();
                String username = usernameEditText.getText().toString().trim();
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString();
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressDialog.dismiss();
                                if (task.isSuccessful()) {
                                    User user = new User(email, username, password);
                                    String id = Objects
                                            .requireNonNull(Objects.requireNonNull(task.getResult()).getUser())
                                            .getUid();
                                    database.getReference().child("Users").child(id).setValue(user);
                                    Toast.makeText(SignUpActivity.this,
                                            "User Created",
                                            Toast.LENGTH_LONG).show();
                                }
                                else {
                                    Toast.makeText(SignUpActivity.this,
                                            Objects.requireNonNull(task.getException()).getMessage(),
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}