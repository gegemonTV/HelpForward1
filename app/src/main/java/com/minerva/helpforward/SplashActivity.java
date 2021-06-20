package com.minerva.helpforward;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

public class SplashActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SharedPreferences sharedPreferences = getSharedPreferences("HelpForwardShared", Context.MODE_PRIVATE);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        FirebaseDatabase _db = FirebaseDatabase.getInstance();
        DatabaseReference _ref = _db.getReference("users");

        if (user != null) {
            user.getIdToken(true).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<GetTokenResult> task) {
                    try {
                        if (task.getResult() != null) {
                            if (sharedPreferences.getBoolean(HomeActivity.APP_PREFERENCES_FIRST_TIME, true)){
                                startActivity(new Intent(getApplicationContext(), TutorialActivity.class));
                            } else {
                                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                            }
                        }
                    } catch (Exception e) {
                        Log.d("ERROR", "USER HAD BEEN DELETED");
                        if (sharedPreferences.getBoolean(HomeActivity.APP_PREFERENCES_FIRST_TIME, true)){
                            startActivity(new Intent(getApplicationContext(), TutorialActivity.class));
                        } else {
                            startActivity(new Intent(getApplicationContext(), StartActivity.class));
                        }

                    }
                }
            });
        } else{
            startActivity(new Intent(getApplicationContext(), StartActivity.class));
        }
    }
}