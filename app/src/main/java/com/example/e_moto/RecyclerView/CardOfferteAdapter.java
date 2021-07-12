package com.example.e_moto.RecyclerView;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_moto.CardItemOfferte;
import com.example.e_moto.R;

import java.util.ArrayList;
import java.util.List;

public class CardOfferteAdapter extends RecyclerView.Adapter<CardOfferteViewHolder> {

    private List<CardItemOfferte> cardItemList;
    private Activity activity;
    private OnItemListener listener;

    public CardOfferteAdapter(Activity activity, OnItemListener listener){
        this.activity = activity;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CardOfferteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_offerte, parent, false);
        return new CardOfferteViewHolder(layoutView, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull CardOfferteViewHolder holder, int position) {
        CardItemOfferte currentCardItem = cardItemList.get(position);
        holder.utente.setText(currentCardItem.getUtente());
        holder.moto.setText(currentCardItem.getMoto());
        holder.prezzo.setText(currentCardItem.getPrezzo());
    }

    @Override
    public int getItemCount() {
        return cardItemList.size();
    }

    public void setData(List<CardItemOfferte> list){
        this.cardItemList = new ArrayList<>(list);
        notifyDataSetChanged();
    }

}
