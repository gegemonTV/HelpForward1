package com.minerva.helpforward;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.appodeal.ads.Appodeal;
import com.explorestack.consent.Consent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import org.jetbrains.annotations.NotNull;

public class HomeActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;

    public static final String APP_PREFERENCES_FIRST_TIME = "is_first_time";

    DatabaseReference _ref = FirebaseDatabase.getInstance().getReference("users");
    FirebaseUser _user = FirebaseAuth.getInstance().getCurrentUser();

    IsCompanyViewModel mViewModel;

    private static BottomNavigationView navView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPreferences = getSharedPreferences("HelpForwardShared", MODE_PRIVATE);
        mViewModel = new ViewModelProvider(this).get(IsCompanyViewModel.class);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        sharedPreferences.edit().putBoolean(APP_PREFERENCES_FIRST_TIME, false).apply();
        mViewModel.setIsCompany(false);
        _ref.child(_user.getDisplayName()).child("is_company").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DataSnapshot> task) {
                try{
                    Log.d("MSG", task.getResult().getValue().toString() + 47);
                    if ((boolean)task.getResult().getValue() == true){
                        mViewModel.setIsCompany(true);
                    }
                    else {
                        mViewModel.setIsCompany(false);
                    }
                    Log.d("MSG", mViewModel.getIsCompany().getValue().toString() + 54);
                } catch (NullPointerException e){
                    e.printStackTrace();
                    mViewModel.setIsCompany(false);
                    Log.d("MSG", mViewModel.getIsCompany().getValue().toString() + 58);
                }
            }
        });



        Appodeal.initialize(this, "93a78ff14f344d2135bb5e09882a3045864eb38a51df1f28", Appodeal.BANNER);
        Appodeal.setBannerViewId(R.id.appodeal_banner);
        Appodeal.show(this, Appodeal.BANNER_TOP);

        navView = (BottomNavigationView) findViewById(R.id.nav_view);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);
    }

    @Override
    protected void onSaveInstanceState(@NonNull @NotNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    public static void setChoosedItem(@IdRes int id){
        navView.setSelectedItemId(id);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Appodeal.show(this, Appodeal.BANNER_TOP);
    }
}