package com.nemanja97.androidposts;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.nemanja97.androidposts.model.User;
import com.nemanja97.androidposts.service.ServiceUtils;
import com.nemanja97.androidposts.service.UserService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    ServiceUtils serviceUtils;
    UserService userService;
    User user;
    String username;
    String password;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPreferences = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
    }

    public void login(final String username, final String password){
        userService = ServiceUtils.userService;
        Call<User> call = userService.findByUsername(username);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                user = response.body();
                if(user !=null)
                {
                    if(user.getUsername().equals(username) && user.getPassword().equals(password)){
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("logovani", user.getUsername());
                        editor.commit();
                        Intent intent = new Intent(LoginActivity.this,PostsActivity.class);
                        startActivity(intent);
                        finish();
                    }else{
                        Context c = getBaseContext();
                        Toast toast = Toast.makeText(c,"Username and password are incorrect!",Toast.LENGTH_LONG);
                        toast.show();
                    }

                }else{
                    Context c = getBaseContext();
                    Toast toast = Toast.makeText(c,"User dont't exist.Please sing up!",Toast.LENGTH_LONG);
                    toast.show();
                }

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }

    public void loginClick(View view){
        EditText usernameText = (EditText)findViewById(R.id.textUsername);
        EditText passwordText = (EditText)findViewById(R.id.textPass);

        username = usernameText.getText().toString();
        password = passwordText.getText().toString();
        login(username, password);
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
