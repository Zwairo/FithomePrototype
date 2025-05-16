package com.example.gymworkout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.auth.User;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth mAuth;

@Override
public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    EdgeToEdge.enable(this);
    setContentView(R.layout.activity_main);
    ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
        Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
        v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
        return insets;
    });


    mAuth = FirebaseAuth.getInstance();
    if (mAuth.getCurrentUser() == null) {
        mAuth.signInAnonymously()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        String userId = user.getUid();
                        UserActions.kaydetVeyaGuncelleKullanici(userId);
                        Log.d("Firebase", "Anonim kullanıcı: " + userId);
                    } else {
                        Log.e("Firebase", "Anonim giriş başarısız", task.getException());
                    }
                });
    } else {
        String userId = mAuth.getCurrentUser().getUid();
        UserActions.kaydetVeyaGuncelleKullanici(userId);
        Log.d("Firebase", "Zaten giriş yapılmış: " + userId);

    }

}

    public void dailysport(View view){
        Intent intent = new Intent(this, DailySport.class);
        startActivity(intent);
    }
    public void kilover(View view){
        Intent intent = new Intent(this, DataUploaderActivity.class);
        startActivity(intent);
    }
}
