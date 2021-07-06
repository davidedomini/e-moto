package com.example.e_moto;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;

public class SellNewMotoFragment extends Fragment {


    private TextInputEditText modello;
    private TextInputEditText descrizione;
    private TextInputEditText prezzo;
    private Button vendi;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.aggiungi_moto, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Activity activity = getActivity();

        if(activity != null){

            this.modello = view.findViewById(R.id.modello_add);
            this.descrizione = view.findViewById(R.id.descrizione_add);
            this.prezzo = view.findViewById(R.id.prezzo_add);
            this.vendi = view.findViewById(R.id.sell_moto);

            this.vendi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Aggiungo moto al realtime db
                    
                }
            });

        }
    }


}
