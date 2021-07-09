package com.example.e_moto;


import android.graphics.Bitmap;

public class CardItem {

    private String cardImage;
    private String modello;
    private String prezzo;
    private String descrizione;
    private String luogo;
    private Bitmap bikeImage;

    public CardItem(String cardImage, String modello, String prezzo, String descrizione, String luogo, Bitmap bikeImage) {
        this.cardImage = cardImage;
        this.modello = modello;
        this.prezzo = prezzo;
        this.descrizione = descrizione;
        this.luogo = luogo;
        this.bikeImage = bikeImage;
    }

    public String getCardImage() {
        return cardImage;
    }

    public String getModello() {
        return modello;
    }

    public String getPrezzo() {
        return prezzo;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public String getLuogo() {
        return luogo;
    }

    public Bitmap getBikeImage() {
        return bikeImage;
    }

}
