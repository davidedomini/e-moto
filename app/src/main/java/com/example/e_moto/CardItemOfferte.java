package com.example.e_moto;

public class CardItemOfferte {
    private String utente;
    private String moto;
    private String prezzo;
    private String statoOfferta;

    public CardItemOfferte(String utente, String moto, String prezzo, String statoOfferta) {
        this.utente = utente;
        this.moto = moto;
        this.prezzo = prezzo;
        this.statoOfferta = statoOfferta;
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

    public String getStato() {
        return statoOfferta;
    }
}
