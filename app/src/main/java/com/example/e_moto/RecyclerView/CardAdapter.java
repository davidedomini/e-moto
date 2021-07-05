package com.example.e_moto.RecyclerView;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_moto.CardItem;
import com.example.e_moto.R;

import java.util.ArrayList;
import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardViewHolder> {

    private List<CardItem> cardItemList;
    private Activity activity;

    public CardAdapter(Activity activity, List<CardItem> cardItemList){
        this.cardItemList = new ArrayList<>(cardItemList);
        this.activity = activity;
    }


    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card, parent, false);
        return new CardViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        CardItem currentCardItem = cardItemList.get(position);
        String imagePath = currentCardItem.getCardImage();

        if(imagePath.contains("ic_")){
            Drawable drawable = ContextCompat.getDrawable(activity, activity.getResources().getIdentifier(
                    imagePath, "drawable", activity.getPackageName()
            ));

            holder.cardImage.setImageDrawable(drawable);
        }

        holder.descrizione.setText(currentCardItem.getDescrizione());
        holder.modello.setText(currentCardItem.getModello());
        holder.prezzo.setText(currentCardItem.getPrezzo());
        holder.luogo.setText(currentCardItem.getLuogo());
    }

    @Override
    public int getItemCount() {
        return cardItemList.size();
    }
}
