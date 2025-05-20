package com.example.gymworkout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.gymworkout.Other.BottomSheetHelper;
import com.example.gymworkout.Other.DifSelecter;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DailySport extends AppCompatActivity {
    FirebaseFirestore db;
    ImageView dificult1,dificult2,dificult3,dificult4,dificult5;
    Button info1,info2,info3,info4,info5;
    TextView name1,name2,name3,name4,name5;
    Button ok1,ok2,ok3,ok4,ok5;
    DifSelecter difSelecter ;
    View sheetView;
    FirebaseAuth auth;
    int toplam;
    TextView kronometreText;
    long baslangicZamani;
    Handler handler = new Handler();
    Runnable runnable;

    Button[] butonlar;
    TextView[]nameler;
    boolean[] tiklandiMi = new boolean[5]; // Her buton için tıklanma durumu
    int tiklananButonSayisi = 0;
    RelativeLayout tebrikKarti ;



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

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
         sheetView = getLayoutInflater().inflate(R.layout.bottom_sheet_info, null);
        bottomSheetDialog.setContentView(sheetView);
        db=FirebaseFirestore.getInstance();
        tebrikKarti = findViewById(R.id.tebrikKarti);
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


//Kronometre

        kronometreText = findViewById(R.id.gunluksure);
        baslangicZamani = System.currentTimeMillis();

        runnable = new Runnable() {
            @Override
            public void run() {
                long simdikiZaman = System.currentTimeMillis();
                long fark = simdikiZaman - baslangicZamani;

                int saniye = (int) (fark / 1000) % 60;
                int dakika = (int) ((fark / (1000 * 60)) % 60);

                kronometreText.setText("Bugün Geçirilen Süre: "+String.format("%02d:%02d", dakika, saniye));

                handler.postDelayed(this, 1000); // her saniye güncelle
            }
        };

        handler.post(runnable);

        nameler=new TextView[]{
                findViewById(R.id.name1)
                ,findViewById(R.id.name2)
                ,findViewById(R.id.name3)
                ,findViewById(R.id.name4)
                ,findViewById(R.id.name5)
        };

        butonlar = new Button[]{
                findViewById(R.id.ok1),
                findViewById(R.id.ok2),
                findViewById(R.id.ok3),
                findViewById(R.id.ok4),
                findViewById(R.id.ok5)
        };

        for (int i = 0; i < butonlar.length; i++) {
            int finalI = i;
            butonlar[i].setOnClickListener(v -> {
                if (!tiklandiMi[finalI]) {
                    tiklandiMi[finalI] = true;
                    tiklananButonSayisi++;
                    nameler[finalI].setText("Tamamlandı");

                    butonlar[finalI].setEnabled(false);




                    if (tiklananButonSayisi == 5) {
                        //Tüm butonlara tıklandı


                        long gecenSure = System.currentTimeMillis() - baslangicZamani;
                        handler.removeCallbacks(runnable);

                        tebrikGoster();

                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                        DocumentReference docRef = db.collection("Users").document(uid);

                        docRef.get().addOnSuccessListener(documentSnapshot -> {
                            long oncekiToplam = 0;
                            if (documentSnapshot.exists()) {
                                Long mevcut = documentSnapshot.getLong("toplamSureMs");
                                if (mevcut != null) {
                                    oncekiToplam = mevcut;
                                }
                            }

                            long yeniToplam = oncekiToplam + gecenSure;

                            Map<String, Object> veri = new HashMap<>();
                            veri.put("toplamSureMs", yeniToplam);
                            veri.put("sonTarih", new Timestamp(new Date()));

                            docRef.set(veri, SetOptions.merge())
                                    .addOnSuccessListener(aVoid -> Toast.makeText(this, "Süre güncellendi", Toast.LENGTH_SHORT).show())
                                    .addOnFailureListener(e -> Toast.makeText(this, "Hata: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                        });
                    }
                }
            });
        }








        //Toplam Gün Sayısını çekmek için Interface ile uğraşmak istemedim, direkt oncreat içinde yaptım
        auth=FirebaseAuth.getInstance();
        String userId=auth.getCurrentUser().getUid();
        db.collection("Users").document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        if (documentSnapshot.contains("toplamGun")) {
                            Long toplamGun = documentSnapshot.getLong("toplamGun");
                            if (toplamGun != null) {
                                toplam = toplamGun.intValue();
                                Log.d("TOPLAM_GUN", "Toplam gün: " + toplam);
                            } else {
                                Log.d("TOPLAM_GUN", "Toplam gün null döndü");
                            }
                        } else {
                            Log.d("TOPLAM_GUN", "toplamGun alanı yok");
                        }
                    } else {
                        Log.d("TOPLAM_GUN", "Belge bulunamadı");
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("TOPLAM_GUN", "Hata oluştu: " + e.getMessage());
                });
        VeriAtama(toplam);

        //Toplam Gün Sayısını ve Level bilgisini TextView'lara yazdırmak için

        TextView toplamGunVeLevel = findViewById(R.id.toplamgunvelevel);
        TextView toplamSure = findViewById(R.id.toplamsure);
        DifSelecter difSelecter = new DifSelecter();

        db.collection("Users").document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Long toplamGun = documentSnapshot.getLong("toplamGun");
                        Long toplamSureMs = documentSnapshot.getLong("toplamSureMs");

                        int gun = (toplamGun != null) ? toplamGun.intValue() : 0;
                        int dakika = (toplamSureMs != null) ? (int) (toplamSureMs / (1000 * 60)) : 0;
                        int level = difSelecter.getLevel(gun);

                        toplamGunVeLevel.setText("Sporda " + gun + ". Günün / Level :" + level);
                        toplamSure.setText("Toplam Süre: " + dakika + " dk");

                        VeriAtama(gun); // sadece veri geldikten sonra çağır
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Veri alınamadı: " + e.getMessage());
                });





    }
    public void tebrikGoster() {
        tebrikKarti.setVisibility(View.VISIBLE);

        // İstersen 3 saniye sonra kendini kapatsın:
        new Handler().postDelayed(() -> {
            tebrikKarti.setVisibility(View.GONE);
            Intent intent=new Intent(DailySport.this,MainActivity.class);
            startActivity(intent);
                },

                3000);

    }

    public  void backtohome(View view){
        Intent intent=new Intent(DailySport.this,MainActivity.class);
        startActivity(intent);
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

                        info1.setOnClickListener(v -> {
                            BottomSheetHelper.showBottomSheet(DailySport.this, parcalar1[4], parcalar1[5]);
                        });




                        // 2. Hareket
                        String[] parcalar2 = hareketler.get(1).split(",");
                        name2.setText(parcalar2[0] + "\n" + parcalar2[3]);

                        if (parcalar2[1].equals("kolay")) dificult2.setImageResource(R.drawable.green);
                        else if (parcalar2[1].equals("orta")) dificult2.setImageResource(R.drawable.orange);
                        else if (parcalar2[1].equals("zor")) dificult2.setImageResource(R.drawable.red);
                        info2.setOnClickListener(v -> {
                            BottomSheetHelper.showBottomSheet(DailySport.this, parcalar2[4], parcalar2[5]);
                        });


                        // 3. Hareket
                        String[] parcalar3 = hareketler.get(2).split(",");
                        name3.setText(parcalar3[0] + "\n" + parcalar3[3]);
                        if (parcalar3[1].equals("kolay")) dificult3.setImageResource(R.drawable.green);
                        else if (parcalar3[1].equals("orta")) dificult3.setImageResource(R.drawable.orange);
                        else if (parcalar3[1].equals("zor")) dificult3.setImageResource(R.drawable.red);
                        info3.setOnClickListener(v -> {
                            BottomSheetHelper.showBottomSheet(DailySport.this, parcalar3[4], parcalar3[5]);
                        });

                        // 4. Hareket
                        String[] parcalar4 = hareketler.get(3).split(",");
                        name4.setText(parcalar4[0] + "\n" + parcalar4[3]);
                        if (parcalar4[1].equals("kolay")) dificult4.setImageResource(R.drawable.green);
                        else if (parcalar4[1].equals("orta")) dificult4.setImageResource(R.drawable.orange);
                        else if (parcalar4[1].equals("zor")) dificult4.setImageResource(R.drawable.red);
                        info4.setOnClickListener(v -> {
                            BottomSheetHelper.showBottomSheet(DailySport.this, parcalar4[4], parcalar4[5]);
                        });

                        // 5. Hareket
                        String[] parcalar5 = hareketler.get(4).split(",");
                        name5.setText(parcalar5[0] + "\n" + parcalar5[3]);
                        if (parcalar5[1].equals("kolay")) dificult5.setImageResource(R.drawable.green);
                        else if (parcalar5[1].equals("orta")) dificult5.setImageResource(R.drawable.orange);
                        else if (parcalar5[1].equals("zor")) dificult5.setImageResource(R.drawable.red);
                        info5.setOnClickListener(v -> {
                            BottomSheetHelper.showBottomSheet(DailySport.this, parcalar5[4], parcalar5[5]);
                        });
                    }

                }
            }
        });

    }








}