package com.example.gymworkout.Other;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.gymworkout.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class BottomSheetHelper {

    public static void showBottomSheet(Activity activity, String aciklama, String url) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(activity);
        View view = LayoutInflater.from(activity).inflate(R.layout.bottom_sheet_info, null);

        TextView aciklamaText = view.findViewById(R.id.hareketAciklama);
        ImageView hareketGif = view.findViewById(R.id.hareketGif);

        aciklamaText.setText(aciklama);

        Glide.with(activity)
                .asGif()
                .load(url)
                .into(hareketGif);

        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();
    }
}