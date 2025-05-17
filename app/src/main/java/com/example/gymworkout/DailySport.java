package com.example.gymworkout;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.gymworkout.Other.BottomSheetHelper;
import com.example.gymworkout.Other.DifSelecter;
import com.google.firebase.Firebase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class DailySport extends AppCompatActivity {
    FirebaseFirestore db;
    ImageView dificult1,dificult2,dificult3,dificult4,dificult5;
    Button info1,info2,info3,info4,info5;
    TextView name1,name2,name3,name4,name5;
    Button ok1,ok2,ok3,ok4,ok5;
    DifSelecter difSelecter ;

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
        info1=findViewById(R.id.info1);
        info2=findViewById(R.id.info2);
        info3=findViewById(R.id.info3);
        info4=findViewById(R.id.info4);
        info5=findViewById(R.id.info5);
        dificult1=findViewById(R.id.dificult1);
        dificult2=findViewById(R.id.dificult2);
        dificult3=findViewById(R.id.dificult3);
        dificult4=findViewById(R.id.dificult4);
        dificult5=findViewById(R.id.dificult5);
        name1=findViewById(R.id.name1);
        name2=findViewById(R.id.name2);
        name3=findViewById(R.id.name3);
        name4=findViewById(R.id.name4);
        name5=findViewById(R.id.name5);
        ok1=findViewById(R.id.ok1);
        ok2=findViewById(R.id.ok2);
        ok3=findViewById(R.id.ok3);
        ok4=findViewById(R.id.ok4);
        ok5=findViewById(R.id.ok5);
        VeriAtama(6);




        info1.setOnClickListener(v -> {


            BottomSheetHelper.showBottomSheet(this, "SQUAD Açıklama", R.drawable.squad);
        });


    }
    public void VeriAtama(int toplamGun){
        DifSelecter difSelecter = new DifSelecter();
        difSelecter.getRecommendations(toplamGun, new OnHareketListReady() {
            @Override
            public void onHareketListReady(ArrayList<String> hareketler) {
                // burada liste hazır, TextView'lara yazabilirsin
                if (!hareketler.isEmpty()) {
                    if (hareketler.size() >= 5) {
                        // 1. Hareket
                        String[] parcalar1 = hareketler.get(0).split(",");
                        name1.setText(parcalar1[0] + "\n" + parcalar1[3]);
                        if (parcalar1[1].equals("kolay")) dificult1.setImageResource(R.drawable.green);
                        else if (parcalar1[1].equals("orta")) dificult1.setImageResource(R.drawable.orange);
                        else if (parcalar1[1].equals("zor")) dificult1.setImageResource(R.drawable.red);

                        // 2. Hareket
                        String[] parcalar2 = hareketler.get(1).split(",");
                        name2.setText(parcalar2[0] + "\n" + parcalar2[3]);
                        if (parcalar2[1].equals("kolay")) dificult2.setImageResource(R.drawable.green);
                        else if (parcalar2[1].equals("orta")) dificult2.setImageResource(R.drawable.orange);
                        else if (parcalar2[1].equals("zor")) dificult2.setImageResource(R.drawable.red);

                        // 3. Hareket
                        String[] parcalar3 = hareketler.get(2).split(",");
                        name3.setText(parcalar3[0] + "\n" + parcalar3[3]);
                        if (parcalar3[1].equals("kolay")) dificult3.setImageResource(R.drawable.green);
                        else if (parcalar3[1].equals("orta")) dificult3.setImageResource(R.drawable.orange);
                        else if (parcalar3[1].equals("zor")) dificult3.setImageResource(R.drawable.red);

                        // 4. Hareket
                        String[] parcalar4 = hareketler.get(3).split(",");
                        name4.setText(parcalar4[0] + "\n" + parcalar4[3]);
                        if (parcalar4[1].equals("kolay")) dificult4.setImageResource(R.drawable.green);
                        else if (parcalar4[1].equals("orta")) dificult4.setImageResource(R.drawable.orange);
                        else if (parcalar4[1].equals("zor")) dificult4.setImageResource(R.drawable.red);

                        // 5. Hareket
                        String[] parcalar5 = hareketler.get(4).split(",");
                        name5.setText(parcalar5[0] + "\n" + parcalar5[3]);
                        if (parcalar5[1].equals("kolay")) dificult5.setImageResource(R.drawable.green);
                        else if (parcalar5[1].equals("orta")) dificult5.setImageResource(R.drawable.orange);
                        else if (parcalar5[1].equals("zor")) dificult5.setImageResource(R.drawable.red);
                    }

                }
            }
        });
    }







}