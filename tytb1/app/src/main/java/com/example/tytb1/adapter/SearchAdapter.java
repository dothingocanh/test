package com.example.tytb1.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.tytb1.Config.GlideApp;
import com.example.tytb1.Model.User;
import com.example.tytb1.R;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
    private static List<User> users = new ArrayList<>();
    private OnItemClickListener onItemClickListener;
    public interface OnItemClickListener {
        void onItemClick(User user);
    }
    public SearchAdapter(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
    public void setUsers(List<User> users) {
        this.users = users;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new ViewHolder(view, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = users.get(position);
        holder.bind(user);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView userNameTextView, email;
        private ImageView userImageView;

       public ViewHolder(View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            userNameTextView = itemView.findViewById(R.id.userNameTextView);
            userImageView = itemView.findViewById(R.id.userImageView);
            email = itemView.findViewById(R.id.email);
           itemView.setOnClickListener(v -> {
               if (onItemClickListener != null) {
                   int position = getAdapterPosition();
                   if (position != RecyclerView.NO_POSITION) {
                       onItemClickListener.onItemClick(users.get(position));
                   }
               }
           });
       }

        void bind(User user) {
            userNameTextView.setText(user.getFullName());
            email.setText(user.getBio());
            GlideApp.with(itemView.getContext())
                    .load(user.getImage())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .dontTransform()
//                    .override(com.bumptech.glide.request.target.Target.SIZE_ORIGINAL)
                    .into(userImageView);
        }
    }
}