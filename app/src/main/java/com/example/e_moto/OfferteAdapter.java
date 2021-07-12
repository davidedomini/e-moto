package com.example.e_moto;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class OfferteAdapter extends FragmentStateAdapter {
    public OfferteAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }


    @NonNull
    @Override
    public Fragment createFragment(int position){
        if(position == 0) return new OfferteMandateFragment();
        else return new OfferteRicevuteFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
