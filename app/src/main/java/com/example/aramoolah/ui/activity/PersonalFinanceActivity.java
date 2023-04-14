package com.example.aramoolah.ui.activity;

import android.os.Bundle;

import com.example.aramoolah.R;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.MenuInflater;

import androidx.core.view.MenuProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.aramoolah.databinding.ActivityPersonalFinanceBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.view.Menu;
import android.view.MenuItem;

public class PersonalFinanceActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private AppBarConfiguration bottomBarConfiguration;
    private ActivityPersonalFinanceBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPersonalFinanceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.menu_main, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                return false;
            }
        });
        // Top toolbar
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.personal_finance_fragment_container_view);
        assert navHostFragment != null;
        NavController navController = navHostFragment.getNavController();
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        //BottomNavBar
        BottomNavigationView bottomNav = binding.bottomNav;
        bottomBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_dashboard_fragment, R.id.nav_history_fragment
        ).build();
        NavigationUI.setupWithNavController(binding.bottomNav, navController);
        NavigationUI.setupActionBarWithNavController(this, navController, bottomBarConfiguration);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.personal_finance_fragment_container_view);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}