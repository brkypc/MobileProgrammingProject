package tr.edu.yildiz.virtualcloset;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import tr.edu.yildiz.virtualcloset.Adapters.OutfitAdapter;
import tr.edu.yildiz.virtualcloset.Database.DatabaseHelper;
import tr.edu.yildiz.virtualcloset.Model.Clothes;
import tr.edu.yildiz.virtualcloset.Model.Outfit;

public class OutfitsActivity extends AppCompatActivity {
    DatabaseHelper databaseHelper;
    ArrayList<Outfit> outfits;
    ArrayList<File> fileList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outfits);


        // eğer kıyafet silindiyse ?
        databaseHelper = new DatabaseHelper(this);
        outfits = databaseHelper.getOutfits();
        if(outfits != null) {
            defineRv();
        }
        else {
            TextView noOutfit = findViewById(R.id.noOutfit);
            noOutfit.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(outfits != null) {
            try {
                createFiles();
            } catch (IOException e) {
                Log.d("virtualCloset", e.getMessage());
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(outfits != null) {
            for (File file : fileList) {
                //noinspection ResultOfMethodCallIgnored
                file.delete();
            }
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void createFiles() throws IOException {
        fileList = new ArrayList<>();
        for (int i=0; i < outfits.size(); i++) {
            Clothes clothes1 = databaseHelper.getClothes(outfits.get(i).getOverhead());
            if(clothes1 != null) {
                byte[] image1 = clothes1.getPhoto();
                File f1 = new File(Environment.getExternalStorageDirectory() + File.separator + clothes1.getId() + ".png");
                if(!f1.exists()) {
                    f1.createNewFile();
                    FileOutputStream fo1 = new FileOutputStream(f1);
                    fo1.write(image1);
                    fo1.close();
                }
                fileList.add(f1);
            }
            Clothes clothes2 = databaseHelper.getClothes(outfits.get(i).getUpper());
            Clothes clothes3 = databaseHelper.getClothes(outfits.get(i).getLower());
            Clothes clothes4 = databaseHelper.getClothes(outfits.get(i).getFoot());

            byte[] image2 = clothes2.getPhoto();
            byte[] image3 = clothes3.getPhoto();
            byte[] image4 = clothes4.getPhoto();

            File f2 = new File(Environment.getExternalStorageDirectory() + File.separator + clothes2.getId() + ".png");
            File f3 = new File(Environment.getExternalStorageDirectory() + File.separator + clothes3.getId() + ".png");
            File f4 = new File(Environment.getExternalStorageDirectory() + File.separator + clothes4.getId() + ".png");

            if(!f2.exists()) {
                f2.createNewFile();
                FileOutputStream fo2 = new FileOutputStream(f2);
                fo2.write(image2);
                fo2.close();
            }
            if(!f3.exists()) {
                f3.createNewFile();
                FileOutputStream fo3 = new FileOutputStream(f3);
                fo3.write(image3);
                fo3.close();
            }
            if(!f4.exists()) {
                f4.createNewFile();
                FileOutputStream fo4 = new FileOutputStream(f4);
                fo4.write(image4);
                fo4.close();
            }
            fileList.add(f2);fileList.add(f3);fileList.add(f4);
        }
    }

    private void defineRv() {
        RecyclerView rvOutfits = findViewById(R.id.rvOutfits);
        rvOutfits.setItemViewCacheSize(outfits.size());
        rvOutfits.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        DividerItemDecoration decoration = new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL);
        decoration.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(this, R.drawable.divider)));
        rvOutfits.addItemDecoration(decoration);

        OutfitAdapter outfitAdapter = new OutfitAdapter(this, outfits);
        rvOutfits.setAdapter(outfitAdapter);
    }
}