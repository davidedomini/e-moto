package com.example.e_moto.ViewModel;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.e_moto.R;
import com.example.e_moto.Utilities;

public class PicViewModel extends AndroidViewModel {

    private final MutableLiveData<Bitmap> imageBitmap = new MutableLiveData<>();

    private final Application application;

    public PicViewModel(@NonNull Application application) {
        super(application);
        this.application = application;

    }


    public void setImageBitmap(Bitmap bitmap){
        imageBitmap.setValue(bitmap);
    }

    public LiveData<Bitmap> getBitmap(){
        return imageBitmap;
    }

    public void initializeBitmap(){
        Drawable drawable = ResourcesCompat.getDrawable(application.getResources(),
                R.drawable.ic_baseline_directions_bike_24_blue, application.getTheme());

        Bitmap bitmap = Utilities.drawableToBitmap(drawable);
        imageBitmap.setValue(bitmap);
    }

}
