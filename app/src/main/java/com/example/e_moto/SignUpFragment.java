package com.example.e_moto;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class SignUpFragment extends Fragment {

    private FirebaseAuth mAuth;
    TextInputEditText username;
    TextInputEditText password;
    Button signupBtn;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.signup, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final Activity activity = getActivity();
        if(activity != null) {

            username = activity.findViewById(R.id.username_signup);
            password = activity.findViewById(R.id.password_signup);
            signupBtn = activity.findViewById(R.id.registrati);
            mAuth = FirebaseAuth.getInstance();


            this.signupBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String usr = username.getText().toString().trim();
                    String pwd = password.getText().toString().trim();

                    if (usr.isEmpty()) {
                        username.setError("Username is required!");
                        return;
                    }
                    if (pwd.isEmpty()) {
                        password.setError("Password is required!");
                        return;
                    }

                    mAuth.createUserWithEmailAndPassword(usr, pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(activity.getApplicationContext(), "Utente registrato correttamente!", Toast.LENGTH_SHORT).show();
                                activity.startActivity(new Intent(activity.getApplicationContext(), HomeActivity.class));
                            } else {
                                Toast.makeText(activity.getApplicationContext(), "Errore!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            });

        }

    }
}
