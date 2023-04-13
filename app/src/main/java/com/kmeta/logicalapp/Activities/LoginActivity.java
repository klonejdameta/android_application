package com.kmeta.logicalapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.kmeta.logicalapp.Database.DatabaseConnector;
import com.kmeta.logicalapp.MainActivity;
import com.kmeta.logicalapp.databinding.ActivityLoginBinding;

import java.util.UUID;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;
    DatabaseConnector databaseConnector;

    private void handleLoginSuccess(String accessToken, String username) {
        SharedPreferences preferences = getSharedPreferences("my_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("access_token", accessToken);
        editor.putString("username", username);
        editor.apply();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        databaseConnector = new DatabaseConnector(this);
        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.loginEmail.getText().toString();
                String password = binding.loginPassword.getText().toString();
                if (email.equals("") && password.equals("")) {
                    Toast.makeText(LoginActivity.this, "Please insert your email and password", Toast.LENGTH_SHORT).show();
                } else if (email.equals("")) {
                    Toast.makeText(LoginActivity.this, "Please insert your email", Toast.LENGTH_SHORT).show();
                } else if (password.equals("")) {
                    Toast.makeText(LoginActivity.this, "Please insert your password", Toast.LENGTH_SHORT).show();
                } else {
                    String username = databaseConnector.getUserName(email);
                    if (username != null) {
                        Boolean checkCredentials = databaseConnector.checkEmailPassword(email, password);
                        if (checkCredentials) {
                            String accessToken = UUID.randomUUID().toString();
                            handleLoginSuccess(accessToken, username);
                            Toast.makeText(LoginActivity.this, "Login Successfully!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.putExtra("USER_EMAIL", email);
                            startActivity(intent);
                        } else {
                            Toast.makeText(LoginActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "This email does not exist in our system", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        binding.signupRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
    }
}