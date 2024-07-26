package com.example.tytb1.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.tytb1.Api.SignApiService;
import com.example.tytb1.Model.Twit;
import com.example.tytb1.Model.User;
import com.example.tytb1.R;
import com.example.tytb1.adapter.HomeAdapter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Home extends Fragment {
    private RecyclerView rcvTwit;
    private HomeAdapter homeAdapter;

        private List<Twit> mListTwit;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,@Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        rcvTwit = view.findViewById(R.id.rcv_twit);
        rcvTwit.setLayoutManager(new LinearLayoutManager(getActivity()));
        mListTwit = new ArrayList<>();
        homeAdapter = new HomeAdapter(mListTwit, getActivity());
        rcvTwit.setAdapter(homeAdapter);
        // Lấy JWT từ SharedPreferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        String jwt = sharedPreferences.getString("jwt", null);
        if (jwt != null) {
            SignApiService.signApiService.getListTwit("Bearer " + jwt).enqueue(new Callback<List<Twit>>() {
                @Override
                public void onResponse(Call<List<Twit>> call, Response<List<Twit>> response) {
                    if (response.isSuccessful()&& response.body() != null) {
                        mListTwit.clear();
                        mListTwit.addAll(response.body());

                        // Hiển thị danh sách Twit lên RecyclerView
                        homeAdapter.notifyDataSetChanged();
                        Log.e("API_CALL", "have data " + response.message());

                    }else {
                        Log.e("API_CALL", "No data available: " + response.message());

                    }
                }
                @Override
                public void onFailure(Call<List<Twit>> call, Throwable throwable) {
                    Log.e("API_CALL", "Failed to fetch twits: " + throwable.getMessage());

                }
            });



        }

        return view;
    }
}