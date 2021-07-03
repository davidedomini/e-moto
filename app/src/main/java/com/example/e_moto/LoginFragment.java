package com.example.e_moto;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class LoginFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final Activity activity = getActivity();
        if(activity != null){
            TextView signup = view.findViewById(R.id.sign_up);

            signup.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Utilities.insertFragment((AppCompatActivity) activity, new SignUpFragment(), "sign up fragment");
                }
            });

        }else{
            Log.e("LoginFragment.java", "activity is null");
        }
    }
}
