package com.sugadev.dietta.User;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sugadev.dietta.Admin.MakananAdapter;
import com.sugadev.dietta.Admin.MakananAdapterHome;
import com.sugadev.dietta.Login;
import com.sugadev.dietta.R;

public class Homepage extends AppCompatActivity {

    int image[] = {
            R.drawable.kare,
            R.drawable.kare,
            R.drawable.kare,
            R.drawable.kare,
            R.drawable.kare
    };
    TextView name;
    ImageView btnLogout;
    FirebaseAuth mAuth;
    DatabaseReference mReference;
    FirebaseUser mUser;
    FirebaseDatabase mDatabase;
    RecyclerView rvMakanan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name = findViewById(R.id.phNama);
        btnLogout = findViewById(R.id.btnLogout);
        rvMakanan = findViewById(R.id.rvMakananHome);

        String[] jdlMakanan = getResources().getStringArray(R.array.judul_makanan);
        String[] descMakanan = getResources().getStringArray(R.array.deskripsi_makanan);

        MakananAdapterHome makananAdapterHome = new MakananAdapterHome(jdlMakanan, descMakanan, image, this);
        rvMakanan.setAdapter(makananAdapterHome);
        rvMakanan.setLayoutManager(new GridLayoutManager(this,2));


        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference().child("users").child(mUser.getUid());

        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                name.setText(user.getName());
                Log.i(TAG, "name: " + user.getName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "the read failed: " + error.getCode());
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent dirLogin = new Intent(getApplicationContext(), Login.class);
                startActivity(dirLogin);
                finish();
            }
        });


    }
}