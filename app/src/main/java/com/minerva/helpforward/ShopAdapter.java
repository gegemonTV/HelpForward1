package com.minerva.helpforward;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.ViewHolder> {

    private final LayoutInflater inflater;
    private final List<Product> products;
    private FirebaseDatabase _db = FirebaseDatabase.getInstance();
    private DatabaseReference _ref = _db.getReference().child("products");
    public ShopAdapter(Context context, List<Product> products) {
        this.inflater = LayoutInflater.from(context);
        this.products = products;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.shop_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        Product product = products.get(position);

        holder.productTitle.setText(product.getName());

        if (product.getDescription().length() <= 80){
            holder.productDescription.setText(product.getDescription());
        } else {
            holder.productDescription.setText(product.getDescription().substring(0, 80) + "...");
        }
        holder.productPrice.setText(String.valueOf(product.getPrice()));
        holder.showInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = LayoutInflater.from(v.getContext());
                View view = inflater.inflate(R.layout.information_product_dialog, null);

                TextView productTitle = view.findViewById(R.id.title_product_text);
                TextView productDescription = view.findViewById(R.id.description_product_text);
                TextView productPrice = view.findViewById(R.id.price_product_text);

                productDescription.setText(product.getDescription());
                productPrice.setText(String.valueOf(product.getPrice()));
                productTitle.setText(product.getName());

                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(v.getContext())
                        .setView(view)
                        .setNeutralButton("Buy", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent i = new Intent(Intent.ACTION_VIEW, product.getUrl());
                                v.getContext().startActivity(i);
                            }
                        })
                        .setCancelable(true)
                        .setNegativeButton("Hide", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                builder.create().show();
            }
        });
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();

        holder.buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _ref.child("users").child(mUser.getDisplayName().toString()).child("crystals").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<DataSnapshot> task) {
                        if ((long)task.getResult().getValue() >= product.getPrice()) {
                            Intent i = new Intent(Intent.ACTION_VIEW, product.getUrl());
                            v.getContext().startActivity(i);
                        } else{
                            Snackbar.make(v, "You don't have enough gems", Snackbar.LENGTH_LONG).show();
                        }
                    }
                });

            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        final TextView productTitle, productDescription, productPrice;
        final Button buyButton, showInfoButton;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            productTitle = itemView.findViewById(R.id.product_title);
            productDescription = itemView.findViewById(R.id.product_description);
            productPrice = itemView.findViewById(R.id.product_price);

            buyButton = itemView.findViewById(R.id.buy_button);
            showInfoButton = itemView.findViewById(R.id.show_shop_info_button);
        }
    }
}
