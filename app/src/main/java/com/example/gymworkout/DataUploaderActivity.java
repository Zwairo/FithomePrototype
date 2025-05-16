package com.example.gymworkout;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DataUploaderActivity extends AppCompatActivity {

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        veriYukleJsondan();
    }

    private void veriYukleJsondan() {
        try {
            AssetManager assetManager = getAssets();
            InputStream is = assetManager.open("a.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            String jsonString = new String(buffer, StandardCharsets.UTF_8);
            JSONArray hareketlerArray = new JSONArray(jsonString);

            for (int i = 0; i < hareketlerArray.length(); i++) {
                JSONObject hareket = hareketlerArray.getJSONObject(i);

                String isim = hareket.getString("isim");
                JSONObject seviye = hareket.getJSONObject("seviye");
                Map<String, Object> seviyeMap = new HashMap<>();

                Iterator<String> keys = seviye.keys();
                while (keys.hasNext()) {
                    String key = keys.next();
                    JSONObject detay = seviye.getJSONObject(key);

                    Map<String, Object> detayMap = new HashMap<>();
                    detayMap.put("tekrar", detay.getString("tekrar"));
                    detayMap.put("dinlenme", detay.getString("dinlenme"));

                    seviyeMap.put(key, detayMap);
                }

                Map<String, Object> hareketMap = new HashMap<>();
                hareketMap.put("isim", isim);
                hareketMap.put("seviye", seviyeMap);

                db.collection("evdeSporHareketleri").add(hareketMap);
            }

        } catch (Exception e) {
            Log.e("FirebaseUpload", "Hata oluştu", e);
        }
    }
}
