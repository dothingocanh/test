package com.example.tytb1.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.tytb1.Api.SignApiService;
import com.example.tytb1.Model.ReplyTwit;
import com.example.tytb1.Model.Twit;
import com.example.tytb1.Model.User;
import com.example.tytb1.R;
import com.example.tytb1.adapter.ReplyAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TwitActivity extends AppCompatActivity {
    private List<ReplyTwit> replyList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ReplyAdapter replyAdapter;
    private Twit twit;
    private Long twitId;
    private EditText replyContent;
    private ImageButton submitReplyButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twit);
        recyclerView  = findViewById(R.id.recyclerViewReplies);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        replyContent = findViewById(R.id.reply_content);
        submitReplyButton = findViewById(R.id.submit_reply_button);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("twitId")) {
            twitId = intent.getLongExtra("twitId", -1);
            if (twitId != -1) {
                loadTwitDetail(twitId);
            }
        }


        submitReplyButton.setOnClickListener(v -> {
            String content = replyContent.getText().toString().trim();
            if (!content.isEmpty()) {
                submitReply(twitId, content);
            }else {
                Toast.makeText(TwitActivity.this, "Reply content cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadTwitDetail(Long twitId) {
        // Gọi API để lấy danh sách replyTwit từ server
        // Sau khi nhận được dữ liệu, cập nhật replyTwitList và thông báo cho Adapter biết rằng dữ liệu đã thay đổi
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        String jwt = sharedPreferences.getString("jwt", null);
        if (jwt != null) {
            Log.d("API_CALL_LOAD_TWIT", "JWT Token: " + jwt);
            SignApiService.signApiService.getTwitDetail(twitId,"Bearer " + jwt).enqueue(new Callback<Twit>() {

                @Override
                public void onResponse(Call<Twit> call, Response<Twit> response) {
                    Log.d("API_CALL_LOAD_TWIT", "Response code: " + response.code());
                    Log.d("API_CALL_LOAD_TWIT", "Response message: " + response.message());
                    if (response.isSuccessful() ) {
                         twit = response.body();
                        if (twit != null) {
                            List<ReplyTwit> replyTwits = twit.getReplyTwits();
                            replyList.clear();
                            if (replyTwits != null && !replyTwits.isEmpty()) {

                                replyList.addAll(replyTwits);
                            }
                            replyAdapter = new ReplyAdapter(twit, replyList, TwitActivity.this);
                            recyclerView.setAdapter(replyAdapter);
                            replyAdapter.notifyDataSetChanged();
                        }

                        else {
                            Log.e("API_CALL_LOAD_TWIT", "Response body is null");
                        }
                    } else {
                        Log.e("API_CALL_LOAD_TWIT", "Response unsuccessful: " + response.code());
                        if (response.errorBody() != null) {
                            try {
                                Log.e("API_CALL_LOAD_TWIT", "Error body: " + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<Twit> call, Throwable t) {
                    Log.e("API_CALL_LOAD_TWIT", "API call failed", t);
                    Toast.makeText(TwitActivity.this, "An error occurred", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Log.e("API_CALL_LOAD_TWIT", "JWT token is null");
        }
    }

    private void submitReply(Long parentTwitId, String content) {
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        String jwt = sharedPreferences.getString("jwt", null);
        if (jwt != null) {
            ReplyTwit replyTwit = new ReplyTwit(content, parentTwitId);
            SignApiService.signApiService.replyTwit(replyTwit, "Bearer " + jwt).enqueue(new Callback<Twit>() {
                @Override
                public void onResponse(Call<Twit> call, Response<Twit> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        replyContent.setText("");
                        Twit updatedTwit = response.body();
                        List<ReplyTwit> newReplies = updatedTwit.getReplyTwits();
                        if (newReplies != null) {
                            replyList.clear();
                            replyList.addAll(newReplies);
                            replyAdapter.notifyDataSetChanged();
                            recyclerView.scrollToPosition(replyList.size() );
                        }
                        Toast.makeText(TwitActivity.this, "Reply posted successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        try {
                            String errorBody = response.errorBody().string();
                            Log.e("SubmitReply", "Response unsuccessful: " + errorBody);
                            Toast.makeText(TwitActivity.this, "Failed to post reply: " + errorBody, Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }                    }
                }

                @Override
                public void onFailure(Call<Twit> call, Throwable t) {
                    Toast.makeText(TwitActivity.this, "An error occurred", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}