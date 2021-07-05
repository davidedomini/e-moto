package com.example.e_moto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null){

            //Recupero username nelle SharedPreferences
            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("login", Context.MODE_PRIVATE);
            String usr = sharedPreferences.getString("username", "not exist");

            //Se non è loggato lo mando al login se no direttamente alla home

            if(usr.contentEquals("not exist")){
                Utilities.insertFragment(this, new LoginFragment(), "Login fragment");
            }else{
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            }

        }

    }
}