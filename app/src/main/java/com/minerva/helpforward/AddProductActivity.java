package com.minerva.helpforward;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.FirebaseDatabase;

public class AddProductActivity extends AppCompatActivity {
    /*_db.getReference("tasks").child(taskName.getText().toString()).child("description").setValue(taskDescription.getText().toString());
                            _db.getReference("tasks").child(taskName.getText().toString()).child("gems").setValue(Long.parseLong(taskPrice.getText().toString()));
                            _db.getReference("tasks").child(taskName.getText().toString()).child("lat").setValue(markerLatLng[0].latitude);
                            _db.getReference("tasks").child(taskName.getText().toString()).child("lon").setValue(markerLatLng[0].longitude);
                            _db.getReference("tasks").child(taskName.getText().toString()).child("name").setValue(taskName.getText().toString());*/
    FirebaseDatabase _db = FirebaseDatabase.getInstance();
    EditText productName, productDescription, productPrice, productURL;
    Button addProductButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        productName = findViewById(R.id.product_name_add);
        productDescription = findViewById(R.id.product_description_add);
        productPrice = findViewById(R.id.product_price_add);
        productURL = findViewById(R.id.product_uri_add);
        addProductButton = findViewById(R.id.add_product_button);

        addProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (productDescription.getText().toString() == ""){
                    Snackbar.make(v, "Set correct product description", Snackbar.LENGTH_LONG).show();
                }else if (productName.getText().toString() == ""){
                    Snackbar.make(v, "Set correct product name", Snackbar.LENGTH_LONG).show();
                }else if (productPrice.getText().toString() == ""){
                    Snackbar.make(v, "Set correct product price", Snackbar.LENGTH_LONG).show();
                }else if (productURL.getText().toString() == ""){
                    Snackbar.make(v, "Set correct product URL", Snackbar.LENGTH_LONG).show();
                } else {
                    _db.getReference("products").child(productName.getText().toString()).child("description").setValue(productDescription.getText().toString());
                    _db.getReference("products").child(productName.getText().toString()).child("price").setValue(Long.parseLong(productPrice.getText().toString()));
                    _db.getReference("products").child(productName.getText().toString()).child("url").setValue(productURL.getText().toString());
                    _db.getReference("products").child(productName.getText().toString()).child("name").setValue(productName.getText().toString());
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                }
            }
        });

    }
}