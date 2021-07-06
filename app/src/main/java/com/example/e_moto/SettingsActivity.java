package com.example.e_moto;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.e_moto.ViewModel.PicViewModel;

public class SettingsActivity extends AppCompatActivity {

    private PicViewModel picViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        if (savedInstanceState == null)
            Utilities.insertFragment(this, new SettingsFragment(), "Settings fragment");

        picViewModel = new ViewModelProvider(this).get(PicViewModel.class);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_app_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        if(item.getItemId() == R.id.app_bar_home){
            Intent intent = new Intent(this, HomeActivity.class);
            this.startActivity(intent);
            return true;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK){
            Bundle extras = data.getExtras();
            if(extras != null){
                Bitmap imageBitmap = (Bitmap) extras.get("data");

                picViewModel.setImageBitmap(imageBitmap);
            }
        }

    }
}
