package com.example.e_moto.ViewModel;

import android.app.Activity;
import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.e_moto.CardItem;

import java.util.ArrayList;
import java.util.List;

public class ListViewModel extends AndroidViewModel {

    private MutableLiveData<List<CardItem>> cardItems;
    private final MutableLiveData<CardItem> itemSelected = new MutableLiveData<>();

    public ListViewModel(@NonNull Application application) {
        super(application);
    }

    public void select(CardItem cardItem){
        this.itemSelected.setValue(cardItem);
    }

    public LiveData<CardItem> getSelected(){
        return this.itemSelected;
    }

    public LiveData<List<CardItem>> getCardItems(){

        if(this.cardItems == null){
            this.cardItems = new MutableLiveData<>();
            loadItems();
        }
        return this.cardItems;
    }

    private  void loadItems(){
        addCardItem(new CardItem("ic_baseline_directions_bike_24", "Aprilia rs50", "800€", "Scooter aprilia", "Ravenna"));
        addCardItem(new CardItem("ic_baseline_directions_bike_24", "BMW G310R", "3000€", "BMW naked media", "Milano"));
        addCardItem(new CardItem("ic_baseline_directions_bike_24", "Yamaha mt03", "5000€", "Yamaha naked", "Napoli"));
        addCardItem(new CardItem("ic_baseline_directions_bike_24", "KTM Duke 390", "6000€", "Naked KTM", "Bologna"));
        addCardItem(new CardItem("ic_baseline_directions_bike_24", "Ducati Monster", "10000€", "Naked Ducati", "Ravenna"));
        addCardItem(new CardItem("ic_baseline_directions_bike_24", "Ducati Scrambler", "15000€", "Scambler Ducati", "Salerno"));
        addCardItem(new CardItem("ic_baseline_directions_bike_24", "Ducati panigale", "2000€", "Ducati sportiva", "Modena"));
        addCardItem(new CardItem("ic_baseline_directions_bike_24", "Ducati hypermotard", "16000€", "Ducati motard", "Roma"));
    }

    public void addCardItem(CardItem item){
        ArrayList<CardItem> list = new ArrayList<>();
        list.add(item);
        if(cardItems.getValue() != null){
            list.addAll(cardItems.getValue());
        }
        cardItems.setValue(list);
    }

    public CardItem getCardItem(int position){
        return cardItems.getValue() == null ? null : cardItems.getValue().get(position);
    }

}
