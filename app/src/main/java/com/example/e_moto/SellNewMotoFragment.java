package com.example.e_moto;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.example.e_moto.ViewModel.PicViewModel;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

public class SellNewMotoFragment extends Fragment {


    private TextInputEditText modello;
    private TextInputEditText descrizione;
    private TextInputEditText prezzo;
    private Button vendi;
    private ImageView imageView;
    private Uri imageUri;
    private Bitmap imageBitmap;

    //Firebase realtime database
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference().child("moto");

    //Firebase cloud storage
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef = storage.getReference();
    String imageDownloadLink;

    //Firestore
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    //GPS
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;
    private ActivityResultLauncher<String> requestPermissionLauncher;
    private boolean requestingLocationUpdates = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.aggiungi_moto, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Activity activity = getActivity();

        if (activity != null) {

            requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(),
                    new ActivityResultCallback<Boolean>() {
                        @Override
                        public void onActivityResult(Boolean result) {
                            if(result){
                                startLocationUpdates(activity);
                                Log.d("LAB", "PERMISSION GRANTED");
                            }else{
                                Log.d("LAB", "PERMISSION DENIED");
                            }
                        }
                    });

            initializeLocation(activity);

            this.modello = view.findViewById(R.id.modello_add);
            this.descrizione = view.findViewById(R.id.descrizione_add);
            this.prezzo = view.findViewById(R.id.prezzo_add);
            this.vendi = view.findViewById(R.id.sell_moto);
            this.imageView = view.findViewById(R.id.imageView);
            TextView luogo = view.findViewById(R.id.luogoTextView);


            this.vendi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Recupero i dati della moto
                    String model = modello.getText().toString().trim();
                    String description = descrizione.getText().toString().trim();
                    String price = prezzo.getText().toString().trim();
                    String location = luogo.getText().toString().trim();

                    //Recupero l'utente dalle SharedPreferences
                    SharedPreferences sharedPreferences = activity.getApplicationContext().getSharedPreferences("login", Context.MODE_PRIVATE);
                    String usr = sharedPreferences.getString("username", "not exist");

                    activity.findViewById(R.id.progressbar_vendi).setVisibility(View.VISIBLE);

                    byte bytes[] = getImageBytes(imageBitmap);
                    String child = usr + ": " + model;

                    storageRef.child(child).putBytes(bytes);

                    imageDownloadLink = "gs://e-moto-3eaf4.appspot.com" + "/" + usr + ": " + model;

                    //Creo una hashmap con i dati della nuova moto
                    HashMap<String, String> newMoto = new HashMap<>();
                    newMoto.put("modello", model);
                    newMoto.put("descrizione", description);
                    newMoto.put("prezzo", price);
                    newMoto.put("utente venditore", usr);
                    newMoto.put("luogo", location);
                    newMoto.put("Download link", imageDownloadLink);


                    //Aggiungo i dati al db
                    /*
                    myRef.push().setValue(newMoto).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(activity.getApplicationContext(), "Dati inseriti!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(activity.getApplicationContext(), "Errore, dati NON inseriti correttamente!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });*/



                    db.collection("moto")
                            .add(newMoto)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Toast.makeText(activity.getApplicationContext(), "Dati inseriti!", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(activity.getApplicationContext(), "ERRORE! Dati non inseriti!", Toast.LENGTH_SHORT).show();
                                }
                            });

                    activity.findViewById(R.id.progressbar_vendi).setVisibility(View.GONE);

                }
            });

            view.findViewById(R.id.gps_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    requestingLocationUpdates = true;
                    startLocationUpdates(activity);
                }
            });

            //Take picture
            view.findViewById(R.id.take_pic).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if(takePictureIntent.resolveActivity(activity.getPackageManager()) != null){
                        activity.startActivityForResult(takePictureIntent, 1);
                    }
                }
            });


            PicViewModel picViewModel = new ViewModelProvider( (ViewModelStoreOwner) activity ).get(PicViewModel.class);
            picViewModel.getBitmap().observe(getViewLifecycleOwner(), new Observer<Bitmap>() {
                @Override
                public void onChanged(Bitmap bitmap) {
                    imageBitmap = bitmap;
                    imageView.setImageBitmap(bitmap);
                }
            });

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(requestingLocationUpdates && getActivity() != null ) {
            startLocationUpdates(getActivity());
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        stopLocationUpdates();
    }


    private void initializeLocation(Activity activity) {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity);
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                TextView luogo = activity.findViewById(R.id.luogoTextView);
                Location location = locationResult.getLastLocation();
                String lat_long = location.getLatitude() + ", " + location.getLongitude();
                luogo.setText(lat_long);

                requestingLocationUpdates = false;
                stopLocationUpdates();

            }
        };

    }

    private void startLocationUpdates(Activity activity) {
        final String PERMISSION_REQUESTED = Manifest.permission.ACCESS_FINE_LOCATION;

        if(ActivityCompat.checkSelfPermission(activity, PERMISSION_REQUESTED) == PackageManager.PERMISSION_GRANTED){
            statusGPSCheck(activity);
            fusedLocationProviderClient.requestLocationUpdates(locationRequest,
                    locationCallback,
                    Looper.getMainLooper());
        }else if(ActivityCompat.shouldShowRequestPermissionRationale(activity, PERMISSION_REQUESTED)){
            showDialog(activity);
        }else{
            requestPermissionLauncher.launch(PERMISSION_REQUESTED);
        }
    }

    private void showDialog(Activity activity){
        new AlertDialog.Builder(activity)
                .setMessage("Permission was denied, but is needed for the gps functionality.")
                .setCancelable(false)
                .setPositiveButton("OK", (dialog, id) -> activity.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)))
                .setNegativeButton("Cancel", (dialog, id) -> dialog.cancel())
                .create()
                .show();

    }

    private void stopLocationUpdates(){
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }


    private void statusGPSCheck(Activity activity) {
        final LocationManager manager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        if (manager != null && !manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            //if gps is off, show the alert message
            new androidx.appcompat.app.AlertDialog.Builder(activity)
                    .setMessage("Your GPS seems to be disabled, do you want to enable it?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", (dialog, id) -> activity.startActivity(
                            new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)))
                    .setNegativeButton("No", (dialog, id) -> dialog.cancel())
                    .create()
                    .show();
        }
    }


    private byte[] getImageBytes(Bitmap thumbnail){
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        byte bb[] = bytes.toByteArray();
        return bb;
    }

}
