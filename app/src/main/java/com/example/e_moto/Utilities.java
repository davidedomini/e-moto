package com.example.e_moto;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class Utilities {

    static void insertFragment(final AppCompatActivity activity, final Fragment fragment, final String tag){
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container_view, fragment, tag);


        if( !(fragment instanceof LoginFragment) ){
            transaction.addToBackStack(tag);
        }


        transaction.commit();
    }

}
