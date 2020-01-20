package com.bignerdranch.android.exercisebuddy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class LoginOrRegisterActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_or_register);
    }

    public void goToLoginPage(View view) {
        Intent intent = new Intent(LoginOrRegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
        return;
    }

    public void goToRegistrationPage(View view) {
        Intent intent = new Intent(LoginOrRegisterActivity.this, RegisterActivity.class);
        startActivity(intent);
        finish();
        return;
    }
}
