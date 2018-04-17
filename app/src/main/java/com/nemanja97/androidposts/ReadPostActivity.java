package com.nemanja97.androidposts;

import android.content.Context;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nemanja97.androidposts.adapters.DrawerListAdapter;
import com.nemanja97.androidposts.model.Comment;
import com.nemanja97.androidposts.model.NavItem;
import com.nemanja97.androidposts.model.Post;
import com.nemanja97.androidposts.model.Tag;
import com.nemanja97.androidposts.model.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReadPostActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private RelativeLayout mDrawerPane;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private ArrayList<NavItem> mNavItems = new ArrayList<NavItem>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_post);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final android.support.v7.app.ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.menu);
        }

        Bitmap b = BitmapFactory.decodeResource(getResources(),R.drawable.ic_launcher_background);
        User user = new User(1, "Petar", b, "pera", "123", null, null);
        Date date = new Date();
        Post post=new Post(1, "Transformers: The Last Knight", "The Last Knight Official Trailer - Teaser (2017) - Michael Bay Movie", b, user, date, null, null, null, 12, 3);
        List<Post> posts = new ArrayList<Post>();
        posts.add(post);

        Tag tag = new Tag(1, "#good", posts);
        List<Tag> tags = new ArrayList<Tag>();
        tags.add(tag);
        post.setTags(tags);

        Comment comm = new Comment(1,"okComment","Is this the last transformers movies or last movie directed by Michael Bay?",user,date,post,4,3,null);
        List<Comment> comme = new ArrayList<Comment>();
        comme.add(comm);
        post.setComments(comme);

        TextView tv = (TextView)findViewById(R.id.textTitle);
        tv.setText(post.getTitle());
        TextView td = (TextView)findViewById(R.id.textDescription);
        td.setText(post.getDescription());
        ImageView im = (ImageView) findViewById(R.id.imagePost);
        im.setImageBitmap(post.getPhoto());
        TextView tt = (TextView)findViewById(R.id.textTag);
        tt.setText(post.getTags().get(0).getName());
        TextView tu = (TextView)findViewById(R.id.textAuthor);
        tu.setText(post.getAuthor().getUsername());
        TextView tdate = (TextView)findViewById(R.id.textDate);
        tdate.setText(post.getDate().toString());
        TextView tl = (TextView)findViewById(R.id.textLocation);
        tl.setText("");
        TextView tc = (TextView)findViewById(R.id.textComment);
        tc.setText(post.getComments().get(0).getDescription());
        TextView tlike = (TextView)findViewById(R.id.textLike);
        tlike.setText(String.valueOf(post.getLikes()));
        TextView tdislike = (TextView)findViewById(R.id.textDislike);
        tdislike.setText(String.valueOf(post.getDislikes()));

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

    }

    private void prepareMenu(ArrayList<NavItem> mNavItems ){
        mNavItems.add(new NavItem(getString(R.string.settings), "", R.drawable.settings_outline));
        mNavItems.add(new NavItem(getString(R.string.posts_label), "", R.drawable.ic_launcher_background));

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
            Intent intent=new Intent(ReadPostActivity.this,SettingsActivity.class);
            startActivity(intent);
        }else if(id == R.id.action_add){
            Context c = getBaseContext();
            Toast toast = Toast.makeText(c,"Click create post.",Toast.LENGTH_SHORT);
            toast.show();
        }else if(id == R.id.action_search){
            Context cc = getBaseContext();
            Toast toast = Toast.makeText(cc,"Click search post.",Toast.LENGTH_SHORT);
            toast.show();
        }
        return super.onOptionsItemSelected(item);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItemFromDrawer(position);
        }
    }

    private void selectItemFromDrawer(int position) {
        if(position == 0){
            Intent preference = new Intent(ReadPostActivity.this,SettingsActivity.class);
            startActivity(preference);
        }else if(position == 1){
            Intent preference = new Intent(ReadPostActivity.this,PostsActivity.class);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
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
