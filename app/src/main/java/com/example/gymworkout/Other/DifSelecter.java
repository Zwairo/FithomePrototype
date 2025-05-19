package com.example.gymworkout.Other;
import android.util.Log;

import androidx.annotation.NonNull;


import com.example.gymworkout.Models.Movements;
import com.example.gymworkout.OnHareketListReady;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.Source;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class DifSelecter {
    FirebaseFirestore db;
    FirebaseAuth auth;


    // Level hesaplama: toplam gün sayısına göre level belirle
    public int getLevel(int totalDays) {
        if (totalDays <= 3) {
            return 1;
        } else if (totalDays <= 5) {
            return 2;
        } else if (totalDays <= 7) {
            return 3;
        } else if (totalDays <= 14) {
            return 4;
        } else {
            return 5;
        }
    }

    /*
     * Level dağılımı (adet bazında):
     * Level 1: 5 Kolay
     * Level 2: 3 Kolay, 2 Orta
     * Level 3: 1 Kolay, 2 Orta, 2 Zor
     * Level 4: 2 Orta, 3 Zor
     * Level 5: 5 Zor
     *
     */

    public void getRecommendations(int totalDays, OnHareketListReady listener) {




        final int level = getLevel(totalDays);
        int easyCount = 0, mediumCount = 0, hardCount = 0;

        switch (level) {
            case 1: easyCount = 5; break;
            case 2: easyCount = 3; mediumCount = 2; break;
            case 3: easyCount = 1; mediumCount = 2; hardCount = 2; break;
            case 4: mediumCount = 2; hardCount = 3; break;
            case 5: hardCount = 5; break;
        }

        hareketleriCek(easyCount, mediumCount, hardCount, new OnHareketListReady() {

            @Override
            public void onHareketListReady(ArrayList<String> hareketler) {
                listener.onHareketListReady(hareketler); // 🔄 geri gönder

            }

        });
    }

    public void hareketleriCek(int kolaySayisi, int ortaSayisi, int zorSayisi, OnHareketListReady callback) {
        ArrayList<String> tumHareketler = new ArrayList<>();
        HashSet<String> secilenHareketIsimleri = new HashSet<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("evdeSporHareketleri")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    ArrayList<String> kolayList = new ArrayList<>();
                    ArrayList<String> ortaList = new ArrayList<>();
                    ArrayList<String> zorList = new ArrayList<>();

                    for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
                        String isim = doc.getString("isim");
                        Map<String, Object> seviyeler = (Map<String, Object>) doc.get("seviye");
                        String aciklama = doc.getString("aciklama");
                        String url = doc.getString("url");

                        for (Map.Entry<String, Object> entry : seviyeler.entrySet()) {
                            String seviye = entry.getKey(); // kolay, orta, zor

                            if (secilenHareketIsimleri.contains(isim)) continue;

                            Map<String, Object> detay = (Map<String, Object>) entry.getValue();
                            String tekrar = (String) detay.get("tekrar");
                            String dinlenme = (String) detay.get("dinlenme");

                            String format = isim + "," + seviye + "," + dinlenme + "," + tekrar + "," + aciklama + "," + url;

                            switch (seviye) {
                                case "kolay":
                                    kolayList.add(format);
                                    break;
                                case "orta":
                                    ortaList.add(format);
                                    break;
                                case "zor":
                                    zorList.add(format);
                                    break;
                            }
                        }
                    }

                    Collections.shuffle(kolayList);
                    Collections.shuffle(ortaList);
                    Collections.shuffle(zorList);

                    // Seçerken aynı isimden tekrar seçilmemesi için kontrol
                    ArrayList<String> secilenler = new ArrayList<>();

                    for (String h : kolayList) {
                        String isim = h.split(",")[0];
                        if (!secilenHareketIsimleri.contains(isim) && secilenler.size() < kolaySayisi) {
                            secilenler.add(h);
                            secilenHareketIsimleri.add(isim);
                        }
                    }

                    for (String h : ortaList) {
                        String isim = h.split(",")[0];
                        if (!secilenHareketIsimleri.contains(isim) && secilenler.size() < kolaySayisi + ortaSayisi) {
                            secilenler.add(h);
                            secilenHareketIsimleri.add(isim);
                        }
                    }

                    for (String h : zorList) {
                        String isim = h.split(",")[0];
                        if (!secilenHareketIsimleri.contains(isim) && secilenler.size() < kolaySayisi + ortaSayisi + zorSayisi) {
                            secilenler.add(h);
                            secilenHareketIsimleri.add(isim);
                        }
                    }

                    callback.onHareketListReady(secilenler);
                }); // <-- addOnSuccessListener kapanışı
    }}


















