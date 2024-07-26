package com.example.tytb1.fragment;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.example.tytb1.Api.SignApiService;
import com.example.tytb1.Config.GlideApp;
import com.example.tytb1.Model.Twit;
import com.example.tytb1.Model.User;
import com.example.tytb1.R;
import com.example.tytb1.TwitGridAdapter;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Profile extends Fragment {
    private CircleImageView profileImage;
    private EditText displayName, bio, location;
    private TextView  tvPosts, tvFollowers, tvFollowing, tvEditProfile;
    private ProgressBar profileProgressBar;
    private GridView gridView;
    private TwitGridAdapter twitGridAdapter;
    private boolean isEditing = false;
    private int totalTwits = 0;
private static final int PERMISSION_REQUEST_CODE = 1001;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;
    private Uri selectedImageUri;
    private String uploadedImageUrl = null;

    private ActivityResultLauncher<Intent> resultLauncher;
    public Profile() {

    }
    private String[] requiredPermissions = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        initViews(view);
        registerResult();
        checkAndRequestPermissions(); // Check and request permissions here

        loadProfileData();
        return view;

    }
    private void checkAndRequestPermissions() {
        if (!hasPermissions(getActivity(), requiredPermissions)) {
            ActivityCompat.requestPermissions(getActivity(), requiredPermissions, PERMISSION_REQUEST_CODE);
        }
    }
    private boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
    private void registerResult() {
        resultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        handleImageActivityResult(result);

                    }
                }
        );
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permissions granted
            } else {
                Toast.makeText(getActivity(), "Permissions not granted", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(getContext(), "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //xu li ket qua chup anh
    private void handleImageActivityResult(ActivityResult result) {
        if (result.getResultCode() == RESULT_OK) {
            Intent data = result.getData();
            if (data != null) {
                if (data.getExtras() != null && data.getExtras().get("data") instanceof Bitmap) {
                    // Camera capture returns a Bitmap
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    selectedImageUri = getImageUri(getContext(), photo);
                } else {
                    // Gallery selection returns a URI
                    selectedImageUri = data.getData();
                }

                if (selectedImageUri != null) {
                    profileImage.setImageURI(selectedImageUri);
                    uploadImageToCloudinary();
                } else {
                    Toast.makeText(getActivity(), "Failed to retrieve image URI", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            Toast.makeText(getActivity(), "No image selected", Toast.LENGTH_SHORT).show();
        }
    }
    private Uri getImageUri(Context context, Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "Title", null);
            return Uri.parse(path);
        } else {
            Toast.makeText(getContext(), "Write permission is required to save images", Toast.LENGTH_SHORT).show();
            return null;
        }
    }
    private void initViews(View view) {
        profileImage = view.findViewById(R.id.profile_image);
        displayName = view.findViewById(R.id.display_name);
        bio = view.findViewById(R.id.description);
        location = view.findViewById(R.id.website);
        tvPosts = view.findViewById(R.id.tvPosts);
        tvFollowers = view.findViewById(R.id.tvFollowers);
        tvFollowing = view.findViewById(R.id.tvFollowing);
        profileProgressBar = view.findViewById(R.id.profileProgressBar);
        gridView = view.findViewById(R.id.gridView);
        tvEditProfile = view.findViewById(R.id.textEditProfile);

        tvEditProfile.setOnClickListener(v -> {
            if (isEditing) {
                updateProfile();
                setEditing(false);
            } else {
                setEditing(true);
            }
        });
        profileImage.setOnClickListener(v -> {
            if (isEditing) {
                showImagePickerDialog();
            }
        });
    }
    private void showImagePickerDialog() {
        CharSequence[] options = {"Chọn từ thư viện", "Chụp ảnh", "Hủy"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Chọn ảnh cho hồ sơ");
        builder.setItems(options, (dialog, item) -> {
            if (options[item].equals("Chọn từ thư viện")) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                resultLauncher.launch(intent);
            } else if (options[item].equals("Chụp ảnh")) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
                    } else {
                        openCamera();
                    }
                }
                    else {
                    openCamera();
                    }

            } else if (options[item].equals("Hủy")) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
        private void openCamera() {
            Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            resultLauncher.launch(takePicture);
        }
    private void uploadImageToCloudinary() {
        if (selectedImageUri != null) { // Kiểm tra xem selectedImageUri có null không
            MediaManager.get().upload(selectedImageUri).callback(new UploadCallback() {
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
                    profileImage.setImageURI(selectedImageUri);
                }

                @Override
                public void onError(String requestId, ErrorInfo error) {
                    Toast.makeText(getActivity(), "Upload failed: " + error.getDescription(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onReschedule(String requestId, ErrorInfo error) {}
            }).dispatch();
        } else {
            Toast.makeText(getActivity(), "No image selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void setEditing(boolean editing) {
        displayName.setEnabled(editing);
        bio.setEnabled(editing);
        location.setEnabled(editing);
        profileImage.setEnabled(editing);
        tvEditProfile.setText(editing ? "Save Profile" : "Edit Profile");
        isEditing = editing;
    }
    private void updateProfile() {
        profileProgressBar.setVisibility(View.VISIBLE);
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        String jwt = sharedPreferences.getString("jwt", null);
        if (jwt != null) {
            User updatedUser = new User();
            updatedUser.setFullName(displayName.getText().toString());
            updatedUser.setBio(bio.getText().toString());
            updatedUser.setLocation(location.getText().toString());
            // Nếu người dùng đã chọn ảnh mới, cập nhật hình ảnh
            if (uploadedImageUrl != null) {
                updatedUser.setImage(uploadedImageUrl);
            }
            SignApiService.signApiService.updateProfile(updatedUser, "Bearer " + jwt).enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    profileProgressBar.setVisibility(View.GONE);
                    if (response.isSuccessful()) {
                        Toast.makeText(getActivity(), "Profile updated successfully", Toast.LENGTH_SHORT).show();
                        setEditing(false);
                    } else {
                        Toast.makeText(getActivity(), "Failed to update profile", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable throwable) {
                    profileProgressBar.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "Error: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    private void loadProfileData() {
        profileProgressBar.setVisibility(View.VISIBLE);
        // Get JWT from SharedPreferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        String jwt = sharedPreferences.getString("jwt", "");

        if (jwt.isEmpty()) {
            Toast.makeText(getActivity(), "JWT Token not found", Toast.LENGTH_SHORT).show();
            profileProgressBar.setVisibility(View.GONE);
            return;
        }
        SignApiService.signApiService.getUserProfile("Bearer " + jwt).enqueue(new Callback<User>() {

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
                        tvPosts.setText(String.valueOf(1));
                        tvFollowers.setText(String.valueOf(user.getFollowers().size()));
                        tvFollowing.setText(String.valueOf(user.getFollowing().size()));
                        int maxTextureSize = 4096;
                        GlideApp.with(getContext())
                                .load(user.getImage())
//                                .override(maxTextureSize, maxTextureSize)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .dontTransform()
//                                .override(com.bumptech.glide.request.target.Target.SIZE_ORIGINAL)
                                .into(profileImage);
                        loadUserTwits(user.getId(), jwt);
                    } else {
                        profileProgressBar.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), "Failed to load profile data", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable throwable) {
                profileProgressBar.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "Error: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void loadUserTwits(Long userId, String jwt) {
        SignApiService.signApiService.getUsersAllTwits(userId, "Bearer " + jwt).enqueue(new Callback<List<Twit>>() {
            @Override
            public void onResponse(Call<List<Twit>> call, Response<List<Twit>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Twit> twitList = response.body();
                    twitGridAdapter = new TwitGridAdapter(getContext(), twitList);
                    gridView.setAdapter(twitGridAdapter);
                    int numberOfPosts = twitList.size();
                    tvPosts.setText(String.valueOf(numberOfPosts));
                    profileProgressBar.setVisibility(View.GONE);
                } else {
                    Log.e(TAG, "Failed to load twits: " + response.message());

                    Toast.makeText(getActivity(), "Failed to load twits", Toast.LENGTH_SHORT).show();
                    profileProgressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<List<Twit>> call, Throwable t) {
                Toast.makeText(getActivity(), "Failed to connect to server: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                profileProgressBar.setVisibility(View.GONE);
            }
        });
    }
}