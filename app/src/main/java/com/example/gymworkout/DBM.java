package com.example.gymworkout;

import android.util.Log;

import com.example.gymworkout.Other.DifSelecter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class DBM {
    FirebaseFirestore db;
    FirebaseAuth auth;

    public void hareketleriYukleVeGoster(int toplam) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        String userId = auth.getCurrentUser().getUid();
        String bugunTarih = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        DocumentReference docRef = db.collection("Users")
                .document(userId)
                .collection("girisGunleri")
                .document(bugunTarih);

        docRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists() && documentSnapshot.contains("hareketler")) {
                // 🔁 Zaten kayıtlı -> göster
                ArrayList<String> hareketler = (ArrayList<String>) documentSnapshot.get("hareketler");
                DailySport dailySport = new DailySport();
               dailySport.VeriAtama(toplam); // bu senin mevcut gösterme metodun
            } else {
                // ❌ Yoksa -> önce totalDays alalım
                db.collection("Users").document(userId)
                        .collection("girisGunleri")
                        .get()
                        .addOnSuccessListener(snapshot -> {
                            int totalDays = snapshot.size(); // toplam gün sayısı
                            DifSelecter selecter = new DifSelecter();

                            selecter.getRecommendations(totalDays, new OnHareketListReady() {
                                @Override
                                public void onHareketListReady(ArrayList<String> hareketler) {
                                    DailySport dailySport = new DailySport();
                                    dailySport.VeriAtama(toplam); // gösterim
                                    // ❗️Burada tekrar Firestore’a yazmaya gerek yok, zaten DifSelecter içinde yazılıyor
                                }
                            });
                        })
                        .addOnFailureListener(e -> Log.e("FIRESTORE", "Gün sayısı alınamadı: " + e.getMessage()));
            }
        }).addOnFailureListener(e -> Log.e("FIRESTORE", "Bugünün hareketleri kontrol edilemedi: " + e.getMessage()));
    }


}




