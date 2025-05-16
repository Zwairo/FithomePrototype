package com.example.gymworkout;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UserActions {


    public static void kaydetVeyaGuncelleKullanici(String userId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("Users").document(userId);

        userRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                // Kullanıcı daha önce kayıt olmuş - güncelle
                userRef.update(
                        "sonGirisTarihi", FieldValue.serverTimestamp(),
                        "toplamGiris", FieldValue.increment(1)
                );
            } else {
                // İlk defa giriş yapıyor - yeni kayıt
                Map<String, Object> userData = new HashMap<>();
                userData.put("kayitTarihi", FieldValue.serverTimestamp());
                userData.put("sonGirisTarihi", FieldValue.serverTimestamp());
                userData.put("toplamGiris", 1);
                userData.put("toplamCalismaSuresi", 0); // dakika cinsinden tutulabilir
                userRef.set(userData);
            }
        });
    }


}
