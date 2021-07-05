package com.example.e_moto;


public class CardItem {

    private String cardImage;
    private String modello;
    private String prezzo;
    private String descrizione;
    private String luogo;

    public CardItem(String cardImage, String modello, String prezzo, String descrizione, String luogo) {
        this.cardImage = cardImage;
        this.modello = modello;
        this.prezzo = prezzo;
        this.descrizione = descrizione;
        this.luogo = luogo;
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
}
