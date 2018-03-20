package com.nemanja97.androidposts;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class PostsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);
    }

    public void createPostClick(View view) {
        Intent intent = new Intent(PostsActivity.this, CreatePostActivity.class);
        startActivity(intent);
    }

    public void readPostClick(View view) {
        Intent intent = new Intent(PostsActivity.this, ReadPostActivity.class);
        startActivity(intent);
    }

    public void settingClick(View view) {
        Intent intent = new Intent(PostsActivity.this, SettingsActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
