package com.globalshops.customer.shoeShop;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.globalshops.customer.shoeShop.ui.bottom_nav.ShopListFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private BottomNavigationView bottomNavigationView;

    @Inject
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);



        setBottomNavigationView();
        //isLoggedIn();
    }

    private void setBottomNavigationView(){
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        showHideBottomNav(navController);
    }

    private void showHideBottomNav(NavController navController){
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                if (destination.getId() == R.id.login_fragment){
                    bottomNavigationView.setVisibility(View.GONE);
                }else {
                    bottomNavigationView.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();
        return NavigationUI.onNavDestinationSelected(item, navController)
                ||super.onOptionsItemSelected(item);
    }


    private void isLoggedIn(){
        if (auth.getCurrentUser() == null){
           bottomNavigationView.setVisibility(View.GONE);
        }else {
            bottomNavigationView.setVisibility(View.VISIBLE);
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        //isLoggedIn();

    }

    @Override
    protected void onResume() {
        super.onResume();
        //isLoggedIn();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //isLoggedIn();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //isLoggedIn();
    }
}