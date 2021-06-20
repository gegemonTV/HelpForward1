package com.minerva.helpforward;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;


import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {

    private static final int RESULT_LOAD_IMAGE = 0;
    private static final String TAG = "HELP";

    DatabaseReference _db;
    ShapeableImageView userImage;
    TextView username, crystalsCounter, karmaCounter, tasksCounter, email, company_name;
    Button editProfile, tutorialButton, aboutButton;
    Bundle savedState;

    FirebaseStorage storage = FirebaseStorage.getInstance("gs://help-forward.appspot.com");
    StorageReference storageRef = storage.getReference();

    IsCompanyViewModel mViewModel;

    StorageReference imageRef = storageRef.child("images/user_images");

    final long ONE_MEGABYTE = 1024 * 1024;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser mUser = mAuth.getCurrentUser();
    boolean isCompany;
    public ProfileFragment(){}
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mViewModel = new ViewModelProvider(getActivity()).get(IsCompanyViewModel.class);

        _db = FirebaseDatabase.getInstance().getReference(getString(R.string.users_db));
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        userImage = (ShapeableImageView) rootView.findViewById(R.id.user_image);
        username = (TextView) rootView.findViewById(R.id.nickname);
        crystalsCounter = (TextView) rootView.findViewById(R.id.crystals_count);
        karmaCounter = (TextView) rootView.findViewById(R.id.karma_count);
        tasksCounter = (TextView) rootView.findViewById(R.id.completed_tasks_count);
        email = rootView.findViewById(R.id.email_profile);
        company_name = (TextView) rootView.findViewById(R.id.company_name);
        editProfile = rootView.findViewById(R.id.edit_profile_button);
        tutorialButton = rootView.findViewById(R.id.tutorial_button);
        aboutButton = rootView.findViewById(R.id.about_button);

        email.setText(mUser.getEmail());
        username.setText(mUser.getDisplayName());

        mViewModel.getIsCompany().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {

                isCompany = aBoolean;
                Log.d("MSG", ""+isCompany+86);
                if (isCompany){
                    company_name.setVisibility(View.VISIBLE);
                } else {
                    company_name.setVisibility(View.GONE);
                }
            }
        });


        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK);
                i.setType("image/*");
                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });

        imageRef.child(mUser.getDisplayName()+".jpeg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getContext())
                        .load(uri)
                        .placeholder(R.mipmap.ic_launcher_foreground)
                        .circleCrop()
                        .into(userImage);
            }
        });

        tutorialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), TutorialActivity.class));
            }
        });

        aboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(v.getContext())
                        .setTitle("About")
                        .setCancelable(true)
                        .setMessage("Приложение для поиска, отклика и создания запросов помощи.\nРазработчики: \nСоколова Мария, Мехтиев Владимир\n" +
                                "Контакты: \n" +
                                "Email: vovakonki776@gmail.com" +
                                "\nVersion: 0.0.1_beta")
                        .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                builder.create().show();
            }
        });
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = LayoutInflater.from(v.getContext());
                View view = inflater.inflate(R.layout.edit_profile_dialog, null);

                EditText changeEmail = view.findViewById(R.id.change_email);
                ImageButton changeEmailButton = view.findViewById(R.id.change_email_button);

                changeEmailButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mUser.updateEmail(changeEmail.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Log.d("EMAIL", "Email updated successfully");
                                }
                            }
                        });
                    }
                });

                Button sendPasswordReset = view.findViewById(R.id.send_change_passwd);

                sendPasswordReset.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mAuth.sendPasswordResetEmail(mUser.getEmail()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "Email sent.");
                                    Snackbar.make(view, "Password update email sent on your email.", Snackbar.LENGTH_LONG).show();
                                }

                            }
                        });
                    }
                });

                Button logOutButton = view.findViewById(R.id.exit_button);
                logOutButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mAuth.signOut();
                        Intent i = new Intent(getContext(), StartActivity.class);
                        startActivity(i);
                    }
                });

                Button deleteButton = view.findViewById(R.id.delete_button);

                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "User account deleted.");
                                    mAuth.signOut();
                                    Intent i = new Intent(getContext(), StartActivity.class);
                                    startActivity(i);
                                }
                            }
                        });
                    }
                });

                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(v.getContext())
                        .setCancelable(true)
                        .setView(view)
                        .setNeutralButton("Exit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                builder.create().show();
            }
        });

        Log.d("MSG", 1+mUser.getDisplayName().toString());

        _db.child(mUser.getDisplayName().toString()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot != null){
                    Log.d(TAG, snapshot.toString());
                    Log.d("MSG1", ""+mViewModel.getIsCompany().getValue());
                    Log.d("MSG", snapshot.child("org_name").getRef().toString()+1);
                    company_name.setText(snapshot.child("org_name").getValue().toString());

                    crystalsCounter.setText(snapshot.child(getString(R.string.crystals_db)).getValue().toString());
                    karmaCounter.setText(snapshot.child(getString(R.string.karma_db)).getValue().toString());
                    tasksCounter.setText(snapshot.child(getString(R.string.completed_tasks_db)).getValue().toString());
                }else{
                    Toast.makeText(getContext(), R.string.error_msg, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        return rootView;
    }

    @Override
    public void onSaveInstanceState(@NonNull @NotNull Bundle outState) {
        super.onSaveInstanceState(outState);
        savedState = outState;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == RESULT_LOAD_IMAGE){
                try {
                    assert data != null;
                    final Uri imageUri = data.getData();
                    final InputStream imageStream = getContext().getContentResolver().openInputStream(imageUri);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                    StorageReference userImgRef = imageRef.child(mUser.getDisplayName()+".jpeg");

                    UploadTask task = userImgRef.putBytes(baos.toByteArray());
                    task.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull @NotNull Exception e) {
                            Log.d("FBS", "Can not upload image into Firebase Storage! " + e.getMessage());
                        }
                    });
                    Glide.with(this)
                            .load(selectedImage)
                            .circleCrop()
                            .into(userImage);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), R.string.error_msg, Toast.LENGTH_LONG).show();
                }
            }
        }else {
            Toast.makeText(getContext(), R.string.image_unpicked,Toast.LENGTH_LONG).show();
        }
    }
}
