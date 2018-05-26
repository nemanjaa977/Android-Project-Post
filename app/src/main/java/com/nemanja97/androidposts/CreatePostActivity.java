package com.nemanja97.androidposts;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nemanja97.androidposts.adapters.DrawerListAdapter;
import com.nemanja97.androidposts.model.NavItem;
import com.nemanja97.androidposts.model.Post;
import com.nemanja97.androidposts.model.User;
import com.nemanja97.androidposts.service.PostService;
import com.nemanja97.androidposts.service.ServiceUtils;
import com.nemanja97.androidposts.service.UserService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreatePostActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private RelativeLayout mDrawerPane;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private ArrayList<NavItem> mNavItems = new ArrayList<NavItem>();
    private SharedPreferences sharedPreferences;
    private PostService postService;
    private EditText text_title;
    private EditText text_description;
    private UserService userService;
    private User logged;
    private String userJson;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final android.support.v7.app.ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.menu);
        }

        prepareMenu(mNavItems);

        sharedPreferences= getSharedPreferences("MyPref",Context.MODE_PRIVATE);
        userJson=sharedPreferences.getString("logovani","");

        mTitle = mDrawerTitle = getTitle();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mDrawerList = (ListView) findViewById(R.id.navList);

        // Populate the Navigtion Drawer with options
        mDrawerPane = (RelativeLayout) findViewById(R.id.drawerPane);
        DrawerListAdapter adapter = new DrawerListAdapter(this, mNavItems);


        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        mDrawerList.setAdapter(adapter);

        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                toolbar,
                R.string.drawer_open,
                R.string.drawer_close
        ) {
            public void onDrawerClosed(View view) {
//                getActionBar().setTitle(mTitle);
                getSupportActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
//                getActionBar().setTitle(mDrawerTitle);
                getSupportActionBar().setTitle("AndroidPost");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        text_title = findViewById(R.id.newTextTitle);
        text_description = findViewById(R.id.newTextDescription);
    }

    private void prepareMenu(ArrayList<NavItem> mNavItems ){
        mNavItems.add(new NavItem(getString(R.string.settings), "", R.drawable.settings_outline));
        mNavItems.add(new NavItem(getString(R.string.posts_label), "", R.drawable.ic_launcher_background));
        mNavItems.add(new NavItem("Log out", "", R.drawable.ic_launcher_background));

    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id == R.id.okCreatePostAction){
            Context c = getBaseContext();
            Toast toast = Toast.makeText(c,"Click create post.",Toast.LENGTH_SHORT);
            toast.show();

        }else if(id == R.id.dontCreatePostAction){
            Context c = getBaseContext();
            Toast toast = Toast.makeText(c,"Click don't create post.",Toast.LENGTH_SHORT);
            toast.show();
            Intent i = new Intent(CreatePostActivity.this, PostsActivity.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.create_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItemFromDrawer(position);
        }
    }

    private void selectItemFromDrawer(int position) {
        if(position == 0){
            Intent preference = new Intent(CreatePostActivity.this,SettingsActivity.class);
            startActivity(preference);
        }else if(position == 1){
            Intent preference = new Intent(CreatePostActivity.this,PostsActivity.class);
            startActivity(preference);
        }else if(position == 2){
            Intent intent = new Intent(CreatePostActivity.this,LoginActivity.class);
            sharedPreferences.edit().clear().commit();
            startActivity(intent);
            finish();
        }else{
            Log.e("DRAWER", "Nesto van opsega!");
        }

        mDrawerList.setItemChecked(position, true);
        if(position != 1) // za sve osim za sync
        {
            setTitle(mNavItems.get(position).getmTitle());
        }
        mDrawerLayout.closeDrawer(mDrawerPane);
    }

    public void okCreateNewPost(View view){
        Post post = new Post();

        String title = text_title.getText().toString();
        String description = text_description.getText().toString();

        post.setTitle(title);
        post.setDescription(description);
        post.setLikes(0);
        post.setDislikes(0);
        Date date= Calendar.getInstance().getTime();
        post.setDate(date);

        post.setComments(null);
        post.setTags(null);
        post.setLocation(null);
        Bitmap b = BitmapFactory.decodeResource(getResources(),R.drawable.ic_launcher_background);
        post.setPhoto(b);


        logged=new Gson().fromJson(userJson,User.class);
        post.setAuthor(logged);
        postService=ServiceUtils.postService;
        Call<Post> call = postService.createPost(post);
        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                Context c = getBaseContext();
                Toast toast = Toast.makeText(c,"Created new post.",Toast.LENGTH_SHORT);
                toast.show();
                Intent i = new Intent(getApplicationContext(), PostsActivity.class);
                startActivity(i);
                finish();
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {

            }
        });
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
