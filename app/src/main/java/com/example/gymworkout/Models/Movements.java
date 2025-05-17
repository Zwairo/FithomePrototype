package com.example.gymworkout.Models;

import java.util.Map;

public class Movements {
    private String isim;
    private Map<String, Map<String, String>> seviye;

    public Movements() {} // Boş constructor Firestore için gerekli

    public String getIsim() {
        return isim;
    }

    public void setIsim(String isim) {
        this.isim = isim;
    }

    public Map<String, Map<String, String>> getSeviye() {
        return seviye;
    }

    public void setSeviye(Map<String, Map<String, String>> seviye) {
        this.seviye = seviye;
    }
}
