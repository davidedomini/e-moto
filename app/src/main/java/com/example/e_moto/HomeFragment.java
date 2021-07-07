package com.example.e_moto;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_moto.RecyclerView.CardAdapter;
import com.example.e_moto.RecyclerView.OnItemListener;
import com.example.e_moto.ViewModel.ListViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

public class HomeFragment extends Fragment implements OnItemListener {

    private CardAdapter adapter;
    private RecyclerView recyclerView;
    private ListViewModel listViewModel;

    //Firestore
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    //Firebase realtime database
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference().child("moto");
    ArrayList<HashMap<String, String>> bikes = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final Activity activity = getActivity();
        if(activity != null){

            Utilities.setUpToolbar((AppCompatActivity) activity, "eMoto");

            setRecyclerView(activity);

            listViewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(ListViewModel.class);


            listViewModel.getCardItems().observe((LifecycleOwner) activity, new Observer<List<CardItem>>() {
                @Override
                public void onChanged(List<CardItem> cardItems) {
                    adapter.setData(cardItems);
                }
            });

/*
            db.collection("moto")
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            for(DocumentChange dc : value.getDocumentChanges()) {
                                Log.d("AAA", "Entra");
                                if(dc.getType() == DocumentChange.Type.MODIFIED){
                                    Map<String, Object> bike = dc.getDocument().getData();
                                    String modello = (String) bike.get("modello");
                                    String descrizione = (String) bike.get("descrizione");
                                    String prezzo = (String) bike.get("prezzo");
                                    String luogo = (String) bike.get("luogo");
                                    listViewModel.addCardItem(new CardItem("ic_baseline_directions_bike_24", modello, prezzo, descrizione, luogo));
                                }
                            }
                        }
                    });*/
/*

            myRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    HashMap<String, String> bike = new HashMap<>();
                    for(DataSnapshot m : snapshot.getChildren()){
                        bike.put(m.getKey().toString(), m.getValue().toString());
                    }
                    bikes.add(bike);


                    for (HashMap<String, String> h : bikes){
                        String modello = h.get("modello");
                        String descrizione = h.get("descrizione");
                        String prezzo = h.get("prezzo");
                        String luogo = h.get("luogo");
                        listViewModel.addCardItem(new CardItem("ic_baseline_directions_bike_24", modello, prezzo, descrizione, luogo));
                    }
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });*/
        }

    }


    private void setRecyclerView(final Activity activity) {
        // Set up the RecyclerView
        recyclerView = getView().findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        final OnItemListener listener = this;
        adapter = new CardAdapter(activity, listener);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(int position) {
        AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();

        if(appCompatActivity != null){

            listViewModel.select(listViewModel.getCardItem(position));
            Utilities.insertFragment(appCompatActivity, new DettaglioMotoFragment(),DettaglioMotoFragment.class.getSimpleName() );

        }
    }
}
