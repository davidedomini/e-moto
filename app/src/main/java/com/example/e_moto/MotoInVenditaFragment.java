package com.example.e_moto;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

import com.example.e_moto.RecyclerView.CardAdapter;
import com.example.e_moto.RecyclerView.OnItemListener;
import com.example.e_moto.ViewModel.ListMotoInVenditaViewModel;
import com.example.e_moto.ViewModel.ListViewModel;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class MotoInVenditaFragment extends Fragment implements OnItemListener{


    private CardAdapter adapter;
    private RecyclerView recyclerView;
    private ListMotoInVenditaViewModel listViewModel;

    //DB
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.moto_in_vendita, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Activity activity = getActivity();
        if(activity != null){
            Utilities.setUpToolbar((AppCompatActivity) activity, "eMoto");

            setRecyclerView(activity);

            listViewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(ListMotoInVenditaViewModel.class);
            listViewModel.getCardItems().observe((LifecycleOwner) activity, new Observer<List<CardItem>>() {
                @Override
                public void onChanged(List<CardItem> cardItems) {
                    adapter.setData(cardItems);
                }
            });

            SwipeRefreshLayout refresh = view.findViewById(R.id.swipe_refresh_moto_in_vendita);
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
        recyclerView = getView().findViewById(R.id.recycler_view_moto_in_vendita);
        recyclerView.setHasFixedSize(true);

        final OnItemListener listener = this;
        adapter = new CardAdapter(activity, listener);
        recyclerView.setAdapter(adapter);
    }


    @Override
    public void onItemClick(int position) {
        AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
        if(appCompatActivity != null){
            showDialog(appCompatActivity,  listViewModel.getCardItem(position));
        }
    }


    private void showDialog(Activity activity, CardItem cardItem){
        new AlertDialog.Builder(activity)
                .setMessage("Cancellare la moto selezionata dalla vendita?")
                .setCancelable(false)
                .setPositiveButton("Si", (dialog, id) -> deleteBike(cardItem))
                .setNegativeButton("NO", (dialog, id) -> dialog.cancel())
                .create()
                .show();
    }

    private void deleteBike(CardItem cardItem){
        //Recupero username nelle SharedPreferences
        SharedPreferences sharedPreferences = getActivity().getApplicationContext().getSharedPreferences("login", Context.MODE_PRIVATE);
        String usr = sharedPreferences.getString("username", "not exist");

        db.collection("moto")
                .whereEqualTo("utente venditore", usr)
                .whereEqualTo("modello", cardItem.getModello())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        for(DocumentSnapshot d : value.getDocuments()){
                            //Delete
                            db.collection("moto").document(d.getId()).delete();
                        }
                        Utilities.insertFragment((AppCompatActivity) getActivity(), new SettingsFragment(), "Settings fragment"); //Vedere se si trova soluzione migliore
                    }
                });
    }

}
