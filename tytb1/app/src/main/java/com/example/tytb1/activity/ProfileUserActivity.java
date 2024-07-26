package com.example.tytb1.activity;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.tytb1.Api.SignApiService;
import com.example.tytb1.Config.GlideApp;

import com.example.tytb1.Model.Twit;
import com.example.tytb1.Model.User;
import com.example.tytb1.R;
import com.example.tytb1.TwitGridAdapter;


import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileUserActivity extends AppCompatActivity {

    private Long userId;
    private CircleImageView profileImage;
    private TextView displayName, bio, location, tvPosts, tvFollowers, tvFollowing, follow;
    private ProgressBar profileProgressBar;
    private GridView gridView;
    private TwitGridAdapter twitGridAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_user);

        profileImage = findViewById(R.id.profile_image);
        displayName = findViewById(R.id.display_name);
        bio = findViewById(R.id.description);
        location = findViewById(R.id.website);
        tvPosts = findViewById(R.id.tvPosts);
        tvFollowers = findViewById(R.id.tvFollowers);
        tvFollowing = findViewById(R.id.tvFollowing);
        profileProgressBar = findViewById(R.id.profileProgressBar);
        gridView = findViewById(R.id.gridView);

        follow = findViewById(R.id.follow);
        userId = getIntent().getLongExtra("userId", -1);
        if (userId != -1) {
            loadProfileDataUser(userId);
        } else {
            Toast.makeText(this, "Invalid user ID", Toast.LENGTH_SHORT).show();
        }

        follow.setOnClickListener(v -> handleFollowUser());
    
    }

    private void handleFollowUser() {
            SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
            String jwt = sharedPreferences.getString("jwt", null);
            if (jwt != null) {
                SignApiService.signApiService.followUser(userId, "Bearer " + jwt).enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.isSuccessful()) {
                            User user = response.body();
                            if (user != null) {
                                updateFollowButton(user.followed());
                                Toast.makeText(ProfileUserActivity.this, user.followed() ? "Followed user" : "Unfollowed user", Toast.LENGTH_SHORT).show();
                                tvFollowers.setText(String.valueOf(user.getFollowers().size()));
                                tvFollowing.setText(String.valueOf(user.getFollowing().size()));

                                refreshProfileData();

                            } else {
                                Toast.makeText(ProfileUserActivity.this, "Failed to update follow status", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(ProfileUserActivity.this, "Failed to follow/unfollow user", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Toast.makeText(ProfileUserActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(this, "JWT token not found", Toast.LENGTH_SHORT).show();
            }

    }

    private void refreshProfileData() {
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        String jwt = sharedPreferences.getString("jwt", null);
        loadProfileDataUser(userId);
        loadUserTwits(userId,jwt);
    }

    private void updateFollowButton(boolean followed) {
        if (followed) {
            follow.setText("Unfollow");
        } else {
            follow.setText("Follow");
        }
    }

    private void loadProfileDataUser(Long userId) {
        profileProgressBar.setVisibility(View.VISIBLE);
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        String jwt = sharedPreferences.getString("jwt", null);
        if (jwt != null) {
            Log.d("API_CALL_LOAD_TWIT", "JWT Token: " + jwt);
            SignApiService.signApiService.getUserById(userId, "Bearer " + jwt).enqueue(new Callback<User>() {

                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    profileProgressBar.setVisibility(View.GONE);
                    if (response.isSuccessful()) {
                        User user = response.body();
                        if (user != null) {
                            displayName.setText(user.getFullName());
                            bio.setText(user.getBio());
                            location.setText(user.getLocation());
                            //
//
                            Log.d("API_RESPONSE", "User data: " + user.toString());

                            Log.d("API_RESPONSE", "Following count: " + user.getFollowing().size());

                            tvFollowers.setText(String.valueOf(user.getFollowers().size()));
                            tvFollowing.setText(String.valueOf(user.getFollowing().size()));
                            GlideApp.with(ProfileUserActivity.this)
                                    .load(user.getImage())
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .dontTransform()
//                                    .override(com.bumptech.glide.request.target.Target.SIZE_ORIGINAL)
                                    .into(profileImage);
                            updateFollowButton(user.followed());
                            loadUserTwits(userId, jwt);

                        } else {
                            profileProgressBar.setVisibility(View.GONE);
                            Toast.makeText(ProfileUserActivity.this, "Failed to load profile data", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable throwable) {
                    profileProgressBar.setVisibility(View.GONE);
                    Toast.makeText(ProfileUserActivity.this, "Error: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

        private void loadUserTwits (Long userId, String jwt){
            profileProgressBar.setVisibility(View.VISIBLE);

            SignApiService.signApiService.getUsersAllTwits(userId, "Bearer " + jwt).enqueue(new Callback<List<Twit>>() {
                @Override
                public void onResponse(Call<List<Twit>> call, Response<List<Twit>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<Twit> twitList = response.body();
                        twitGridAdapter = new TwitGridAdapter(ProfileUserActivity.this, twitList);
                        gridView.setAdapter(twitGridAdapter);
                        profileProgressBar.setVisibility(View.GONE);
                        // Cập nhật số lượng bài viết
                        tvPosts.setText(String.valueOf(twitList.size()));
                    } else {
                        Log.e(TAG, "Failed to load twits: " + response.message());

                        Toast.makeText(ProfileUserActivity.this, "Failed to load twits", Toast.LENGTH_SHORT).show();
                        profileProgressBar.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<List<Twit>> call, Throwable t) {
                    Toast.makeText(ProfileUserActivity.this, "Failed to connect to server: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    profileProgressBar.setVisibility(View.GONE);
                }
            });

    }
}