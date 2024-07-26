package com.example.tytb1.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.tytb1.Api.SignApiService;
import com.example.tytb1.Model.User;
import com.example.tytb1.R;
import com.example.tytb1.activity.ProfileUserActivity;
import com.example.tytb1.adapter.SearchAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Search extends Fragment {
private ImageButton btnSearch;
private EditText edtSearch;
private RecyclerView rcvSearch;
private SearchAdapter searchAdapter;
    public Search() {
        // Required empty public constructor
    }
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);

       btnSearch = view.findViewById(R.id.btnSearch);
       edtSearch = view.findViewById(R.id.edtSearch);
       rcvSearch = view.findViewById(R.id.rcvSearch);


       rcvSearch.setLayoutManager(new LinearLayoutManager(getContext()));
        searchAdapter = new SearchAdapter(user -> {
            Intent intent = new Intent(getActivity(), ProfileUserActivity.class);
            intent.putExtra("userId", user.getId());
            startActivity(intent);
        });
       rcvSearch.setAdapter(searchAdapter);
        btnSearch.setOnClickListener(v -> {

            searchUser();
        });

        return view;
    }

    private void searchUser() {
        String query = edtSearch.getText().toString().trim();
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        String jwt = sharedPreferences.getString("jwt", null);
        if (!query.isEmpty() && jwt != null) {
            SignApiService.signApiService.searchUser("Bearer "+ jwt, query).enqueue(new Callback<List<User>>() {
                @Override
                public void onResponse(@NonNull Call<List<User>> call, @NonNull Response<List<User>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<User> users = response.body();
                        searchAdapter.setUsers(users);
                        edtSearch.setText("");
                    } else {
                        Toast.makeText(getActivity(), "Search failed: " + response.message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<List<User>> call, @NonNull Throwable t) {
                    Toast.makeText(getActivity(), "Search failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getActivity(), "Enter a search query and make sure you are logged in.", Toast.LENGTH_SHORT).show();
        }
    }

}