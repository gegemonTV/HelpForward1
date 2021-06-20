package com.minerva.helpforward;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Debug;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ShopFragment extends Fragment {

    private RecyclerView recyclerView;

    private FloatingActionButton addProductButton;
    private ShopAdapter shopAdapter;

    private List<Product> products = new ArrayList<Product>();

    private FirebaseDatabase _db = FirebaseDatabase.getInstance();

    private IsCompanyViewModel mViewModel;
    private boolean isCompany;

    private DatabaseReference _ref = _db.getReference().child("products");

    public static ShopFragment newInstance() {
        return new ShopFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop, container, false);
        addProductButton = view.findViewById(R.id.floating_add_product);

        mViewModel = new ViewModelProvider(getActivity()).get(IsCompanyViewModel.class);
        mViewModel.getIsCompany().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                isCompany = aBoolean;
                if (isCompany){
                    addProductButton.setVisibility(View.VISIBLE);
                }
                else {
                    addProductButton.setVisibility(View.GONE);
                }
            }
        });

        addProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), AddProductActivity.class));
            }
        });
        _ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                products.clear();
                Log.d("SHOP_UPD", "Started updating events");
                for (DataSnapshot snap: snapshot.getChildren()){
                    products.add(snap.getValue(Product.class));
                    Log.d("SHOP_UPD", snap.getValue(Product.class).getName());
                }
                shopAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        recyclerView = view.findViewById(R.id.shop_recycler_view);

        shopAdapter = new ShopAdapter(getContext(), products);

        recyclerView.setAdapter(shopAdapter);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

}