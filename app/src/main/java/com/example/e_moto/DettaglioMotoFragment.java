package com.example.e_moto;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.example.e_moto.ViewModel.ListViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class DettaglioMotoFragment extends Fragment implements OnMapReadyCallback {

    private TextView modello;
    private TextView descrizione;
    private TextView prezzo;
    private ImageView image;
    private MapView mapView;
    private MapboxMap mapboxMap;
    private Activity activity;

    private Double lat;
    private Double lng;

    private static final String SOURCE_ID = "SOURCE_ID";
    private static final String ICON_ID = "ICON_ID";
    private static final String LAYER_ID = "LAYER_ID";



    //Firestore
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (Activity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(activity, getString(R.string.mapbox_access_token));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dettaglio_moto, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        modello = view.findViewById(R.id.modello_dettaglio);
        descrizione = view.findViewById(R.id.descrizione_dettaglio);
        prezzo = view.findViewById(R.id.prezzo_dettaglio);
        image = view.findViewById(R.id.moto_images);
        mapView = view.findViewById(R.id.bikemap);
        

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        //Activity activity = getActivity();

        if(activity != null){

            ListViewModel listViewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(ListViewModel.class);
            listViewModel.getSelected().observe(getViewLifecycleOwner(), new Observer<CardItem>() {
                @Override
                public void onChanged(CardItem cardItem) {
                    modello.setText(cardItem.getModello());
                    descrizione.setText(cardItem.getDescrizione());
                    prezzo.setText(cardItem.getPrezzo());
                    String image_path = cardItem.getCardImage();

                    /*if(image_path.contains("ic_")){
                        Drawable drawable = ContextCompat.getDrawable(activity,
                                activity.getResources().getIdentifier(image_path,
                                        "drawable", activity.getPackageName()));

                        image.setImageDrawable(drawable);
                    }*/
                    image.setImageBitmap(cardItem.getBikeImage());

                    String luogo  = cardItem.getLuogo();
                    //lat = Double.parseDouble(luogo.split(", ")[0]);
                    //lng = Double.parseDouble(luogo.split(", ")[1]);
                    Geocoder geocoder = new Geocoder(activity, Locale.ITALY);
                    try {
                        List<Address> addresses = geocoder.getFromLocationName(luogo, 1);
                        if(addresses.size() > 0){
                            lat = addresses.get(0).getLatitude();
                            lng = addresses.get(0).getLongitude();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }
            });

            view.findViewById(R.id.contatta_venditore).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    //Recupero l'utente dalle SharedPreferences
                    SharedPreferences sharedPreferences = activity.getApplicationContext().getSharedPreferences("login", Context.MODE_PRIVATE);
                    String usr = sharedPreferences.getString("username", "not exist");

                    //Recupero dati della moto
                    CardItem moto = listViewModel.getSelected().getValue();

                    db.collection("moto")
                            .whereEqualTo("modello", moto.getModello())
                            .whereEqualTo("descrizione", moto.getDescrizione())
                            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                    Map<String, Object> motoDB = new HashMap<>();
                                    for(DocumentSnapshot d : value.getDocuments()){
                                        motoDB = d.getData();
                                    }

                                    String utenteVenditore = (String) motoDB.get("utente venditore");

                                    //Manda offerta al venditore

                                    HashMap<String, String> nuovaOfferta = new HashMap<>();
                                    nuovaOfferta.put("utente acquirente", usr);
                                    nuovaOfferta.put("prezzo", moto.getPrezzo());
                                    nuovaOfferta.put("utente venditore", utenteVenditore);
                                    nuovaOfferta.put("moto", moto.getModello());

                                    db.collection("offerte")
                                            .add(nuovaOfferta)
                                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                @Override
                                                public void onSuccess(DocumentReference documentReference) {
                                                    Toast.makeText(activity.getApplicationContext(), "Offerta inviata!", Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(activity.getApplicationContext(), "Ops...si è verificato un errore!", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            });

                }
            });

        }


    }

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {

        this.mapboxMap = mapboxMap;

        this.mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {

                CameraPosition position = new CameraPosition.Builder()
                        .target(new LatLng(lat, lng))
                        .zoom(5)
                        .tilt(20)
                        .build();
                mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), 100);

                MarkerOptions options = new MarkerOptions();
                options.title("Posizione della moto");
                options.position(new LatLng(lat, lng));
                mapboxMap.addMarker(options);

            }
        });

    }
}
