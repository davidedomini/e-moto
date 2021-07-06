package com.example.e_moto;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class Utilities {

    static void insertFragment(final AppCompatActivity activity, final Fragment fragment, final String tag){
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container_view, fragment, tag);


        if( !(fragment instanceof LoginFragment) && !(fragment instanceof SettingsFragment) && !(fragment instanceof SignUpFragment) && !(fragment instanceof HomeFragment)  ){
            transaction.addToBackStack(tag);
        }


        transaction.commit();
    }

    static void saveLoginStatus(String usr, Activity activity){
        SharedPreferences sharedPreferences = activity.getApplicationContext().getSharedPreferences("login", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", usr);
        editor.apply();
    }

    static void setUpToolbar(AppCompatActivity activity, String title) {
        Toolbar toolbar = activity.findViewById(R.id.top_bar);
        toolbar.setTitle(title);

        if (activity.getSupportActionBar() == null){
            //Set a Toolbar to act as the ActionBar for the Activity
            activity.setSupportActionBar(toolbar);
        }
    }


    public static Bitmap drawableToBitmap(Drawable drawable){
        if(drawable instanceof BitmapDrawable){
            return ( (BitmapDrawable) drawable ).getBitmap();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        drawable.setBounds(0,0,canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }



}
