package com.example.e_moto.RecyclerView;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_moto.R;

public class CardOfferteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    TextView utente;
    TextView moto;
    TextView prezzo;
    OnItemListener listener;

    public CardOfferteViewHolder(@NonNull View itemView, OnItemListener listener) {
        super(itemView);
        utente = itemView.findViewById(R.id.offerta_acquirente);
        moto = itemView.findViewById(R.id.offerta_moto);
        prezzo = itemView.findViewById(R.id.offerta_prezzo);
        this.listener = listener;
        itemView.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        listener.onItemClick(getAdapterPosition());
    }
}
