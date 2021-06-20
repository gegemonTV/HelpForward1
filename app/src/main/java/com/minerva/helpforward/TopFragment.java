package com.minerva.helpforward;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;


public class TopFragment extends Fragment {

    FirebaseDatabase _db = FirebaseDatabase.getInstance();
    DatabaseReference _ref = _db.getReference();

    List<User> usersTop = new ArrayList<>();
    List<String> usernamesTop = new ArrayList<>();

    RecyclerView topView;

    TopAdapter topAdapter;

    public TopFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_top, container, false);

        topView = (RecyclerView) view.findViewById(R.id.top_recycler_view);

        topAdapter = new TopAdapter(getContext(), usersTop, usernamesTop);

        topView.setAdapter(topAdapter);
        _ref.child("users").orderByChild("karma").limitToLast(10).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                usersTop.clear();
                usernamesTop.clear();
                for (DataSnapshot snap : snapshot.getChildren()){
                    usersTop.add(snap.getValue(User.class));
                    usernamesTop.add(snap.getKey());
                    /*Log.d("Usersч", usersTop.peek().toString());
                    Log.d("Usersч", usernamesTop.peek());*/
                }
                Collections.reverse(usernamesTop);
                Collections.reverse(usersTop);
                topAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Log.d("DBERR", error.getMessage());
            }
        });


        return view;
    }
}