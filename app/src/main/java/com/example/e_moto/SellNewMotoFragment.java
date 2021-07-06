package com.example.e_moto;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SellNewMotoFragment extends Fragment {


    private TextInputEditText modello;
    private TextInputEditText descrizione;
    private TextInputEditText prezzo;
    private Button vendi;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference().child("moto");

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
                    //Recupero i dati della moto
                    String model = modello.getText().toString().trim();
                    String description = descrizione.getText().toString().trim();
                    String price = prezzo.getText().toString().trim();

                    //Recupero l'utente dalle SharedPreferences
                    SharedPreferences sharedPreferences = activity.getApplicationContext().getSharedPreferences("login", Context.MODE_PRIVATE);
                    String usr = sharedPreferences.getString("username", "not exist");


                    //Creo una hashmap con i dati della nuova moto
                    HashMap<String, String> newMoto = new HashMap<>();
                    newMoto.put("modello", model);
                    newMoto.put("descrizione", description);
                    newMoto.put("prezzo", price);
                    newMoto.put("utente venditore", usr);

                    //Aggiungo i dati al db
                    myRef.push().setValue(newMoto).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(activity.getApplicationContext(), "Dati inseriti!", Toast.LENGTH_SHORT ).show();
                            }else{
                                Toast.makeText(activity.getApplicationContext(), "Errore, dati NON inseriti correttamente!", Toast.LENGTH_SHORT ).show();
                            }

                        }
                    });

                }
            });

        }
    }


}
