package com.bignerdranch.android.exercisebuddy.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bignerdranch.android.exercisebuddy.R;
import com.bignerdranch.android.exercisebuddy.staticHelpers.StorageHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private EditText mEmail;
    private EditText mPassword;
    private EditText mName;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        firebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null){
                    Intent intent = new Intent(RegisterActivity.this, UserMatchesActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                    return;
                }
            }
        };

        mEmail = (EditText) findViewById(R.id.email);
        mPassword = (EditText) findViewById(R.id.password);
        mName = (EditText) findViewById(R.id.name);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(firebaseAuthStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(firebaseAuthStateListener);
    }

    public void registerUser(View v){
        final String email = mEmail.getText().toString();
        final String password = mPassword.getText().toString();
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()){
                    Toast.makeText(RegisterActivity.this, R.string.registration_error, Toast.LENGTH_SHORT).show();
                }
                else{
                    setUserInformation();
                }
            }
        });
    }

    public void setUserInformation() {
        String userId = mAuth.getCurrentUser().getUid();
        DatabaseReference currentUserDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(userId);
        Map userInfo = extractAndPopulateUserInfo(currentUserDatabase);
        userInfo.put("name", mName.getText().toString());
        currentUserDatabase.setValue(userInfo);
    }

    private Map extractAndPopulateUserInfo(DatabaseReference userDB){
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        Map userInfo = new HashMap();
        String profileImageUrl = extras.getString("profileImageUrl");
        userInfo.put("profileImageUri", profileImageUrl);
        userInfo.put("exercise", extras.getString("exercise"));
        userInfo.put("userGender", extras.getString("userGender"));
        userInfo.put("dateOfBirth", convertCalendarToString ((Calendar) extras.getSerializable("dateOfBirth")));
        userInfo.put("userExperienceLevel", extras.getString("userExperienceLevel"));
        userInfo.put("genderPreference", extras.getString("genderPreference"));
        userInfo.put("lowerAgePreference", extras.getInt("lowerAgePreference"));
        userInfo.put("upperAgePreference", extras.getInt("upperAgePreference"));
        userInfo.put("experienceLevelPreference", extras.getString("experienceLevelPreference"));
        userInfo.put("userDescription", extras.getString("userDescription"));
        String userId = mAuth.getCurrentUser().getUid();
        StorageHelper.loadProfileImageIntoStorage(profileImageUrl, userId, this.getContentResolver());

        return userInfo;
    }

    private String convertCalendarToString(Calendar calendar){

        return String.valueOf(calendar.get(Calendar.MONTH)) +
                "-" + String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)) +
                "-" + String.valueOf(calendar.get(Calendar.YEAR));
    }
}
