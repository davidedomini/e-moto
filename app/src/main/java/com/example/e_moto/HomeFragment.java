package com.example.e_moto;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_moto.RecyclerView.CardAdapter;
import com.example.e_moto.RecyclerView.OnItemListener;
import com.example.e_moto.ViewModel.ListViewModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class HomeFragment extends Fragment implements OnItemListener {

    private CardAdapter adapter;
    private RecyclerView recyclerView;
    private ListViewModel listViewModel;

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
        }

    }

    private void setRecyclerView(final Activity activity) {
        // Set up the RecyclerView
        recyclerView = getView().findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        final OnItemListener listener = this;
        adapter = new CardAdapter(activity, listener);
        recyclerView.setAdapter(adapter);
        /*
        List<CardItem> list = new ArrayList<>() ;

        list.add(new CardItem("ic_baseline_directions_bike_24", "Aprilia rs50", "800€", "Scooter aprilia", "Ravenna"));
        list.add(new CardItem("ic_baseline_directions_bike_24", "BMW G310R", "3000€", "BMW naked media", "Milano"));
        list.add(new CardItem("ic_baseline_directions_bike_24", "Yamaha mt03", "5000€", "Yamaha naked", "Napoli"));
        list.add(new CardItem("ic_baseline_directions_bike_24", "KTM Duke 390", "6000€", "Naked KTM", "Bologna"));
        list.add(new CardItem("ic_baseline_directions_bike_24", "Ducati Monster", "10000€", "Naked Ducati", "Ravenna"));
        list.add(new CardItem("ic_baseline_directions_bike_24", "Ducati Scrambler", "15000€", "Scambler Ducati", "Salerno"));
        list.add(new CardItem("ic_baseline_directions_bike_24", "Ducati panigale", "2000€", "Ducati sportiva", "Modena"));
        list.add(new CardItem("ic_baseline_directions_bike_24", "Ducati hypermotard", "16000€", "Ducati motard", "Roma"));

        adapter = new CardAdapter(activity, list);
        recyclerView.setAdapter(adapter);*/
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
