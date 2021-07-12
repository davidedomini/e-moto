package com.example.e_moto;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.e_moto.RecyclerView.CardOfferteAdapter;
import com.example.e_moto.RecyclerView.OnItemListener;
import com.example.e_moto.ViewModel.ListOfferteInviateViewModel;
import com.example.e_moto.ViewModel.ListOfferteRicevuteViewModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OfferteRicevuteFragment extends Fragment implements OnItemListener {

    private CardOfferteAdapter adapter;
    private RecyclerView recyclerView;
    private ListOfferteRicevuteViewModel listViewModel;

    //DB
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.offerte_ricevute, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Activity activity = getActivity();
        if(activity != null){

            setRecyclerView(activity);

            listViewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(ListOfferteRicevuteViewModel.class);
            listViewModel.getCardItems().observe((LifecycleOwner) activity, new Observer<List<CardItemOfferte>>() {
                @Override
                public void onChanged(List<CardItemOfferte> cardItems) {
                    adapter.setData(cardItems);
                }
            });

            SwipeRefreshLayout refresh = view.findViewById(R.id.swipe_refresh_offerte_ricevute);
            refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    listViewModel.getElements();
                    refresh.setRefreshing(false);
                }
            });

        }

    }



    private void setRecyclerView(final Activity activity) {
        // Set up the RecyclerView
        recyclerView = getView().findViewById(R.id.recycler_view_offerte_ricevute);
        recyclerView.setHasFixedSize(true);

        final OnItemListener listener = this;
        adapter = new CardOfferteAdapter(activity, listener);
        recyclerView.setAdapter(adapter);
    }


    @Override
    public void onItemClick(int position) {
        AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
        if(appCompatActivity != null){
            showDialog(appCompatActivity,  listViewModel.getCardItem(position));
        }
    }


    private void showDialog(Activity activity, CardItemOfferte cardItem){
        new AlertDialog.Builder(activity)
                .setMessage("Accettare l'offerta selezionata?")
                .setCancelable(false)
                .setPositiveButton("Si", (dialog, id) -> acceptOffer(cardItem))
                .setNegativeButton("NO", (dialog, id) -> declineOffer(cardItem))
                .create()
                .show();
    }


    private void declineOffer(CardItemOfferte cardItem){
        //Recupero username nelle SharedPreferences
        SharedPreferences sharedPreferences = getActivity().getApplicationContext().getSharedPreferences("login", Context.MODE_PRIVATE);
        String usr = sharedPreferences.getString("username", "not exist");

        db.collection("offerte")
                .whereEqualTo("utente venditore", usr)
                .whereEqualTo("moto", cardItem.getMoto())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        for(DocumentSnapshot d : value.getDocuments()){
                            db.collection("offerte")
                                    .document(d.getId())
                                    .update("stato offerta", "Rifiutata");
                            Toast.makeText(getActivity().getApplicationContext(), "Offerta rifiutata!", Toast.LENGTH_SHORT ).show();
                        }
                        Utilities.insertFragment((AppCompatActivity) getActivity(), new SettingsFragment(), "Settings fragment"); //Vedere se si trova soluzione migliore
                    }
                });
    }


    private void acceptOffer(CardItemOfferte cardItem){

        //Recupero username nelle SharedPreferences
        SharedPreferences sharedPreferences = getActivity().getApplicationContext().getSharedPreferences("login", Context.MODE_PRIVATE);
        String usr = sharedPreferences.getString("username", "not exist");

        db.collection("offerte")
                .whereEqualTo("utente venditore", usr)
                .whereEqualTo("moto", cardItem.getMoto())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        for(DocumentSnapshot d : value.getDocuments()){

                           db.collection("offerte")
                                   .document(d.getId())
                                   .update("stato offerta", "Accettata");
                            Toast.makeText(getActivity().getApplicationContext(), "Offerta accettata!", Toast.LENGTH_SHORT ).show();
                        }
                        Utilities.insertFragment((AppCompatActivity) getActivity(), new SettingsFragment(), "Settings fragment"); //Vedere se si trova soluzione migliore
                    }
                });

    }




}
