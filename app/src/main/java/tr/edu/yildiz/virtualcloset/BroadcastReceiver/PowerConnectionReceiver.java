package tr.edu.yildiz.virtualcloset.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

import tr.edu.yildiz.virtualcloset.Database.DatabaseHelper;
import tr.edu.yildiz.virtualcloset.Model.Clothes;

public class PowerConnectionReceiver extends BroadcastReceiver {
    ArrayList<String> names;

    public PowerConnectionReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_POWER_CONNECTED)) {
            Toast.makeText(context, "Cihaz şarja takıldı. Fotoğraflar Firebase'e yedekleniyor.", Toast.LENGTH_LONG).show();

            storeImages(context);
        } else if (intent.getAction().equals(Intent.ACTION_POWER_DISCONNECTED)) {
            Toast.makeText(context, "Cihaz şarjdan çıkarıldı.", Toast.LENGTH_SHORT).show();
        }
    }

    private void storeImages(Context context) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        names = new ArrayList<>();

        StorageReference listRef = storage.getReference().child("images/");

        listRef.listAll()
                .addOnSuccessListener(listResult -> {
                    for (StorageReference item : listResult.getItems()) {
                        names.add(item.getName());
                    }
                    checkUpload(context);
                })
                .addOnFailureListener(e -> {
                    // Uh-oh, an error occurred!
                });

    }

    private void checkUpload(Context context) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference imagesRef;

        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        ArrayList<Clothes> clothes = databaseHelper.getAllClothes();
        boolean inDatabase = true;

        for (Clothes aClothes : clothes) {
            if (names.contains(aClothes.getId() + ".jpg")) {
                Log.d("mytag", "image " + aClothes.getId() + " exists");
            } else {
                inDatabase = false;
                Log.d("mytag", "image " + aClothes.getId() + " not exists");

                byte[] image = aClothes.getPhoto();

                imagesRef = storageRef.child("images/" + aClothes.getId() + ".jpg");

                UploadTask uploadTask = imagesRef.putBytes(image);
                uploadTask.addOnFailureListener(exception -> Toast.makeText(context, "Fotoğraflar yüklenemedi", Toast.LENGTH_SHORT).show());
                uploadTask.addOnSuccessListener(taskSnapshot -> Toast.makeText(context, "Fotoğraflar yüklendi", Toast.LENGTH_SHORT).show());
            }
        }
        if(inDatabase) {
            Toast.makeText(context, "Fotoğraflar zaten Firebase'de var.", Toast.LENGTH_SHORT).show();
        }
    }
}
