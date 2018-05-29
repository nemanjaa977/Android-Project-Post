package com.nemanja97.androidposts;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.content.SharedPreferences;
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
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.google.gson.Gson;
import com.nemanja97.androidposts.adapters.DrawerListAdapter;
import com.nemanja97.androidposts.adapters.ListViewAdapter;
import com.nemanja97.androidposts.model.NavItem;
import com.nemanja97.androidposts.model.Post;
import com.nemanja97.androidposts.model.User;
import com.nemanja97.androidposts.service.PostService;
import com.nemanja97.androidposts.service.ServiceUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PostsActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private RelativeLayout mDrawerPane;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private ArrayList<NavItem> mNavItems = new ArrayList<NavItem>();
    private ListViewAdapter listViewAdapter;
    List<Post> posts;
    private Post post1;
    private Post post2;
    private SharedPreferences sharedPreferences;
    private boolean sortPostByDate;
    private boolean sortPostByPopularity;
    private PostService postService;
    private ListView listVieww;
    private String userJson;
    private User logged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final android.support.v7.app.ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.menu);
        }

        sharedPreferences= getSharedPreferences("MyPref",Context.MODE_PRIVATE);
        userJson=sharedPreferences.getString("logovani","");
        logged=new Gson().fromJson(userJson,User.class);

        prepareMenu(mNavItems);

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
                getActionBar().setTitle(mTitle);
                getSupportActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                getSupportActionBar().setTitle("AndroidPost");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

          listVieww= findViewById(R.id.listPost);

        postService = ServiceUtils.postService;

        Call call = postService.getAll();
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                posts = response.body();
                listViewAdapter = new ListViewAdapter(getApplicationContext(), posts);
                listVieww.setAdapter(listViewAdapter);
            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });


        listVieww.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Post post=posts.get(position);
                Intent openReadPost = new Intent(PostsActivity.this, ReadPostActivity.class);
                openReadPost.putExtra("Post",new Gson().toJson(post));
                startActivity(openReadPost);
            }
        });

        TextView tt = (TextView)findViewById(R.id.userName);
        tt.setText(logged.getUsername());

//        consultPreference();

    }

//    private void consultPreference(){
//        sortPostByDate=sharedPreferences.getBoolean(getString(R.string.prefer_sort_post_date_key),false);
//        sortPostByPopularity=sharedPreferences.getBoolean(getString(R.string.sort_post_popularity_key),false);
//
//        if(sortPostByDate == true){
//            sortDate();
//        }
//        if(sortPostByPopularity == true){
//            sortByPopularity();
//        }
//    }

//    public void sortDate(){
//        Collections.sort(posts, new Comparator<Post>() {
//            @Override
//            public int compare(Post post, Post t1) {
//                return post1.getDate().compareTo(post.getDate());
//            }
//        });
//
//
//        listViewAdapter.notifyDataSetChanged();
//    }

//    public void sortByPopularity(){
//
//        Collections.sort(posts, new Comparator<Post>() {
//            @Override
//            public int compare(Post post, Post t1) {
//                int first;
//                int second ;
//                first = post.getLikes() - post.getDislikes();
//                second = post1.getLikes() - post1.getDislikes();
//                return Integer.valueOf(second).compareTo(first);
//            }
//        });
//
//
//        listViewAdapter.notifyDataSetChanged();
//    }

    private void prepareMenu(ArrayList<NavItem> mNavItems ){
        mNavItems.add(new NavItem(getString(R.string.settings), "Open all settings.", R.drawable.settings_outline));
        mNavItems.add(new NavItem(getString(R.string.create_label), "Open create post.", R.drawable.plus_circle_outline));
        mNavItems.add(new NavItem("Log out", "Log out now.", R.drawable.ic_launcher_background));

    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id == R.id.action_add){
            Context c = getBaseContext();
            Toast toast = Toast.makeText(c,"Click create post.",Toast.LENGTH_SHORT);
            toast.show();
            Intent intent = new Intent(PostsActivity.this, CreatePostActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
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
            Intent preference = new Intent(PostsActivity.this,SettingsActivity.class);
            startActivity(preference);
        }else if(position == 1) {
            Intent preference = new Intent(PostsActivity.this, CreatePostActivity.class);
            startActivity(preference);
        }else if(position == 2){
            Intent intent = new Intent(PostsActivity.this,LoginActivity.class);
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
//        finish();
//        startActivity(getIntent());
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
