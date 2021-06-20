package com.minerva.helpforward;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

import org.jetbrains.annotations.NotNull;

public class StartActivity extends AppCompatActivity {

    Button startRegister;
    Button startLogin;

    ProgressBar progressBar;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        startLogin = (Button) findViewById(R.id.start_login);
        startRegister = (Button) findViewById(R.id.start_register);

        progressBar = (ProgressBar) findViewById(R.id.is_auth_bar);

        mAuth = FirebaseAuth.getInstance();

        RelativeLayout mainContainer = (RelativeLayout) findViewById(R.id.main_container_start);

        AnimationDrawable animationDrawable = (AnimationDrawable) mainContainer.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(2000);
        animationDrawable.start();

        startLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
            }
        });

        startRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            }
        });
    }

    /*@Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        progressBar.setVisibility(View.VISIBLE);

        if (user != null) {
            user.getIdToken(true).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<GetTokenResult> task) {
                    try {
                        if (task.getResult() != null) {
                            progressBar.setVisibility(View.GONE);
                            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        }
                    } catch (Exception e) {
                        Log.d("ERROR", "USER HAD BEEN DELETED");
                        progressBar.setVisibility(View.GONE);
                    }
                }
            });
        }
        progressBar.setVisibility(View.GONE);
    }*/
}