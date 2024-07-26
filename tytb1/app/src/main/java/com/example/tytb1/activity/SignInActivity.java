package com.example.tytb1.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tytb1.Api.SignApiService;
import com.example.tytb1.Config.AuthResponse;
import com.example.tytb1.Model.User;
import com.example.tytb1.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends AppCompatActivity {

    EditText edt_email, edt_password;
    Button sign_in_btn;
    TextView tv_signup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        edt_email = findViewById(R.id.edt_email);
        edt_password = findViewById(R.id.edt_password);
        sign_in_btn = findViewById(R.id.sign_in_btn);

        tv_signup = findViewById(R.id.tv_signup);
        tv_signup.setOnClickListener(v -> signup());
        sign_in_btn.setOnClickListener(v -> login());
    }

    private void signup() {
        Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
        startActivity(intent);
    }

    private void login() {
        String email = edt_email.getText().toString().trim();
        String password = edt_password.getText().toString().trim();
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(SignInActivity.this, "Please enter email and password", Toast.LENGTH_SHORT).show();
            return;
        }
        User user = new User(email, password);
        SignApiService.signApiService.loginUser(user).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful()) {
                    AuthResponse authResponse = response.body();
                    if (authResponse != null) {
                        String jwt = authResponse.getJwt();
                        Log.d("JWT", jwt);
                        saveJwt(jwt);
                        fetchUserProfile(jwt);
                        Toast.makeText(SignInActivity.this, "login success", Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(SignInActivity.this, TestActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    Toast.makeText(SignInActivity.this, "Failed to login: " + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable throwable) {
                Toast.makeText(SignInActivity.this, "Failed to connect to server: " + throwable.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }

    private void fetchUserProfile(String jwt) {
        SignApiService.signApiService.getUserProfile("Bearer " + jwt).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User user = response.body();
                    Long userId = user.getId();

                    // Lưu userId vào SharedPreferences
                    saveUserId(userId);
                    Log.d("SignInActivity", "UserId: " + userId);
                } else {
                    Toast.makeText(SignInActivity.this, "Failed to get user profile: " + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable throwable) {
                Toast.makeText(SignInActivity.this, "Failed to connect to server: " + throwable.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void saveUserId(Long userId) {
        SharedPreferences sharedPreferences1 = getSharedPreferences("MySharedPref1", MODE_PRIVATE);
        SharedPreferences.Editor myEdit1 = sharedPreferences1.edit();
        myEdit1.putLong("userId", userId);
        myEdit1.apply();
    }

    private void saveJwt(String jwt) {
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putString("jwt", jwt);
        myEdit.apply();
    }
}