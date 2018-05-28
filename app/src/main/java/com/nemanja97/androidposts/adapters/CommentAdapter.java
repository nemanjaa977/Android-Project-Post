package com.nemanja97.androidposts.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.nemanja97.androidposts.PostsActivity;
import com.nemanja97.androidposts.R;
import com.nemanja97.androidposts.ReadPostActivity;
import com.nemanja97.androidposts.model.Comment;
import com.nemanja97.androidposts.service.CommentService;
import com.nemanja97.androidposts.service.ServiceUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentAdapter extends ArrayAdapter<Comment> {
    private Comment comment;
    private Context context;

    public CommentAdapter(Context context, List<Comment> comments){
        super(context,0,comments);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int pozicija, @Nullable View convertView, @NonNull ViewGroup parent){
        comment = getItem(pozicija);
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.list_comment_items, null);
        TextView description = (TextView) convertView.findViewById(R.id.sadrzajComment);
        Button button = (Button) convertView.findViewById((R.id.deleteCommentare));

        description.setText(comment.getDescription());

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteComment();
                Intent intent = new Intent(context.getApplicationContext(), PostsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                context.getApplicationContext().startActivity(intent);
            }
        });

        return convertView;
    }

    public void deleteComment(){
        CommentService commentService = ServiceUtils.commentService;
        Call<Void>call= commentService.deleteComment(comment.getId());

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(getContext(),"Delete comment.",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
        this.notifyDataSetChanged();
    }

}
