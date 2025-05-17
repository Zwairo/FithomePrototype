package com.example.gymworkout.Other;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class UserActions {


    public static void kaydetVeyaGuncelleKullanici(String userId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("Users").document(userId);
        CollectionReference girisGunleriRef = userRef.collection("girisGunleri");

        // Bugünün tarihini al (sadece YIL-AY-GÜN)
        String bugun = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        // Önce bugünün giriş kaydı olup olmadığını kontrol et
        girisGunleriRef.document(bugun).get().addOnSuccessListener(girisDoc -> {
            boolean bugunZatenGirilmis = girisDoc.exists();

            userRef.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    // Güncelle: sadece ilk girişse toplam gün/giriş artır
                    Map<String, Object> updates = new HashMap<>();
                    updates.put("sonGirisTarihi", FieldValue.serverTimestamp());

                    if (!bugunZatenGirilmis) {
                        updates.put("toplamGiris", FieldValue.increment(1));
                        updates.put("toplamGun", FieldValue.increment(1));

                        // Bugünün girişini kaydet
                        girisGunleriRef.document(bugun).set(Collections.singletonMap("girisYapildi", true));
                    }

                    userRef.update(updates);
                } else {
                    // Yeni kullanıcı: ilk kez giriş
                    Map<String, Object> userData = new HashMap<>();
                    userData.put("kayitTarihi", FieldValue.serverTimestamp());
                    userData.put("sonGirisTarihi", FieldValue.serverTimestamp());
                    userData.put("toplamGiris", 1);
                    userData.put("toplamGun", 1);
                    userData.put("toplamCalismaSuresi", 0);

                    userRef.set(userData);

                    // İlk gün giriş kaydı
                    girisGunleriRef.document(bugun).set(Collections.singletonMap("girisYapildi", true));
                }
            });
        });
    }




}
