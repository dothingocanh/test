package com.example.tytb1.Api;

import com.example.tytb1.Config.AuthResponse;
import com.example.tytb1.Config.NetworkConfig;
import com.example.tytb1.Model.Like;
import com.example.tytb1.Model.ReplyTwit;
import com.example.tytb1.Model.Twit;
import com.example.tytb1.Model.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface SignApiService {
    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();

    HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY);

    OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build();
    SignApiService signApiService = new Retrofit.Builder()
            .baseUrl(NetworkConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(SignApiService.class);


    //sign
    @POST("auth/signup")
    Call<AuthResponse> registerUser(@Body User user);
    @POST("auth/signin")
    Call<AuthResponse> loginUser(@Body User user);
//---------------------------------------------------------------------------------------------------
    //twit
    @GET("api/twits/")
    Call<List<Twit>> getListTwit(@Header("Authorization") String jwt);
    ////////////////////
    @GET("api/twits/user/{userId}")
    Call<List<Twit>> getUsersAllTwits(@Path("userId") Long userId, @Header("Authorization") String jwt);

    @POST("api/twits/create")
    Call<Twit> createTwit(@Body Twit twit, @Header("Authorization") String jwt);
    ////////////reply Twit
    @POST("api/twits/reply")
    Call<Twit> replyTwit(@Body ReplyTwit replyTwit, @Header("Authorization") String jwt);
    ////////////get all reply of 1 twit
    @GET("api/twits/{twitId}")
    Call<Twit> getTwitDetail(@Path("twitId") Long twitId, @Header("Authorization") String jwt);

    //-------------------------------------------------------------------------------------------------
    //like
    @POST("api/{twitId}/likes")
    Call<Like> likeTwit(@Path("twitId") Long twitId, @Header("Authorization") String jwt);

    @POST("/api/twit/{twitId}")
    Call<List<Like>> getAllLikes(@Path("twitId") Long twitId, @Header("Authorization") String jwt);
//--------------------------------------------------------------------------------------------------
    //profile
    @GET("api/users/profile")
    Call<User> getUserProfile(@Header("Authorization") String jwt);
    //search user
    @GET("api/users/search")
    Call<List<User>> searchUser(@Header("Authorization") String jwt, @Query("query") String query);

    ///update profile
@PUT("api/users/update")
Call<User> updateProfile(@Body User user,@Header("Authorization")String jwt);
    ///profile user
    @GET("api/users/{userId}")
    Call<User> getUserById(@Path("userId") Long userId, @Header("Authorization") String jwt);

//-----------------------------------------------------------------------------------
    //follow

    @PUT("api/users/{userId}/follow")
    Call<User> followUser(@Path("userId") Long userId, @Header("Authorization") String jwt);

}