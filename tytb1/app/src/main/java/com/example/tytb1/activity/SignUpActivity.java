package com.example.tytb1.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tytb1.Api.SignApiService;
import com.example.tytb1.Config.AuthResponse;
import com.example.tytb1.Model.User;
import com.example.tytb1.R;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {

    private EditText edt_fullName, edt_email, edt_password, edt_confirmpassword;
    private TextView tv_signin, tv_birthDate;
    private Button selectBirthDate, sign_up_btn;
    private String birthDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        edt_fullName = findViewById(R.id.full_name);
        edt_email = findViewById(R.id.email);
        edt_password = findViewById(R.id.password);
        edt_confirmpassword = findViewById(R.id.confirm);
        tv_birthDate = findViewById(R.id.birthDate);
        sign_up_btn = findViewById(R.id.sign_up_btn);
        selectBirthDate = findViewById(R.id.selectDateButton);
        tv_signin = findViewById(R.id.tv_signin);

        //
        tv_signin.setOnClickListener(v -> signin());
        selectBirthDate.setOnClickListener(v -> showDatePickerDialog());
        sign_up_btn.setOnClickListener(v -> register());
    }

    private void signin() {
        Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
        startActivity(intent);
    }

    private void register() {
        String fullName = edt_fullName.getText().toString().trim();
        String email = edt_email.getText().toString().trim();
        String password= edt_password.getText().toString().trim();
        String confirmPassword = edt_confirmpassword.toString().trim();

//        if (!password.equals(confirmPassword)) {
//            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
//            return;
//        }
        User user = new User(fullName, email, password, birthDate);

//
        SignApiService.signApiService.registerUser(user).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful()) {
                    AuthResponse authResponse = response.body();
                    if (authResponse != null) {
//                        String jwt = authResponse.getJwt();
//                        Log.d("JWT", jwt);
//                        saveJwt(jwt); // Lưu JWT vào SharedPreferences
                        Toast.makeText(SignUpActivity.this, "User registered successfully", Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    Toast.makeText(SignUpActivity.this, "Failed to register user: " + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable throwable) {
                Toast.makeText(SignUpActivity.this, "Failed to connect to server: " + throwable.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                SignUpActivity.this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    birthDate = selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDay;
                    tv_birthDate.setText(birthDate);
                },
                year, month, day);
        datePickerDialog.show();
    }

}