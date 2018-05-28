package com.nemanja97.androidposts.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nemanja97.androidposts.R;
import com.nemanja97.androidposts.model.Post;

import java.util.ArrayList;
import java.util.List;

public class ListViewAdapter extends ArrayAdapter<Post> {

    public ListViewAdapter(Context context, List<Post> posts){
        super(context,0,posts);
    }

    @NonNull
    @Override
    public View getView(int pozicija, @Nullable View convertView, @NonNull ViewGroup parent){
        Post post = getItem(pozicija);
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.list_view_items, null);
        TextView title = (TextView) convertView.findViewById(R.id.title_list);
        ImageView image = (ImageView) convertView.findViewById(R.id.image_list);

        title.setText(post.getTitle());
//        image.setImageResource(R.mipmap.transformers5);

        return convertView;
    }

}
