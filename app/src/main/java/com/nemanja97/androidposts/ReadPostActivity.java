package com.nemanja97.androidposts;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nemanja97.androidposts.adapters.DrawerListAdapter;
import com.nemanja97.androidposts.model.Comment;
import com.nemanja97.androidposts.model.NavItem;
import com.nemanja97.androidposts.model.Post;
import com.nemanja97.androidposts.model.Tag;
import com.nemanja97.androidposts.model.User;
import com.nemanja97.androidposts.service.CommentService;
import com.nemanja97.androidposts.service.PostService;
import com.nemanja97.androidposts.service.ServiceUtils;
import com.nemanja97.androidposts.service.UserService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReadPostActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private RelativeLayout mDrawerPane;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private SharedPreferences sharedPreferences;
    private ArrayList<NavItem> mNavItems = new ArrayList<NavItem>();
    private String jsonMyObject;
    private CommentService commentService;
    List<Comment> comments;
    private PostService postService;
    private EditText text_new_comment;
    private int postID;
    private LinearLayout linearLayout;
    private UserService userService;
    private User logged;
    private String userJson;
    private String post_like;
    private String post_dislike;
    private int commentID;
    private Post post;
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

        sharedPreferences= getSharedPreferences("MyPref",Context.MODE_PRIVATE);
        userJson=sharedPreferences.getString("logovani","");
        logged=new Gson().fromJson(userJson, User.class);

        linearLayout =(LinearLayout)findViewById(R.id.mainContent);

        jsonMyObject = getIntent().getStringExtra("Post");
         post = new Gson().fromJson(jsonMyObject, Post.class);
        postID=post.getId();

        TextView tv = (TextView)findViewById(R.id.textTitle);
        tv.setText(post.getTitle());
        TextView td = (TextView)findViewById(R.id.textDescription);
        td.setText(post.getDescription());
        ImageView im = (ImageView) findViewById(R.id.imagePost);
        im.setImageBitmap(post.getPhoto());
        TextView tt = (TextView)findViewById(R.id.textTag);
      //  tt.setText(post.getTags().get(0).getName());
        TextView tu = (TextView)findViewById(R.id.textAuthor);
        tu.setText(post.getAuthor().getUsername());
        TextView tdate = (TextView)findViewById(R.id.textDate);
        String printDate=new SimpleDateFormat("dd.MM.yyyy HH:mm").format(post.getDate());
        tdate.setText(printDate);
        TextView tl = (TextView)findViewById(R.id.textLocation);
        tl.setText("");

        commentService = ServiceUtils.commentService;
        Call call = commentService.getAllComment(postID);

        postService=ServiceUtils.postService;
        Call calll = postService.getPostOneComment(commentID); // sta sa ovim!!!!!!!!

        call.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                comments = response.body();
                for( Comment c:comments )
                {
                    TextView textView = new TextView(getApplicationContext());
                    textView.setText(c.getDescription());
                    textView.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    linearLayout.addView(textView);

                    if (logged.getUsername() != c.getAuthor().getUsername()) {


                        Button button = new Button(getApplicationContext());
                        button.setText("Delete comment");
                        button.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                deleteComment(commentID);
                                Context c = getBaseContext();
                                Toast toast = Toast.makeText(c, "Delete comment.", Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        });
                        button.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                        linearLayout.addView(button);
                    }
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });

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

        text_new_comment = findViewById(R.id.addNewComment);

        invalidateOptionsMenu();

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
        if(id == R.id.deletePost){
            deletePost(postID);
        }
        return super.onOptionsItemSelected(item);
    }

    public void addNewLike(View view) {
        TextView like=(TextView) view.findViewById(R.id.textLike);
        post_like = like.getText().toString();
        int brojLajkova = Integer.parseInt(post_like) + 1;
        like.setText(brojLajkova);

    }

    public void addNewDislike(View view) {
        TextView dislike = (TextView) findViewById(R.id.textDislike);
        post_dislike = dislike.getText().toString();
        int brojDislajkova = Integer.parseInt(post_dislike) +1;
        dislike.setText(brojDislajkova);
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
        }else if(position == 2){
            Intent intent = new Intent(ReadPostActivity.this,LoginActivity.class);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.read_menu, menu);
        if (!post.getAuthor().getUsername().equals(logged.getUsername())){
            MenuItem mainMenu = menu.findItem(R.id.deletePost);
            mainMenu.setVisible(false);
        }
        return super.onCreateOptionsMenu(menu);
    }

    public void deletePost(Integer id){
        postService=ServiceUtils.postService;
        Call<Void> call = postService.deletePost(id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Context c = getBaseContext();
                Toast toast = Toast.makeText(c,"Delete post.",Toast.LENGTH_SHORT);
                toast.show();
                Intent i = new Intent(getApplicationContext(), PostsActivity.class);
                startActivity(i);
                finish();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }

    public void deleteComment(Integer id){
        commentService = ServiceUtils.commentService;
        Call<Void> call = commentService.deleteComment(id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Context c = getBaseContext();
                Toast toast = Toast.makeText(c,"Delete comment.",Toast.LENGTH_SHORT);
                toast.show();
                Intent i = new Intent(ReadPostActivity.this, PostsActivity.class);
                startActivity(i);
                finish();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }

    public void addNewComment(View view){
        Comment comment = new Comment();

        String description = text_new_comment.getText().toString();
        comment.setTitle("");
        comment.setDescription(description);
        Date date = Calendar.getInstance().getTime();
        comment.setDate(date);
        comment.setPost(post);
        comment.setLikes(0);
        comment.setDislikes(0);
        comment.setStatus(null);

        comment.setAuthor(logged);
        commentService= ServiceUtils.commentService;
        postService=ServiceUtils.postService;
        Call<Comment> call = commentService.createComment(comment);
        call.enqueue(new Callback<Comment>() {
            @Override
            public void onResponse(Call<Comment> call, Response<Comment> response) {
                Context c = getBaseContext();
                Toast toast = Toast.makeText(c,"Created new comment.",Toast.LENGTH_SHORT);
                toast.show();
                Intent i = new Intent(getApplicationContext(), PostsActivity.class);
                startActivity(i);
                finish();
            }

            @Override
            public void onFailure(Call<Comment> call, Throwable t) {

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
