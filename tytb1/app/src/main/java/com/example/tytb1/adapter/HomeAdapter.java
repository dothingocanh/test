package com.example.tytb1.adapter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.tytb1.Api.SignApiService;

import com.example.tytb1.Config.GlideApp;
import com.example.tytb1.Model.Like;
import com.example.tytb1.Model.ReplyTwit;
import com.example.tytb1.Model.Twit;
import com.example.tytb1.R;
import com.example.tytb1.activity.ProfileUserActivity;
import com.example.tytb1.activity.TwitActivity;
import com.example.tytb1.fragment.Home;
import com.example.tytb1.fragment.Profile;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeViewHolder> {

    private List<Twit> mlistTwit;
    private Context mContext;

    public HomeAdapter(List<Twit> mlistTwit, Context mContext) {
        this.mlistTwit = mlistTwit;
        this.mContext = mContext;

    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.home_items,parent, false);
        return new HomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        Twit twit = mlistTwit.get(position);
        if (twit == null) {
            return;
        }

        // Sử dụng Glide để tải hình ảnh profile
        if (twit.getUser().getImage() != null) {

            GlideApp.with(mContext)
                    .load(twit.getUser().getImage())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .dontTransform()
//                    .override(com.bumptech.glide.request.target.Target.SIZE_ORIGINAL)
                    .into(holder.profileImage);
        } else {
            holder.profileImage.setImageResource(R.drawable.ic_dashboard_black_24dp); // Hình ảnh mặc định
        }

        // Sử dụng Glide để tải hình ảnh twit
        if (twit.getImage() != null && !twit.getImage().isEmpty()) {
            GlideApp.with(mContext)
                    .load(twit.getImage())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .dontTransform()
//                    .override(com.bumptech.glide.request.target.Target.SIZE_ORIGINAL)
                    .into(holder.image);
            holder.image.setVisibility(View.VISIBLE);
        } else {
            holder.image.setVisibility(View.GONE);
        }
        holder.fullName.setText(twit.getUser().getFullName());
        holder.likeCount.setText(String.valueOf(twit.getTotalLikes())); //count like now
        String createAt = twit.getCreatedAt();
        if (createAt != null) {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS", Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            try {
                Date date = inputFormat.parse(createAt);
                String formattedDate = outputFormat.format(date);
                holder.time.setText(formattedDate);
            } catch (ParseException e) {
                Log.e("HomeAdapter", "Failed to parse createAt field: " + e.getMessage());
                holder.time.setText(""); // Or set a default value or handle the error appropriately
            }
        } else {
            holder.time.setText(""); // Handle the case when createAt is null
        }
        holder.tvContent.setText(twit.getContent());
        if (twit.isLiked()) {
            holder.btnLike.setBackgroundResource(R.mipmap.ic_liked); // Đặt hình ảnh nút like màu đỏ
        } else {
            holder.btnLike.setBackgroundResource(R.drawable.ic_favorite_border_black_24dp); // Đặt hình ảnh nút like màu ban đầu
        }
        holder.btnLike.setOnClickListener(v -> {
            int adapterPosition = holder.getAdapterPosition(); // Lấy vị trí của item trong adapter
            if (adapterPosition != RecyclerView.NO_POSITION) { // Kiểm tra vị trí hợp lệ
                Twit clickedTwit = mlistTwit.get(adapterPosition);
                // Lấy JWT từ SharedPreferences
                SharedPreferences sharedPreferences = mContext.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
                String jwt = sharedPreferences.getString("jwt", null);
                if (jwt != null) {
                    SignApiService.signApiService.likeTwit(clickedTwit.getId(), "Bearer " + jwt).enqueue(new Callback<Like>() {
                        @Override
                        public void onResponse(Call<Like> call, Response<Like> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                // Cập nhật trạng thái like của Twit và cập nhật giao diện
                                clickedTwit.setLiked(!clickedTwit.isLiked());
                                getLikesForTwit(clickedTwit.getId(), jwt); // Gọi lại hàm để lấy số lượt like mới


                                Toast.makeText(mContext, twit.isLiked() ? "Liked!" : "Unliked!", Toast.LENGTH_SHORT).show();

                            } else {
                                Log.e("API_CALL", "Failed to like twit: " + response.message());
                                Toast.makeText(mContext, "Failed to like twit", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Like> call, Throwable t) {
                            Log.e("API_CALL", "API call failed: " + t.getMessage());
                            Toast.makeText(mContext, "API call failed", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        holder.btnComment.setOnClickListener(v -> {
            if (twit.getId() != null) {
                Long twitId = twit.getId();
                Intent intent = new Intent(mContext, TwitActivity.class);
                intent.putExtra("twitId", twitId);
                mContext.startActivity(intent);
                Log.e("HOME ADAPTER", "Twit id haha" + twitId);

            } else {
                Log.e("HomeAdapter", "Twit id is null");
            }
        });
        holder.profileImage.setOnClickListener(v -> {

            if (twit.getUser().getId() != null) {
                Long userId = twit.getUser().getId();

                // Lấy userId từ SharedPreferences
                SharedPreferences sharedPreferences1 = mContext.getSharedPreferences("MySharedPref1", Context.MODE_PRIVATE);
                Long currentUserId = sharedPreferences1.getLong("userId", -1);

                if (currentUserId != -1 && userId.equals(currentUserId)) {

                    FragmentManager fragmentManager = ((AppCompatActivity) mContext).getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, new Profile())
                            .addToBackStack(null)
                            .commit();
                } else {
                    // Redirect to ProfileUserActivity for other users' profiles
                    Intent intent = new Intent(mContext, ProfileUserActivity.class);
                    intent.putExtra("userId", userId);
                    mContext.startActivity(intent);
                }
            } else {
                Log.e("HomeAdapter", "User id is null");
            }
        });
    }

    public void getLikesForTwit(Long twitId, String jwt) {
        SignApiService.signApiService.getAllLikes(twitId, "Bearer " + jwt).enqueue(new Callback<List<Like>>() {
            @Override
            public void onResponse(Call<List<Like>> call, Response<List<Like>> response) {
                if (response.isSuccessful() && response.body() != null) {

                    int likesCount = response.body().size();
                    for (Twit twit : mlistTwit) {
                        if (twit.getId().equals(twitId)) {
                            twit.setTotalLikes(likesCount);
                            break;
                        }
                    }
                    notifyDataSetChanged(); // Cập nhật lại RecyclerView
                } else {
                    Log.e("API_CALL", "Failed to get likes: " + response.message());
                    Toast.makeText(mContext, "Failed to get likes", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Like>> call, Throwable t) {
                Log.e("API_CALL", "API call failed: " + t.getMessage());
                Toast.makeText(mContext, "API call failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mlistTwit != null){
            return mlistTwit.size();
        }
        return 0;
    }


    public static class HomeViewHolder extends RecyclerView.ViewHolder{
        private CircleImageView profileImage;
        private TextView fullName,time, tvContent, likeCount;
        private ImageView image;
        private ImageButton btnLike, btnComment, btnShare;
        public HomeViewHolder(@NonNull View itemView){
            super(itemView);


            profileImage = itemView.findViewById(R.id.profileImage);
            image = itemView.findViewById(R.id.image);
            fullName = itemView.findViewById(R.id.fullName);
            time = itemView.findViewById(R.id.time);
            tvContent = itemView.findViewById(R.id.content);
            likeCount = itemView.findViewById(R.id.tv_like_count);

            btnLike = itemView.findViewById(R.id.btn_like);

            btnComment = itemView.findViewById(R.id.btn_comment);

//            btnShare = itemView.findViewById(R.id.btn_share);

        }
    }

}
