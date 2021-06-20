package com.minerva.helpforward;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    EditText Email;
    EditText Nickname;
    EditText Password;
    Button RegisterButton;
    ProgressBar Bar;

    FirebaseAuth mAuth;
    Switch organizationSwitch;
    EditText orgNameText, orgAddrText;
    TextInputLayout orgNameLayout, orgAddrLayout;

    boolean isChecked = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        RelativeLayout mainContainer = (RelativeLayout) findViewById(R.id.main_container_register);

        AnimationDrawable animationDrawable = (AnimationDrawable) mainContainer.getBackground();
        animationDrawable.setExitFadeDuration(2000);
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.start();

        Email = (EditText) findViewById(R.id.email_reg);
        Nickname = (EditText) findViewById(R.id.nickname_reg);
        Password = (EditText) findViewById(R.id.password_reg);
        RegisterButton = (Button) findViewById(R.id.register);
        orgNameText = (EditText) findViewById(R.id.org_name_reg);
        orgNameLayout = (TextInputLayout) findViewById(R.id.org_name_layout);
        orgAddrText = (EditText) findViewById(R.id.org_addr_reg);
        orgAddrLayout = (TextInputLayout) findViewById(R.id.org_addr_layout);

        Bar = (ProgressBar) findViewById((R.id.register_bar));

        organizationSwitch = (Switch) findViewById(R.id.organization_switch);

        organizationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isCheck) {
                isChecked = isCheck;
                if (isChecked){
                    orgAddrLayout.setVisibility(View.VISIBLE);
                    orgNameLayout.setVisibility(View.VISIBLE);
                } else {
                    orgAddrLayout.setVisibility(View.GONE);
                    orgNameLayout.setVisibility(View.GONE);
                }
            }
        });

        RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bar.setVisibility(View.VISIBLE);
                if (Email.getText().toString().equals("") ){
                    Snackbar.make(v, R.string.empty_email, Snackbar.LENGTH_LONG).show();
                    Bar.setVisibility(View.GONE);
                }else if (Password.getText().toString().equals("")) {
                    Snackbar.make(v, R.string.empty_password, Snackbar.LENGTH_LONG).show();
                    Bar.setVisibility(View.GONE);
                } else if(Nickname.getText().toString().equals((""))){
                    Snackbar.make(v, R.string.empty_nickname, Snackbar.LENGTH_LONG).show();
                    Bar.setVisibility(View.GONE);
                } else if (Password.getText().toString().length() < 6) {
                    Snackbar.make(v, getString(R.string.more_than_six_chars), Snackbar.LENGTH_LONG).show();
                    Bar.setVisibility(View.GONE);
                } else {
                    mAuth.createUserWithEmailAndPassword(Email.getText().toString(), Password.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                DatabaseReference _ref = FirebaseDatabase.getInstance().getReference(getString(R.string.users_db));
                                _ref.child(Nickname.getText().toString()).child(getString(R.string.crystals_db)).setValue(0);
                                _ref.child(Nickname.getText().toString()).child(getString(R.string.karma_db)).setValue(0);
                                _ref.child(Nickname.getText().toString()).child(getString(R.string.completed_tasks_db)).setValue(0);
                                if (isChecked){
                                    _ref.child(Nickname.getText().toString()).child("org_name").setValue(orgNameText.getText().toString());
                                    _ref.child(Nickname.getText().toString()).child("is_company").setValue(isChecked);
                                    _ref.child(Nickname.getText().toString()).child("org_addr").setValue(orgAddrText.getText().toString());
                                } else {
                                    _ref.child(Nickname.getText().toString()).child("org_name").setValue(".");
                                    _ref.child(Nickname.getText().toString()).child("is_company").setValue(isChecked);
                                    _ref.child(Nickname.getText().toString()).child("org_addr").setValue(".");
                                }

                                Bar.setVisibility(View.GONE);
                                task.getResult().getUser().updateProfile(new UserProfileChangeRequest.Builder().setDisplayName(Nickname.getText().toString()).build())
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){
                                                    Log.d("OK", "COOL!");
                                                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                                                } else {
                                                    Log.d("TASK", task.getResult().toString());
                                                    Snackbar.make(v, R.string.registration_failed,
                                                            Snackbar.LENGTH_SHORT).show();
                                                    Bar.setVisibility(View.GONE);
                                                }
                                            }


                                        });
                            } else {
                                Log.d("TASK", task.getResult().toString());
                                Snackbar.make(v, getString(R.string.registration_failed)+task.getResult().toString(),
                                        Snackbar.LENGTH_SHORT).show();
                                Bar.setVisibility(View.GONE);
                            }
                        }
                    });
                }
            }
        });
    }
}