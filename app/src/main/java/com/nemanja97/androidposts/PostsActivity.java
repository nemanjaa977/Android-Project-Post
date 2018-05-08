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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import com.nemanja97.androidposts.adapters.DrawerListAdapter;
import com.nemanja97.androidposts.adapters.ListViewAdapter;
import com.nemanja97.androidposts.model.NavItem;
import com.nemanja97.androidposts.model.Post;
import com.nemanja97.androidposts.model.User;


public class PostsActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private RelativeLayout mDrawerPane;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private ArrayList<NavItem> mNavItems = new ArrayList<NavItem>();
    private ListViewAdapter listViewAdapter;
    ArrayList<Post> posts=new ArrayList<>();
    private Post post1;
    private Post post2;
    private SharedPreferences sharedPreferences;
    private boolean sortPostByDate;
    private boolean sortPostByPopularity;

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

        Bitmap b = BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher);
        User user = new User(1, "Petar", b, "pera", "123", null, null);
        User user2 = new User(2, "Aljosa", b, "aljosa", "12345", null, null);
        Date date = new Date();
        Date date2 = new Date();
        post1=new Post(1, "Transformers: The Last Knight", "The Last Knight Official Trailer - Teaser (2017) - Michael Bay Movie", b, user, date, null, null, null, 12, 3);
        post2=new Post(2, "Fast & Furious 8 ", "On the heels of 2015’s Fast & Furious 7", b, user2, date2, null, null, null, 11, 1);

        posts.add(post1);
        posts.add(post2);

        ListView listVieww = findViewById(R.id.listPost);

        ListViewAdapter listViewAdapter = new ListViewAdapter(this, posts);
        listVieww.setAdapter(listViewAdapter);
        listVieww.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent openReadPost = new Intent(PostsActivity.this, ReadPostActivity.class);
                startActivity(openReadPost);
            }
        });

        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
        consultPreference();

    }

    private void consultPreference(){
        sortPostByDate=sharedPreferences.getBoolean(getString(R.string.prefer_sort_post_date_key),false);
        sortPostByPopularity=sharedPreferences.getBoolean(getString(R.string.sort_post_popularity_key),false);

        if(sortPostByDate == true){
            sortDate();
        }
        if(sortPostByPopularity == true){
            sortByPopularity();
        }
    }

    public void sortDate(){
        Collections.sort(posts, new Comparator<Post>() {
            @Override
            public int compare(Post post, Post t1) {
                return post1.getDate().compareTo(post.getDate());
            }
        });


        listViewAdapter.notifyDataSetChanged();
    }

    public void sortByPopularity(){

        Collections.sort(posts, new Comparator<Post>() {
            @Override
            public int compare(Post post, Post t1) {
                int first;
                int second ;
                first = post.getLikes() - post.getDislikes();
                second = post1.getLikes() - post1.getDislikes();
                return Integer.valueOf(second).compareTo(first);
            }
        });


        listViewAdapter.notifyDataSetChanged();
    }

    private void prepareMenu(ArrayList<NavItem> mNavItems ){
        mNavItems.add(new NavItem(getString(R.string.settings), "", R.drawable.settings_outline));
        mNavItems.add(new NavItem(getString(R.string.create_label), "", R.drawable.plus_circle_outline));

    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id == R.id.action_setting){
            Intent intent=new Intent(PostsActivity.this,SettingsActivity.class);
            startActivity(intent);
        }else if(id == R.id.action_add){
            Context c = getBaseContext();
            Toast toast = Toast.makeText(c,"Click create post.",Toast.LENGTH_SHORT);
            toast.show();
        }
        else if(id == R.id.action_search){
            Context cc = getBaseContext();
            Toast toast = Toast.makeText(cc,"Click search post.",Toast.LENGTH_SHORT);
            toast.show();
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
        }else if(position == 1){
            Intent preference = new Intent(PostsActivity.this,CreatePostActivity.class);
            startActivity(preference);
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

//    public void createPostClick(View view) {
//        Intent intent = new Intent(PostsActivity.this, CreatePostActivity.class);
//        startActivity(intent);
//    }
//
//    public void readPostClick(View view) {
//        Intent intent = new Intent(PostsActivity.this, ReadPostActivity.class);
//        startActivity(intent);
//    }
//
//    public void settingClick(View view) {
//        Intent intent = new Intent(PostsActivity.this, SettingsActivity.class);
//        startActivity(intent);
//    }

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
