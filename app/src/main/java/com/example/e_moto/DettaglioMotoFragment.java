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


public class DettaglioMotoFragment extends Fragment {

    private TextView modello;
    private TextView descrizione;
    private TextView prezzo;
    private ImageView image;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        Activity activity = getActivity();

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
                }
            });
        }



    }
}
