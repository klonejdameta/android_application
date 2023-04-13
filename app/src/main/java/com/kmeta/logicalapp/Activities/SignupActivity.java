package com.kmeta.logicalapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.kmeta.logicalapp.Database.DatabaseConnector;
import com.kmeta.logicalapp.databinding.ActivitySignupBinding;

public class SignupActivity extends AppCompatActivity {
    ActivitySignupBinding binding;
    DatabaseConnector databaseConnector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        databaseConnector = new DatabaseConnector(this);
        binding.signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = binding.signupName.getText().toString();
                if (name.equals("")) {
                    Toast.makeText(SignupActivity.this, "Please insert your name", Toast.LENGTH_SHORT).show();
                    return;
                }
                String username = binding.signupUserName.getText().toString();
                if (username.equals("")) {
                    Toast.makeText(SignupActivity.this, "Please insert your username", Toast.LENGTH_SHORT).show();
                    return;
                }
                String email = binding.signupEmail.getText().toString();
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(SignupActivity.this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
                    return;
                }
                String password = binding.signupPassword.getText().toString();
                if (password.length() < 8) {
                    Toast.makeText(SignupActivity.this, "Password must be at least 8 characters long", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!password.matches(".*[A-Z].*")) {
                    Toast.makeText(SignupActivity.this, "Password must contain at least one uppercase letter", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!password.matches(".*[0-9].*")) {
                    Toast.makeText(SignupActivity.this, "Password must contain at least one number", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!password.matches(".*[@#$%^&+=].*")) {
                    Toast.makeText(SignupActivity.this, "Password must contain at least one special character @ # $ % ^ & + =", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Confirm password validation
                String confirmPassword = binding.signupConfirm.getText().toString();
                if (!password.equals(confirmPassword)) {
                    Toast.makeText(SignupActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (email.equals("") || password.equals("") || confirmPassword.equals("")) {
                    Toast.makeText(SignupActivity.this, "All fields are mandatory", Toast.LENGTH_SHORT).show();
                } else {
                    if (password.equals(confirmPassword)) {
                        Boolean checkUserEmail = databaseConnector.checkEmail(email);
                        if (checkUserEmail == false) {
                            Boolean insert = databaseConnector.insertDataUsers(name, username, email, password);
                            if (insert == true) {
                                Toast.makeText(SignupActivity.this, "Signup Successfully!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(SignupActivity.this, "Signup Failed!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(SignupActivity.this, "User already exists! Please login", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(SignupActivity.this, "Invalid Password!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        binding.loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}