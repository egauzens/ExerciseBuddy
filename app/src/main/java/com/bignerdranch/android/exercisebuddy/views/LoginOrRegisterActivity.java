package com.bignerdranch.android.exercisebuddy.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bignerdranch.android.exercisebuddy.R;
import com.google.firebase.auth.FirebaseAuth;

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
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(LoginOrRegisterActivity.this, ExerciseSelectionActivity.class);
        startActivity(intent);
        return;
    }
}
