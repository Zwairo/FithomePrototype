package com.example.gymworkout;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.gymworkout.Other.BottomSheetHelper;
import com.google.firebase.Firebase;
import com.google.firebase.firestore.FirebaseFirestore;

public class DailySport extends AppCompatActivity {
    FirebaseFirestore db;
    Button info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_daily_sport);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db=FirebaseFirestore.getInstance();
        info=findViewById(R.id.info1);

       info.setOnClickListener(v -> {


            BottomSheetHelper.showBottomSheet(this, "SQUAD Açıklama", R.drawable.squad);
        });


    }





}