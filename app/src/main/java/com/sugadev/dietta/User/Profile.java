package com.sugadev.dietta.User;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sugadev.dietta.JsonPlaceHolderAPI;
import com.sugadev.dietta.Login;
import com.sugadev.dietta.R;
import com.sugadev.dietta.User.UserProfile.UserProfile;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Profile extends AppCompatActivity {

    Button btnLogout;
    TextView name;
    FirebaseAuth mAuth;
    DatabaseReference mReference;
    FirebaseUser mUser;
    FirebaseDatabase mDatabase;


    TextView Username, Password, Nama, Email, Notelp, BeratBadan, TinggiBadan;

    int idUser;
    String iUsername, iPassword, iNama, iEmail;
    int iNotelp, iBeratBadan, iTinggiBadan;

    Retrofit retrofit;
    JsonPlaceHolderAPI jsonPlaceHolderAPI;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        declaration();
        getData();
        setData();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Profile");

    }

    private void declaration() {
        btnLogout = findViewById(R.id.btnLogout);
        Username = findViewById(R.id.tvUsername);
        Password = findViewById(R.id.tvPassword);
        Nama = findViewById(R.id.tvNama);
        Email = findViewById(R.id.tvEmail);
        Notelp = findViewById(R.id.tvNotelp);
        TinggiBadan = findViewById(R.id.tvTinggiBadan);
        BeratBadan = findViewById(R.id.tvBeratBadan);
    }

//    private void firebaseGetUser(){
//        mAuth = FirebaseAuth.getInstance();
//        mUser = mAuth.getCurrentUser();
//        mDatabase = FirebaseDatabase.getInstance();
//        mReference = mDatabase.getReference().child("users").child(mUser.getUid());
//
//        mReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                User user = snapshot.getValue(User.class);
//                name.setText(user.getName());
//                Log.i(TAG, "name: " + user.getName());
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Log.d(TAG, "the read failed: " + error.getCode());
//            }
//        });
//
//        btnLogout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mAuth.signOut();
//                Intent dirLogin = new Intent(getApplicationContext(), Login.class);
//                startActivity(dirLogin);
//            }
//        });
//    }

    private void setData() {
        Username.setText(iUsername);
        Password.setText(iPassword);
        Nama.setText(String.valueOf(iNama));
        Email.setText(String.valueOf(iEmail));
        Notelp.setText(String.valueOf(iNotelp));
        TinggiBadan.setText(String.valueOf(iTinggiBadan));
        BeratBadan.setText(String.valueOf(iBeratBadan));

    }


    private void getData() {
        retrofit = new Retrofit.Builder()
                .baseUrl("http://103.31.39.4:8383/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        jsonPlaceHolderAPI = retrofit.create(JsonPlaceHolderAPI.class);

        Call<UserProfile> call = jsonPlaceHolderAPI.getUserDetail(1);

        call.enqueue(new Callback<UserProfile>() {
            @Override
            public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Code : " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }

                UserProfile userProfile = response.body();
                if (userProfile != null) {
                    iUsername = userProfile.getUsername();
                    iPassword = userProfile.getPassword();
                    iNama = userProfile.getName();
                    iEmail = userProfile.getEmail();
                    iNotelp = userProfile.getNo_telp();
                    iTinggiBadan = userProfile.getTinggiBadan();
                    iBeratBadan = userProfile.getBeratBadan();
                    setData();
                } else {
                    Toast.makeText(getApplicationContext(), "User profile data is null", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserProfile> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                Toast.makeText(getApplicationContext(), "Failed to fetch user profile data", Toast.LENGTH_SHORT).show();
            }

        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}