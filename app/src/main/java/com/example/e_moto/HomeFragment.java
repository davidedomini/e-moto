package com.example.e_moto;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_moto.RecyclerView.CardAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class HomeFragment extends Fragment {

    private CardAdapter adapter;
    private RecyclerView recyclerView;


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

            setRecyclerView(activity);

        }

    }

    private void setRecyclerView(final Activity activity) {
        // Set up the RecyclerView
        recyclerView = getView().findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

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
        recyclerView.setAdapter(adapter);
    }

}
