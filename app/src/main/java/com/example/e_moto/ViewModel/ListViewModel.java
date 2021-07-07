package com.example.e_moto.ViewModel;

import android.app.Activity;
import android.app.Application;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.e_moto.CardItem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import okhttp3.internal.cache.DiskLruCache;

public class ListViewModel extends AndroidViewModel {

    private MutableLiveData<List<CardItem>> cardItems;
    private final MutableLiveData<CardItem> itemSelected = new MutableLiveData<>();

    //Firebase realtime database
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference().child("moto");
    ArrayList<HashMap<String, String>> bikes = new ArrayList<>();


    FirebaseFirestore db = FirebaseFirestore.getInstance();

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
        //Recupero i dati dal db e setto

        Log.d("load", "Chiamato il load");

        /*
        myRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {

                if(task.isSuccessful()){

                    Iterator<DataSnapshot> i = task.getResult().getChildren().iterator();
                    while (i.hasNext()){
                        DataSnapshot d = i.next();
                        HashMap<String, String> bike = new HashMap<>();
                        for(DataSnapshot m : d.getChildren()){
                            bike.put(m.getKey().toString(), m.getValue().toString());
                        }
                        bikes.add(bike);
                    }

                }

                for (HashMap<String, String> h : bikes){
                    String modello = h.get("modello");
                    String descrizione = h.get("descrizione");
                    String prezzo = h.get("prezzo");
                    String luogo = h.get("luogo");
                    addCardItem(new CardItem("ic_baseline_directions_bike_24", modello, prezzo, descrizione, luogo));
                }
            }
        });
*/


        db.collection("moto")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for(QueryDocumentSnapshot d : task.getResult()){
                                Map<String, Object> bike = d.getData();
                                //Log.d("AAA", d.getData().toString());
                                String modello = (String) bike.get("modello");
                                String descrizione = (String) bike.get("descrizione");
                                String prezzo = (String) bike.get("prezzo");
                                String luogo = (String) bike.get("luogo");
                                addCardItem(new CardItem("ic_baseline_directions_bike_24", modello, prezzo, descrizione, luogo));
                            }
                        }
                    }
                });
        addCardItem(new CardItem("ic_baseline_directions_bike_24", "Aprilia rs50", "800€", "Scooter aprilia", "Ravenna"));
        /*addCardItem(new CardItem("ic_baseline_directions_bike_24", "BMW G310R", "3000€", "BMW naked media", "Milano"));
        addCardItem(new CardItem("ic_baseline_directions_bike_24", "Yamaha mt03", "5000€", "Yamaha naked", "Napoli"));
        addCardItem(new CardItem("ic_baseline_directions_bike_24", "KTM Duke 390", "6000€", "Naked KTM", "Bologna"));
        addCardItem(new CardItem("ic_baseline_directions_bike_24", "Ducati Monster", "10000€", "Naked Ducati", "Ravenna"));
        addCardItem(new CardItem("ic_baseline_directions_bike_24", "Ducati Scrambler", "15000€", "Scambler Ducati", "Salerno"));
        addCardItem(new CardItem("ic_baseline_directions_bike_24", "Ducati panigale", "2000€", "Ducati sportiva", "Modena"));
        addCardItem(new CardItem("ic_baseline_directions_bike_24", "Ducati hypermotard", "16000€", "Ducati motard", "Roma"));*/
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
