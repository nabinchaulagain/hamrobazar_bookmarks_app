package com.example.tracker;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tracker.models.Bookmark;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BottomNavigationActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    BottomNavigationView bottomNavigationView;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public void initialize(){
        bottomNavigationView = findViewById(R.id.navigation);
        if(getIntent().getIntExtra("actionId",0) != 0){
            bottomNavigationView.setSelectedItemId(getIntent().getIntExtra("actionId",0));
        }
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == bottomNavigationView.getSelectedItemId()){
            return false;
        }
        Intent intent;
        switch (item.getItemId()){
            case R.id.navigation_notifications:
                intent = new Intent(this,NotificationsActivity.class);
                intent.putExtra("actionId",R.id.navigation_notifications);
                break;
            default:
                intent = new Intent(this, BookmarksActivity.class);
        }
        startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
        return true;
    }
}
