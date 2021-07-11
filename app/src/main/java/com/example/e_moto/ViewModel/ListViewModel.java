package com.example.e_moto.ViewModel;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.e_moto.CardItem;
import com.example.e_moto.R;
import com.example.e_moto.Repository;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.model.Document;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mapbox.api.geocoding.v5.GeocodingCriteria;
import com.mapbox.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.api.geocoding.v5.models.GeocodingResponse;
import com.mapbox.geojson.Point;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.internal.cache.DiskLruCache;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListViewModel extends AndroidViewModel {

    private MutableLiveData<List<CardItem>> cardItems;
    private final MutableLiveData<CardItem> itemSelected = new MutableLiveData<>();
    private String usr;

    ArrayList<HashMap<String, String>> bikes = new ArrayList<>();


    //Image Storage

    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef = storage.getReference();

    //DB
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public ListViewModel(@NonNull Application application) {
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

        /*
        if(this.cardItems == null){
            this.cardItems = new MutableLiveData<>();
            loadItems();
        }*/
        return this.cardItems;
    }

    public void reset(){
        ArrayList<CardItem> list = new ArrayList<>();
        this.cardItems.setValue(list);
    }


    public void getElements(){

        this.cardItems.setValue(new ArrayList<CardItem>());


        db.collection("moto").whereNotEqualTo("utente venditore", usr).addSnapshotListener(new EventListener<QuerySnapshot>() {
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

                        //Converto il luogo da coordinate a nome
                        double lat = Double.parseDouble(luogo.split(", ")[0]);
                        double lng = Double.parseDouble(luogo.split(", ")[1]);

                        Geocoder geocoder = new Geocoder(getApplication().getApplicationContext(), Locale.ITALY);
                        try {
                            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
                            if(addresses.size() > 0){
                               String address = addresses.get(0).getLocality();

                                storageRef.child(usr + ": " + modello).getBytes(1024*1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                    @Override
                                    public void onSuccess(byte[] bytes) {
                                        Bitmap bt = BitmapFactory.decodeByteArray(bytes, 0 ,bytes.length);
                                        addCardItem(new CardItem("ic_baseline_directions_bike_24", modello, prezzo, descrizione, address, bt));
                                    }
                                });

                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }



                }
            });

    }


    private  void loadItems(){
        //Recupero i dati dal db e setto

        Log.d("load", "Chiamato il load");

/*
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
                });*/
        //addCardItem(new CardItem("ic_baseline_directions_bike_24", "Aprilia rs50", "800€", "Scooter aprilia", "Ravenna"));
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
