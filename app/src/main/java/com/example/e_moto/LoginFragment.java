package com.example.e_moto;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginFragment extends Fragment {

    private FirebaseAuth mAuth;
    Button loginBtn;
    TextInputEditText username;
    TextInputEditText password;


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

            loginBtn = view.findViewById(R.id.login);
            username = view.findViewById(R.id.username);
            password = view.findViewById(R.id.password);
            mAuth = FirebaseAuth.getInstance();

            loginBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String usr = username.getText().toString().trim();
                    String pwd = password.getText().toString().trim();
                    if(usr.isEmpty()){
                        username.setError("Username is required!");
                        return;
                    }
                    if(pwd.isEmpty()){
                        password.setError("Password is required!");
                        return;
                    }

                    activity.findViewById(R.id.progressbar_login).setVisibility(View.VISIBLE);

                    mAuth.signInWithEmailAndPassword(usr, pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(activity.getApplicationContext(), "Login effettuato con successo!", Toast.LENGTH_SHORT ).show();
                                activity.startActivity(new Intent(activity.getApplicationContext(), HomeActivity.class));
                            }else{
                                Toast.makeText(activity.getApplicationContext(), "Errore username o password sbagliati!", Toast.LENGTH_SHORT ).show();
                                activity.findViewById(R.id.progressbar_login).setVisibility(View.GONE);
                            }
                        }
                    });

                }
            });



        }else{
            Log.e("LoginFragment.java", "activity is null");
        }
    }
}
