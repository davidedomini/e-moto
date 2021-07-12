package com.example.e_moto.ViewModel;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.e_moto.CardItemOfferte;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListOfferteInviateViewModel extends AndroidViewModel {

    private MutableLiveData<List<CardItemOfferte>> cardItems;
    private final MutableLiveData<CardItemOfferte> itemSelected = new MutableLiveData<>();
    private String usr;

    ArrayList<HashMap<String, String>> offerte = new ArrayList<>();

    //DB
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public ListOfferteInviateViewModel(@NonNull Application application) {
        super(application);

        SharedPreferences sharedPreferences = application.getApplicationContext().getSharedPreferences("login", Context.MODE_PRIVATE);
        usr = sharedPreferences.getString("username", "not exist");

        ArrayList<CardItemOfferte> items = new ArrayList<>();
        this.cardItems = new MutableLiveData<>(items);
        getElements();

    }

    public void select(CardItemOfferte cardItem){
        this.itemSelected.setValue(cardItem);
    }

    public LiveData<CardItemOfferte> getSelected(){
        return this.itemSelected;
    }

    public LiveData<List<CardItemOfferte>> getCardItems(){
        return this.cardItems;
    }

    public void reset(){
        ArrayList<CardItemOfferte> list = new ArrayList<>();
        this.cardItems.setValue(list);
    }



    public CardItemOfferte getCardItem(int position){
        return cardItems.getValue() == null ? null : cardItems.getValue().get(position);
    }


    public void getElements() {

        reset();
        db.collection("offerte")
                .whereEqualTo("utente acquirente", usr )
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        for(DocumentSnapshot d : value.getDocuments()){
                            Map<String, Object> offerta = d.getData();
                            String venditore = (String) offerta.get("utente venditore");
                            String moto = (String) offerta.get("moto");
                            String prezzo = (String) offerta.get("prezzo");
                            addCardItem(new CardItemOfferte(venditore, moto, prezzo));
                        }
                    }
                });
    }

    public void addCardItem(CardItemOfferte item){

        ArrayList<CardItemOfferte> list = new ArrayList<>();
        list.add(item);
        if(cardItems.getValue() != null){
            list.addAll(cardItems.getValue());
        }
        cardItems.setValue(list);

    }


}
