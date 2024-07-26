package com.example.tytb1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import com.example.tytb1.Config.GlideApp;
import com.example.tytb1.Model.Twit;
import com.example.tytb1.R;


import java.util.List;

public class TwitGridAdapter extends BaseAdapter {
    private Context context;
    private List<Twit> twitList;

    public TwitGridAdapter(Context context, List<Twit> twitList) {
        this.context = context;
        this.twitList = twitList;
    }

    @Override
    public int getCount() {
        return twitList.size();
    }

    @Override
    public Object getItem(int position) {
        return twitList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_grid, parent, false);
        }


        Twit twit = twitList.get(position);
//        TextView fullName = convertView.findViewById(R.id.fullName);
//        TextView content = convertView.findViewById(R.id.content);
//        TextView time = convertView.findViewById(R.id.time);
        ImageView image = convertView.findViewById(R.id.image);
//        ImageView profileImage = convertView.findViewById(R.id.profileImage);
//        TextView tvLikeCount = convertView.findViewById(R.id.tv_like_count);

        // set data
//        fullName.setText(twit.getUser().getFullName());
//        content.setText(twit.getContent());
////        time.setText(twit.getCreateAt().toString()); // Convert this to a human-readable format if necessary
//        tvLikeCount.setText(String.valueOf(twit.getTotalLikes()));
        // Load images using Glide
//        if (twit.getUser().getImage() != null) {
//            GlideApp.with(context)
//                    .load(twit.getUser().getImage())
//                    .override(100, 100) // Set fixed dimensions to avoid OOM
//                    .into(profileImage);
//        } else {
//            profileImage.setImageResource(android.R.drawable.ic_menu_gallery); // Default image
//        }

        if (twit.getImage() != null && !twit.getImage().isEmpty()) {
            GlideApp.with(context)
                    .load(twit.getImage())
                    .override(300, 300) // Set fixed dimensions to avoid OOM
                    .into(image);
            image.setVisibility(View.VISIBLE);
        } else {
            image.setVisibility(View.GONE);
        }

        return convertView;
    }
}