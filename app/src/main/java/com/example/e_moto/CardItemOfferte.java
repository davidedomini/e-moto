package com.example.e_moto;

public class CardItemOfferte {
    private String utente;
    private String moto;
    private String prezzo;

    public CardItemOfferte(String utente, String moto, String prezzo) {
        this.utente = utente;
        this.moto = moto;
        this.prezzo = prezzo;
    }

    public String getUtente() {
        return utente;
    }

    public String getMoto() {
        return moto;
    }

    public String getPrezzo() {
        return prezzo;
    }
}
