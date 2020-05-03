package com.example.tracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private BottomNavigationView bottomNav;
    private AlertDialog exitDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,
                    new BookmarksFragment()).commit();
        }
        bottomNav = findViewById(R.id.navigation);
        bottomNav.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        if (exitDialog == null) {
            exitDialog = new AlertDialog.Builder(this)
                    .setIcon(R.drawable.ic_warning_black_24dp)
                    .setTitle("Exit")
                    .setMessage("Are you sure you want to exit???")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            MainActivity.super.onBackPressed();
                        }

                    })
                    .setNegativeButton("No", null)
                    .create();
        }
        exitDialog.show();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment newFragment;
        switch (item.getItemId()){
            case R.id.navigation_notifications:
                newFragment = new NotificationsFragment();
                setTitle("Your notifications");
                break;
            default:
                newFragment = new BookmarksFragment();
                setTitle("Your bookmarks");
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragmentContainer,newFragment);
        transaction.commit();
        return true;
    }
}
