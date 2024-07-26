package com.example.tytb1.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.example.tytb1.Api.SignApiService;
import com.example.tytb1.Config.GlideApp;
import com.example.tytb1.Model.Twit;
import com.example.tytb1.R;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Add extends Fragment{
    private static final String TAG = "Upload ###";

    private Uri imagePath;
    private String uploadedImageUrl = null;
    private static boolean isMediaManagerInitialized = false;

    private EditText edtContent;
    private ImageView imageView;
    private ImageButton btnPost, btnSelectImg;

    private ActivityResultLauncher<Intent> resultLauncher;


    public Add() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);
        edtContent = view.findViewById(R.id.edtContent);
        imageView = view.findViewById(R.id.imageView);
        btnPost = view.findViewById(R.id.btnPost);
        btnSelectImg = view.findViewById(R.id.btnSelectImg);
//        initConfig();
        registerResult();

        btnSelectImg.setOnClickListener(v -> {
            pickImage();
        });


        btnPost.setOnClickListener(v -> {
            if (imagePath != null) {
                uploadImageToCloudinary();
            } else {
                Toast.makeText(getActivity(), "No image selected", Toast.LENGTH_SHORT).show();
            }
        });


        return view;
    }

    private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        try {
            resultLauncher.launch(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getActivity(), "No app found to pick images", Toast.LENGTH_SHORT).show();
        }
    }


private void registerResult(){
        resultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {

                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            if (data != null) {
                                imagePath = data.getData();
//                                imageView.setImageURI(imagePath);
                                int maxTextureSize = 4096;
                                GlideApp.with(getContext())
                                        .load(imagePath)
//                                        .override(maxTextureSize, maxTextureSize)
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .dontTransform()
//                                .override(com.bumptech.glide.request.target.Target.SIZE_ORIGINAL)
                                        .into(imageView);
                            }
                        } else {
                            Toast.makeText(getActivity(), "No image selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
}

    private void uploadImageToCloudinary() {
        MediaManager.get().upload(imagePath).callback(new UploadCallback() {
            @Override
            public void onStart(String requestId) {
                Log.d(TAG, "onStart: started");
                Toast.makeText(getActivity(), "Uploading...", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onProgress(String requestId, long bytes, long totalBytes) {
                Log.d(TAG, "onProgress: uploading");
            }

            @Override
            public void onSuccess(String requestId, Map resultData) {
                Log.d(TAG, "onSuccess: upload success");
                uploadedImageUrl = resultData.get("secure_url").toString();
                Log.d(TAG, "Image URL: " + uploadedImageUrl);
                Toast.makeText(getActivity(), "Upload success: " + uploadedImageUrl, Toast.LENGTH_SHORT).show();
                submitTwitToServer(uploadedImageUrl);
            }

            @Override
            public void onError(String requestId, ErrorInfo error) {
                Toast.makeText(getActivity(), "Upload failed: " + error.getDescription(), Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onReschedule(String requestId, ErrorInfo error) {
            }
        }).dispatch();
    }


    private void submitTwitToServer(String url) {

        String content = edtContent.getText().toString().trim();
        Twit twit = new Twit(content, url);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        String jwt = sharedPreferences.getString("jwt", null);

        if (jwt != null) {
            SignApiService.signApiService.createTwit(twit, "Bearer " + jwt).enqueue(new Callback<Twit>() {
                @Override
                public void onResponse(Call<Twit> call, Response<Twit> response) {
                    if (response.isSuccessful() && response.body() != null) {

                        Toast.makeText(getActivity(), "Twit posted successfully", Toast.LENGTH_SHORT).show();

                        Log.e("API_CALL", "post twit success" + response.message());
                        // Transition to Home fragment
                        Fragment homeFragment = new Home();
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.replace(R.id.fragment_container, homeFragment); // Replace with your container ID
                        transaction.addToBackStack(null);
                        transaction.commit();
                    } else {
                        Log.e("API_CALL", "Failed to post twit " + response.message());

                    }
                }

                @Override
                public void onFailure(Call<Twit> call, Throwable throwable) {
                    Log.e("API_CALL", "Failed to fetch twits: " + throwable.getMessage());

                }
            });


        }
    }
}