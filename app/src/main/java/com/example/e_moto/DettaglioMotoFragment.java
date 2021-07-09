package com.example.e_moto;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.example.e_moto.ViewModel.ListViewModel;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;


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
                    lat = Double.parseDouble(luogo.split(", ")[0]);
                    lng = Double.parseDouble(luogo.split(", ")[1]);
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
