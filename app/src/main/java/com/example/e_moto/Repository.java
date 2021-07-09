package com.example.e_moto;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Repository {

    private LiveData<List<CardItem>> cardItemList;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public Repository(){

        List<CardItem> items = new ArrayList<>();
        db.collection("moto")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for(QueryDocumentSnapshot d : task.getResult()){
                                Map<String, Object> bike = d.getData();
                                String modello = (String) bike.get("modello");
                                String descrizione = (String) bike.get("descrizione");
                                String prezzo = (String) bike.get("prezzo");
                                String luogo = (String) bike.get("luogo");
                                //items.add(new CardItem("ic_baseline_directions_bike_24", modello, prezzo, descrizione, luogo));
                                //addCardItem(new CardItem("ic_baseline_directions_bike_24", modello, prezzo, descrizione, luogo));
                            }
                        }
                    }
                });
    }

    public LiveData<List<CardItem>> getCardItemList() {
        return cardItemList;
    }
}
