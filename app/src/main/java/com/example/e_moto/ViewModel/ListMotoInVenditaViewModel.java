package com.example.e_moto.ViewModel;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.e_moto.CardItem;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListMotoInVenditaViewModel extends AndroidViewModel {

    private MutableLiveData<List<CardItem>> cardItems;
    private final MutableLiveData<CardItem> itemSelected = new MutableLiveData<>();
    private String usr;

    ArrayList<HashMap<String, String>> bikes = new ArrayList<>();


    //Image Storage

    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef = storage.getReference();

    //DB
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public ListMotoInVenditaViewModel(@NonNull Application application) {
        super(application);

        SharedPreferences sharedPreferences = application.getApplicationContext().getSharedPreferences("login", Context.MODE_PRIVATE);
        usr = sharedPreferences.getString("username", "not exist");

        ArrayList<CardItem> items = new ArrayList<>();
        this.cardItems = new MutableLiveData<>(items);

        getElements();

    }


    public void select(CardItem cardItem){
        this.itemSelected.setValue(cardItem);
    }

    public LiveData<CardItem> getSelected(){
        return this.itemSelected;
    }

    public LiveData<List<CardItem>> getCardItems(){
        return this.cardItems;
    }

    public void reset(){
        ArrayList<CardItem> list = new ArrayList<>();
        this.cardItems.setValue(list);
    }


    public void getElements(){

        this.cardItems.setValue(new ArrayList<CardItem>());

        db.collection("moto").whereEqualTo("utente venditore", usr).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                for(DocumentSnapshot d : value.getDocuments()){
                    Map<String, Object> bike = d.getData();
                    String modello = (String) bike.get("modello");
                    String descrizione = (String) bike.get("descrizione");
                    String prezzo = (String) bike.get("prezzo");
                    String luogo = (String) bike.get("luogo");
                    String usr = (String) bike.get("utente venditore");
                    String downloadLink = (String) bike.get("Download link");


                    storageRef.child(usr + ": " + modello).getBytes(1024*1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            Bitmap bt = BitmapFactory.decodeByteArray(bytes, 0 ,bytes.length);
                            addCardItem(new CardItem("ic_baseline_directions_bike_24", modello, prezzo, descrizione, luogo, bt));
                        }
                    });
                }

            }
        });

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
