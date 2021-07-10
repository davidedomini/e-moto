package com.example.e_moto;

import android.app.Activity;
import android.os.Bundle;
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

import java.util.List;

public class MotoInVenditaFragment extends Fragment implements OnItemListener{


    private CardAdapter adapter;
    private RecyclerView recyclerView;
    private ListMotoInVenditaViewModel listViewModel;


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
            //Cambia layout del dettaglio
            listViewModel.select(listViewModel.getCardItem(position));
            Utilities.insertFragment(appCompatActivity, new DettaglioMotoFragment(),DettaglioMotoFragment.class.getSimpleName() );
        }
    }


}
