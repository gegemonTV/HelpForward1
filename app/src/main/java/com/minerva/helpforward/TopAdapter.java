package com.minerva.helpforward;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class TopAdapter extends RecyclerView.Adapter<TopAdapter.ViewHolder> {

    private final LayoutInflater inflater;
    private List<User> usersTop = new ArrayList<>();
    private List<String> usernamesTop = new ArrayList<>();

    FirebaseStorage storage = FirebaseStorage.getInstance("gs://help-forward.appspot.com");
    StorageReference storageRef = storage.getReference();

    StorageReference imageRef = storageRef.child("images/user_images");

    public TopAdapter(Context ctx, List<User> usersTop, List<String> usernamesTop) {
        this.inflater = LayoutInflater.from(ctx);
        this.usersTop = usersTop;
        this.usernamesTop = usernamesTop;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.top_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        holder.userPlace.setText(String.valueOf(position+1));
        holder.userScore.setText(String.valueOf(usersTop.get(position).karma));
        String currentUsername = usernamesTop.get(position);
        holder.username.setText(currentUsername);
        imageRef.child(currentUsername+".jpeg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(holder.userImage.getContext())
                        .load(uri)
                        .placeholder(R.mipmap.ic_launcher_foreground)
                        .circleCrop()
                        .into(holder.userImage);
            }
        });
        //TODO: Make cute dialog with another users in top...

        /*
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(v.getContext()).setTitle(currentUsername)
                        .setView(R.layout.user_profile_dialog)
                        .setCancelable(true)
                        .setNegativeButton(R.string.exit_string, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return usersTop.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        final TextView username, userScore, userPlace;
        final ImageView userImage;
        final MaterialCardView cardView;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.username_top);
            userScore = itemView.findViewById(R.id.user_score_top);
            userPlace = itemView.findViewById(R.id.user_place_top);
            userImage = itemView.findViewById(R.id.user_image_top);
            cardView = itemView.findViewById(R.id.top_card_view);
        }
    }
}
