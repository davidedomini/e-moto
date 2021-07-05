package com.example.e_moto.RecyclerView;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_moto.R;

public class CardViewHolder extends RecyclerView.ViewHolder {

    ImageView cardImage;
    TextView modello;
    TextView prezzo;
    TextView descrizione;
    TextView luogo;

    public CardViewHolder(@NonNull View itemView) {
        super(itemView);
        cardImage = itemView.findViewById(R.id.card_moto_image);
        modello = itemView.findViewById(R.id.card_modello_moto);
        prezzo = itemView.findViewById(R.id.card_prezzo_moto);
        descrizione = itemView.findViewById(R.id.card_descrizione_moto);
        luogo = itemView.findViewById(R.id.card_luogo_moto);
    }
}
