package com.minerva.helpforward;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    EditText email;
    EditText password;
    Button logInButton;
    ProgressBar bar;
    RelativeLayout mainLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        bar = (ProgressBar) findViewById(R.id.login_progress_bar);
        bar.setVisibility(View.GONE);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        mainLayout = (RelativeLayout) findViewById(R.id.main_container_login);

        AnimationDrawable animationDrawable = (AnimationDrawable) mainLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();

        logInButton = (Button) findViewById(R.id.login);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);

        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bar.setVisibility(View.VISIBLE);
                if (email.getText().equals("")){
                    Toast.makeText(getApplicationContext(), R.string.empty_email, Toast.LENGTH_LONG).show();
                    bar.setVisibility(View.GONE);
                }else if (password.getText().equals("")){
                    Toast.makeText(getApplicationContext(), R.string.empty_password, Toast.LENGTH_LONG).show();
                    bar.setVisibility(View.GONE);
                } else {
                    mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                bar.setVisibility(View.GONE);
                                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                            }else{
                                Toast.makeText(getApplicationContext(), R.string.auth_error,
                                        Toast.LENGTH_SHORT).show();
                                bar.setVisibility(View.GONE);
                            }
                        }
                    });
                }
            }
        });
    }
}
