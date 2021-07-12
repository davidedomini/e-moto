package com.example.e_moto;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class OfferteFragment extends Fragment {

    TabLayout tabLayout;
    ViewPager2 viewPager2;
    OfferteAdapter offerteAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.offerte, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        offerteAdapter = new OfferteAdapter(this);
        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager2 = view.findViewById(R.id.view_pager);
        viewPager2.setAdapter(offerteAdapter);

        new TabLayoutMediator(tabLayout, viewPager2, (tab, position)->{
            if(position == 0) tab.setText("Inviate");
            else tab.setText("Ricevute");
        }).attach();

    }


    /*
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if(activity != null){

            tabLayout = view.findViewById(R.id.tabLayout);
            viewPager2 = view.findViewById(R.id.view_pager);

            FragmentManager fm = activity.getSupportFragmentManager();
            offerteAdapter = new OfferteAdapter(fm, activity.getLifecycle());
            viewPager2.setAdapter(offerteAdapter);

            tabLayout.addTab(tabLayout.newTab().setText("Inviate"));
            tabLayout.addTab(tabLayout.newTab().setText("Ricevute"));

            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    viewPager2.setCurrentItem(tab.getPosition());
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });

            viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageSelected(int position) {
                    //super.onPageSelected(position);
                    tabLayout.selectTab(tabLayout.getTabAt(position));
                }
            });

        }
    }*/
}
