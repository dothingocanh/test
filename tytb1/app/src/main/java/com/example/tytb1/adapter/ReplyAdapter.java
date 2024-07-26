package com.example.tytb1.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.tytb1.Config.GlideApp;
import com.example.tytb1.Model.ReplyTwit;
import com.example.tytb1.Model.Twit;
import com.example.tytb1.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ReplyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_TWIT = 0;
    private static final int VIEW_TYPE_REPLY = 1;
    private static final int VIEW_TYPE_HINT = 2;
    private Twit twit;
    private List<ReplyTwit> replyList;
    private Context context;

    public ReplyAdapter(Twit twit, List<ReplyTwit> replyList, Context context) {
        this.replyList = replyList;
        this.twit = twit;
        this.context = context;
    }
    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return VIEW_TYPE_TWIT;
        } else if (replyList.isEmpty() && position == 1) {
            return VIEW_TYPE_HINT;
        } else {
            return VIEW_TYPE_REPLY;
        }
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_TWIT) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_twit, parent, false);
            return new TwitViewHolder(view);
        }  else if (viewType == VIEW_TYPE_HINT) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_hint, parent, false);
            return new HintViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_reply, parent, false);
            return new ReplyViewHolder(view);
        }
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == VIEW_TYPE_TWIT) {
            ((TwitViewHolder) holder).bind(twit);
        } else if (holder.getItemViewType() == VIEW_TYPE_HINT) {
            ((HintViewHolder) holder).bind();
        } else {
            ReplyTwit replyTwit = replyList.get(position - 1); // Adjust for twit at position 0
            ((ReplyViewHolder) holder).bind(replyTwit);
        }
    }

    @Override
    public int getItemCount() {
        return replyList.isEmpty() ? 2 : replyList.size() + 1; // +1 for the twit at the top, +1 for hint if empty
    }
    public class TwitViewHolder extends RecyclerView.ViewHolder {
        public TextView twitContent;
        public TextView twitFullName;
        public CircleImageView profileImage;
        public ImageView twitImage;

        public TwitViewHolder(View view) {
            super(view);
            twitContent = view.findViewById(R.id.twitContent);
            twitFullName = view.findViewById(R.id.twitUsername);
            twitImage = view.findViewById(R.id.image);
            profileImage = view.findViewById(R.id.profileImage);
        }

        public void bind(Twit twit) {
            twitContent.setText(twit.getContent());
            twitFullName.setText(twit.getUser().getFullName());
            String profileImageUrl = twit.getUser().getImage();
            if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
                GlideApp.with(context)
                        .load(profileImageUrl)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .dontTransform()
//                        .override(com.bumptech.glide.request.target.Target.SIZE_ORIGINAL)
                        .into(profileImage);
            } else {
                profileImage.setImageResource(R.mipmap.ic_person); // Placeholder image
            }
            String imageUrl = twit.getImage();
            if (imageUrl != null && !imageUrl.isEmpty()) {
                GlideApp.with(context)
                        .load(imageUrl)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .dontTransform()
//                        .override(com.bumptech.glide.request.target.Target.SIZE_ORIGINAL)
                        .into(twitImage);
            } else {
                twitImage.setImageResource(R.mipmap.ic_person); // Placeholder image
            }
        }
    }

    public class ReplyViewHolder extends RecyclerView.ViewHolder {
        public TextView replyContent;
        public TextView replyFullName;
        public CircleImageView profileImage;

        public ReplyViewHolder(View view) {
            super(view);
            replyContent = view.findViewById(R.id.replyContent);
            replyFullName = view.findViewById(R.id.replyUsername);
            profileImage = view.findViewById(R.id.image);
        }

        public void bind(ReplyTwit replyTwit) {
            replyContent.setText(replyTwit.getContent());
            replyFullName.setText(replyTwit.getUser().getFullName());
            String imageUrl = replyTwit.getUser().getImage();
            if (imageUrl != null && !imageUrl.isEmpty()) {
                GlideApp.with(context)
                        .load(imageUrl)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .dontTransform()
//                        .override(com.bumptech.glide.request.target.Target.SIZE_ORIGINAL)
                        .into(profileImage);
            } else {
                profileImage.setImageResource(R.mipmap.ic_person); // Placeholder image
            }
        }
    }
    public class HintViewHolder extends RecyclerView.ViewHolder {
        public TextView hintText;

        public HintViewHolder(View view) {
            super(view);
            hintText = view.findViewById(R.id.hintText);
        }

        public void bind() {
            hintText.setText("Hãy là người đầu tiên bình luận bài viết này");
        }
    }
}
